package com.stockproject.service

import com.stockproject.entity.Exchange
import com.stockproject.repository.StockRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StockService @Autowired constructor(
        stockRepository : StockRepository

){

    fun getExchanges() : List<Exchange>{
        return emptyList()
    }

}