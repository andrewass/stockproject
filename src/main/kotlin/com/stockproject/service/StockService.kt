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

    fun getStockSymbols(exchangeCode: String): List<Symbol> {
        val exchange = exchangeRepository.findByCode(exchangeCode) ?: return emptyList()
        val stockSymbolList = stockConsumer.getStockSymbols(exchange)
        persistNewSymbols(stockSymbolList)
        return stockSymbolList
    }

    fun getCandlesOfTrendingSymbols(count: Int, days: Long): List<Candle> {
        val trendingSymbols = symbolRepository.findMostTrendingSymbols(count)
        return stockConsumer.getStockCandles("", days)
    }

    fun getCandles(symbolName: String, days: Long): List<Candle> {
        val symbol = symbolRepository.findBySymbol(symbolName) ?: return emptyList()
        symbol.addSingleHit()
        return stockConsumer.getStockCandles(symbol, days)
    }


    private fun persistNewSymbols(symbolList: List<Symbol>) {
        val newSymbols = symbolList
                .filter { !symbolRepository.existsBySymbol(it.symbol) }
                .toList()
        symbolRepository.saveAll(newSymbols)
    }

    private fun persistNewExchanges(exchangeList: List<Exchange>) {
        val newExchanges = exchangeList
                .filter { !exchangeRepository.existsByCode(it.code) }
                .toList()
        exchangeRepository.saveAll(newExchanges)
    }

}