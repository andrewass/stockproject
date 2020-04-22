package com.stockproject.entity

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "T_CANDLE")
class Candle(
        @Id
        @GeneratedValue
        val id : Long? = null,

        @ManyToOne
        @JoinColumn(name = "SYMBOL")
        val symbol: Symbol? = null,

        val lowPrice : Double = 0.00,

        val highPrice : Double = 0.00,

        val closingPrice : Double = 0.00,

        val candleDate : LocalDate? = null
)