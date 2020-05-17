package com.stockproject.controller

import com.stockproject.consumer.util.CRYPTO_EXCHANGE_PATH
import com.stockproject.entity.Exchange
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

@WebMvcTest(controllers = [CryptoController::class])
internal class CryptoControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var commonStockService: CommonStockService

    private val cryptoExchangeList = listOf(Exchange(code = "GEMINI"), Exchange(code = "Bitmex"),
            Exchange(code = "OKEX"), Exchange(code = "KRAKEN"))

    @TestConfiguration
    class TestConfig {
        @Bean
        fun stockService() = mockk<CommonStockService>()
    }

    @Test
    fun `should return expected status and content of crypto exchanges`() {
        val builder = MockMvcRequestBuilders.get("/crypto/crypto-exchanges")

        every { commonStockService.getExchanges(CRYPTO_EXCHANGE_PATH) } returns cryptoExchangeList

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].code").value("GEMINI"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].code").value("Bitmex"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].code").value("OKEX"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].code").value("KRAKEN"))
    }
}