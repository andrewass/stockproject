package com.stockproject.consumer

import com.stockproject.consumer.util.*
import com.stockproject.entity.Candle
import com.stockproject.entity.Exchange
import com.stockproject.entity.Symbol
import com.stockproject.entity.enum.ExchangeType
import org.json.JSONArray
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class StockConsumer @Autowired constructor(
        private val restTemplate: RestTemplate
) {
    private val log = LoggerFactory.getLogger(StockConsumer::class.java)

    fun getStockExchanges(): List<Exchange> {
        val response = exchange(STOCK_EXCHANGE_URL)

        return if (response.statusCode.is2xxSuccessful) {
            convertToStockExchangeList(response.body!!)
        } else {
            log.error("Unable to fetch stock exchanges : Statuscode ${response.statusCode}")
            emptyList()
        }
    }

    fun getCryptoExchanges(): List<Exchange> {
        val response = exchange(CRYPTO_EXCHANGE_URL)

        return if (response.statusCode.is2xxSuccessful) {
            convertToCryptoExchangeList(response.body!!)
        } else {
            log.error("Unable to fetch crypto exchanges : Statuscode ${response.statusCode}")
            emptyList()
        }
    }

    fun getStockSymbols(exchange: Exchange): List<Symbol> {
        val response = exchange(STOCK_SYMBOL_URL, Pair("exchange", exchange.code))

        return if (response.statusCode.is2xxSuccessful) {
            convertToStockSymbolList(response.body!!, exchange)
        } else {
            log.error("Unable to fetch stock symbols : Statuscode ${response.statusCode}")
            emptyList()
        }
    }

    fun getStockCandles(symbol: String, days: Long): List<Candle> {
        val endDate = LocalDateTime.now()
        val startDate = endDate.minusDays(days)
        val response = exchange(STOCK_CANDLE,
                Pair("symbol", symbol), Pair("resolution", "D"),
                Pair("from", startDate.toEpochSecond(ZoneOffset.UTC).toString()),
                Pair("to", endDate.toEpochSecond(ZoneOffset.UTC).toString()))

        return emptyList()
    }

    private fun exchange(url: String, vararg parameters: Pair<String, String>) =
            restTemplate.exchange(createURI(url, *parameters),
                    HttpMethod.GET,
                    HttpEntity("body", createHeaders()),
                    String::class.java)

    private fun convertToStockSymbolList(responseBody: String, exchange: Exchange): List<Symbol> {
        val symbolList = mutableListOf<Symbol>()
        val jsonArray = JSONArray(responseBody)
        for (i in 0 until jsonArray.length()) {
            val jsonSymbol = jsonArray.getJSONObject(i)
            symbolList.add(Symbol(
                    description = jsonSymbol.getString("description"),
                    displaySymbol = jsonSymbol.getString("displaySymbol"),
                    symbol = jsonSymbol.getString("symbol"),
                    exchange = exchange
            ))
        }
        return symbolList
    }

    private fun convertToStockExchangeList(responseBody: String): List<Exchange> {
        val exchangeList = mutableListOf<Exchange>()
        val jsonArray = JSONArray(responseBody)
        for (i in 0 until jsonArray.length()) {
            val jsonExchange = jsonArray.getJSONObject(i)
            exchangeList.add(Exchange(
                    code = jsonExchange.getString("code"),
                    currency = jsonExchange.getString("currency"),
                    exchangeName = jsonExchange.getString("name"),
                    exchangeType = ExchangeType.STOCK))
        }
        return exchangeList
    }

    private fun convertToCryptoExchangeList(responseBody: String): List<Exchange> {
        val exchangeList = mutableListOf<Exchange>()
        val jsonArray = JSONArray(responseBody)
        for (i in 0 until jsonArray.length()) {
            val code = jsonArray.getString(i)
            exchangeList.add(Exchange(code = code, exchangeType = ExchangeType.CRYPTO))
        }
        return exchangeList
    }

}