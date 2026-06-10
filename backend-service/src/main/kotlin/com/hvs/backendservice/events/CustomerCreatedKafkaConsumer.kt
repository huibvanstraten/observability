package com.hvs.backendservice.events

import com.hvs.backendservice.event.CustomerCreatedEvent
import com.hvs.backendservice.repository.DemoCustomerRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class CustomerCreatedKafkaConsumer(
    private val repository: DemoCustomerRepository,
) {
    private val logger = KotlinLogging.logger {}

    @KafkaListener(
        topics = ["customer-created"],
        groupId = "backend-service",
    )
    fun consume(event: CustomerCreatedEvent) {
        logger.info { "Consumed customer-created event for customerId=${event.customerId}" }

        val customer = repository.findById(event.customerId)

        logger.info { "Customer exists after event? ${customer.isPresent}" }
    }
}