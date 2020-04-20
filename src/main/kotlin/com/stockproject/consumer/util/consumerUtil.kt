package com.stockproject.consumer.util

import org.apache.http.client.utils.URIBuilder
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType


const val BASE_URL = "https://finnhub.io/api/v1/"
const val TOKEN_KEY = "token"

val TOKEN_VALUE: String = System.getenv("FINNHUB_API_KEY")
val STOCK_EXCHANGE_URL = createURL("stock/exchange")
val CRYPTO_EXCHANGE_URL = createURL("crypto/exchange")
val STOCK_SYMBOL_URL = createURL("stock/symbol")
val STOCK_CANDLE = createURL("stock/candle")

fun createURI(url: String, vararg parameters: Pair<String, String>): String {
    val uriBuilder = URIBuilder(url)
    for (parameter in parameters) {
        uriBuilder.addParameter(parameter.first, parameter.second)
    }
    return uriBuilder.build().toString()
}

fun createHeaders(): HttpHeaders {
    val headers = HttpHeaders()
    headers.accept = listOf(MediaType.APPLICATION_JSON)
    headers.contentType = MediaType.APPLICATION_JSON
    headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36")
    return headers
}

private fun createURL(subPath: String) = URIBuilder(BASE_URL + subPath)
        .addParameter(TOKEN_KEY, TOKEN_VALUE)
        .build().toString()
