package io.pivotal

import org.springframework.data.repository.CrudRepository

interface ItemRepository : CrudRepository<Item, Long>{
    fun findByUser(user: User?): MutableIterable<Item>
}