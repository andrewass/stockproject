package com.stockproject.consumer

import com.stockproject.entity.Exchange
import com.stockproject.util.EXCHANGE_URL
import com.stockproject.util.createHeaders
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

    fun getExchanges(): List<Exchange> {
        val httpEntity = HttpEntity("body", createHeaders())

        val response = restTemplate.exchange(EXCHANGE_URL,
                HttpMethod.GET,
                httpEntity,
                String::class.java)

        return if (response.statusCode.is2xxSuccessful) {
            convertToExchangeList(response.body!!)
        } else {
            log.error("Unable to fetch static items : Statuscode ${response.statusCode}")
            emptyList()
        }
    }

    private fun convertToExchangeList(responseBody: String): List<Exchange> {
        val exchangeList = mutableListOf<Exchange>()
        val jsonArray = JSONArray(responseBody)
        for (i in 0 until jsonArray.length()) {
            val jsonExchange = jsonArray.getJSONObject(i)
            exchangeList.add(Exchange(code = jsonExchange.getString("code"),
                    currency = jsonExchange.getString("currency"),
                    exchangeName = jsonExchange.getString("name")))
        }
        return exchangeList
    }
}