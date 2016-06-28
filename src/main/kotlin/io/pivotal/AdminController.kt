package io.pivotal

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class AdminController @Autowired constructor(val userRepository: UserRepository, val itemRepository: ItemRepository) {

    @RequestMapping(value = "/resource/admin/list/", method = arrayOf(RequestMethod.GET))
    fun adminList(principal: Principal): List<Item> {
        val user = userRepository.findByUsername(principal.name)
        if (user?.isAdmin!!) {
            return itemRepository.findAll().toList()
        } else if (user != null){
            return user.items;
        }
        return emptyList()
    }

    @RequestMapping(value = "/resource/admin/userlist/", method = arrayOf(RequestMethod.GET))
    fun userList(principal: Principal): List<String> {
        val user = userRepository.findByUsername(principal.name)
        val result:MutableList<String> = mutableListOf()
        if (user?.isAdmin!!) {
            userRepository.findAll().forEach { user -> result.add(user.username)}
        }
        return result
    }

    @RequestMapping(value = "/resource/user/{username}/", method = arrayOf(RequestMethod.GET))
    fun adminShowUser(principal: Principal, @PathVariable username: String): User? {
        val user = userRepository.findByUsername(principal.name)
        if (user?.isAdmin!!) {
            return userRepository.findByUsername(username)
        } else {
            return null
        }
    }

    @RequestMapping(value="/who/")
    fun whoIsLoggedIn(principal: Principal?): String{
        if (principal != null) {
            // angular only wants json hashes as input, we can create one by hand or use the jackson json
            // library which is already a dependency of spring boot
            return ObjectMapper().writeValueAsString(hashMapOf(Pair("name", principal.name)));
        }
        return "{\"name\":\"\"}";
    }
}