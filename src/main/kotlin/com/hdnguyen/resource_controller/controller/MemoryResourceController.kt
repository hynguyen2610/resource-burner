package com.hdnguyen.resource_controller.controller

import com.hdnguyen.resource_controller.service.MemoryResourceManager
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus

@RestController
@RequestMapping("/memory")
class MemoryResourceController(private var memoryResourceManager: MemoryResourceManager) {

    @PostMapping("/start-load")
    fun startMemoryLoad(): ResponseEntity<String> {
        memoryResourceManager.startMemoryLoad()
        return ResponseEntity("Memory load started", HttpStatus.OK)
    }

    @PostMapping("/stop-load")
    fun stopMemoryLoad(): ResponseEntity<String> {
        memoryResourceManager.stopMemoryLoad()
        return ResponseEntity("Memory load stopped", HttpStatus.OK)
    }

    @PostMapping("/pause-load")
    fun pauseMemoryLoad(): ResponseEntity<String> {
        memoryResourceManager.pauseMemoryLoad()
        return ResponseEntity("Memory load paused", HttpStatus.OK)
    }

    @PostMapping("/resume-load")
    fun resumeMemoryLoad(): ResponseEntity<String> {
        memoryResourceManager.resumeMemoryLoad()
        return ResponseEntity("Memory load resumed", HttpStatus.OK)
    }

    @PostMapping("/gc")
    fun forceGarbageCollecting(): ResponseEntity<String> {
        Runtime.getRuntime().gc();
        return ResponseEntity("Forced GC!", HttpStatus.OK)
    }
}
