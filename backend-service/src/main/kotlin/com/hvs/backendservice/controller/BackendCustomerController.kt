package com.hvs.backendservice.controller

import com.hvs.backendservice.service.BackendCustomerKafkaService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class CustomerResponse(
    val id: Long,
    val name: String,
    val source: String,
)

@RestController
@RequestMapping("/backend")
class BackendCustomerController(
    private val service: BackendCustomerKafkaService,
) {
    @GetMapping("/customer")
    fun getCustomer(): CustomerResponse =
        service.getCustomer()
}