package com.example.runhwguo.mediaproject.camera

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.TextureView
import android.view.View
import com.example.runhwguo.mediaproject.R
import kotlinx.android.synthetic.main.activity_camera_preview.*

class CameraPreviewActivity : AppCompatActivity(), TextureView.SurfaceTextureListener {
    private lateinit var mCamera: Camera
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_preview)

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1003)
        }
    }

    fun onCameraPreviewClick(v: View) {
        textureView.surfaceTextureListener = this
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        mCamera.release()
        return false;
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        mCamera.setPreviewTexture(surface);
        mCamera.startPreview();
    }
}
