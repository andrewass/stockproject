package com.stockproject.consumer.util

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.util.UriComponentsBuilder


private const val BASE_URL = "https://finnhub.io/api/v1/"
private const val TOKEN_KEY = "token"
private val TOKEN_VALUE: String = System.getenv("FINNHUB_API_KEY")

const val STOCK_EXCHANGE_URL = "stock/exchange"
const val CRYPTO_EXCHANGE_URL = "crypto/exchange"

const val STOCK_SYMBOL_URL = "stock/symbol"
const val CRYPTO_SYMBOL_URL = "crypto/symbol"

const val STOCK_CANDLE_URL = "stock/candle"
const val CRYPTO_CANDLE_URL = "crypto/candle"

fun createURI(urlPath: String, vararg parameters: Pair<String, String>): String {
    val url = BASE_URL + urlPath
    val uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url)
            .queryParam(TOKEN_KEY, TOKEN_VALUE)
    for (parameter in parameters) {
        uriComponentsBuilder.queryParam(parameter.first, parameter.second)
    }
    return uriComponentsBuilder.toUriString()
}

fun createHeaders(): HttpHeaders {
    val headers = HttpHeaders()
    headers.accept = listOf(MediaType.APPLICATION_JSON)
    headers.contentType = MediaType.APPLICATION_JSON
    headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36")
    return headers
}

