package com.example.songlearn
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView

class metronomo : AppCompatActivity() {

    private lateinit var soundPool: SoundPool
    private var soundId: Int = 0
    private var bpm: Int = 120
    private var isPlaying: Boolean = false
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metronomo)

        // Initialize SoundPool
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_GAME)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        // Load sound file
        soundId = soundPool.load(this, R.raw.click_sound, 1)

        // Initialize views
        val bpmEditText: EditText = findViewById(R.id.bpm_edit_text)
        val bpmSeekBar: SeekBar = findViewById(R.id.bpm_seek_bar)
        val bpmTextView: TextView = findViewById(R.id.bpm_text_view)
        val playButton: Button = findViewById(R.id.play_button)

        // Set initial values
        bpmSeekBar.progress = bpm - 40
        bpmTextView.text = "$bpm bpm"
        bpmEditText.setText(bpm.toString())

        // Set up listeners
        bpmSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                bpm = progress + 40
                bpmTextView.text = "$bpm bpm"
                bpmEditText.setText(bpm.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        bpmEditText.setOnEditorActionListener { textView, _, _ ->
            val inputBpm = textView.text.toString().toIntOrNull()
            if (inputBpm != null) {
                bpm = inputBpm
                bpmSeekBar.progress = bpm - 40
                bpmTextView.text = "$bpm bpm"
            }
            true
        }

        playButton.setOnClickListener {
            if (isPlaying) {
                // Stop metronome
                isPlaying = false
                playButton.text = "PLAY"
                handler.removeCallbacksAndMessages(null)
            } else {
                // Start metronome
                isPlaying = true
                playButton.text = "STOP"
                val intervalMs = (60000f / bpm).toLong()
                playSound()
                handler.postDelayed({
                    playSound()
                    if (isPlaying) {
                     
                    }
                }, intervalMs)
            }
        }
    }

    private fun playSound() {
        soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}

