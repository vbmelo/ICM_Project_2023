package com.example.songlearn

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.songlearn.databinding.ActivityHomeBinding

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.setOnItemSelectedListener { item ->

            when (item.itemId) {

                R.id.home -> startActivity(Intent(this, Home::class.java))
                R.id.diapason -> startActivity(Intent(this, DiapasonActivity::class.java))
                R.id.classes -> startActivity(Intent(this, VideoListActivity::class.java))
                R.id.upload -> startActivity(Intent(this, VideoUpload::class.java))
                R.id.quiz -> startActivity(Intent(this, Quiz::class.java))

                else -> {

                }
            }

            true
        }
    }
}
