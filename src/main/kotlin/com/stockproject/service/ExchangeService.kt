package com.stockproject.service

import com.stockproject.consumer.StockConsumer
import com.stockproject.entity.Exchange
import com.stockproject.repository.ExchangeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ExchangeService @Autowired constructor(
        private val exchangeRepository: ExchangeRepository,
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

    private fun updatePersistedExchanges(exchangeResponse: List<Exchange>) {
        val newExchanges = exchangeResponse.
                filter { !exchangeRepository.existsByCode(it.code) }
                .toList()
        exchangeRepository.saveAll(newExchanges)
    }
}