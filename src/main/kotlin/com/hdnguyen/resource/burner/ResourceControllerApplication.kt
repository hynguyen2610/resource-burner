package com.hdnguyen.resource.burner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ResourceControllerApplication

fun main(args: Array<String>) {
	runApplication<ResourceControllerApplication>(*args)
	println("Resource Burner is running")
}
