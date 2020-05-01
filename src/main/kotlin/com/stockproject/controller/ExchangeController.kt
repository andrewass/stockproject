package com.stockproject.controller

import com.stockproject.entity.Candle
import com.stockproject.entity.Exchange
import com.stockproject.entity.Symbol
import com.stockproject.service.StockService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/exchange")
class ExchangeController @Autowired constructor(
        private val stockService: StockService
) {
    private val log = LoggerFactory.getLogger(ExchangeController::class.java)

    @GetMapping("/populate-stock-symbols")
    fun populateStockSymbols(): ResponseEntity<HttpStatus> {
        val stockExchanges = stockService.getStockExchanges()
        stockExchanges.forEach {
            val startTime = System.currentTimeMillis()
            val symbols = stockService.getStockSymbols(it.exchangeName)
            val totalTime = System.currentTimeMillis() - startTime
            log.info("Fetched ${symbols.size} symbols for exchange ${it.exchangeName} in ${totalTime/1000L} seconds")
            Thread.sleep(1000L)
        }
        log.info("Population of stock symbols complete")
        return ResponseEntity(HttpStatus.OK)
    }

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
    fun getSymbols(@PathVariable("exchange") exchangeName: String): ResponseEntity<List<Symbol>> {
        val symbols = stockService.getStockSymbols(exchangeName)
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
        val candles = stockService.getCandlesForSymbol(symbol, days)
        return ResponseEntity(candles, HttpStatus.OK)
    }
}