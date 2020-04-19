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
}