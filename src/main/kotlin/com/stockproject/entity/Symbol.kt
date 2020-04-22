package com.stockproject.entity

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
        var exchange: Exchange? = null
) {
    private var hits = 0

    fun addHits() {
        hits++
    }
}