package com.hvs.frontendservice.service

import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.trace.StatusCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

data class BackendCustomerResponse(
    val id: Long,
    val name: String,
    val source: String,
)

data class FrontendCustomerResponse(
    val message: String,
    val backendCustomer: BackendCustomerResponse,
)

@Service
class FrontendCustomerService(
    private val restClient: RestClient,
    @Value("\${backend.base-url}") private val backendBaseUrl: String,
) {
    private val tracer = GlobalOpenTelemetry.getTracer("frontend-customer-service")

    fun getCustomer(): FrontendCustomerResponse {
        val span = tracer.spanBuilder("frontend-get-customer").startSpan()

        return span.makeCurrent().use {
            try {
                val customer = restClient.get()
                    .uri("$backendBaseUrl/backend/customer")
                    .retrieve()
                    .body(BackendCustomerResponse::class.java)
                    ?: error("Backend response was empty")

                span.setAttribute("backend.customer.id", customer.id)

                FrontendCustomerResponse(
                    message = "Frontend called backend successfully",
                    backendCustomer = customer,
                )
            } catch (ex: Exception) {
                span.recordException(ex)
                span.setStatus(StatusCode.ERROR)
                throw ex
            } finally {
                span.end()
            }
        }
    }
}