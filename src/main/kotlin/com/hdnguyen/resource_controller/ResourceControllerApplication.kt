package com.hdnguyen.resource_controller

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ResourceControllerApplication

fun main(args: Array<String>) {
	runApplication<ResourceControllerApplication>(*args)
	println("Resource controller is running")
}
