package com.stockproject.controller

import com.stockproject.consumer.util.CRYPTO_EXCHANGE_PATH
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
@RequestMapping("/crypto")
class CryptoController @Autowired constructor(
    private val commonStockService: CommonStockService
){

    private val log = LoggerFactory.getLogger(CryptoController::class.java)

    @GetMapping("/crypto-exchanges")
    fun getCryptoExchanges(): ResponseEntity<List<Exchange>> {
        val cryptoExchanges = commonStockService.getExchanges(CRYPTO_EXCHANGE_PATH)
        return ResponseEntity(cryptoExchanges, HttpStatus.OK)
    }

    @GetMapping("/populate-crypto-symbols")
    fun populateStockSymbols(): ResponseEntity<HttpStatus> {
        val cryptoExchanges = commonStockService.getExchanges(CRYPTO_EXCHANGE_PATH)
        cryptoExchanges.forEach {
            val startTime = System.currentTimeMillis()
            val symbols = commonStockService.getCryptoSymbolsOfExchangeCode(it.code)
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
        val symbolCandles = commonStockService.getCandlesOfTrendingSymbols(count, days, ExchangeType.CRYPTO)
        return ResponseEntity(symbolCandles, HttpStatus.OK)
    }


}