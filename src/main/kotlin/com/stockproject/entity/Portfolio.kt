package com.stockproject.entity

import javax.persistence.*

@Entity
@Table(name ="T_USER")
class Portfolio(
            @Id
            @GeneratedValue
            val id : Long? = null,

            @ManyToOne
            val user : User? = null
)
