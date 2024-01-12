package com.demo.sync

import android.util.Log
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.demo.base.log.MyLog
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Thread.sleep
import java.util.concurrent.Callable
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionService
import java.util.concurrent.ExecutorCompletionService
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.FutureTask
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock

/**
 * Created by lizhiping on 2023/4/2.
 *
 *
 * 本类为了实现如下示例：
 * Task1、Task2 等多个并行任务，如何等待全部执行完成后，执行 Task3
 *
 * 参考：[...](https://juejin.cn/post/6981952428786597902)
 */

private const val TAG = "SyncManager"

val task1: () -> String = {
    sleep(6_000L)
    "Hello".also {
        val msg = "task1 finished $it\n"
        MyLog.i(TAG, msg)
//            appendText(msg)
    }
}

val task2: () -> String = {
    sleep(3_000L)
    "World".also {
        val msg = "task2 finished $it\n"
        MyLog.i(TAG, msg)
//            appendText(msg)
    }
}

val task3: (String, String) -> String = { p1, p2 ->
    sleep(100L)
    "$p1 $p2".also {
        val msg = "task3 finished $it\n"
        MyLog.i(TAG, msg)
//            appendText(msg)
    }
}

val task4: (List<String>) -> String = {
    sleep(1_000L)
    it.reduce { acc, s ->
        "$s, $acc"
    }.apply {
        val msg = "task4 finished $this\n"
        MyLog.i(TAG, msg)
    }
}

val task5: (String) -> String = {
    sleep(1_000L)
    it.also {
        val msg = "task5 finished $it\n"
        MyLog.i(TAG, msg)
    }
}

class SyncManager(private val textView: TextView, private val fragment: Fragment) {

    companion object {
        private const val TAG = "SyncManager"
    }

    fun startTest() {
        clearText()
//        testThreadJoin()
//        testSynchronized()
//        testReentrantLock()
//        testBlockingQueue()
//        testCoroutine()
//        testFuture()
//        testCompletableFuture()

//        testFutureGeek()
//        testFutureTaskGeek()
        testFutureTask2Geek()
//        testCompletableFutureGeek()
//        testCompletionServiceGeek()
    }

    // 1. Thread.join
    private fun testThreadJoin(start: Long = System.currentTimeMillis()) {
        lateinit var s1: String
        lateinit var s2: String

        val t1 = Thread { s1 = task1() }
        val t2 = Thread { s2 = task2() }

        t1.start()
        t2.start()

        t1.join()
        t2.join()

        appendText("[testThreadJoin] cost: ${MyLog.getTimeCost(start)}ms, ${task3(s1, s2)}")
    }

    // 2. Synchronized
    private fun testSynchronized(start: Long = System.currentTimeMillis()) {
        lateinit var s1: String
        lateinit var s2: String

        Thread {
            synchronized(Unit) {
                s1 = task1()
            }
        }.start()

        s2 = task2()

        synchronized(Unit) {
            appendText("[testSynchronized] cost: ${MyLog.getTimeCost(start)}ms, ${task3(s1, s2)}")
        }

    }

    // 3. ReentrantLock
    private fun testReentrantLock(start: Long = System.currentTimeMillis()) {
        lateinit var s1: String
        lateinit var s2: String

        val lock = ReentrantLock()
        Thread {
            lock.lock()
            s1 = task1()
            lock.unlock()
        }.start()
        s2 = task2()

        lock.lock()
        appendText("[testReentrantLock] cost: ${MyLog.getTimeCost(start)}ms, ${task3(s1, s2)}")
        lock.unlock()
    }

    // 4. BlockingQueue
    private fun testBlockingQueue(start: Long = System.currentTimeMillis()) {
        lateinit var s1: String
        lateinit var s2: String

        val queue = SynchronousQueue<Unit>()

        Thread {
            s1 = task1()
            queue.put(Unit)
        }.start()

        s2 = task2()

        queue.take()
        task3(s1, s2)

        appendText("[testBlockingQueue] cost: ${MyLog.getTimeCost(start)}ms, ${task3(s1, s2)}")
    }

    // 5. CountDownLatch
    private fun testCountDownLatch(start: Long = System.currentTimeMillis()) {}

    // 6. CyclicBarrier
    private fun testCyclicBarrier(start: Long = System.currentTimeMillis()) {}

    // 7. CAS
    private fun testCAS(start: Long = System.currentTimeMillis()) {}

