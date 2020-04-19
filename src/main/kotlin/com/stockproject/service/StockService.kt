package com.stockproject.service

import com.stockproject.consumer.StockConsumer
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
        val exchangeResponse = stockConsumer.getStockExchanges()
        updatePersistedExchanges(exchangeResponse)
        return exchangeRepository.findAll()
    }

    fun getCryptoExchanges(): List<Exchange> {
        val exchangeResponse = stockConsumer.getCryptoExchanges()
        updatePersistedExchanges(exchangeResponse)
        return exchangeRepository.findAll()
    }

    fun getStockSymbols(exchange: String): List<Symbol> {
        val stockSymbols = stockConsumer.getStockSymbols(exchange)
        val newSymbols = fetchNewSymbols(stockSymbols)
        persistNewSymbols(newSymbols, exchange)
        return stockSymbols
    }

    private fun persistNewSymbols(newSymbols: List<Symbol>, exchangeCode: String){
        val exchange = exchangeRepository.findByCode(exchangeCode) ?: return emptyList()
        newSymbols.forEach { it.exchange = exchange }
        symbolRepository.saveAll(newSymbols)
    }

    private fun fetchNewSymbols(symbolResponse: List<Symbol>) = symbolResponse
            .filter { !symbolRepository.existsBySymbol(it.symbol) }
            .toList()
}

private fun updatePersistedExchanges(exchangeResponse: List<Exchange>) {
    val newExchanges = exchangeResponse.filter { !exchangeRepository.existsByCode(it.code) }
            .toList()
    exchangeRepository.saveAll(newExchanges)
}
}