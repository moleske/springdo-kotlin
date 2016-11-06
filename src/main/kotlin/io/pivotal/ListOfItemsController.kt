package io.pivotal

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class ListOfItemsController(val itemRepository: ItemRepository, val userRepository: UserRepository) {

    @GetMapping("/resource/list/")
    fun listOfItems(principal: Principal): MutableIterable<Item>? {
        val user = userRepository.findByUsername(principal.name)
        return itemRepository.findByUser(user)
    }

    @GetMapping("/resource/dummylist/")
    fun listOfDummyItems() = listOf(
            mapOf(
                    "id" to "1",
                    "title" to "Go for a swim",
                    "content" to "Go swimming on Monday night"),
            mapOf(
                    "id" to "2",
                    "title" to "Visit farmer's market",
                    "content" to "Buy dairy and eggs at farmers market"))

    @PostMapping("/resource/done/{id}/{done}/")
    fun postDoneUpdate(@PathVariable id: Long, @PathVariable done: String): String {
        val item = itemRepository.findOne(id)
        if (done.equals("yes") or done.equals("no")) {
            item.done = done
        } else {
            println("Invalid argument to postDoneUpdate:  " + done)
        }
        itemRepository.save(item)
        return "[\"ok\"]"
    }

    @PostMapping("/resource/save/{id}/{title}/{content}/{done}/")
    fun postSaveUpdate(@PathVariable id: Long, @PathVariable title: String, @PathVariable content: String, @PathVariable done: String): String {
        val item = itemRepository.findOne(id)
        if (done.equals("yes") or done.equals("no")) {
            item.done = done
        } else {
            println("Invalid argument to postSaveUpdate:  " + done)
        }
        item.title = title
        item.content = content
        itemRepository.save(item)
        return "[\"ok\"]"
    }

    @PostMapping("/resource/create/")
    fun postCreate(principal: Principal?): Item? {
        val principal2 = fixPrincipalForTest(principal)
        val user = userRepository.findByUsername(principal2.name)
        return itemRepository.save(Item(user = user))
    }

    @PostMapping("/resource/delete/{id}")
    fun deleteItem(@PathVariable id: Long): String {
        itemRepository.delete(id)
        return "[\"ok\"]"
    }

    private fun fixPrincipalForTest(principal: Principal?): Principal {
        if (principal == null) {
            val principal2: Principal? = SecurityContextHolder.getContext().authentication
            if (principal2 == null) {
                println("unauthorized or principal is null")
            }
            return principal2!!
        }
        return principal
    }
}