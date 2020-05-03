package com.stockproject.entity.dto

import java.time.LocalDate

data class Candle(
        val lowPrice: Double = 0.00,

        val highPrice: Double = 0.00,

        val closingPrice: Double = 0.00,

        val candleDate: LocalDate = LocalDate.now()
)