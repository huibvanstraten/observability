package com.hvs.backendservice.event

import com.hvs.backendservice.metrics.CustomerMetrics
import com.hvs.backendservice.repository.DemoCustomerRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class CustomerCreatedConsumer(
    private val repository: DemoCustomerRepository,
    private val customerMetrics: CustomerMetrics,
) {
    private val logger = KotlinLogging.logger {}

    @KafkaListener(
        topics = ["customer-created"],
        groupId = "backend-service",
    )
    fun consume(event: CustomerCreatedEvent) {
        logger.info { "Consumed customer-created event for customerId=${event.customerId}" }

        customerMetrics.recordCustomerEventConsumed()

        val customer = repository.findById(event.customerId)

        logger.info { "Customer exists after event? ${customer.isPresent}" }
        customerMetrics.recordCustomerEventConsumed()
    }
}