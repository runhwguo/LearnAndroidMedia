package com.example.runhwguo.mediaproject.drawimage

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import com.example.runhwguo.mediaproject.R
import kotlinx.android.synthetic.main.activity_image_view_pic.*
import java.io.File


class ImageViewPicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view_pic)

        val bitmap =
            BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().path + File.separator + "test.jpg")
        imageView.setImageBitmap(bitmap)
    }
}
