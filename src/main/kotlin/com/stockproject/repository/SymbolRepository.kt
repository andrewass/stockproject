package com.stockproject.repository

import com.stockproject.entity.Exchange
import com.stockproject.entity.Symbol
import com.stockproject.entity.enum.ExchangeType
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SymbolRepository : JpaRepository<Symbol, Long> {

    @Query("SELECT s FROM Symbol s inner join s.exchange e where e.exchangeType = ?1")
    fun findMostTrendingSymbols(exchangeType: ExchangeType, pageRequest: PageRequest): List<Symbol>

    @Query("SELECT s FROM Symbol s where s.exchange = ?1")
    fun findAllSymbolsFromExchange(exchange: Exchange) : List<Symbol>

    fun findTop10ByDescriptionContainingIgnoreCase(description : String) : List<Symbol>

    fun findTop10BySymbolContainingIgnoreCase(symbol : String) : List<Symbol>
}