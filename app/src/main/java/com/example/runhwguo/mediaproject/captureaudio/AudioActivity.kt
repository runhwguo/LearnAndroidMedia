package com.example.runhwguo.mediaproject.captureaudio

import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.example.runhwguo.mediaproject.R
import kotlinx.android.synthetic.main.activity_audio.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class AudioActivity : AppCompatActivity() {
    private var mRecordBufSize = 0 // 声明recordBuffer的大小字段
    private lateinit var mAudioRecord: AudioRecord// 声明 AudioRecord 对象
    private var mIsRecording = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)
    }

    fun startRecord() {
        val data = ByteArray(mRecordBufSize)
        val file = File(Environment.getExternalStorageDirectory().path, "test.pcm")
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created")
        }
        if (file.exists()) {
            file.delete()
        }

       mRecordBufSize = AudioRecord.getMinBufferSize(
            Config.SAMPLE_RATE_INHZ,
            Config.CHANNEL_CONFIG,
            Config.AUDIO_FORMAT
        ) // 声明recordBuffer的大小字段
        mAudioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            Config.SAMPLE_RATE_INHZ,
            Config.CHANNEL_CONFIG,
            Config.AUDIO_FORMAT,
            mRecordBufSize
        )// 声明 AudioRecord 对象

        mAudioRecord.startRecording()
        mIsRecording = true
        Thread(Runnable {
            var os: FileOutputStream? = null
            try {
                os = FileOutputStream(file)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            if (null != os) {
                while (mIsRecording) {
                    val read = mAudioRecord.read(data, 0, mRecordBufSize)
                    // 如果读取音频数据没有出现错误，就将数据写入到文件
                    if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                        try {
                            os.write(data)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
                try {
                    Log.i(TAG, "run: close file output stream !")
                    os.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }).start()
    }

    fun onAudioRecordClick(v: View) {
        if (mIsRecording) {
            btnAudioRecord.text = "开始录制"
            stopRecord()
        } else {
            btnAudioRecord.text = "停止录制"
            startRecord()
        }
    }

    fun stopRecord() {
        mIsRecording = false
        // 释放资源
        mAudioRecord.stop()
        mAudioRecord.release()
    }

    companion object {
        private const val TAG = "AudioActivity"
    }
}
