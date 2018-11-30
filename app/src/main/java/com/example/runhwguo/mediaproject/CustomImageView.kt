package com.example.runhwguo.mediaproject

import android.content.Context
import android.graphics.*
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.io.File

class CustomImageView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val mPaint = Paint()
    private var mBitmap: Bitmap
    private var mBitWidth = 0
    private var mBitHeight = 0
    private var mSrcRect: Rect
    private var mDestRect: Rect

    init {
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().path + File.separator + "test.jpg")

        Log.i(TAG, "origin mBitmap width " + mBitmap.width + ", height " + mBitmap.height)
        mBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.width / SCALE, mBitmap.height / SCALE, false)
        Log.i(TAG, "scale mBitmap width $mBitWidth, height $mBitHeight")

        mBitWidth = mBitmap.width
        mBitHeight = mBitmap.height
        mSrcRect = Rect(0, 0, mBitWidth, mBitHeight)
        mDestRect = Rect(0, 0, mBitWidth, mBitHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        Log.i(TAG, "onDraw")
        canvas.drawBitmap(mBitmap, mSrcRect, mDestRect, mPaint)
    }

    companion object {
        const val TAG = "CustomImageView"
        const val SCALE = 4
    }
}