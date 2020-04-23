package com.stockproject.controller

import com.stockproject.entity.Candle
import com.stockproject.entity.Exchange
import com.stockproject.entity.Symbol
import com.stockproject.service.StockService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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

    @GetMapping("/stock-symbols/{exchange}")
    fun getSymbols(@PathVariable("exchange") exchange: String): ResponseEntity<List<Symbol>> {
        val symbols = stockService.getStockSymbols(exchange)
        return ResponseEntity(symbols, HttpStatus.OK)
    }

    @GetMapping("/trending-stock-candles")
    fun getTrendingStockCandles(@RequestParam("count") count: Int, @RequestParam("days") days: Long):
            ResponseEntity<List<Candle>> {
        val candles = stockService.getCandlesOfTrendingSymbols(count, days)
        return ResponseEntity(candles, HttpStatus.OK)
    }

    @GetMapping("/stock-candles")
    fun getStockCandles(@RequestParam("symbol") symbol: String, @RequestParam("days") days: Long):
            ResponseEntity<List<Candle>> {
        val candles = stockService.getCandles(symbol, days)
        return ResponseEntity(candles, HttpStatus.OK)
    }
}