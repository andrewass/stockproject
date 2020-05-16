package com.stockproject.service

import com.stockproject.consumer.StockConsumer
import com.stockproject.entity.Exchange
import com.stockproject.entity.Symbol
import com.stockproject.entity.dto.SymbolCandles
import com.stockproject.entity.enum.ExchangeType
import com.stockproject.repository.ExchangeRepository
import com.stockproject.repository.SymbolRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class StockService @Autowired constructor(
        private val exchangeRepository: ExchangeRepository,
        private val symbolRepository: SymbolRepository,
        private val stockConsumer: StockConsumer
) {

    fun getStockExchanges(): List<Exchange> {
        val stockExchangeList = stockConsumer.getStockExchanges()
        persistNewExchanges(stockExchangeList)
        return stockExchangeList
    }

    fun getCryptoExchanges(): List<Exchange> {
        val cryptoExchangeList = stockConsumer.getCryptoExchanges()
        persistNewExchanges(cryptoExchangeList)
        return cryptoExchangeList
    }

    fun getStockSymbolsOfExchangeName(exchangeName: String): List<Symbol> {
        val exchange = exchangeRepository.findByExchangeName(exchangeName) ?: return emptyList()
        val stockSymbolList = stockConsumer.getStockSymbols(exchange)
        return persistNewSymbols(stockSymbolList, exchange)
    }

    fun getCryptoSymbolsOfExchangeCode(exchangeCode: String): List<Symbol> {
        val exchange = exchangeRepository.findByCode(exchangeCode) ?: return emptyList()
        val stockSymbolList = stockConsumer.getCryptoSymbols(exchange)
        return persistNewSymbols(stockSymbolList, exchange)
    }

    @Cacheable("trending")
    fun getCandlesOfTrendingSymbols(count: Int, days: Long, exchangeType: ExchangeType): List<SymbolCandles> {
        val trendingSymbols = symbolRepository.findMostTrendingSymbols(exchangeType)
        return trendingSymbols.mapNotNull { stockConsumer.getStockCandles(it, days) }
    }

    fun getCandlesForSymbol(symbolName: String, days: Long): List<SymbolCandles> {
        val symbols = symbolRepository.findTop10BySymbolContainingIgnoreCase(symbolName).toHashSet()
        symbols.addAll(symbolRepository.findTop10ByDescriptionContainingIgnoreCase(symbolName))
        symbols.forEach { it.addSingleHit() }
        val symbolCandles = mutableListOf<SymbolCandles?>()
        symbols.forEach {
            it.addSingleHit()
            symbolCandles.add(stockConsumer.getStockCandles(it, days))
        }
        return symbolCandles.filterNotNull()
    }

    private fun persistNewSymbols(symbolList: List<Symbol>, exchange: Exchange): List<Symbol> {
        val persistedSymbols = symbolRepository.findAllSymbolsFromExchange(exchange)
        val symbolSet = persistedSymbols.toHashSet()
        symbolSet.addAll(symbolList)
        return symbolRepository.saveAll(symbolSet)
    }

    private fun persistNewExchanges(exchangeList: List<Exchange>) {
        val persistedExchanges = exchangeRepository.findAll()
        val exchangeSet = persistedExchanges.toHashSet()
        exchangeSet.addAll(exchangeList)
        exchangeRepository.saveAll(exchangeSet)
    }
}