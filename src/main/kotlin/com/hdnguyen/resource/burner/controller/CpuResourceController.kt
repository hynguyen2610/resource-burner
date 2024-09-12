package com.hdnguyen.resource.burner.controller

import com.hdnguyen.resource.burner.service.CpuResourceManager
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cpu")
class CpuResourceController (private val cpuResourceManager: CpuResourceManager) {

    @PostMapping("/start-load")
    fun startCpuLoad(): ResponseEntity<String> {
        cpuResourceManager.startCpuLoad()  // Call the method on the injected instance
        val message = String.format("CPU load started with sleep time %s", cpuResourceManager.getCpuSleepTime())
        return ResponseEntity(message, HttpStatus.OK)
    }

    @PostMapping("/resume-load")
    fun pauseCpuLoad(): ResponseEntity<String> {
        cpuResourceManager.resumeCpuLoad()  // Call the method on the injected instance
        return ResponseEntity("CPU load paused", HttpStatus.OK)
    }

    @PostMapping("/stop-load")
    fun stopCpuLoad(): ResponseEntity<String> {
        cpuResourceManager.stopCpuLoad()  // Call the method on the injected instance
        return ResponseEntity("CPU load stopped", HttpStatus.OK)
    }

    @PostMapping("/sleepTime")
    fun setSleepTime(@RequestBody sleepTimeRequest: SleepTimeRequest): ResponseEntity<String> {
        cpuResourceManager.setSleepTime(sleepTimeRequest.sleepTime.toLong())  // Call the method on the injected instance
        return ResponseEntity("CPU sleep time set to ${sleepTimeRequest.sleepTime}", HttpStatus.OK)
    }
}

data class SleepTimeRequest(val sleepTime: String)

