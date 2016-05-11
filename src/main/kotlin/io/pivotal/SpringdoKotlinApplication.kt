package io.pivotal

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class SpringdoKotlinApplication : CommandLineRunner {
    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var itemRepository: ItemRepository

    override fun run(vararg args: String?) {
        val defaultUser = userRepository.save(
                User(
                        username = "Navya",
                        password = "secret",
                        email = "n@example.com",
                        isAdmin = true))
        val secondUser = userRepository.save(
                User(
                        username = "Dirk",
                        password = "secret",
                        email = "d@example.com"))

        itemRepository.save(Item("swim", "in the pool", defaultUser))
        itemRepository.save(Item("run", "around the park", defaultUser))
        itemRepository.save(Item("shop", "for new shoes", defaultUser))
        itemRepository.save(Item("drive", "to great viewpoint", secondUser))
        itemRepository.save(Item("sleep", "at least 8 hours", secondUser))
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(SpringdoKotlinApplication::class.java, *args)
}