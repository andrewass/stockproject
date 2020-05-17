package com.stockproject.controller

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(controllers = [StockController::class])
internal class StockControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var commonStockService: CommonStockService

    private val stockExchangeList = listOf(Exchange(code = "BE"), Exchange(code = "NO"),
            Exchange(code = "US"), Exchange(code = "JAP"))

    private val symbolList = listOf(Symbol(symbol = "A"), Symbol(symbol = "B"), Symbol(symbol = "C"))

    private val candleList = mutableListOf(Candle(), Candle(), Candle())

    private val symbolCandles = SymbolCandles(symbol = symbolList[0], candles = candleList)

    @TestConfiguration
    class TestConfig {
        @Bean
        fun stockService() = mockk<CommonStockService>()
    }

    @Test
    fun `should return expected status and content of stock exchanges`() {
        val builder = MockMvcRequestBuilders.get("/stock/stock-exchanges")

        every { commonStockService.getExchanges(STOCK_EXCHANGE_PATH) } returns stockExchangeList

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].code").value("BE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].code").value("NO"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].code").value("US"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].code").value("JAP"))
    }

    @Test
    fun `should return status OK when populating stock symbols`() {
        val builder = MockMvcRequestBuilders.get("/stock/populate-stock-symbols")

        every { commonStockService.getStockSymbolsOfExchangeName(any()) } returns emptyList()
        every { commonStockService.getExchanges(STOCK_EXCHANGE_PATH) } returns stockExchangeList

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `should return list candles of trending stock symbols`() {
        val builder = MockMvcRequestBuilders
                .get("/stock/trending-stock-candles?count=10&days=15")

        every {
            commonStockService.getCandlesOfTrendingSymbols(10, 15L, ExchangeType.STOCK)
        } returns listOf(symbolCandles)

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk)
    }
}