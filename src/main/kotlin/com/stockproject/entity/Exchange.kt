package com.stockproject.entity

import com.stockproject.entity.enum.ExchangeType
import javax.persistence.*

@Entity
@Table(name = "T_EXCHANGE")
class Exchange(
        @Id
        @GeneratedValue
        val id: Long? = null,

        val exchangeName: String = "",

        val code: String = "",

        val currency: String = "",

        @Enumerated(EnumType.STRING)
        val exchangeType: ExchangeType? = null
) {

    override fun equals(other: Any?): Boolean {
        if (other is Exchange) {
            return other.code == code
                    && other.currency == currency
                    && other.exchangeName == exchangeName
                    && other.exchangeType == exchangeType
        }
        return false
    }
}