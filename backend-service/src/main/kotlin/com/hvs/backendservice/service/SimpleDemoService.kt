package com.hvs.backendservice.service

import com.hvs.backendservice.dto.Todo
import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.trace.StatusCode
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import kotlin.use

@Service
class SimpleDemoService(
    private val restClient: RestClient,
) {

    private val tracer = GlobalOpenTelemetry.getTracer("observability-demo")
    private val meter = GlobalOpenTelemetry.getMeter("observability-demo")

    private val requestCounter = meter
        .counterBuilder("demo.requests")
        .setDescription("Number of demo metric requests")
        .setUnit("requests")
        .build()

    fun doBusinessWork(): Map<String, String> {
        val span = tracer.spanBuilder("business-work").startSpan()

        return span.makeCurrent().use {
            try {
                validateInput()
                calculateSomething()
                span.setAttribute("business.result", "success")
                mapOf("message" to "business work done")
            } catch (ex: Exception) {
                span.recordException(ex)
                span.setStatus(StatusCode.ERROR)
                throw ex
            } finally {
                span.end()
            }
        }
    }

    fun doSlowWork(): Map<String, String> {
        val span = tracer.spanBuilder("slow-operation").startSpan()

        return span.makeCurrent().use {
            try {
                Thread.sleep(750)
                span.setAttribute("slow.reason", "simulated delay")
                mapOf("message" to "slow work done")
            } finally {
                span.end()
            }
        }
    }

    fun doFailingWork(): Nothing {
        val span = tracer.spanBuilder("failing-operation").startSpan()

        span.makeCurrent().use {
            try {
                throw IllegalStateException("This is a test error")
            } catch (ex: Exception) {
                span.recordException(ex)
                span.setStatus(StatusCode.ERROR)
                throw ex
            } finally {
                span.end()
            }
        }
    }

    fun recordMetric(): Map<String, String> {
        requestCounter.add(1)
        return mapOf("message" to "metric recorded")
    }

    private fun validateInput() {
        val span = tracer.spanBuilder("validate-input").startSpan()
        span.makeCurrent().use {
            Thread.sleep(50)
            span.setAttribute("validation.valid", true)
            span.end()
        }
    }

    private fun calculateSomething() {
        val span = tracer.spanBuilder("calculate-something").startSpan()
        span.makeCurrent().use {
            Thread.sleep(100)
            span.setAttribute("calculation.type", "demo")
            span.end()
        }
    }

    fun getTodo(): Todo =
        restClient.get()
            .uri("https://jsonplaceholder.typicode.com/todos/1")
            .retrieve()
            .body<Todo>()!!
}