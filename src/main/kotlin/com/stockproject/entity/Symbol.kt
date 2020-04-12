package com.stockproject.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "T_SYMBOL")
class Symbol(
        @Id
        @GeneratedValue
        val id : Long? = null,

        val symbol : String = "",

        val displaySymbol : String = "",

        val description : String = ""
)