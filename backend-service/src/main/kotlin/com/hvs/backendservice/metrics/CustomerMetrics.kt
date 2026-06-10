package com.hvs.backendservice.metrics

import io.opentelemetry.api.GlobalOpenTelemetry
import org.springframework.stereotype.Component

@Component
class CustomerMetrics {
    private val meter = GlobalOpenTelemetry.getMeter("backend-business-metrics")

    private val creationAttempted = meter
        .counterBuilder("customers.creation.attempted")
        .setDescription("Customer creation attempts")
        .setUnit("1")
        .build()

    private val creationSucceeded = meter
        .counterBuilder("customers.creation.succeeded")
        .setDescription("Successful customer creations")
        .setUnit("1")
        .build()

    private val creationFailed = meter
        .counterBuilder("customers.creation.failed")
        .setDescription("Failed customer creations")
        .setUnit("1")
        .build()

    private val eventsConsumed = meter
        .counterBuilder("customer.events.consumed")
        .setDescription("Customer created events consumed")
        .setUnit("1")
        .build()

    fun recordCustomerCreationAttempted() {
        creationAttempted.add(1)
    }

    fun recordCustomerCreationSucceeded() {
        creationSucceeded.add(1)
    }

    fun recordCustomerCreationFailed() {
        creationFailed.add(1)
    }

    fun recordCustomerEventConsumed() {
        eventsConsumed.add(1)
    }
}