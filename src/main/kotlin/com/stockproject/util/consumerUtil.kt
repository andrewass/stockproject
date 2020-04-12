package com.stockproject.util

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.util.UriComponentsBuilder


const val BASE_URL = "https://finnhub.io/api/v1/"
const val TOKEN_KEY = "token"

val TOKEN_VALUE : String = System.getenv("FINNHUB_API_KEY")

val STOCK_EXCHANGE_URL = UriComponentsBuilder
        .fromUriString(BASE_URL + "stock/exchange")
        .queryParam(TOKEN_KEY, TOKEN_VALUE)
        .build().toUriString()


val CRYPTO_EXCHANGE_URL = UriComponentsBuilder
        .fromUriString(BASE_URL + "crypto/exchange")
        .queryParam(TOKEN_KEY, TOKEN_VALUE)
        .build().toUriString()

fun createHeaders(): HttpHeaders {
    val headers = HttpHeaders()
    headers.accept = listOf(MediaType.APPLICATION_JSON)
    headers.contentType = MediaType.APPLICATION_JSON
    headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36")
    return headers
}
