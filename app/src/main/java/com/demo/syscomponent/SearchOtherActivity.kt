package com.demo.syscomponent

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.demo.databinding.ActivitySearchOtherBinding
import com.demo.base.BaseActivity

class SearchOtherActivity : BaseActivity<ActivitySearchOtherBinding>() {


    override fun getViewBinding() = ActivitySearchOtherBinding.inflate(layoutInflater)

    override fun initBaseViews(savedInstanceState: Bundle?) {
        super.initBaseViews(savedInstanceState)

        binding.btnSearchView.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SearchableActivity::class.java
                )
            )
        }
        binding.btnSearchDialog.setOnClickListener { onSearchRequested() }

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchManager.setOnDismissListener {
            // 搜索弹窗隐藏，可以在此处理其他功能
            runOnUiThread {
                Toast.makeText(this, "Search Dialog dismiss", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSearchRequested(): Boolean {
        val appData = Bundle().apply {
            putBoolean(SearchableActivity.JARGON, true)
        }
        startSearch(null, false, appData, false)
        return true
    }

}