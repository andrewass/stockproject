package com.stockproject.controller

import com.stockproject.entity.Symbol
import com.stockproject.entity.dto.SymbolCandles
import com.stockproject.service.CommonStockService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/common")
class CommonStockController @Autowired constructor(
        private val commonStockService: CommonStockService
) {
    private val log = LoggerFactory.getLogger(CommonStockController::class.java)

    @GetMapping("/stock-symbols/{exchange}")
    fun getSymbols(@PathVariable("exchange") exchangeName: String): ResponseEntity<List<Symbol>> {
        val symbols = commonStockService.getStockSymbolsOfExchangeName(exchangeName)
        return ResponseEntity(symbols, HttpStatus.OK)
    }

    @GetMapping("/stock-candles")
    fun getStockCandles(@RequestParam("symbol") symbol: String, @RequestParam("days") days: Long):
            ResponseEntity<List<SymbolCandles>> {
        val candles = commonStockService.getCandlesForSymbol(symbol, days)
        return ResponseEntity(candles, HttpStatus.OK)
    }
}