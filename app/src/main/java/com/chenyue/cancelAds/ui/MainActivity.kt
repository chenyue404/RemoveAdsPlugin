package com.chenyue.cancelAds.ui

import android.app.Activity
import android.os.Bundle
import com.chenyue.cancelAds.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isActive()) {
            tv.text = "已激活"
        } else {
            tv.text = "未激活"
        }
    }

    fun isActive(): Boolean {
        return false
    }
}
