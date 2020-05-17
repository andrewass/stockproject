package com.stockproject.consumer.util

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.util.UriComponentsBuilder


private const val BASE_URL = "https://finnhub.io/api/v1/"
private const val TOKEN_KEY = "token"
private val TOKEN_VALUE: String = System.getenv("FINNHUB_API_KEY")

const val STOCK_EXCHANGE_PATH = "stock/exchange"
const val CRYPTO_EXCHANGE_PATH = "crypto/exchange"
const val FOREX_EXCHANGE_PATH = "forex/exchange"

const val STOCK_SYMBOL_PATH = "stock/symbol"
const val CRYPTO_SYMBOL_PATH = "crypto/symbol"
const val FOREX_SYMBOL_PATH = "forex/symbol"

const val STOCK_CANDLE_PATH = "stock/candle"
const val CRYPTO_CANDLE_PATH = "crypto/candle"
const val FOREX_CANDLE_PATH = "forex/candle"

fun createURI(urlPath: String, vararg parameters: Pair<String, String>): String {
    val url = BASE_URL + urlPath
    val uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url).queryParam(TOKEN_KEY, TOKEN_VALUE)
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

