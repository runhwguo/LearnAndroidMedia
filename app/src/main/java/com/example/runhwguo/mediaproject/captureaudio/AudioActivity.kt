package com.example.runhwguo.mediaproject.captureaudio

import android.Manifest
import android.content.pm.PackageManager
import android.media.*
import android.media.AudioTrack
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.runhwguo.mediaproject.R
import kotlinx.android.synthetic.main.activity_audio.*
import java.io.*
import java.util.concurrent.atomic.AtomicBoolean


class AudioActivity : AppCompatActivity() {
    private var mRecordBufSize = 0 // 声明recordBuffer的大小字段
    private lateinit var mAudioRecord: AudioRecord// 声明 AudioRecord 对象
    private var mIsRecording = AtomicBoolean(false)

    private var mAudioTrack: AudioTrack? = null
    private lateinit var mAudioData: ByteArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 1002)
        }
    }

    private fun startRecord() {
        val data = ByteArray(mRecordBufSize)
        val file = File(Environment.getExternalStorageDirectory().path, "test.pcm")
        if (!file.mkdirs()) {
            Log.e(TAG, "Directory not created")
        }
        if (file.exists()) {
            file.delete()
        }

        // 声明recordBuffer的大小字段
        mRecordBufSize = AudioRecord.getMinBufferSize(
            Config.SAMPLE_RATE_INHZ,
            Config.CHANNEL_CONFIG,
            Config.AUDIO_FORMAT
        )

        // 声明 AudioRecord 对象
        mAudioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            Config.SAMPLE_RATE_INHZ,
            Config.CHANNEL_CONFIG,
            Config.AUDIO_FORMAT,
            mRecordBufSize
        )

        mAudioRecord.startRecording()
        mIsRecording.set(true)
        Thread(Runnable {
            var os: FileOutputStream? = null
            try {
                os = FileOutputStream(file)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            if (null != os) {
                while (mIsRecording.get()) {
                    val read = mAudioRecord.read(data, 0, mRecordBufSize)
                    // 如果读取音频数据没有出现错误，就将数据写入到文件
                    if (AudioRecord.ERROR_INVALID_OPERATION != read && AudioRecord.ERROR_BAD_VALUE != read) {
                        try {
                            os.write(data)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    } else {
                        Log.w(TAG, "read value is $read")
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

    private fun stopRecord() {
        mIsRecording.set(false)
        // 释放资源
        mAudioRecord.stop()
        mAudioRecord.release()
    }

    // TODO: 有时候采集的pcm文件，是0 KB
    fun onAudioRecordClick(v: View) {
        if (mIsRecording.get()) {
            btnAudioRecord.text = "开始录制"
            stopRecord()
        } else {
            btnAudioRecord.text = "停止录制"
            startRecord()
        }
    }

    fun onPcm2WavClick(v: View) {
        val pcmToWavUtil = PcmToWavUtil(Config.SAMPLE_RATE_INHZ, Config.CHANNEL_CONFIG, Config.AUDIO_FORMAT)
        val pcmFile = File(Environment.getExternalStorageDirectory().path, "test.pcm")
        val wavFile = File(Environment.getExternalStorageDirectory().path, "test.wav")
        if (!wavFile.mkdirs()) {
            Log.e(TAG, "wavFile Directory not created")
        }
        if (wavFile.exists()) {
            wavFile.delete()
        }
        pcmToWavUtil.pcmToWav(pcmFile.absolutePath, wavFile.absolutePath)
        Toast.makeText(this, "转换ok, 保存路径为" + wavFile.absolutePath, Toast.LENGTH_SHORT).show()
    }

    // MODE_STREAM
    fun onPlayPcmClick(v: View) {
        v.isEnabled = false

        releaseAudioTrack()

        val channelConfig = AudioFormat.CHANNEL_OUT_MONO
        val minBufferSize =
            AudioTrack.getMinBufferSize(Config.SAMPLE_RATE_INHZ, channelConfig, Config.AUDIO_FORMAT)
        mAudioTrack = AudioTrack(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build(),
            AudioFormat.Builder().setSampleRate(Config.SAMPLE_RATE_INHZ)
                .setEncoding(Config.AUDIO_FORMAT)
                .setChannelMask(channelConfig)
                .build(),
            minBufferSize,
            AudioTrack.MODE_STREAM,
            AudioManager.AUDIO_SESSION_ID_GENERATE
        )
        mAudioTrack?.play()

        Thread(Runnable {
            val tempBuffer = ByteArray(minBufferSize)
            var readCount = -1
            val fin = FileInputStream((Environment.getExternalStorageDirectory().path + File.separator + "test.pcm"))
            while (fin.available() > 0) {
                readCount = fin.read(tempBuffer)
                if (readCount != -1) {
                    mAudioTrack?.write(tempBuffer, 0, readCount)
                }
            }
            fin.close()
        }).start()

        v.isEnabled = true
    }

    private fun releaseAudioTrack() {
        mAudioTrack?.run {
            stop()
            release()
        }
    }

    companion object {
        private const val TAG = "AudioActivity"
    }
}
