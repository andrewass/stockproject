package com.stockproject.controller

import com.stockproject.consumer.util.FOREX_EXCHANGE_PATH
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
@RequestMapping("/forex")
class ForexController @Autowired constructor(
        private val commonStockService: CommonStockService
){

    private val log = LoggerFactory.getLogger(ForexController::class.java)

    @GetMapping("/populate-forex-symbols")
    fun populateForexSymbols(): ResponseEntity<HttpStatus> {
        val cryptoExchanges = commonStockService.getExchanges(FOREX_EXCHANGE_PATH)
        cryptoExchanges.forEach {
            val symbols = commonStockService.getCryptoSymbolsOfExchangeCode(it.code)
            log.info("Fetched ${symbols.size} symbols for forex exchange ${it.code}")
            Thread.sleep(1000L)
        }
        log.info("Population of forex symbols complete")
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/trending-forex-candles")
    fun getTrendingForexCandles(@RequestParam("count") count: Int, @RequestParam("days") days: Long):
            ResponseEntity<List<SymbolCandles>> {
        val symbolCandles = commonStockService.getCandlesOfTrendingSymbols(count, days, ExchangeType.CRYPTO)
        return ResponseEntity(symbolCandles, HttpStatus.OK)
    }
}