    // 8. Future
    private fun testFuture(start: Long = System.currentTimeMillis()) {
        val future1 = FutureTask(Callable(task1))
        val future2 = FutureTask(Callable(task2))
        Executors.newCachedThreadPool().execute(future1)
        Executors.newCachedThreadPool().execute(future2)

        appendText(
            "[testFuture] cost: ${MyLog.getTimeCost(start)}ms, ${
                task3(
                    future1.get(),
                    future2.get()
                )
            }"
        )
    }

    private val futureExecutor: ExecutorService = Executors.newFixedThreadPool(3)
    // 9. CompletableFuture
    private fun testCompletableFuture(start: Long = System.currentTimeMillis()) {

        val f1: CompletableFuture<String> = CompletableFuture.supplyAsync(task1, futureExecutor)
        val f2: CompletableFuture<String> = CompletableFuture.supplyAsync(task2, futureExecutor)
        val thenCombine = f1
            .thenCombine(f2) { p1, p2 ->
                task3(p1, p2)
            }.exceptionally { "null" }

        appendText("[testCompletableFuture] result: ${thenCombine.join()}, cost: ${MyLog.getTimeCost(start)}ms")
    }

    // 10. Rxjava
    private fun testsRxjava(start: Long = System.currentTimeMillis()) {}

    // 11. Coroutine
    private suspend fun testCoroutine(start: Long = System.currentTimeMillis()) {
        runBlocking {
//            val c1: Deferred<String> = async(Dispatchers.IO) {
//                task1()
//            }
//            val c2 = async(Dispatchers.IO) {
//                task2()
//            }
            val asyncList = mutableListOf<Deferred<String>>().apply {
                repeat(10) {
                    add(async(Dispatchers.IO) {
                        task1()
                    })
                }
            }
            val awaitAllResults: List<String> = asyncList.awaitAll()
            val result = awaitAllResults.reduce { acc, s ->
                "$s, $acc"
            }

//            val await1 = c1.await()
//            val await2 = c2.await()
//            appendText("[testCoroutine] result: ${task3(await1, await2)}, timeCost: ${MyLog.getTimeCost(start)}")
            appendText(
                "[testCoroutine] result: ${task5(result)}, timeCost: ${
                    MyLog.getTimeCost(
                        start
                    )
                }ms"
            )
        }
    }

    private fun testCoroutine2(start: Long = System.currentTimeMillis()) {
        fragment.lifecycleScope.launch(Dispatchers.Main) {
            val result = mutableListOf<Pair<Int, Deferred<String>>>().apply {
                repeat(10) {
                    add(it to async(Dispatchers.IO) {
                        task1()
                    })
                }
            }.map {
                it.first to it.second.await()
            }.map {
                it.second
            }.reduce { acc, s ->
                "$s, $acc"
            }
            appendText(
                "[testCoroutine] result: ${task5(result)}, timeCost: ${
                    MyLog.getTimeCost(
                        start
                    )
                }ms"
            )
        }
    }

    // 12. Flow
    private fun testFlow(start: Long = System.currentTimeMillis()) {}

    // --- 极客时间-Java并发编程实战 ---
    // 适合做批量任务
    private fun testFutureGeek(start: Long = System.currentTimeMillis()) {
        val futures = mutableListOf<Future<String>>()
        val executor = Executors.newFixedThreadPool(10)
        repeat(10) {
            futures.add(executor.submit(task1))
        }

        val fStart = System.currentTimeMillis()
        val results: List<String> = futures.map {
            runCatching {
                it.get(4000, TimeUnit.MILLISECONDS).also {
                    appendText("[testFutureTaskGeek] get: $it, cost: ${MyLog.getTimeCost(fStart)}ms")
                }
            }.getOrElse {
                "null".also {
                    appendText("[testFutureTaskGeek] error: $it, cost: ${MyLog.getTimeCost(fStart)}ms")
                }
            }
        }
        val reduce = results.reduce { acc, s ->
            "$acc, $s"
        }
        appendText("[testFutureGeek] result: $reduce, cost: ${MyLog.getTimeCost(start)}ms")
    }

    private fun testFutureTaskGeek(start: Long = System.currentTimeMillis()) {
        val ft = mutableListOf<FutureTask<String>>()
        val executor = Executors.newFixedThreadPool(10)
        repeat(10) {
            ft.add(FutureTask(T1Task(it)))
        }

        ft.forEach {
            executor.execute(it)
        }

        val ftsStart = System.currentTimeMillis()
        val results: List<String> = ft.map {
            runCatching {
                it.get(5_000, TimeUnit.MILLISECONDS).also {
                    appendText("[testFutureTaskGeek] get: $it, cost: ${MyLog.getTimeCost(ftsStart)}ms")
                }
            }.getOrElse {
                "error".also {
                    appendText("[testFutureTaskGeek] error: $it, cost: ${MyLog.getTimeCost(ftsStart)}ms")
                }
            }
        }

        val reduce = results.reduce { acc, s ->
            "$acc, $s"
        }
        appendText("[testFutureTaskGeek] result: $reduce, cost: ${MyLog.getTimeCost(start)}ms")
    }

    private fun testFutureTask2Geek(start: Long = System.currentTimeMillis()) {
        val executor = Executors.newFixedThreadPool(3)
        val futureTask1 = FutureTask(FutureTask1())
        val futureTask2 = FutureTask(FutureTask2())
        val futureTask3 = FutureTask(FutureTask3())


        executor.submit(futureTask1)
        executor.submit(futureTask2)
        executor.submit(futureTask3)

        val list = mutableListOf<Int>()

        val ftsStart1 = System.currentTimeMillis()
        val result1 = runCatching {
            futureTask1.get(5_000, TimeUnit.MILLISECONDS).also {
                appendText("[testFutureTask2Geek] get: $it, cost: ${MyLog.getTimeCost(ftsStart1)}ms")
            }
        }.getOrElse {
            "error".also {
                appendText("[testFutureTask2Geek] error: $it, cost: ${MyLog.getTimeCost(ftsStart1)}ms")
            }
        }

        val ftsStart2 = System.currentTimeMillis()
        val result2 = runCatching {
            futureTask2.get(5_000, TimeUnit.MILLISECONDS).also {
                appendText("[testFutureTask2Geek] get: $it, cost: ${MyLog.getTimeCost(ftsStart2)}ms")
            }
        }.getOrElse {
            "error".also {
                appendText("[testFutureTask2Geek] error: $it, cost: ${MyLog.getTimeCost(ftsStart2)}ms")
            }
        }

        val ftsStart3 = System.currentTimeMillis()
        val result3 = runCatching {
            futureTask3.get(5_000, TimeUnit.MILLISECONDS).also {
                appendText("[testFutureTask2Geek] get: $it, cost: ${MyLog.getTimeCost(ftsStart3)}ms")
            }
        }.getOrElse {
            "error".also {
                appendText("[testFutureTask2Geek] error: $it, cost: ${MyLog.getTimeCost(ftsStart3)}ms")
            }
        }
        val result = "$result1, $result2, $result3"
        appendText("[testFutureTask2Geek] result: $result, cost: ${MyLog.getTimeCost(start)}ms")

    }

    // 适合做一些拓扑图类的任务结构
    private fun testCompletableFutureGeek(start: Long = System.currentTimeMillis()) {
        val f1: CompletableFuture<Void> = CompletableFuture.runAsync {
            println("T1: STARTING...")
            task1()
        }
        val f2: CompletableFuture<String> = CompletableFuture.supplyAsync {
            task2()
        }
        val f3 = f1.thenCombine(f2) { _, tf ->
            task3("", tf)
        }
        appendText("[testCompletableFutureGeek] result: ${f3.join()}, cost: ${MyLog.getTimeCost(start)}ms")
    }

    private fun testCompletionServiceGeek(start: Long = System.currentTimeMillis()) {

        val size = 10

        // 创建线程池
        val executor = Executors.newFixedThreadPool(3)
        // 创建CompletionService
        val cs: CompletionService<String> = ExecutorCompletionService(executor)

        repeat(size) {
            cs.submit { task1() }
        }

        repeat(size) {
            val r: String = cs.take().get()
//            appendText("[testCompletionService] result: $r, cost: ${MyLog.getTimeCost(start)}ms")
            executor.execute {
                Log.i(
                    TAG,
                    "[testCompletionService] r: $r, cost: ${MyLog.getTimeCost(start)}ms"
                )
            }
        }
    }

    private fun appendText(msg: String) {
        textView.text = "${textView.text}\n$msg\n"
    }

    private fun clearText() {
        textView.text = ""
    }
}

