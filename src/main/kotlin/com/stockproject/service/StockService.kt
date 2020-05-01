package com.stockproject.service

import com.stockproject.consumer.StockConsumer
import com.stockproject.entity.Candle
import com.stockproject.entity.Exchange
import com.stockproject.entity.Symbol
import com.stockproject.repository.ExchangeRepository
import com.stockproject.repository.SymbolRepository
import org.springframework.beans.factory.annotation.Autowired
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

    fun getStockSymbols(exchangeName: String): List<Symbol> {
        val exchange = exchangeRepository.findByExchangeName(exchangeName) ?: return emptyList()
        val stockSymbolList = stockConsumer.getStockSymbols(exchange)
        persistNewSymbols(stockSymbolList, exchange)
        return stockSymbolList
    }

    fun getCandlesOfTrendingSymbols(count: Int, days: Long): List<Candle> {
        val trendingSymbols = symbolRepository.findMostTrendingSymbols(count)
        return trendingSymbols.flatMap { stockConsumer.getStockCandles(it, days) }
    }

    fun getCandles(symbolName: String, days: Long): List<Candle> {
        val symbol = symbolRepository.findBySymbol(symbolName) ?: return emptyList()
        symbol.addSingleHit()
        return stockConsumer.getStockCandles(symbol, days)
    }

    private fun persistNewSymbols(symbolList: List<Symbol>, exchange : Exchange) {
        val persistedSymbols = symbolRepository.findAllSymbolsFromExchange(exchange)
        val symbolSet = persistedSymbols.toHashSet()
        symbolSet.addAll(symbolList)
        symbolRepository.saveAll(symbolSet)
    }

    private fun persistNewExchanges(exchangeList: List<Exchange>) {
        val persistedExchanges = exchangeRepository.findAll()
        val exchangeSet = persistedExchanges.toHashSet()
        exchangeSet.addAll(exchangeList)
        exchangeRepository.saveAll(exchangeSet)
    }
}