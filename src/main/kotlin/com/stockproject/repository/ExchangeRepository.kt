package com.stockproject.repository

import com.stockproject.entity.Exchange
import com.stockproject.entity.enum.ExchangeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExchangeRepository : JpaRepository<Exchange, Long> {

    fun existsByCode(code: String): Boolean

    fun findByCode(code: String) : Exchange?

}

