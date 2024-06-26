package com.demo.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLayoutChangeListener
import android.view.ViewGroup
import android.transition.TransitionInflater
import android.widget.Toast
import androidx.core.app.SharedElementCallback
import androidx.core.view.ViewCompat
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.lifecycleScope
import com.demo.R
import com.demo.base.BaseFragment
import com.demo.databinding.MainFragmentGridBinding
import com.demo.fragment.adapter.GridAdapter
import com.demo.base.log.MyLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A fragment for displaying a grid of images.
 */
class GridFragment : BaseFragment<MainFragmentGridBinding>() {

    private var param1: String? = null
    private var param2: String? = null

    private var listener: OnActionListener? = null
    override fun createFragment() = newInstance()

    override fun createFragment(arg1: String, arg2: String) = newInstance(arg1, arg2)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnActionListener
        if (listener == null) {
            throw ClassCastException("$context must implement OnActionListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
        MyLog.i(TAG, "[onCreate]")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MyLog.i(TAG, "[onViewCreated]")

        binding.blankTv.text = "$param1: $param2"
        binding.testBtn.setOnClickListener {
            Toast.makeText(context, "test", Toast.LENGTH_SHORT).show()
            listener?.onAction("from OnActionListener: ${binding.blankTv.text}")
        }
        ViewCompat.setTransitionName(binding.sharedEleIv, "item_image")
        binding.sharedEleIv.setOnClickListener {
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                addSharedElement(binding.sharedEleIv, SharedElementFragment.HERO_IMAGE)
                replace<SharedElementFragment>(R.id.fragment_container,
                    SharedElementFragment::class.simpleName,
                    Bundle().apply {
                        putString(ARG_PARAM1, "hello world")
                        putString(ARG_PARAM2, "hello fragment")
                    }
                )
                addToBackStack(null)
            }
        }

        binding.recyclerView.adapter = GridAdapter(this)
        prepareTransitions()
        postponeEnterTransition()
        scrollToPosition()
    }

    private fun scrollToPosition() {
        binding.recyclerView.apply {
            addOnLayoutChangeListener(object : OnLayoutChangeListener {
                override fun onLayoutChange(
                    v: View?,
                    left: Int, top: Int, right: Int, bottom: Int,
                    oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int
                ) {
                    removeOnLayoutChangeListener(this)

                    layoutManager?.apply {
                        val viewAtPosition = findViewByPosition(currentPosition)
                        if (viewAtPosition == null
                            || isViewPartiallyVisible(viewAtPosition, false, true)
                        ) {
                            lifecycleScope.launch(Dispatchers.Main) {
                                scrollToPosition(currentPosition)
                            }
                        }
                    }
                }
            })
        }
    }

    private fun prepareTransitions() {
        exitTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.grid_exit_transition)

        // This will be done when exiting the GridFragment, and re-entering the GridFragment by popped back.
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>,
                sharedElements: MutableMap<String, View>
            ) {
                MyLog.i(TAG, "[onMapSharedElements] name: ${names[0]}")
                val selectedViewHolder =
                    binding.recyclerView.findViewHolderForAdapterPosition(currentPosition)
                selectedViewHolder?.let {
                    sharedElements[names[0]] =
                        selectedViewHolder.itemView.findViewById(R.id.card_image)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        MyLog.i(TAG, "[onDestroyView]")
    }

    companion object {

        private const val TAG = "GridFragment"

        @JvmField
        var currentPosition = 0

        @JvmStatic
        fun newInstance() = GridFragment().apply {
            arguments = Bundle().apply {}
        }

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GridFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    /**
     * 用于与其它fragment通信，fragment之间通信首选ViewModel，通过共享的ViewModel实现通信。
     * 如需传播无法使用ViewModel处理，则可用此回调方法，此法要求宿主Activity实现此接口。。
     */
    interface OnActionListener {
        fun onAction(msg: String)
    }

}