package com.stockproject.controller

import com.stockproject.entity.dto.SymbolCandles
import com.stockproject.service.StockService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/crypto")
class CryptoController @Autowired constructor(
    private val stockService: StockService
){

    private val log = LoggerFactory.getLogger(CryptoController::class.java)

    @GetMapping("/populate-crypto-symbols")
    fun populateStockSymbols(): ResponseEntity<HttpStatus> {
        val cryptoExchanges = stockService.getCryptoExchanges()
        cryptoExchanges.forEach {
            val startTime = System.currentTimeMillis()
            val symbols = stockService.getCryptoSymbolsOfExchangeCode(it.code)
            val totalTime = System.currentTimeMillis() - startTime
            log.info("Fetched ${symbols.size} symbols for exchange ${it.code} in ${totalTime / 1000L} seconds")
            Thread.sleep(1000L)
        }
        log.info("Population of crypto symbols complete")
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/trending-crypto-candles")
    fun getTrendingStockCandles(@RequestParam("count") count: Int, @RequestParam("days") days: Long):
            ResponseEntity<List<SymbolCandles>> {
        val symbolCandles = stockService.getCandlesOfTrendingSymbols(count, days)
        return ResponseEntity(symbolCandles, HttpStatus.OK)
    }


}