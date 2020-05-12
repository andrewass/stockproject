package com.stockproject.entity

import com.fasterxml.jackson.annotation.JsonIgnore
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
    @JsonIgnore
    @OneToMany(mappedBy = "exchange", cascade = [CascadeType.ALL])
    val symbols = mutableListOf<Symbol>()

    override fun equals(other: Any?): Boolean {
        if (other is Exchange) {
            return other.exchangeName == exchangeName
                    && other.code == code
        }
        return false
    }

    override fun hashCode() = exchangeName.hashCode() + code.hashCode()
}