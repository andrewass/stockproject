package com.stockproject.repository

import com.stockproject.entity.Symbol
import org.springframework.data.jpa.repository.JpaRepository

interface SymbolRepository : JpaRepository<Symbol, Long> {

    fun existsBySymbol(symbol : String) : Boolean
}