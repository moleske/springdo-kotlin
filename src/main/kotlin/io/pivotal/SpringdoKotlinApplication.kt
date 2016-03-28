package io.pivotal

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class SpringdoKotlinApplication

fun main(args: Array<String>) {
    SpringApplication.run(SpringdoKotlinApplication::class.java, *args)
}
