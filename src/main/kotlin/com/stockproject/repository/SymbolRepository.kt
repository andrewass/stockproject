package com.stockproject.repository

import com.stockproject.entity.Symbol
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SymbolRepository : JpaRepository<Symbol, Long> {

    fun existsBySymbol(symbol : String) : Boolean

    @Query("SELECT * FROM Symbol s order by s.hits desc limit ?1 ", nativeQuery = true)
    fun findMostTrendingSymbols(count: Int): List<Symbol>
}