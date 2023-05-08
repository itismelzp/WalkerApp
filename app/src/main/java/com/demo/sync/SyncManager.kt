package com.demo.sync

import android.widget.TextView
import com.demo.logger.MyLog
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
class SyncManager(private val textView: TextView) {

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
        sleep(2000)
        "World".also {
            val msg = "task2 finished $it\n"
            MyLog.i(TAG, msg)
//            appendText(msg)
        }
    }

    val task3: (String, String) -> String = { p1, p2 ->
        sleep(2000)
        "$p1 $p2".also {
            val msg = "task3 finished $it\n"
            MyLog.i(TAG, msg)
//            appendText(msg)
        }
    }

    private fun appendText(msg: String) {
        textView.text = "${textView.text}\n$msg\n"
    }

    private fun clearText() {
        textView.text = ""
    }

    fun startTest() {
        clearText()
        testThreadJoin()
        testSynchronized()
//        testReentrantLock()
        testBlockingQueue()
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
    private fun testCoroutine(start: Long = System.currentTimeMillis()) {}

    // 12. Flow
    private fun testFlow(start: Long = System.currentTimeMillis()) {}
}