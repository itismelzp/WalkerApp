package com.demo.storage_ktx

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.base.BaseFragment
import com.demo.base.log.MyLog
import com.demo.storage_ktx.databinding.FragmentMainRoomKtxBinding
import com.demo.storage_ktx.model.User
import com.demo.storage_ktx.repository.UserRepository
import com.demo.storage_ktx.repository.WordRepository
import com.demo.storage_ktx.viewmodel.UserViewModel
import com.demo.storage_ktx.viewmodel.UserViewModelFactory
import com.demo.storage_ktx.viewmodel.WordViewModel
import com.demo.storage_ktx.viewmodel.WordViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


class RoomKtxFragment : BaseFragment<FragmentMainRoomKtxBinding>() {

    private val wordRepository: WordRepository by lazy {
        val dao = WordRoomDatabase.getDatabase(requireContext(), applicationScope).wordDao()
        WordRepository(dao)
    }

    private val userRepository: UserRepository by lazy {
        val dao = WordRoomDatabase.getDatabase(requireContext(), applicationScope).userDao()
        UserRepository(dao)
    }

    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory(wordRepository)
    }

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(userRepository)
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
    ): BaseFragment<FragmentMainRoomKtxBinding> = newInstance()

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

        val user1 = User(
            null,
            "zp",
            "lee",
            listOf("baidu", "tencent"),
            listOf("lily", "wawa"),
            123456789L,
            "test"
        )
        val user2 = User(
            null,
            "walker",
            "lee",
            null,
            null,
            123456789L,
            "test"
        )

        binding.fab.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                MyLog.i(TAG, "getSingleUser: ${userViewModel.getSingleUser(2)}")
            }
            userViewModel.insert(listOf(user1, user2))
            val intent = Intent(requireContext(), NewWordActivity::class.java)
            startActivity(intent)
        }

    }

    companion object {

        private const val TAG = "RoomKtxFragment"

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