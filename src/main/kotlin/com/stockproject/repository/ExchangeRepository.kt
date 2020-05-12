package com.stockproject.repository

import com.stockproject.entity.Exchange
import com.stockproject.entity.enum.ExchangeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExchangeRepository : JpaRepository<Exchange, Long> {

    fun findByCode(code : String) : Exchange?

    fun findByExchangeName(exchangeName : String) : Exchange?

}

