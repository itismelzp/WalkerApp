package com.demo.storage_ktx

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import androidx.activity.viewModels
import com.demo.base.BaseActivity
import com.demo.storage_ktx.RoomKtxFragment.Companion.applicationScope
import com.demo.storage_ktx.databinding.ActivityNewWordBinding
import com.demo.storage_ktx.model.Word
import com.demo.storage_ktx.repository.WordRepository
import com.demo.storage_ktx.viewmodel.WordViewModel
import com.demo.storage_ktx.viewmodel.WordViewModelFactory

class NewWordActivity : BaseActivity<ActivityNewWordBinding>() {

    private lateinit var editWordView: EditText

    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory(WordRepository(WordRoomDatabase.getDatabase(this, applicationScope).wordDao()))
    }

    override fun initBaseViews(savedInstanceState: Bundle?) {
        super.initBaseViews(savedInstanceState)

        editWordView = binding.editWord
        binding.buttonSave.setOnClickListener {
            val replyIntent = Intent()
            if (!TextUtils.isEmpty(editWordView.text)) {
//                setResult(Activity.RESULT_CANCELED, replyIntent)
                val word = Word(word = editWordView.text.toString())
                wordViewModel.insert(word)
            } else {
                val word = editWordView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, word)
            }
            finish()
        }
    }

    companion object {
        private const val TAG = "NewWordActivity"

        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"

    }
}