package com.demo.appbar

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.demo.R
import com.demo.databinding.ActivityScrollingBinding
import com.demo.base.BaseActivity


class ScrollingActivity : BaseActivity<ActivityScrollingBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show()
        }
    }

    override fun getViewBinding() = ActivityScrollingBinding.inflate(layoutInflater)

}