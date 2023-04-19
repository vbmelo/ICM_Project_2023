package com.example.songlearn

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import java.io.File
import java.io.FileOutputStream

class AudioRecord(private val outputFile: File) {
    private val audioSource = MediaRecorder.AudioSource.MIC
    private val sampleRate = 44100
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
    private var recorder: AudioRecord? = null
    private var isRecording = false

    @SuppressLint("MissingPermission")
    fun start() {
        isRecording = true
        recorder = AudioRecord(audioSource, sampleRate, channelConfig, audioFormat, bufferSize).apply {
            startRecording()
        }

        Thread(Runnable {
            val buffer = ByteArray(bufferSize)
            val output = FileOutputStream(outputFile)
            while (isRecording) {
                val read = recorder!!.read(buffer, 0, bufferSize)
                output.write(buffer, 0, read)
            }
            output.close()
        }).start()
    }

    fun stop() {
        isRecording = false
        recorder?.stop()
        recorder?.release()
        recorder = null
    }
}
