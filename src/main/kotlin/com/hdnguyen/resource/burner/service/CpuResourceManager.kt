package com.hdnguyen.resource.burner.service

import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import org.springframework.stereotype.Component
import java.util.concurrent.RejectedExecutionException

@Component
class CpuResourceManager {

    var cpuSleepTimeMs: Long = 100;

    private val numberOfThreads = Runtime.getRuntime().availableProcessors()
    private val executor: ThreadPoolExecutor = Executors.newFixedThreadPool(numberOfThreads) as ThreadPoolExecutor
    private var isRunning = false

    fun getCpuSleepTime(): Long {
        return cpuSleepTimeMs
    }

    @Synchronized
    fun startCpuLoad() {
        if (isRunning) return
        isRunning = true
        repeat(numberOfThreads) {
            try {
                executor.execute(ControlledCpuLoadTask())
            }
            catch (e: RejectedExecutionException) {
                executor.shutdownNow()
            }

        }
    }

    @Synchronized
    fun stopCpuLoad() {
        if (!isRunning) return
        isRunning = false
        executor.shutdownNow()
    }

    @Synchronized
    fun setSleepTime(sleepTime: Long) {
        cpuSleepTimeMs = sleepTime
    }

    private inner class ControlledCpuLoadTask : Runnable {
        override fun run() {
            while (isRunning) {
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
