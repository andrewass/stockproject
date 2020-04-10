package com.stockproject.util

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

const val BASE_URL = "https://finnhub.io/api/v1/stock/"

const val EXCHANGE_URL = "$BASE_URL+exchange?token="


fun createHeaders(): HttpHeaders {
    val headers = HttpHeaders()
    headers.accept = listOf(MediaType.APPLICATION_JSON)
    headers.contentType = MediaType.APPLICATION_JSON
    headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36")
    return headers
}