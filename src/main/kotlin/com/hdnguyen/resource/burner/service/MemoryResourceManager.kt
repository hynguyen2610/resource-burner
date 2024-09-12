package com.hdnguyen.resource.burner.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import kotlin.concurrent.thread

@Component
class MemoryResourceManager : BaseService() {

    @Value("\${memory.chunk.size:50}")
    private var memoryChunkSize: Int = 50 // Default to 50MB if not specified

    @Volatile private var isRunning = false
    @Volatile private var isPaused = false
    private val memoryHoggers = mutableListOf<Thread>()
    private var memoryBlocks = mutableListOf<ByteArray>()

    @Synchronized
    fun startMemoryLoad() {
        if (isRunning) return
        logger.info("Memory load started")
        isRunning = true
        isPaused = false

        // Start a thread that allocates memory
        val memoryThread = thread(start = true) {
            try {
                memoryBlocks = mutableListOf() // Reinitialize memoryBlocks
                while (isRunning) {
                    if (!isPaused) {
                        try {
                            // Allocate memory chunk (size specified)
                            val chunk = ByteArray(memoryChunkSize * 1024 * 1024)
                            chunk.fill(0) // Fill the array with zeroes or any data
                            memoryBlocks.add(chunk)
                        } catch (e: OutOfMemoryError) {
                            // Handle out of memory situations
                            e.printStackTrace()
                            stopMemoryLoad() // Stop memory load when OOM occurs
                        }
                    } else {
                        // Sleep when paused
                        Thread.sleep(1000) // Sleep for 1 second
                    }
                }
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
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

        // Clear the allocated memory to free up resources
        memoryBlocks.clear()
        System.gc() // Suggest GC to reclaim memory (note that GC is not guaranteed)
        logger.info("Memory load stopped")
    }

    @Synchronized
    fun pauseMemoryLoad() {
        isPaused = true
        logger.info("Memory load paused")
    }

    @Synchronized
    fun resumeMemoryLoad() {
        isPaused = false
        logger.info("Memory load resumed")
    }

    @Synchronized
    fun updateMemoryChunkSize(newSizeMB: Int) {
        memoryChunkSize = newSizeMB
    }
}
