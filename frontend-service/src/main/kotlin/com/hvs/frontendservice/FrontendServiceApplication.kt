package com.hvs.frontendservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FrontendServiceApplication

fun main(args: Array<String>) {
    runApplication<FrontendServiceApplication>(*args)
}
