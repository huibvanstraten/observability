package com.hvs.backendservice.service

import com.hvs.backendservice.domain.DemoCustomerEntity
import com.hvs.backendservice.repository.DemoCustomerRepository

import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.trace.StatusCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.use

@Service
class JpaDemoService(
    private val repository: DemoCustomerRepository,
) {
    private val tracer = GlobalOpenTelemetry.getTracer("observability-demo")

    @Transactional
    fun jpaDemo(): Map<String, Any?> {
        val span = tracer.spanBuilder("jpa-demo").startSpan()

        return span.makeCurrent().use {
            try {
                repository.save(DemoCustomerEntity(id = 1, name = "Huib"))

                val customer = repository.findByName("Huib")

                span.setAttribute("customer.id", customer?.id ?: -1)
                span.setAttribute("customer.name", customer?.name ?: "not-found")

                mapOf(
                    "id" to customer?.id,
                    "name" to customer?.name,
                )
            } catch (ex: Exception) {
                span.recordException(ex)
                span.setStatus(StatusCode.ERROR)
                throw ex
            } finally {
                span.end()
            }
        }
    }
}