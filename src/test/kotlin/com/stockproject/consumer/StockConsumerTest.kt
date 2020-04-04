package com.stockproject.consumer

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class StockConsumerTest {

    @Autowired
    private lateinit var consumer: StockConsumer

    @Test
    fun test1() {
        val result = consumer.getExchanges()
    }

}