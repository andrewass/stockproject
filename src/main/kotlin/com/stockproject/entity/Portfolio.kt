package com.stockproject.entity

import javax.persistence.*

@Entity
@Table(name = "T_USER")
class Portfolio(
        @Id
        @GeneratedValue
        val id: Long? = null,

        @ManyToOne
        val user: User? = null,

        @OneToMany(mappedBy = "portfolio", fetch = FetchType.EAGER)
        val investments: List<Investment> = mutableListOf(),

        @Transient
        val funds: Int = 10000
) {
    var remainingFunds: Int = funds

}
