package com.demo.storage_ktx

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.base.BaseFragment
import com.demo.storage_ktx.databinding.FragmentMainRoomKtxBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


class RoomKtxFragment : BaseFragment<FragmentMainRoomKtxBinding>() {

    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory(WordRepository(WordRoomDatabase.getDatabase(requireContext(), applicationScope).wordDao()))
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentMainRoomKtxBinding =
        FragmentMainRoomKtxBinding.inflate(inflater, container, attachToRoot)

    override fun createFragment(
        arg1: String,
        arg2: String
    ): BaseFragment<FragmentMainRoomKtxBinding> =
        newInstance()

    override fun initBaseViews(savedInstanceState: Bundle?) {
        super.initBaseViews(savedInstanceState)

        val adapter = WordListAdapter()
        binding.recyclerview.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        wordViewModel.allWords.observe(viewLifecycleOwner) { words ->
            words?.let {
                adapter.submitList(it)
            }
        }

        binding.fab.setOnClickListener {
            val intent = Intent(requireContext(), NewWordActivity::class.java)
            startActivity(intent)
        }

    }

    companion object {

        @JvmStatic
        val applicationScope = CoroutineScope(SupervisorJob())

        @JvmStatic
        fun newInstance() =
            RoomKtxFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}