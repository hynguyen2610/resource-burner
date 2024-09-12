package com.hdnguyen.resource.burner.controller

import com.hdnguyen.resource.burner.service.BaseService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/misc")
class MiscController {

    @GetMapping("/version")
    fun version(): ResponseEntity<String> {
        val packageVersion = MiscController::class.java.`package`.implementationVersion
        return ResponseEntity(packageVersion ?: "Version not found", HttpStatus.OK)
    }
}