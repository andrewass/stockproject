package com.stockproject.repository

import com.stockproject.entity.Exchange
import com.stockproject.entity.Symbol
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SymbolRepository : JpaRepository<Symbol, Long> {

    @Query("SELECT * FROM T_SYMBOL s order by s.hits desc limit ?1 ", nativeQuery = true)
    fun findMostTrendingSymbols(count: Int): List<Symbol>

    @Query("SELECT * FROM T_SYMBOL s where s.exchange = ?1", nativeQuery = true)
    fun findAllSymbolsFromExchange(exchange: Exchange) : List<Symbol>

    fun findTop10ByDescriptionContainingIgnoreCase(description : String) : List<Symbol>

    fun findTop10BySymbolContainingIgnoreCase(symbol : String) : List<Symbol>
}