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
    fun `should return list of size 3 from service`() {
        every { stockConsumer.getStockExchanges() } returns getResponseList()
        every { exchangeRepository.findAll() } returns getResponseList()

        val exchangeList = exchangeService.getStockExchanges()

        verify(exactly = 3) { exchangeRepository.existsByCode(any()) }

        assertEquals(3, exchangeList.size)
    }

    private fun getResponseList() = listOf(Exchange(code = "ARB", currency = "USD", exchangeName = "TestExchange1")
            , Exchange(code = "RAB", currency = "USD", exchangeName = "TestExchange2")
            , Exchange(code = "BAR", currency = "USD", exchangeName = "TestExchange3"))

}