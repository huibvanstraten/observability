package com.hvs.backendservice.controller

import com.hvs.backendservice.service.JpaDemoService
import com.hvs.backendservice.service.ObservabilityDemoService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/demo")
class ObservabilityDemoController(
    private val jpaDemoService: JpaDemoService,
    private val demoService: ObservabilityDemoService,
) {
    @GetMapping("/jpa")
    fun jpa() =
        jpaDemoService.jpaDemo()

    @GetMapping("/customer")
    fun customer() =
        demoService.getTodo()


    @GetMapping("/simple")
    fun simple() = mapOf("message" to "simple trace")

    @GetMapping("/work")
    fun work() = demoService.doBusinessWork()

    @GetMapping("/slow")
    fun slow() = demoService.doSlowWork()

    @GetMapping("/error")
    fun error(): Nothing = demoService.doFailingWork()

    @GetMapping("/metrics")
    fun metrics() = demoService.recordMetric()
}