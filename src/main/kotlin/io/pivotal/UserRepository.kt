package io.pivotal

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component

@Component
interface UserRepository : CrudRepository<User, Long> {
    fun findByUsername(username: String?): User?
}