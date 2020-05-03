package com.stockproject.entity.dto

import com.stockproject.entity.Symbol

data class SymbolCandles(
        val symbol: Symbol,
        val candles: MutableList<Candle> = mutableListOf()
)