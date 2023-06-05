package com.demo.sync

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.demo.base.log.MyLog
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import java.util.concurrent.SynchronousQueue
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
class SyncManager(private val textView: TextView, private val fragment: Fragment) {

    companion object {
        private const val TAG = "SyncManager"
    }

    val task1: () -> String = {
        sleep(2_000L)
        "Hello".also {
            val msg = "task1 finished $it\n"
            MyLog.i(TAG, msg)
//            appendText(msg)
        }
    }


    val task2: () -> String = {
        sleep(2_000L)
        "World".also {
            val msg = "task2 finished $it\n"
            MyLog.i(TAG, msg)
//            appendText(msg)
        }
    }

    val task3: (String, String) -> String = { p1, p2 ->
        sleep(1_000L)
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

    fun startTest() {
        clearText()
//        testThreadJoin()
//        testSynchronized()
//        testReentrantLock()
//        testBlockingQueue()
        testCoroutine()
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
    private fun testFuture(start: Long = System.currentTimeMillis()) {}

    // 9. CompletableFuture
    private fun testCompletableFuture(start: Long = System.currentTimeMillis()) {}

    // 10. Rxjava
    private fun testsRxjava(start: Long = System.currentTimeMillis()) {}

    // 11. Coroutine
    private fun testCoroutine(start: Long = System.currentTimeMillis()) {
        fragment.lifecycleScope.launch(Dispatchers.Main) {
//            val c1: Deferred<String> = async(Dispatchers.IO) {
//                task1()
//            }
//            val c2 = async(Dispatchers.IO) {
//                task2()
//            }
            val result = mutableListOf<Deferred<String>>().apply {
                repeat(10) {
                    add(async(Dispatchers.IO) {
                        task1()
                    })
                }
            }.awaitAll().reduce { acc, s ->
                "$s, $acc"
            }
//            val await1 = c1.await()
//            val await2 = c2.await()
//            appendText("[testCoroutine] result: ${task3(await1, await2)}, timeCost: ${MyLog.getTimeCost(start)}")
            appendText("[testCoroutine] result: ${task5(result)}, timeCost: ${MyLog.getTimeCost(start)}ms")
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
            appendText("[testCoroutine] result: ${task5(result)}, timeCost: ${MyLog.getTimeCost(start)}ms")
        }
    }

    // 12. Flow
    private fun testFlow(start: Long = System.currentTimeMillis()) {}

    private fun appendText(msg: String) {
        textView.text = "${textView.text}\n$msg\n"
    }

    private fun clearText() {
        textView.text = ""
    }
}