package com.stockproject.entity

import javax.persistence.*

@Entity
@Table(name = "T_INVESTMENT")
class Investment(

        @Id
        @GeneratedValue
        val id: Long? = null,

        @ManyToOne
        @JoinColumn(name = "PORTFOLIO")
        val portfolio: Portfolio? = null,

        @OneToOne
        val symbol: Symbol? = null,

        private var amount: Int = 0

)