package io.pivotal

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
open class SpringdoKotlinApplication {
    @Bean
    open fun init(itemRepository: ItemRepository) = CommandLineRunner {
        itemRepository.save(Item("swim", "in the pool"))
        itemRepository.save(Item("run", "around the park"))
        itemRepository.save(Item("shop", "for new shoes"))
        itemRepository.save(Item("drive", "to great viewpoint"))
        itemRepository.save(Item("sleep", "at least 8 hours"))
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(SpringdoKotlinApplication::class.java, *args)
}
