package com.example.songlearn

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ExperimentalGetImage
import androidx.core.content.ContextCompat.startActivity
import com.example.songlearn.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage



@ExperimentalGetImage
class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewVideo: VideoView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var storageRef: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        storageRef = FirebaseStorage.getInstance()

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> startActivity(Intent(this, Home::class.java))
                R.id.diapason -> startActivity(Intent(this, DiapasonActivity::class.java))
                R.id.classes -> startActivity(Intent(this, VideoListActivity::class.java))
                R.id.upload -> startActivity(Intent(this, VideoUpload::class.java))
                R.id.quiz -> startActivity(Intent(this, Quiz::class.java))
                else -> {}
            }
            true
        }

        viewVideo = findViewById(R.id.videoView)
        viewVideo.setOnClickListener {
            if (viewVideo.isPlaying) {
                viewVideo.pause()
            } else {
                viewVideo.start()
            }
        }

        val grade1 : TextView = findViewById(R.id.grade1)
        val grade2 : TextView = findViewById(R.id.grade2)
        var  database: DatabaseReference;

        database = FirebaseDatabase.getInstance("https://songlearn-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users")

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val quizResultsRef = database.child(userId).child("quiz_results")

            quizResultsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val guitarQuizScore = dataSnapshot.child("GuitarQuiz").child("score").getValue(Double::class.java)
                    val musicTheoryQuizScore = dataSnapshot.child("MusicTheoryQuiz").child("score").getValue(Double::class.java)
                    grade1.text = String.format("GuitarQuiz: %.2f", guitarQuizScore)
                    grade2.text = String.format("MusicTheoryQuiz: %.2f", musicTheoryQuizScore)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors here
                }
            })






        val logoutButton: Button = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val videosRef = storageRef.getReference("videos")
        videosRef.listAll().addOnSuccessListener { listResult ->
            val sortedList = listResult.items
            val latestVideoRef = sortedList.last()
            latestVideoRef?.downloadUrl?.addOnSuccessListener { uri: Uri ->
                viewVideo.setVideoURI(uri)
            }
        }.addOnFailureListener { exception ->
            // Handle any errors
        }
    }
}}