class T1Task(private val idx: Int) : Callable<String> {

    override fun call(): String {
        println("T1: START...")
        println("T1: END")
        return task1() + "-$idx"
    }
}

class FutureTask1(private val delayMs: Long = 6_000) : Callable<String> {
    override fun call(): String {
        sleep(delayMs)
        return "FutureTask1".also {
            val msg = "FutureTask1 finished $it\n"
            MyLog.i(TAG, msg)
        }
    }
}

class FutureTask2(private val delayMs: Long = 6_000) : Callable<String> {
    override fun call(): String {
        sleep(delayMs)
        return "FutureTask2".also {
            val msg = "FutureTask2 finished $it\n"
            MyLog.i(TAG, msg)
        }
    }
}

class FutureTask3(private val delayMs: Long = 6_000) : Callable<String> {
    override fun call(): String {
        sleep(delayMs)
        return "FutureTask3".also {
            val msg = "FutureTask3 finished $it\n"
            MyLog.i(TAG, msg)
        }
    }
}

class CountTask(private val taskId: Int) : Callable<Int> {
    override fun call(): Int {
        sleep(500)
        return taskId.also {
            val msg = "$taskId finished $it\n"
            MyLog.i(TAG, msg)
        }
    }
}

class T2Task : Callable<String> {
    override fun call(): String {
        println("T2: START...")
        println("T2: END")
        return task2()
    }
}

data class Result(
    var result: String
)