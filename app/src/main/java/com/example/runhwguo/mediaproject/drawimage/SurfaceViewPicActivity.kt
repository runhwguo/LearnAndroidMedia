package com.example.runhwguo.mediaproject.drawimage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.SurfaceHolder
import com.example.runhwguo.mediaproject.R
import kotlinx.android.synthetic.main.activity_surface_view_pic.*
import java.io.File


class SurfaceViewPicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surface_view_pic)

        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(surfaceHolder: SurfaceHolder?) {
                if (surfaceHolder == null) {
                    return
                }

                val paint = Paint()
                paint.isAntiAlias = true
                paint.style = Paint.Style.STROKE

                val bitmap =
                    BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().path + File.separator + "test.jpg")  // 获取bitmap

                Log.i(TAG, "origin bitmap width " + bitmap.width + ", height " + bitmap.height)
                val scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / SCALE, bitmap.height / SCALE, false)
                val mBitWidth = scaledBitmap.width
                val mBitHeight = scaledBitmap.height
                Log.i(TAG, "scale bitmap width $mBitWidth, height $mBitHeight")
                val mSrcRect = Rect(0, 0, mBitWidth, mBitHeight)
                val mDestRect = Rect(0, 0, mBitWidth, mBitHeight)
                val canvas = surfaceHolder.lockCanvas(mSrcRect)  // 先锁定当前surfaceView的画布

                canvas.drawBitmap(scaledBitmap, mSrcRect, mDestRect, paint) //执行绘制操作
                surfaceHolder.unlockCanvasAndPost(canvas) // 解除锁定并显示在界面上
            }

            override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {

            }

            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {

            }
        })
    }

    companion object {
        private const val SCALE = 4
        private const val TAG = "SurfaceViewPicActivity"
    }
}
