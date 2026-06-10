package com.hvs.backendservice.service

import com.hvs.backendservice.controller.CustomerResponse
import com.hvs.backendservice.domain.DemoCustomerEntity
import com.hvs.backendservice.repository.DemoCustomerRepository
import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.trace.StatusCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BackendCustomerService(
    private val repository: DemoCustomerRepository,
) {
    private val tracer = GlobalOpenTelemetry.getTracer("backend-customer-service")

    @Transactional
    fun getCustomer(): CustomerResponse {
        val span = tracer.spanBuilder("backend-get-customer").startSpan()

        return span.makeCurrent().use {
            try {
                repository.save(DemoCustomerEntity(id = 1, name = "Huib"))

                val customer = repository.findByName("Huib")
                    ?: error("Customer not found")

                span.setAttribute("customer.id", customer.id)
                span.setAttribute("customer.name", customer.name)

                CustomerResponse(
                    id = customer.id,
                    name = customer.name,
                    source = "backend-service",
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