package com.demo.album

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commitNow
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.demo.base.log.MyLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FragmentState(private val fm: FragmentManager, private val stateMap: MutableMap<String, FragmentState>) {

    private val childState = mutableListOf<String>()

    private var tag: String? = null
    private var parentTag: String? = null
    private var isFinished = true

    private fun changeLifecycleState(lifecycleState: Lifecycle.State, excludeTag: String? = null) {
        // avoid loop recursion
        if (!isFinished) {
            return
        }
        isFinished = false
        if ((excludeTag == null) || (excludeTag != tag)) {
            fm.findFragmentByTag(tag)?.apply {
                doChangeLifecycle(this, lifecycleState, fm, excludeTag)
            }
        }
        isFinished = true
    }

    private fun doChangeLifecycle(
        fragment: Fragment,
        lifecycleState: Lifecycle.State,
        fragmentManager: FragmentManager,
        excludeTag: String?
    ) {
        val copyChildState = ArrayList(childState)
        fragment.lifecycleScope.launchWhenCreated {
            /**
             * 退出页面时[FragmentStack.pop]会占用[fragmentManager]的事务，这里需要使用Dispatchers.UI
             * 切换到下一帧执行，避免抢占事务导致异常
             */
            withContext(Dispatchers.Main) {
                runCatching {
                    if (fragment.lifecycle.currentState >= Lifecycle.State.INITIALIZED) {
                        MyLog.d(TAG, "[doChangeLifecycle] tag = $tag, childStateList = $copyChildState, lifecycleState=$lifecycleState")
                        fragmentManager.commitNow(true) {
                            setMaxLifecycle(fragment, lifecycleState)
                        }
                        copyChildState.forEach {
                            stateMap[it]?.changeLifecycleState(lifecycleState, excludeTag)
                        }
                    }
                }.onFailure {
                    MyLog.e(TAG, "doChangeLifecycle. onFailure=$it")
                }
            }
        }
    }

    fun setParent(tag: String, thisLifecycleState: Lifecycle.State, parent: String?, parentLifecycleState: Lifecycle.State) {
        this.tag = tag
        MyLog.d(TAG, "setParent: this : $this, parent : $parent, parentLevel: $parentLifecycleState, thisLevel : $thisLifecycleState")
        stateMap[parent]?.apply {
            this@FragmentState.parentTag = parent
            /**
             *  修复bug:4804119 第二次调用setParent时[childState]就已经包含[tag],
             *  调用[changeLifecycleState]前需要先移除[tag]
             */
            childState.remove(tag)
            changeLifecycleState(parentLifecycleState)
            childState.add(tag)
        }
        stateMap[tag] = this
        changeLifecycleState(thisLifecycleState)
    }

    fun removeParent() {
        MyLog.d(TAG, "removeParent: tag: $tag, parentTag:$parentTag")
        parentTag?.apply {
            stateMap[this]?.apply {
                childState.remove(this@FragmentState.tag)
                changeLifecycleState(Lifecycle.State.RESUMED)
            }
        }
        fm.findFragmentByTag(tag)?.apply {
            if (this.lifecycle.currentState > Lifecycle.State.STARTED) {
                changeLifecycleState(Lifecycle.State.STARTED)
            }
        }
        stateMap.remove(tag)
    }

    override fun toString(): String {
        return "FragmentState(tag='$tag')"
    }

    companion object {
        private const val TAG = "FragmentState"

    }
}