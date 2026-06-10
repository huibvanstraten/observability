package com.hvs.backendservice.repository

import com.hvs.backendservice.domain.DemoCustomerEntity
import org.springframework.data.jpa.repository.JpaRepository

interface DemoCustomerRepository : JpaRepository<DemoCustomerEntity, Long> {
    fun findByName(name: String): DemoCustomerEntity?
}