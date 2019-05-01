package com.example.runhwguo.mediaproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.runhwguo.mediaproject.camera.CameraPreviewActivity
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

        // 申请必要权限
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1001
            )
        }
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

    fun onCameraPreviewClick(v:View){
        startActivity(Intent(this, CameraPreviewActivity::class.java))
    }
}
