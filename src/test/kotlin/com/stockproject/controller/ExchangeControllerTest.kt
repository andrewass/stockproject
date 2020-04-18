package com.stockproject.controller

import com.stockproject.entity.Exchange
import com.stockproject.entity.Symbol
import com.stockproject.service.StockService
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

@WebMvcTest(controllers = [ExchangeController::class])
internal class ExchangeControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var stockService: StockService

    private val cryptoExchangeList = listOf(Exchange(code = "GEMINI"), Exchange(code = "Bitmex"),
            Exchange(code = "OKEX"), Exchange(code = "KRAKEN"))

    private val stockExchangeList = listOf(Exchange(code = "BE"), Exchange(code = "NO"),
            Exchange(code = "US"), Exchange(code = "JAP"))

    private val symbolList = listOf(Symbol(symbol = "A"), Symbol(symbol = "B"), Symbol(symbol =  "C"))

    @TestConfiguration
    class TestConfig {
        @Bean
        fun exchangeService() = mockk<StockService>()
    }

    @Test
    fun `should return expected status and content of stock exchanges`() {
        val builder = MockMvcRequestBuilders.get("/exchange/stock-exchanges")

        every { stockService.getStockExchanges() } returns stockExchangeList

        mockMvc.perform(builder)
                .andExpect(status().isOk)
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].code").value("BE"))
                .andExpect(jsonPath("$[1].code").value("NO"))
                .andExpect(jsonPath("$[2].code").value("US"))
                .andExpect(jsonPath("$[3].code").value("JAP"))
    }

    @Test
    fun `should return expected status and content of crypto exchanges`() {
        val builder = MockMvcRequestBuilders.get("/exchange/crypto-exchanges")

        every { stockService.getCryptoExchanges() } returns cryptoExchangeList

        mockMvc.perform(builder)
                .andExpect(status().isOk)
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].code").value("GEMINI"))
                .andExpect(jsonPath("$[1].code").value("Bitmex"))
                .andExpect(jsonPath("$[2].code").value("OKEX"))
                .andExpect(jsonPath("$[3].code").value("KRAKEN"))
    }

    @Test
    fun `should return expected status and content of exchange symbols`(){
        val builder = MockMvcRequestBuilders.get("/exchange/symbols/US")

        every { stockService.getStockSymbols("US") } returns symbolList

        mockMvc.perform(builder)
                .andExpect(status().isOk)
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].symbol").value("A"))
                .andExpect(jsonPath("$[1].symbol").value("B"))
                .andExpect(jsonPath("$[2].symbol").value("C"))
    }
}