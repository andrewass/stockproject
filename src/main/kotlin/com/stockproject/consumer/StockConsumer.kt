package com.stockproject.consumer

import com.stockproject.consumer.util.*
import com.stockproject.entity.Candle
import com.stockproject.entity.Exchange
import com.stockproject.entity.Symbol
import com.stockproject.entity.enum.ExchangeType
import org.json.JSONArray
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.time.*

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

    fun getCandlesFromListOfSymbols(symbols : List<Symbol>, days : Long) : List<Candle> {
        val candles = mutableListOf<Candle>()
        symbols.forEach {
            candles.addAll(getStockCandles(it, days))
        }
        return candles
    }

    fun getStockCandles(symbol: Symbol, days: Long): List<Candle> {
        val endDate = LocalDateTime.now()
        val startDate = endDate.minusDays(days)
        val response = exchange(STOCK_CANDLE,
                Pair("symbol", symbol.symbol), Pair("resolution", "D"),
                Pair("from", startDate.toEpochSecond(ZoneOffset.UTC).toString()),
                Pair("to", endDate.toEpochSecond(ZoneOffset.UTC).toString()))

        return if (response.statusCode.is2xxSuccessful) {
            convertToCandles(response.body!!)
        } else {
            emptyList()
        }
    }


    private fun exchange(url: String, vararg parameters: Pair<String, String>) =
            restTemplate.exchange(createURI(url, *parameters),
                    HttpMethod.GET,
                    HttpEntity("body", createHeaders()),
                    String::class.java)


    private fun convertToCandles(responseBody: String): List<Candle> {
        val candleList = mutableListOf<Candle>()
        val jsonBody = JSONObject(responseBody)
        val closingPrice = jsonBody.getJSONArray("c")
        val highPrice = jsonBody.getJSONArray("h")
        val lowPrice = jsonBody.getJSONArray("l")
        val timestamp = jsonBody.getJSONArray("t")
        val count = closingPrice.length()
        for (i in 0 until count) {
            candleList.add(Candle(
                    lowPrice = lowPrice.getDouble(i),
                    highPrice = highPrice.getDouble(i),
                    closingPrice = closingPrice.getDouble(i),
                    candleDate = getLocalDate(timestamp.getLong(i))
            ))
        }
        return candleList
    }

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

    private fun getLocalDate(epochTime: Long) =
            Instant.ofEpochSecond(epochTime).atZone(ZoneOffset.UTC).toLocalDate()

}