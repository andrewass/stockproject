package com.stockproject.controller

import com.stockproject.entity.Exchange
import com.stockproject.service.ExchangeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/exchange")
class ExchangeController @Autowired constructor(
        private val exchangeService: ExchangeService
) {

    @GetMapping("/stock-exchanges")
    fun getStockExchanges(): ResponseEntity<List<Exchange>> {
        val stockExchanges = exchangeService.getStockExchanges()
        return ResponseEntity(stockExchanges, HttpStatus.OK)
    }

    @GetMapping("/crypto-exchanges")
    fun getCryptoExchanges(): ResponseEntity<List<Exchange>> {
        val cryptoExchanges = exchangeService.getCryptoExchanges()
        return ResponseEntity(cryptoExchanges, HttpStatus.OK)
    }
}