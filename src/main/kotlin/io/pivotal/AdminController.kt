package io.pivotal

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class AdminController(val userRepository: UserRepository, val itemRepository: ItemRepository) {

    @GetMapping("/resource/admin/list/")
    fun adminList(principal: Principal): List<Item> {
        val user = userRepository.findByUsername(principal.name)
        if (user?.isAdmin!!) {
            return itemRepository.findAll().toList()
        } else {
            return user.items
        }
    }

    @GetMapping("/resource/admin/userlist/")
    fun userList(principal: Principal): List<String> {
        val user = userRepository.findByUsername(principal.name)
        val result: MutableList<String> = mutableListOf()
        if (user?.isAdmin!!) {
            userRepository.findAll().forEach { result.add(it.username) }
        }
        return result
    }

    @GetMapping("/resource/user/{username}/")
    fun adminShowUser(principal: Principal, @PathVariable username: String): User? {
        val user = userRepository.findByUsername(principal.name)
        if (user?.isAdmin!!) {
            return userRepository.findByUsername(username)
        } else {
            return null
        }
    }

    @GetMapping("/who/")
    fun whoIsLoggedIn(principal: Principal?): String {
        if (principal != null) {
            // angular only wants json hashes as input, we can create one by hand or use the jackson json
            // library which is already a dependency of spring boot
            return ObjectMapper().writeValueAsString(hashMapOf(Pair("name", principal.name)));
        }
        return "{\"name\":\"\"}";
    }
}