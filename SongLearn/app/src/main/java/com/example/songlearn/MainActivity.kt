package com.example.songlearn

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth
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
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        val intent = Intent(this, Home::class.java)
                        startActivity(intent)

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }

        }

    }
}