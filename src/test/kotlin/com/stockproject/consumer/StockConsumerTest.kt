package com.stockproject.consumer

import com.stockproject.util.EXCHANGE_URL
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

class StockConsumerTest {

    @MockK
    lateinit var restTemplate: RestTemplate

    @InjectMockKs
    lateinit var stockConsumer: StockConsumer

    @BeforeEach
    fun setUp() = MockKAnnotations.init(this)

    @Test
    fun shouldReturnExchangeListWhenSuccessful() {
        every {
            restTemplate.exchange(EXCHANGE_URL, HttpMethod.GET, any(), String::class.java)
        } returns ResponseEntity(getResponseBody(), HttpStatus.OK)

        val exchangeList = stockConsumer.getExchanges()

        assertEquals(4, exchangeList.size)
    }

    @Test
    fun shouldReturnExchangeListWhenUnsuccessful() {
        every {
            restTemplate.exchange(EXCHANGE_URL, HttpMethod.GET, any(), String::class.java)
        } returns ResponseEntity(HttpStatus.UNAUTHORIZED)

        val exchangeList = stockConsumer.getExchanges()

        assertEquals(true, exchangeList.isEmpty())
    }

    private fun getResponseBody(): String {
        return "[{code:mutualFund,currency:USD,name:US_Mutual_funds}," +
                "{code:indices,currency:USD,name:World_Indices}," +
                "{code:US,currency:USD,name:US_exchanges}," +
                "{code:BR,currency:EUR,name:NYSE}]"
    }
}