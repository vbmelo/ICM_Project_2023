package com.example.songlearn

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.camera.core.ExperimentalGetImage
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

@ExperimentalGetImage
class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth
        var  database: DatabaseReference;
        database = FirebaseDatabase.getInstance("https://songlearn-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val registerEmail : EditText = findViewById(R.id.idEmailLogin)
        val registerPassword : EditText = findViewById(R.id.idPasswordLogin)
        val buttonLogin : Button = findViewById(R.id.buttonLogin)
        val buttonSignUp: Button = findViewById(R.id.buttonSignUp)
        buttonSignUp.setOnClickListener{
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
        buttonLogin.setOnClickListener{
            val email: String = registerEmail.text.toString()
            val password : String = registerPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(baseContext, "Please fill in all the fields.", Toast.LENGTH_SHORT).show()
            } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    // Check if user is authenticated
                    if (user != null) {
                        getCurrentLocation()
                        val intent = Intent(this, Home::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(baseContext, "Please verify your email address.",
                            Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }}



    }
    fun saveLocationToFirebase(latitude: Double, longitude: Double) {
        val user = auth.currentUser
        if (user != null) {
            val uid = user.uid
            var  database: DatabaseReference;
            database = FirebaseDatabase.getInstance("https://songlearn-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users")

            val ref =  database.child("users").child(uid).child("location")
            val location = mapOf("lat" to latitude, "long" to longitude)
            ref.child("lat").setValue(latitude)
            ref.child("lon").setValue(longitude)
        }
    }

    fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    saveLocationToFirebase(latitude, longitude)
                }
            }
    }

}