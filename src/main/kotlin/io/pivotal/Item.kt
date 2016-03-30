package io.pivotal

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Item(
        var title: String = "",
        var content: String = "",
        var done: String = "no",
        @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long = 0
)