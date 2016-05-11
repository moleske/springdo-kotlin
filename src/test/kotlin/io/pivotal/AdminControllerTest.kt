package io.pivotal

import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder

@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(SpringdoKotlinApplication::class))
@WebAppConfiguration
class AdminControllerTest {

    @Autowired lateinit var context: WebApplicationContext
    lateinit var mvc: MockMvc

    @Before
    fun setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply<DefaultMockMvcBuilder>(springSecurity())
                .build()
    }

    @Test
    @WithMockUser("Navya")
    fun adminList_returnsAListOfAllItemsForAdmin() {
        mvc.perform(get("/resource/admin/list/"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$", hasSize<Any>(5)))
                .andExpect(jsonPath("$[0].title", equalTo("swim")))
    }

    @Test
    @WithMockUser("Dirk")
    fun adminList_returnsAListOfOwnItemsForUser() {
        mvc.perform(get("/resource/admin/list/"))
                .andExpect(status().isOk)
                .andDo(print())
                .andExpect(jsonPath("$", hasSize<Any>(2)))
                .andExpect(jsonPath("$[1].title", equalTo("sleep")))
    }

    @Test
    @WithMockUser("Navya")
    fun userList_returnsTwoUserForAdmin() {
        mvc.perform(get("/resource/admin/userlist/"))
                .andExpect(status().isOk)
                .andDo(print())
                .andExpect(jsonPath("$", hasSize<Any>(2)))
    }

    @Test
    @WithMockUser("Dirk")
    fun userListTest_returnsNoItemsForUser() {
        mvc.perform(get("/resource/admin/userlist/"))
                .andExpect(status().isOk);
    }

    @Test
    @WithMockUser("Navya")
    fun adminShowUser_worksForAdmin() {
        mvc.perform(get("/resource/user/Dirk/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", equalTo("Dirk")))
                .andExpect(jsonPath("$.email", equalTo("d@example.com")))
    }

    @Test
    fun adminShowUser_failsWithoutAuthentication() {
        mvc.perform(get("/resource/user/Navya/"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated())
    }

    @Test
    @WithMockUser("Dirk")
    fun whoIsLoggedIn_returnsUsernameWhenLoggedIn() {
        mvc.perform(get("/who/"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.name", equalTo("Dirk")))

    }

    @Test
    fun whoIsLoggedIn_returnsEmptyOtherwise() {
        mvc.perform(get("/who/"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.name", isEmptyString()))
    }
}