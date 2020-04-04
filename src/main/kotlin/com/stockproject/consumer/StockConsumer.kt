package com.stockproject.consumer

import com.stockproject.entity.Exchange
import org.json.JSONArray
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class StockConsumer @Autowired constructor(
        private val restTemplate: RestTemplate
) {

    private val log = LoggerFactory.getLogger(StockConsumer::class.java)

    @Value("\${stock.base.url}")
    private lateinit var baseUrl: String

    fun getExchanges(): List<Exchange> {

        val urlPath = "$baseUrl/exchange?token="

        val httpEntity = HttpEntity("body", createHeaders())

        val response = restTemplate.exchange(urlPath,
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

    private fun createHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_JSON)
        headers.contentType = MediaType.APPLICATION_JSON
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36")
        return headers
    }

}