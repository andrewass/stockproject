package com.stockproject.controller

import com.stockproject.entity.Exchange
import com.stockproject.entity.Symbol
import com.stockproject.service.StockService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/exchange")
class ExchangeController @Autowired constructor(
        private val stockService: StockService
) {

    @GetMapping("/stock-exchanges")
    fun getStockExchanges(): ResponseEntity<List<Exchange>> {
        val stockExchanges = stockService.getStockExchanges()
        return ResponseEntity(stockExchanges, HttpStatus.OK)
    }

    @GetMapping("/crypto-exchanges")
    fun getCryptoExchanges(): ResponseEntity<List<Exchange>> {
        val cryptoExchanges = stockService.getCryptoExchanges()
        return ResponseEntity(cryptoExchanges, HttpStatus.OK)
    }

    @GetMapping("/symbols/{exchange}")
    fun getSymbols(@PathVariable("exchange") exchange: String): ResponseEntity<List<Symbol>> {
        val symbols = stockService.getStockSymbols(exchange)
        val res = ResponseEntity(symbols, HttpStatus.OK)
        return res
    }
}