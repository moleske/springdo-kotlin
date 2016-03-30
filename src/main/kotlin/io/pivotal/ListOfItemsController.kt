package io.pivotal

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ListOfItemsController {

    @RequestMapping("/resource/list")
    fun simpleMinded() = listOf(
            mapOf(
                    "id" to "1",
                    "title" to "Go for a swim",
                    "content" to "Go swimming on Monday night"),
            mapOf(
                    "id" to "2",
                    "title" to "Visit farmer's market",
                    "content" to "Buy dairy and eggs at farmers market"))
}
