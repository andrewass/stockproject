package com.stockproject.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "T_EXCHANGE")
class Exchange(
        @Id
        @GeneratedValue
        val id : Long? = null,

        val exchangeName : String = "",

        val code : String = "",

        val currency : String = "",

        val isActive : Boolean = true
)