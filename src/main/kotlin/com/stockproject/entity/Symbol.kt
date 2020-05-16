package com.stockproject.entity

import com.stockproject.entity.enum.ExchangeType
import javax.persistence.*

@Entity
@Table(name = "T_SYMBOL")
class Symbol(
        @Id
        @GeneratedValue
        val id: Long? = null,

        val symbol: String = "",

        val displaySymbol: String = "",

        val description: String = "",

        @ManyToOne
        @JoinColumn(name = "EXCHANGE")
        var exchange: Exchange? = null
) {
    private var hits = 0

    fun addSingleHit() {
        hits++
    }

    override fun hashCode(): Int {
        return symbol.hashCode() + exchange!!.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if(other is Symbol){
            return other.symbol == symbol && other.exchange!! == exchange
        }
        return false
    }
}