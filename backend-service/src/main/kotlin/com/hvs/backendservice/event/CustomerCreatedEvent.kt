package com.hvs.backendservice.event


data class CustomerCreatedEvent(
    val customerId: Long,
    val name: String,
)