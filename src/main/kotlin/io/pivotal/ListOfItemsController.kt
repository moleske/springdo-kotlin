package io.pivotal

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class ListOfItemsController @Autowired constructor(val itemRepository: ItemRepository, val userRepository: UserRepository) {

    @RequestMapping(value = "/resource/list/", method = arrayOf(RequestMethod.GET))
    fun listOfItems(principal: Principal): MutableIterable<Item>? {
        val user = userRepository.findByUsername(principal.name)
        return itemRepository.findByUser(user)
    }

    @RequestMapping(value = "/resource/dummylist/", method = arrayOf(RequestMethod.GET))
    fun listOfDummyItems() = listOf(
            mapOf(
                    "id" to "1",
                    "title" to "Go for a swim",
                    "content" to "Go swimming on Monday night"),
            mapOf(
                    "id" to "2",
                    "title" to "Visit farmer's market",
                    "content" to "Buy dairy and eggs at farmers market"))

    @RequestMapping(value = "/resource/done/{id}/{done}/", method = arrayOf(RequestMethod.POST))
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

    @RequestMapping(value = "/resource/save/{id}/{title}/{content}/{done}/", method = arrayOf(RequestMethod.POST))
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

    @RequestMapping(value = "/resource/create/", method = arrayOf(RequestMethod.POST))
    fun postCreate(principal: Principal?): Item? {
        val principal2 = fixPrincipalForTest(principal)
        val user = userRepository.findByUsername(principal2.name)
        return itemRepository.save(Item(user = user))
    }

    @RequestMapping(value = "/resource/delete/{id}")
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