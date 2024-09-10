package com.hdnguyen.resource.burner.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import kotlin.concurrent.thread

@Component
class MemoryResourceManager {

    @Value("\${memory.chunk.size}")
    lateinit var memoryChunkSize: String // Default to 50MB if not specified

    @Volatile private var isRunning = false
    @Volatile private var isPaused = false
    private val memoryHoggers = mutableListOf<Thread>()

    @Synchronized
    fun startMemoryLoad() {
        if (isRunning) return
        isRunning = true
        isPaused = false

        // Start a thread that allocates memory
        val memoryThread = thread(start = true) {
            val memoryBlocks = mutableListOf<ByteArray>()
            while (isRunning) {
                try {
                    if (!isPaused) {
                        // Allocate and fill 50MB chunks of memory
                        val chunk = ByteArray(memoryChunkSize.toInt() * 1024 * 1024) // 50 MB
                        chunk.fill(0) // Fill the array with zeroes or any data
                        memoryBlocks.add(chunk)
                    }
                } catch (e: OutOfMemoryError) {
                    // Handle out of memory situations
                    e.printStackTrace()
                }
                // Sleep to simulate load over time
                try {
                    Thread.sleep(1000) // Sleep for 1 second
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
            }
        }
        memoryHoggers.add(memoryThread)
    }

    @Synchronized
    fun stopMemoryLoad() {
        if (!isRunning) return
        isRunning = false
        isPaused = false

        // Interrupt all memory hogging threads
        memoryHoggers.forEach { it.interrupt() }
        memoryHoggers.clear()
    }

    @Synchronized
    fun pauseMemoryLoad() {
        isPaused = true
    }

    @Synchronized
    fun resumeMemoryLoad() {
        isPaused = false
    }
}
