package com.demo.logger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.R
import com.orhanobut.logger.Logger

class LoggerActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LoggerActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logger_layout)

        Logger.d("logger test.")
    }

}