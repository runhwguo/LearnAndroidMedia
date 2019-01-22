package com.example.runhwguo.mediaproject.captureaudio

import android.media.*
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.runhwguo.mediaproject.R
import kotlinx.android.synthetic.main.activity_audio.*
import java.io.*


class AudioActivity : AppCompatActivity() {
    private var mRecordBufSize = 0 // 声明recordBuffer的大小字段
    private lateinit var mAudioRecord: AudioRecord// 声明 AudioRecord 对象
    private var mIsRecording = false
    private var mIsPlaying = false
    private lateinit var mAudioTrack: AudioTrack

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)
    }

    fun startRecord() {
        val data = ByteArray(mRecordBufSize)
        val file = File(Environment.getExternalStorageDirectory().path, "test.pcm")
        if (!file.exists() && !file.mkdirs()) {
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

    fun stopRecord() {
        mIsRecording = false
        // 释放资源
        mAudioRecord.stop()
        mAudioRecord.release()
    }

    fun releaseAudioTrack() {
        mAudioTrack.stop()
        mAudioTrack.release()
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

    fun onPcm2WavClick(v: View) {
        val pcmToWavUtil = PcmToWavUtil(Config.SAMPLE_RATE_INHZ, Config.CHANNEL_CONFIG, Config.AUDIO_FORMAT)
        val pcmFile = File(Environment.getExternalStorageDirectory().path, "test.pcm")
        val wavFile = File(Environment.getExternalStorageDirectory().path, "test.wav")
        if (!wavFile.exists() && !wavFile.mkdirs()) {
            Log.e(TAG, "wavFile Directory not created")
        }
        if (wavFile.exists()) {
            wavFile.delete()
        }
        pcmToWavUtil.pcmToWav(pcmFile.absolutePath, wavFile.absolutePath)
        Toast.makeText(this, "转换ok, 保存路径为" + wavFile.absolutePath, Toast.LENGTH_SHORT).show()
    }

    fun onPlayAudioClick(v: View) {
        if (btnPlayAudio.text == "播放音频") {
            mIsPlaying = true
            playInModeStream()
            btnPlayAudio.text = "停止播放"
        } else if (btnPlayAudio.text == "停止播放") {
            mIsPlaying = false
            releaseAudioTrack()
        }
    }

    /**
     * 播放，使用stream模式
     */
    private fun playInModeStream() {
        /*
        * SAMPLE_RATE_INHZ 对应pcm音频的采样率
        * channelConfig 对应pcm音频的声道
        * AUDIO_FORMAT 对应pcm音频的格式
        * */
        val channelConfig = AudioFormat.CHANNEL_OUT_MONO
        val minBufferSize =
            AudioTrack.getMinBufferSize(Config.SAMPLE_RATE_INHZ, AudioFormat.CHANNEL_OUT_MONO, Config.AUDIO_FORMAT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
        } else {
            mAudioTrack = AudioTrack(
                AudioManager.STREAM_MUSIC,
                Config.SAMPLE_RATE_INHZ,
                channelConfig,
                Config.AUDIO_FORMAT,
                minBufferSize,
                AudioTrack.MODE_STREAM
            )
        }
        mAudioTrack.play()

        val file = File(Environment.getExternalStorageDirectory().path, "test.pcm")
        try {
            val fileInputStream = FileInputStream(file)
            Thread(Runnable {
                try {
                    val tempBuffer = ByteArray(minBufferSize)
                    while (fileInputStream.available() > 0 && mIsPlaying) {
                        val readCount = fileInputStream.read(tempBuffer)
                        if (readCount == AudioTrack.ERROR_INVALID_OPERATION || readCount == AudioTrack.ERROR_BAD_VALUE) {
                            continue
                        }
                        if (readCount != 0 && readCount != -1) {
                            mAudioTrack.write(tempBuffer, 0, readCount)
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }).start()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val TAG = "AudioActivity"
    }
}
