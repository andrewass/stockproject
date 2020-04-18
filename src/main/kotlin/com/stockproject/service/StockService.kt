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
        private val stockConsumer : StockConsumer
) {

    fun getStockExchanges() : List<Exchange> {
        val exchangeResponse = stockConsumer.getStockExchanges()
        updatePersistedExchanges(exchangeResponse)
        return exchangeRepository.findAll()
    }

    fun getCryptoExchanges() : List<Exchange> {
        val exchangeResponse = stockConsumer.getCryptoExchanges()
        updatePersistedExchanges(exchangeResponse)
        return exchangeRepository.findAll()
    }

    fun getStockSymbols(exchange: String): List<Symbol> {
        val symbolResponse = stockConsumer.getStockSymbols(exchange)
        updatePersistedSymbols(symbolResponse)
        return emptyList()
    }

    private fun updatePersistedSymbols(symbolResponse: List<Symbol>) {
        val newSymbols = symbolResponse
                .filter {  !symbolRepository.existsBySymbol(it.symbol) }
                .toList()
        symbolRepository.saveAll(newSymbols)
    }

    private fun updatePersistedExchanges(exchangeResponse: List<Exchange>) {
        val newExchanges = exchangeResponse.
                filter { !exchangeRepository.existsByCode(it.code) }
                .toList()
        exchangeRepository.saveAll(newExchanges)
    }
}