package com.stockproject.service

import com.stockproject.consumer.StockConsumer
import com.stockproject.entity.Exchange
import com.stockproject.repository.ExchangeRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ExchangeServiceTest {

    @MockK(relaxed = true)
    private lateinit var exchangeRepository: ExchangeRepository

    @MockK
    private lateinit var stockConsumer: StockConsumer

    @InjectMockKs
    private lateinit var exchangeService: ExchangeService

    @BeforeEach
    private fun setUp() = MockKAnnotations.init(this)

    @Test
    fun `should return list of 3 stock exchanges from service`() {
        every { stockConsumer.getStockExchanges() } returns getStockExchangeResponseList()
        every { exchangeRepository.findAll() } returns getStockExchangeResponseList()

        val exchangeList = exchangeService.getStockExchanges()

        verify(exactly = 3) { exchangeRepository.existsByCode(any()) }

        assertEquals(3, exchangeList.size)
    }

    @Test
    fun `should return list of 4 crypto exchanges from service`() {
        every { stockConsumer.getCryptoExchanges() } returns getCryptoExchangeResponseList()
        every { exchangeRepository.findAll() } returns getCryptoExchangeResponseList()

        val exchangeList = exchangeService.getCryptoExchanges()

        verify(exactly = 4) { exchangeRepository.existsByCode(any()) }

        assertEquals(4, exchangeList.size)
    }

    private fun getStockExchangeResponseList() = listOf(Exchange(code = "ARB", currency = "USD", exchangeName = "TestExchange1")
            , Exchange(code = "RAB", currency = "USD", exchangeName = "TestExchange2")
            , Exchange(code = "BAR", currency = "USD", exchangeName = "TestExchange3"))

    private fun getCryptoExchangeResponseList() = listOf(Exchange(code = "GEMINI"), Exchange(code = "Bitmex"),
            Exchange(code = "OKEX"), Exchange(code = "KRAKEN"))
}