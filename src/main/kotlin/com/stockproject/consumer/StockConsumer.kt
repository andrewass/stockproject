package com.stockproject.consumer

import com.stockproject.entity.Exchange
import com.stockproject.entity.enum.ExchangeType
import com.stockproject.consumer.util.CRYPTO_EXCHANGE_URL
import com.stockproject.consumer.util.STOCK_EXCHANGE_URL
import com.stockproject.consumer.util.createHeaders
import org.json.JSONArray
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

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
            log.error("Unable fetch to stock exchanges : Statuscode ${response.statusCode}")
            emptyList()
        }
    }

    fun getCryptoExchanges(): List<Exchange> {
        val response = exchange(CRYPTO_EXCHANGE_URL)

        return if (response.statusCode.is2xxSuccessful) {
            convertToCryptoExchangeList(response.body!!)
        } else {
            log.error("Unable to stock exchanges : Statuscode ${response.statusCode}")
            emptyList()
        }
    }

    private fun exchange(url: String) = restTemplate.exchange(url,
            HttpMethod.GET,
            HttpEntity("body", createHeaders()),
            String::class.java)


    private fun convertToStockExchangeList(responseBody: String): List<Exchange> {
        val exchangeList = mutableListOf<Exchange>()
        val jsonArray = JSONArray(responseBody)
        for (i in 0 until jsonArray.length()) {
            val jsonExchange = jsonArray.getJSONObject(i)
            exchangeList.add(Exchange(code = jsonExchange.getString("code"),
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