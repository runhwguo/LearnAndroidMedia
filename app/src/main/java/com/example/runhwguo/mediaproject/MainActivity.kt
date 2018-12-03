package com.example.runhwguo.mediaproject

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.runhwguo.mediaproject.captureaudio.AudioActivity
import com.example.runhwguo.mediaproject.drawimage.CustomVIewPicActivity
import com.example.runhwguo.mediaproject.drawimage.ImageViewPicActivity
import com.example.runhwguo.mediaproject.drawimage.SurfaceViewPicActivity

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

    fun onAudioRecordClick(v: View) {
        startActivity(Intent(this, AudioActivity::class.java))
    }
}
