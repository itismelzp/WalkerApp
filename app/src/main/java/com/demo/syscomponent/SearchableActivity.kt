package com.demo.syscomponent

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import com.demo.R
import com.demo.databinding.LayoutSearchActivityBinding
import com.demo.base.BaseActivity
import com.demo.network.RequestAccessManager
import com.demo.network.model.SearchMediaItem
import com.demo.network.model.SearchRequest
import com.demo.network.model.SearchResultResponse
import com.demo.network.utils.DataConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * 使用系统搜索框架：
 * 1.创建搜索UI；
 *   1.1 创建可搜索配置；
 *   1.2 创建可搜索activity；
 * 2.添加最近的搜索建议；
 * 3.添加自定义建议；
 * 4.提供搜索配置；
 */
class SearchableActivity : BaseActivity<LayoutSearchActivityBinding>() {

    companion object {
        const val JARGON = "JARGON"
        private const val TAG = "SearchableActivity"
    }

    private val textDataAdapter = TextDataAdapter()
    private val originData = ArrayList<String>()
    private var lastQueryValue = ""


    override fun initBaseData(savedInstanceState: Bundle?) {
        super.initBaseData(savedInstanceState)

        originData.add("test data qwertyuiop")
        originData.add("test data asdfghjkl")
        originData.add("test data zxcvbnm")
        originData.add("test data 123456789")
        originData.add("test data /.,?-+")
    }

    override fun initBaseViews(savedInstanceState: Bundle?) {
        super.initBaseViews(savedInstanceState)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.run {
            title = "SearchableActivity"
            setHomeAsUpIndicator(R.drawable.icon_back)
            setDisplayHomeAsUpEnabled(true)
        }
        textDataAdapter.setNewData(originData)
        binding.rvContent.adapter = textDataAdapter

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent, true)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.example_seach_menu, menu)
//        menu?.run {
//            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//            val searchView = findItem(R.id.action_search).actionView as SearchView
//
//            searchView.apply {
//                setOnCloseListener {
//                    textDataAdapter.setNewData(originData)
//                    false
//                }
//                setSearchableInfo(searchManager.getSearchableInfo(componentName)) // 设置搜索配置
//                if (lastQueryValue.isNotEmpty()) {
//                    setQuery(lastQueryValue, false)
//                }
//            }
//        }
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_clear_search_histor) {
            SearchRecentSuggestions(this, RecentSearchProvider.AUTHORITY, RecentSearchProvider.MODE)
                .clearHistory()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleIntent(intent: Intent, newIntent: Boolean = false) {

        val appData = getIntent().getBundleExtra(SearchManager.APP_DATA)
        if (appData != null) {
            val jargon = appData.getBoolean(JARGON)
        }

        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                if (query.isEmpty()) {
                    return
                }
                SearchRecentSuggestions(
                    this,
                    RecentSearchProvider.AUTHORITY,
                    RecentSearchProvider.MODE
                ).saveRecentQuery(query, "history $query")
                if (!newIntent) {
                    lastQueryValue = query
                }
                doMySearch(query)
            }
        }
    }

    private fun doMySearch(query: String) {

        val request = SearchRequest(query, 2000, "all")
        lifecycleScope.launch(Dispatchers.Main) {
            val response: SearchResultResponse = withContext(Dispatchers.IO) {
                RequestAccessManager.INSTANCE.coroutineSearchWithUserId("12345", request)
            }
            val data = response.aggregations.data
            val itemList: List<SearchMediaItem> = DataConverter.mediaPathList2MediaItemList(data)

            val map = itemList.map {
                it.name
            }
            val vo = arrayListOf<String>()
            vo.addAll(map)
            textDataAdapter.setNewData(vo)
        }

    }

    private fun mergeResult(localData: List<String>, cloudData: List<String>): List<String> {

        if (localData == null && cloudData == null) {
            return listOf()
        }

        if (localData == null) {
            return cloudData
        }
        if (cloudData == null) {
            return localData
        }
        return localData + cloudData
    }

}