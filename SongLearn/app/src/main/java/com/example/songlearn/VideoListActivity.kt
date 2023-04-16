package com.example.songlearn


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class VideoListActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var videosRef: StorageReference
    private lateinit var videoList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)

        auth = Firebase.auth
        currentUser = auth.currentUser!!

        storage = Firebase.storage
        storageRef = storage.reference
        videosRef = storageRef.child("users").child(currentUser.uid).child("videos")

        videoList = findViewById(R.id.video_list)

        loadVideos()
    }

    private fun loadVideos() {
        val videoListItems = ArrayList<String>()
        videosRef.listAll().addOnSuccessListener { listResult ->
            listResult.items.forEach { item ->
                videoListItems.add(item.name)
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, videoListItems)
            videoList.adapter = adapter
            videoList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val videoRef = videosRef.child(videoListItems[position])
                videoRef.downloadUrl.addOnSuccessListener { uri ->
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    intent.setDataAndType(uri, "video/*")
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivity(intent)
                }
            }
        }
    }
}
