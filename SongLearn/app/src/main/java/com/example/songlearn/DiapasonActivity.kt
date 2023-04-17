package com.example.songlearn

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.songlearn.R

class DiapasonActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diapason)
        val diapason_button : Button = findViewById(R.id.diapason_button)
        val diapason_stop_button : Button = findViewById(R.id.stopDiapason)

        mediaPlayer = MediaPlayer.create(this, R.raw.a4)
        diapason_button.setOnClickListener {
            mediaPlayer.start()
        }

        diapason_stop_button.setOnClickListener{
            mediaPlayer.pause()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
