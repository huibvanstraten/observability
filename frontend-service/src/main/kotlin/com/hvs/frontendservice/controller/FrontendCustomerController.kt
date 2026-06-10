package com.hvs.frontendservice.controller

import com.hvs.frontendservice.service.FrontendCustomerResponse
import com.hvs.frontendservice.service.FrontendCustomerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/frontend")
class FrontendCustomerController(
    private val service: FrontendCustomerService,
) {
    @GetMapping("/customer")
    fun getCustomer(): FrontendCustomerResponse =
        service.getCustomer()
}