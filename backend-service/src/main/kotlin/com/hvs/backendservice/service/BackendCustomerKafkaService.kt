package com.hvs.backendservice.service

import com.hvs.backendservice.controller.CustomerResponse
import com.hvs.backendservice.domain.DemoCustomerEntity
import com.hvs.backendservice.event.CustomerCreatedEvent
import com.hvs.backendservice.repository.DemoCustomerRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.trace.StatusCode
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BackendCustomerKafkaService(
    private val repository: DemoCustomerRepository,
    private val kafkaTemplate: KafkaTemplate<String, CustomerCreatedEvent>,
) {
    private val logger = KotlinLogging.logger {}
    private val tracer = GlobalOpenTelemetry.getTracer("backend-customer-service")

    @Transactional
    fun getCustomer(): CustomerResponse {
        val span = tracer.spanBuilder("backend-get-customer").startSpan()

        return span.makeCurrent().use {
            try {
                logger.info { "Saving customer" }

                repository.save(DemoCustomerEntity(id = 1, name = "Huib")).also { logger.info { "Saving customer" } }

                val customer = repository.findByName("Huib")
                    ?: error("Customer not found")

                logger.info { "Publishing customer-created event" }

                kafkaTemplate.send(
                    "customer-created",
                    customer.id.toString(),
                    CustomerCreatedEvent(customer.id, customer.name),
                )

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