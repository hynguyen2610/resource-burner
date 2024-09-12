package com.hdnguyen.resource.burner.service

import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import org.springframework.stereotype.Component
import java.util.concurrent.RejectedExecutionException

@Component
class CpuResourceManager : BaseService() {

    var cpuSleepTimeMs: Long = 100

    private val numberOfThreads = Runtime.getRuntime().availableProcessors()
    private val executor: ThreadPoolExecutor = Executors.newFixedThreadPool(numberOfThreads) as ThreadPoolExecutor
    private var isRunning = false
    private var isPaused = false

    fun getCpuSleepTime(): Long {
        return cpuSleepTimeMs
    }

    @Synchronized
    fun startCpuLoad() {
        if (isRunning) return
        logger.info("CPU Load started")
        isRunning = true
        isPaused = false
        repeat(numberOfThreads) {
            try {
                executor.execute(ControlledCpuLoadTask())
            } catch (e: RejectedExecutionException) {
                executor.shutdownNow()
            }
        }
    }

    @Synchronized
    fun stopCpuLoad() {
        if (!isRunning) return
        logger.info("CPU Load stopped")
        isRunning = false
        executor.shutdownNow()
    }

    @Synchronized
    fun pauseCpuLoad() {
        if (!isRunning || isPaused) return
        logger.info("CPU Load paused")
        isPaused = true
    }

    @Synchronized
    fun resumeCpuLoad() {
        if (!isRunning || !isPaused) return
        logger.info("CPU Load resumed")
        isPaused = false
        repeat(numberOfThreads) {
            try {
                executor.execute(ControlledCpuLoadTask())
            } catch (e: RejectedExecutionException) {
                executor.shutdownNow()
            }
        }
    }

    @Synchronized
    fun setSleepTime(sleepTime: Long) {
        cpuSleepTimeMs = sleepTime
        logger.info("CPU Load sleep time set to {}", sleepTime)
    }

    private inner class ControlledCpuLoadTask : Runnable {
        override fun run() {
            while (isRunning) {
                if (isPaused) {
                    try {
                        Thread.sleep(100) // Sleep briefly while paused
                    } catch (e: InterruptedException) {
                        Thread.currentThread().interrupt()
                    }
                    continue
                }

                val startTime = System.nanoTime()
                // Busy work
                while (System.nanoTime() - startTime < 1_000_000_000) { // Busy for 1 second
                    val result = Math.sqrt(Math.random())
                }
                // Sleep to reduce CPU load
                try {
                    Thread.sleep(cpuSleepTimeMs) // Sleep for 100 milliseconds
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
            }
        }
    }
}

