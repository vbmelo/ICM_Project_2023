package com.example.songlearn

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class VideoUpload : AppCompatActivity() {
    private val SELECT_VIDEO_REQUEST_CODE = 1
    private var selectedVideoUri: Uri? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_upload)
        auth = Firebase.auth
        val currentUser = auth.currentUser

        val user = auth.currentUser
        if (user == null) {
            // If user is not authenticated, redirect to login page
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val storage: FirebaseStorage = Firebase.storage
        var storageRef = storage.reference
        var videosRef = storageRef.child("users").child(currentUser!!.uid).child("videos")
        val selectVideoButton: Button = findViewById(R.id.selectVideo)
        val uploadVideoButton : Button = findViewById(R.id.UploadVideo)

        selectVideoButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "video/*"
            startActivityForResult(intent, SELECT_VIDEO_REQUEST_CODE)
        }

        uploadVideoButton.setOnClickListener{
            val currentUser: FirebaseUser? = Firebase.auth.currentUser
            if (currentUser != null) {
            if (selectedVideoUri != null) {
                val videoName = selectedVideoUri?.lastPathSegment
                val videoRef = videosRef.child(videoName!!)
                val uploadTask = videoRef.putFile(selectedVideoUri!!)
                uploadTask.addOnSuccessListener {
                    Toast.makeText(this, "Video uploaded successfully", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "Error uploading video", Toast.LENGTH_SHORT).show()
                }
            }
        }}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_VIDEO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedVideoUri = data.data
        }
    }
}

