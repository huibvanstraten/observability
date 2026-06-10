package com.hvs.backendservice.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {
    @GetMapping("/hello")
    fun hello(): Map<String, String> =
        mapOf("message" to "hello otel")
}