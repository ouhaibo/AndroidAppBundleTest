package com.ohb.androidappbundletest

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.android.play.core.splitcompat.SplitCompat

abstract class BaseActivity : Activity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.install(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }
}
