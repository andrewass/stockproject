package com.stockproject.service

import com.stockproject.consumer.StockConsumer
import com.stockproject.entity.Exchange
import com.stockproject.entity.Symbol
import com.stockproject.repository.ExchangeRepository
import com.stockproject.repository.SymbolRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class StockServiceTest {

    @MockK(relaxed = true)
    private lateinit var exchangeRepository: ExchangeRepository

    @MockK
    private lateinit var symbolRepository: SymbolRepository

    @MockK
    private lateinit var stockConsumer: StockConsumer

    @InjectMockKs
    private lateinit var stockService: StockService

    @BeforeEach
    private fun setUp() = MockKAnnotations.init(this)

    private val stockExchanges = listOf(Exchange(code = "EA", exchangeName = "EA"),
            Exchange(code = "EB", exchangeName = "EB"), Exchange(code = "EC", exchangeName = "EC"),
            Exchange(code = "ED", exchangeName = "ED"))

    private val stockSymbols = listOf(Symbol(symbol = "SA", exchange = stockExchanges[0]),
            Symbol(symbol = "SB", exchange = stockExchanges[0]),
            Symbol(symbol = "SC", exchange = stockExchanges[0]),
            Symbol(symbol = "SD", exchange = stockExchanges[0]))


    @Test
    fun `should return list of 3 stock exchanges from service`() {
        every { stockConsumer.getStockExchanges() } returns getStockExchangeResponseList()
        every { exchangeRepository.findAll() } returns getStockExchangeResponseList()

        val exchangeList = stockService.getStockExchanges()

        assertEquals(4, exchangeList.size)
    }

    @Test
    fun `should return list of 4 crypto exchanges from service`() {
        every { stockConsumer.getCryptoExchanges() } returns getCryptoExchangeResponseList()
        every { exchangeRepository.findAll() } returns getCryptoExchangeResponseList()

        val exchangeList = stockService.getCryptoExchanges()

        assertEquals(4, exchangeList.size)
    }

    @Test
    fun `should return list of 4 new stock symbols for given exchange`() {
        every { stockConsumer.getStockSymbols(stockExchanges[0]) } returns stockSymbols
        every { exchangeRepository.findByExchangeName(stockExchanges[0].exchangeName) } returns stockExchanges[0]
        every { symbolRepository.findAllSymbolsFromExchange(stockExchanges[0]) } returns emptyList()
        every { symbolRepository.saveAll(stockSymbols.toHashSet()) } returns stockSymbols

        val symbolList = stockService.getStockSymbols(stockExchanges[0].exchangeName)

        assertEquals(4, symbolList.size)
        assertTrue(symbolList.containsAll(stockSymbols))
    }

    @Test
    fun `should return list of 4 previous persisted stock symbols for given exchange`() {
        val slot = slot<HashSet<Symbol>>()

        every { stockConsumer.getStockSymbols(stockExchanges[0]) } returns stockSymbols
        every { exchangeRepository.findByExchangeName(stockExchanges[0].exchangeName) } returns stockExchanges[0]
        every { symbolRepository.findAllSymbolsFromExchange(stockExchanges[0]) } returns stockSymbols
        every { symbolRepository.saveAll(capture(slot)) } returns stockSymbols

        val symbolList = stockService.getStockSymbols(stockExchanges[0].exchangeName)

        assertEquals(4, slot.captured.size)
        assertTrue(slot.captured.containsAll(stockSymbols))
        assertEquals(4, symbolList.size)
        assertTrue(symbolList.containsAll(stockSymbols))
    }

    private fun getStockExchangeResponseList() =
            listOf(Exchange(code = "EA"), Exchange(code = "EB"), Exchange(code = "EC"), Exchange(code = "ED"))

    private fun getCryptoExchangeResponseList() = listOf(Exchange(code = "GEMINI"), Exchange(code = "Bitmex"),
            Exchange(code = "OKEX"), Exchange(code = "KRAKEN"))
}