package com.stockproject.consumer

import com.stockproject.consumer.util.CRYPTO_EXCHANGE_PATH
import com.stockproject.consumer.util.STOCK_EXCHANGE_PATH
import com.stockproject.consumer.util.STOCK_SYMBOL_PATH
import com.stockproject.consumer.util.createURI
import com.stockproject.entity.Exchange
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

internal class CommonStockConsumerTest {

    @MockK
    private lateinit var restTemplate: RestTemplate

    @InjectMockKs
    private lateinit var commonStockConsumer: CommonStockConsumer

    @BeforeEach
    private fun setUp() = MockKAnnotations.init(this)

    private val stockSymbolURI = createURI(STOCK_SYMBOL_PATH, Pair("exchange", "US"))

    @Test
    fun `should return stock symbol list when successful`() {
        every {
            restTemplate.exchange(stockSymbolURI, HttpMethod.GET, any(), String::class.java)
        } returns ResponseEntity(getStockSymbolResponseBody(), HttpStatus.OK)

        val symbolList = commonStockConsumer.getStockSymbols(Exchange(code = "US"))

        assertEquals(4, symbolList.size)
    }

    @Test
    fun `should return empty stock symbol list when unsuccessful`() {
        every {
            restTemplate.exchange(stockSymbolURI, HttpMethod.GET, any(), String::class.java)
        } returns ResponseEntity(HttpStatus.UNAUTHORIZED)

        val symbolList = commonStockConsumer.getStockSymbols(Exchange(code = "US"))

        assertEquals(true, symbolList.isEmpty())
    }

    @Test
    fun `should return stock exchange list when successful`() {
        every {
            restTemplate.exchange(createURI(STOCK_EXCHANGE_PATH), HttpMethod.GET, any(), String::class.java)
        } returns ResponseEntity(getStockExchangeResponseBody(), HttpStatus.OK)

        val exchangeList = commonStockConsumer.getExchanges(STOCK_EXCHANGE_PATH)

        assertEquals(4, exchangeList.size)
    }

    @Test
    fun `should return empty list of stock exchange when unsuccessful`() {
        every {
            restTemplate.exchange(createURI(STOCK_EXCHANGE_PATH), HttpMethod.GET, any(), String::class.java)
        } returns ResponseEntity(HttpStatus.UNAUTHORIZED)

        val exchangeList = commonStockConsumer.getExchanges(STOCK_EXCHANGE_PATH)

        assertEquals(true, exchangeList.isEmpty())
    }

    @Test
    fun `should return crypto exchange list when successful`() {
        every {
            restTemplate.exchange(createURI(CRYPTO_EXCHANGE_PATH), HttpMethod.GET, any(), String::class.java)
        } returns ResponseEntity(getCryptoExchangeResponseBody(), HttpStatus.OK)

        val exchangeList = commonStockConsumer.getExchanges(CRYPTO_EXCHANGE_PATH)

        assertEquals(3, exchangeList.size)
    }

    @Test
    fun `should return empty list of crypto exchange when unsuccessful`() {
        every {
            restTemplate.exchange(createURI(CRYPTO_EXCHANGE_PATH), HttpMethod.GET, any(), String::class.java)
        } returns ResponseEntity(HttpStatus.UNAUTHORIZED)

        val exchangeList = commonStockConsumer.getExchanges(CRYPTO_EXCHANGE_PATH)

        assertEquals(true, exchangeList.isEmpty())
    }

    private fun getStockSymbolResponseBody() =
            "[{description:AGILENT_TECHNOLOGIES_INC,displaySymbol:A,symbol:A}," +
                    "{description:ALCOA_CORP,displaySymbol:AA,symbol:AA}," +
                    "{description:PERTH_MINT_PHYSICAL_GOLD_ETF,displaySymbol:AAAU,symbol:AAAU}," +
                    "{description:ATA_CREATIVITY_GLOBAL,displaySymbol:AACG,symbol:AACG}]"

    private fun getStockExchangeResponseBody() =
            "[{code:mutualFund,currency:USD,name:US_Mutual_funds}," +
                    "{code:indices,currency:USD,name:World_Indices}," +
                    "{code:US,currency:USD,name:US_exchanges}," +
                    "{code:BR,currency:EUR,name:NYSE}]"

    private fun getCryptoExchangeResponseBody() = "[GEMINI,KRAKEN,COINBASE]"
}
