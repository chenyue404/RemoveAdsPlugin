package com.chenyue.cancelAds.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.chenyue.cancelAds.R

class MainActivity : Activity() {

    companion object {
        const val PREF_NAME = "main_prefs"
        const val KEY_VIDEO_SPEED = "key_video_speed"
    }

    private val tv by lazy { findViewById<TextView>(R.id.tv) }
    private val etSpeed by lazy { findViewById<EditText>(R.id.etSpeed) }
    private val btSave by lazy { findViewById<ImageButton>(R.id.btSave) }

    private val sp by lazy {
        try {
            getSharedPreferences(
                PREF_NAME,
                Context.MODE_WORLD_READABLE
            )
        } catch (e: SecurityException) {
            // The new XSharedPreferences is not enabled or module's not loading
            null // other fallback, if any
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isActive()) {
            tv.text = "已激活"
        } else {
            tv.text = "未激活"
        }

        sp?.getFloat(KEY_VIDEO_SPEED, 1.0f)?.takeIf { it != 1.0f }?.let {
            etSpeed.setText(it.toString())
        }
        btSave.setOnClickListener {
            hideKeyboard()
            val newSpeed = etSpeed.text.toString().toFloatOrNull() ?: 1.0f
            sp?.edit()?.putFloat(KEY_VIDEO_SPEED, newSpeed)?.apply()
            Toast.makeText(this, "保存成功\n$newSpeed", Toast.LENGTH_SHORT).show()
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

    private fun hideKeyboard() {
        currentFocus?.let {
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}
