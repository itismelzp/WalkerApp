package com.demo.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLayoutChangeListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.SharedElementCallback
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.transition.TransitionInflater
import com.demo.R
import com.demo.databinding.FragmentGridBinding
import com.demo.fragment.adapter.GridAdapter


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GridFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GridFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentGridBinding? = null
    private val binding get() = _binding!!

    private var listener: OnActionListener? = null

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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentGridBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.blankTv.text = "$param1: $param2"
        binding.testBtn.setOnClickListener {
            Toast.makeText(context, "test", Toast.LENGTH_SHORT).show()
            listener?.onAction("from OnActionListener: ${binding.blankTv.text}")
        }
        ViewCompat.setTransitionName(binding.sharedEleIv, "item_image")
        binding.sharedEleIv.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                setReorderingAllowed(true)
                addSharedElement(binding.sharedEleIv, SharedElementFragment.HERO_IMAGE)
                replace(
                    R.id.fragment_container,
                    SharedElementFragment.newInstance("hello world", "hello fragment"),
                    "blockFragment"
                )
                addToBackStack(null)
            }
        }

        binding.imageRecyclerView.adapter = GridAdapter(this)
        prepareTransitions()
        postponeEnterTransition()
        scrollToPosition()
    }

    private fun scrollToPosition() {
        binding.imageRecyclerView.apply {
            addOnLayoutChangeListener(object : OnLayoutChangeListener {
                override fun onLayoutChange(
                    v: View?,
                    left: Int, top: Int, right: Int, bottom: Int,
                    oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int
                ) {
                    removeOnLayoutChangeListener(this)
                    val layoutManager = layoutManager
                    val viewAtPosition =
                        layoutManager?.findViewByPosition(currentPosition)
                    if (viewAtPosition == null
                        || layoutManager
                            .isViewPartiallyVisible(viewAtPosition, false, true)
                    ) {
                        post {
                            layoutManager?.scrollToPosition(currentPosition)
                        }
                    }
                }
            })
        }
    }

    private fun prepareTransitions() {
        exitTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.grid_exit_transition)

        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>,
                sharedElements: MutableMap<String, View>
            ) {
                val selectedViewHolder =
                    binding.imageRecyclerView.findViewHolderForAdapterPosition(currentPosition)
                selectedViewHolder?.let {
                    sharedElements[names[0]] =
                        selectedViewHolder.itemView.findViewById(R.id.card_image)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        var currentPosition = 0

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
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