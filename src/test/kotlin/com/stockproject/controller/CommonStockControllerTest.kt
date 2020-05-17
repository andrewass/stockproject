package com.stockproject.controller

import com.stockproject.consumer.util.CRYPTO_EXCHANGE_PATH
import com.stockproject.consumer.util.STOCK_EXCHANGE_PATH
import com.stockproject.entity.Exchange
import com.stockproject.entity.Symbol
import com.stockproject.entity.dto.Candle
import com.stockproject.entity.dto.SymbolCandles
import com.stockproject.entity.enum.ExchangeType
import com.stockproject.service.CommonStockService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(controllers = [CommonStockController::class])
internal class CommonStockControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var commonStockService: CommonStockService

    private val symbolList = listOf(Symbol(symbol = "A"), Symbol(symbol = "B"), Symbol(symbol = "C"))

    private val candleList = mutableListOf(Candle(), Candle(), Candle())

    private val symbolCandles = SymbolCandles(symbol = symbolList[0], candles = candleList)

    @TestConfiguration
    class TestConfig {
        @Bean
        fun exchangeService() = mockk<CommonStockService>()
    }

    @Test
    fun `should return expected status and content of exchange symbols`() {
        val builder = MockMvcRequestBuilders.get("/common/stock-symbols/US")

        every { commonStockService.getStockSymbolsOfExchangeName("US") } returns symbolList

        mockMvc.perform(builder)
                .andExpect(status().isOk)
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].symbol").value("A"))
                .andExpect(jsonPath("$[1].symbol").value("B"))
                .andExpect(jsonPath("$[2].symbol").value("C"))
    }

    @Test
    fun `should return stock candles of a given symbol`() {
        val builder = MockMvcRequestBuilders
                .get("/common/stock-candles?symbol=AAPL&days=15")

        every { commonStockService.getCandlesForSymbol("AAPL", 15) } returns listOf(symbolCandles)

        mockMvc.perform(builder)
                .andExpect(content().contentType("application/json"))
                .andExpect(status().isOk)
    }
}