package io.pivotal

import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringRunner::class)
@SpringBootTest
class ListOfItemsControllerTest {

    @Autowired lateinit var itemRepository: ItemRepository
    @Autowired lateinit var context: WebApplicationContext
    lateinit var mvc: MockMvc

    @Before
    fun setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(this.context).build()
    }

    @Test
    fun whenDefaultPlaceholdersLoadedTwoTasksShouldShow() {
        mvc.perform(get("/resource/dummylist/"))
                .andExpect(status().isOk)
                .andDo(print())
                .andExpect(jsonPath("$", hasSize<Any>(2)))
                .andExpect(jsonPath("$[0].id", equalTo("1")))
                .andExpect(jsonPath("$[1].id", equalTo("2")))
                .andExpect(jsonPath("$[0].title", equalTo("Go for a swim")))
                .andExpect(jsonPath("$[1].title", equalTo("Visit farmer's market")))
                .andExpect(jsonPath("$[0].content", equalTo("Go swimming on Monday night")))
                .andExpect(jsonPath("$[1].content", equalTo("Buy dairy and eggs at farmers market")))
    }

    @Test
    fun whenItemIsCheckedAsDoneModelIsUpdated() {
        val item = Item("Fake Todo", "Do Lots of stuff")
        itemRepository.save(item)

        mvc.perform(post(String.format("/resource/done/%d/yes/", item.id)))
                .andDo(print())
                .andExpect(status().isOk)

        val newItem = itemRepository.findOne(item.id)
        assertEquals(item.done, "no")
        assertEquals(newItem.done, "yes")
    }

    @Test
    fun whenEditIsMadeTheModelIsUpdated() {
        val newTitle = "New Test Title"
        val item = Item("Test Todo", "Do Lots of stuff")
        itemRepository.save(item)

        mvc.perform(post(String.format("/resource/save/%d/%s/%s/no/", item.id, newTitle, item.content)))
                .andExpect(status().isOk)

        assertNotEquals(item.title, newTitle)
        val newItem = itemRepository.findOne(item.id)
        assertEquals(newItem.title, newTitle)
        assertEquals(newItem.content, item.content)
    }

    @Test
    @WithMockUser("Navya")
    fun whenCreateIsHitANewItemIsCreatedAndReturned() {
        mvc.perform(post("/resource/create/"))
                .andExpect(status().isOk)
                .andDo(print())
                .andExpect(jsonPath("$.title", isEmptyString()))
                .andExpect(jsonPath("$.content", isEmptyString()))
                .andExpect(jsonPath("$.done", equalTo("no")))
    }

    @Test
    fun whenDeleteIsHitItemIsTrashed() {
        val item = Item("Test Todo", "Do Lots of stuff - then delete")
        itemRepository.save(item)

        mvc.perform(post(String.format("/resource/delete/%d/", item.id)))
                .andExpect(status().isOk)

        val newItem = itemRepository.findOne(item.id)  // returns Null if not found
        assertNull(newItem)
    }
}