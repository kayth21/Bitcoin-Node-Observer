package com.ceaver.bno.splash

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ceaver.bno.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)
        supportActionBar?.hide()
    }
}
