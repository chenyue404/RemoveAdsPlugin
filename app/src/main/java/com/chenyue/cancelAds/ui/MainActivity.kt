package com.chenyue.cancelAds.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
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
        //https://github.com/android-hacker/VirtualXposed/wiki/Difference-between-Xposed-and-VirtualXposed
        //Methods which are too short(less than 2 assembly instructions) can not be hooked or not stable.(eg: methods with empty body or return constant directly) 。If the method is in your app, you can add redundant statement : Log.d("fake", "just for vxp") to avoid this.
        Log.d("cancelAds", "isActive")
        Log.d("cancelAds", "isActive")
        Log.d("cancelAds", "isActive")
        Log.d("cancelAds", "isActive")
        Log.d("cancelAds", "isActive")
        return false
    }
}
