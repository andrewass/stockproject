package com.stockproject.service

import com.stockproject.consumer.CommonStockConsumer
import com.stockproject.entity.Exchange
import com.stockproject.entity.Symbol
import com.stockproject.entity.dto.SymbolCandles
import com.stockproject.entity.enum.ExchangeType
import com.stockproject.repository.ExchangeRepository
import com.stockproject.repository.SymbolRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class CommonStockService @Autowired constructor(
        private val exchangeRepository: ExchangeRepository,
        private val symbolRepository: SymbolRepository,
        private val commonStockConsumer: CommonStockConsumer
) {

    fun getExchanges(exchangePath : String): List<Exchange> {
        val stockExchangeList = commonStockConsumer.getExchanges(exchangePath)
        persistNewExchanges(stockExchangeList)
        return stockExchangeList
    }

    fun getStockSymbolsOfExchangeName(exchangeName: String): List<Symbol> {
        val exchange = exchangeRepository.findByExchangeName(exchangeName) ?: return emptyList()
        val stockSymbolList = commonStockConsumer.getStockSymbols(exchange)
        return persistNewSymbols(stockSymbolList, exchange)
    }

    fun getCryptoSymbolsOfExchangeCode(exchangeCode: String): List<Symbol> {
        val exchange = exchangeRepository.findByCode(exchangeCode) ?: return emptyList()
        val stockSymbolList = commonStockConsumer.getCryptoSymbols(exchange)
        return persistNewSymbols(stockSymbolList, exchange)
    }

    @Cacheable("trending")
    fun getCandlesOfTrendingSymbols(count: Int, days: Long, exchangeType: ExchangeType): List<SymbolCandles> {
        val trendingSymbols = symbolRepository.findMostTrendingSymbols(exchangeType, PageRequest.of(0,count))
        return trendingSymbols.mapNotNull { commonStockConsumer.getStockCandles(it, days, exchangeType) }
    }

    fun getCandlesForSymbol(symbolName: String, days: Long): List<SymbolCandles> {
        val symbols = symbolRepository.findTop10BySymbolContainingIgnoreCase(symbolName).toHashSet()
        symbols.addAll(symbolRepository.findTop10ByDescriptionContainingIgnoreCase(symbolName))
        symbols.forEach { it.addSingleHit() }
        val symbolCandles = mutableListOf<SymbolCandles?>()
        symbols.forEach {
            it.addSingleHit()
            symbolCandles.add(commonStockConsumer.getStockCandles(it, days, it.exchange!!.exchangeType))
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