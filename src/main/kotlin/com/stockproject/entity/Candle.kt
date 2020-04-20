package com.stockproject.entity

import javax.persistence.*

@Entity
@Table(name = "T_CANDLE")
class Candle(
        @Id
        @GeneratedValue
        val id : Long? = null,

        @JoinColumn(name = "SYMBOL")
        @ManyToOne
        val symbol: Symbol
){

}