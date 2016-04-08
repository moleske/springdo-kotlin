package io.pivotal

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class ListOfItemsController @Autowired constructor(val itemRepository: ItemRepository) {

    @RequestMapping(value = "/resource/list/", method = arrayOf(RequestMethod.GET))
    fun listOfItems() = itemRepository.findAll()

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

    @RequestMapping(value = "/resource/done/{id}/{state}/", method = arrayOf(RequestMethod.POST))
    fun postDoneUpdate(@PathVariable id: Long, @PathVariable state: String): String {
        val item = itemRepository.findOne(id)
        if ( state.equals("yes") or state.equals("no")) {
            item.done = state
        } else {
            println("Invalid argument to postDoneUpdate:  " + state)
        }
        itemRepository.save(item)
        return "[\"ok\"]"
    }

    @RequestMapping(value = "/resource/save/{id}/{title}/{content}/{done}/", method = arrayOf(RequestMethod.POST))
    fun postSaveUpdate(@PathVariable id: Long, @PathVariable title: String, @PathVariable content: String, @PathVariable done: String): String {
        val item = itemRepository.findOne(id)
        if ( done.equals("yes") or done.equals("no")) {
            item.done = done
        } else {
            println("Invalid argument to postDoneUpdate:  " + done)
        }
        item.title = title
        item.content = content
        itemRepository.save(item)
        return "[\"ok\"]"
    }
}