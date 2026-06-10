package com.hvs.backendservice.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "demo_customer")
class DemoCustomerEntity(

    @Id
    var id: Long = 0,

    var name: String = "",
)