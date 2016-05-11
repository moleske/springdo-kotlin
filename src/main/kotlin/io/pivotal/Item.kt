package io.pivotal

import javax.persistence.*

@Entity
data class Item(
        var title: String = "",
        var content: String = "",
        @ManyToOne var user: User? = null,
        var done: String = "no",
        @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long = 0
)