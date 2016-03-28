package io.pivotal

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class ListOfItemsController {
    @RequestMapping(value = "/", method = arrayOf(RequestMethod.GET))
    fun mainPage() = "<html><body><h1>List of ToDO items</h1></body></html>"
}
