package com.example.songlearn

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.*
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class DiapasonActivity : AppCompatActivity() {

    private lateinit var btnE: Button
    private lateinit var btnA: Button
    private lateinit var btnD: Button
    private lateinit var btnG: Button
    private lateinit var btnB: Button
    private lateinit var btnE2: Button

    private lateinit var noteDisplay: TextView
    private var dbTextView: TextView? = null
    private var recorder: MediaRecorder? = null
    private var isRecording = false

    private lateinit var soundPool: SoundPool

    companion object {
        private const val PERMISSION_CODE = 1
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diapason)

        noteDisplay = findViewById(R.id.note_display)
        dbTextView = findViewById(R.id.dbTextView)

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), PERMISSION_CODE)
        } else {
            startRecording()
        }

        soundPool = SoundPool.Builder()
            .setMaxStreams(6)
            .build()


        btnE = findViewById(R.id.btn_e)
        btnA = findViewById(R.id.btn_a)
        btnD = findViewById(R.id.btn_d)
        btnG = findViewById(R.id.btn_g)
        btnB = findViewById(R.id.btn_b)
        btnE2 = findViewById(R.id.btn_e2)

        btnE.setOnClickListener { playNote(40) }
        btnA.setOnClickListener { playNote(45) }
        btnD.setOnClickListener { playNote(50) }
        btnG.setOnClickListener { playNote(55) }
        btnB.setOnClickListener { playNote(59) }
        btnE2.setOnClickListener { playNote(64) }

    }

    private fun playNote(note: Int) {
        val duration = 5000L // 5 seconds
        val volume = 100 // full volume
        val priority = 1 // define a prioridade da fila de reprodução
        val streamType = AudioManager.STREAM_MUSIC // define o tipo de fluxo de áudio

        val toneGenerator = ToneGenerator(streamType, volume) // cria um gerador de tom
        toneGenerator.startTone(note, duration.toInt()) // toca o tom correspondente à frequência MIDI
        toneGenerator.release() // libera o gerador de tom quando o som terminar de ser tocado
    }









    private fun startRecording() {
        recorder = MediaRecorder()
        recorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        recorder?.setAudioSamplingRate(44100)
        recorder?.setAudioChannels(1)
        recorder?.setAudioEncodingBitRate(16 * 44100)
        recorder?.setOutputFile("/dev/null")

        try {
            recorder?.prepare()
            recorder?.start()
            isRecording = true
            calculateNote()
            calculateDb()
        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao iniciar gravação", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopRecording() {
        recorder?.stop()
        recorder?.release()
        recorder = null
        isRecording = false
    }

    private fun calculateDb() {
        if (isRecording) {
            val amplitude = recorder?.maxAmplitude ?: 0
            val db = 20 * Math.log10(amplitude / 2700.0)
            dbTextView?.text = String.format("%.2f", db)
        }
        dbTextView?.postDelayed({ calculateDb() }, 100)
    }
    private fun calculateNote() {
        if (isRecording) {
            val amplitude = recorder?.maxAmplitude ?: 0
            val frequency = 440 * Math.pow(2.0, (amplitude / 2700.0 - 5.0) / 12.0)
            val noteIndex = (12 * Math.log(frequency / 440.0) / Math.log(2.0)).toInt()
            val notes = arrayOf("A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#")
            val note = if (noteIndex >= 0 && noteIndex < notes.size) notes[noteIndex] else ""
            noteDisplay.text = note
        }
        noteDisplay.postDelayed({ calculateNote() }, 100)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording()
            } else {
                Toast.makeText(this, "Permissão de gravação de áudio negada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
        stopRecording()
    }
}
