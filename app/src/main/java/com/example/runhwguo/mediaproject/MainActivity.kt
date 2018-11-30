package com.example.runhwguo.mediaproject

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

/**
 * 各种操作按钮入口
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onBtnImageViewPicClick(v: View) {
        startActivity(Intent(this, ImageViewPicActivity::class.java))
    }

    fun onBtnSurfaceViewPicClick(v: View) {
        startActivity(Intent(this, SurfaceViewPicActivity::class.java))
    }

    fun onCustomViewClick(v: View) {
        startActivity(Intent(this, CustomVIewPicActivity::class.java))
    }
}
