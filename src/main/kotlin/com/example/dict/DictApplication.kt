package com.example.dict

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DictApplication

fun main(args: Array<String>) {
	runApplication<DictApplication>(*args)
}
