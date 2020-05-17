package com.stockproject.controller

import com.stockproject.consumer.util.STOCK_EXCHANGE_PATH
import com.stockproject.entity.Exchange
import com.stockproject.entity.dto.SymbolCandles
import com.stockproject.entity.enum.ExchangeType
import com.stockproject.service.CommonStockService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/stock")
class StockController @Autowired constructor(
        private val commonStockService: CommonStockService
) {
    private val log = LoggerFactory.getLogger(StockController::class.java)

    @GetMapping("/stock-exchanges")
    fun getStockExchanges(): ResponseEntity<List<Exchange>> {
        val stockExchanges = commonStockService.getExchanges(STOCK_EXCHANGE_PATH)
        return ResponseEntity(stockExchanges, HttpStatus.OK)
    }

    @GetMapping("/populate-stock-symbols")
    fun populateStockSymbols(): ResponseEntity<HttpStatus> {
        val stockExchanges = commonStockService.getExchanges(STOCK_EXCHANGE_PATH)
        stockExchanges.forEach {
            val symbols = commonStockService.getStockSymbolsOfExchangeName(it.exchangeName)
            log.info("Fetched ${symbols.size} symbols for exchange ${it.exchangeName}")
            Thread.sleep(1000L)
        }
        log.info("Population of stock symbols complete")
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/trending-stock-candles")
    fun getTrendingStockCandles(@RequestParam("count") count: Int, @RequestParam("days") days: Long):
            ResponseEntity<List<SymbolCandles>> {
        val symbolCandles = commonStockService.getCandlesOfTrendingSymbols(count, days, ExchangeType.STOCK)
        return ResponseEntity(symbolCandles, HttpStatus.OK)
    }
}