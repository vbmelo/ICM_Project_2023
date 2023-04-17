package com.example.songlearn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Quiz : AppCompatActivity() {

    data class Question(
        val questionText: String,
        val options: List<String>,
        val answerIndex: Int
    )
    val quizName= "GuitarQuiz"
    var currentQuestionIndex = 0
    var numCorrectAnswers = 0
    val questions = listOf(
        Question("What are the names of the six strings on a standard guitar, starting from the thinnest string?", listOf("EADGBE", "EBGDFA", "GBDFAE", "EDAGBF"), 0),
        Question("What is the name of the symbol used to indicate a sharp note in music notation?", listOf("♯", "♭", "°", "∆"), 0),
        Question("What is the name of the scale that is commonly used in rock and blues music, and is often referred to as the 'pentatonic scale'?", listOf("Minor pentatonic", "Major pentatonic", "Melodic minor", "Harmonic minor"), 0),
        Question("What is the name of the part of the guitar that is used to adjust the tension of the strings and therefore change the pitch of the notes played?", listOf("Bridge", "Headstock", "Fretboard", "Pickup"), 1),
        Question("What is the name of the chord that consists of the notes C, E, and G?", listOf("C major", "C minor", "C diminished", "C augmented"), 0),
        Question("What is the name of the technique used to play two notes on adjacent strings at the same fret, commonly used in rock and metal music?", listOf("Power chord", "Hammer-on", "Pull-off", "Slide"), 1),
        Question("What is the name of the musical interval between the notes C and E?", listOf("Major third", "Minor third", "Perfect fifth", "Perfect fourth"), 0),
        Question("What is the name of the type of scale that consists of seven different notes and is commonly used in Western classical music?", listOf("Major scale", "Minor scale", "Pentatonic scale", "Blues scale"), 0),
        Question("What is the name of the part of the guitar that is used to mute or dampen the strings, and is commonly used in funk and rhythm guitar playing?", listOf("Fretting hand", "Picking hand", "Strumming hand", "Left hand"), 3),
        Question("What is the name of the chord that consists of the notes C, E♭, and G?", listOf("C minor", "C major", "C diminished", "C augmented"), 0)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        var  database: DatabaseReference;
        database = FirebaseDatabase.getInstance("https://songlearn-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users")
        val mAuth = FirebaseAuth.getInstance()
        val nextQuiz = findViewById<Button>(R.id.nextQuiz)
        nextQuiz.visibility = View.GONE

        val questionTextView = findViewById<TextView>(R.id.questionText)
        val option1Button = findViewById<Button>(R.id.option1)
        val option2Button = findViewById<Button>(R.id.option2)
        val option3Button = findViewById<Button>(R.id.option3)
        val option4Button = findViewById<Button>(R.id.option4)
        fun updateQuestion() {
            if(currentQuestionIndex < questions.size){
            val currentQuestion = questions[currentQuestionIndex]
            questionTextView.text = currentQuestion.questionText
            option1Button.text = currentQuestion.options[0]
            option2Button.text = currentQuestion.options[1]
            option3Button.text = currentQuestion.options[2]
            option4Button.text = currentQuestion.options[3]
        }}
        fun showResults() {
            // Hide answer buttons and show result message
            option1Button.visibility = View.GONE
            option2Button.visibility = View.GONE
            option3Button.visibility = View.GONE
            option4Button.visibility = View.GONE
            nextQuiz.visibility = View.VISIBLE
            var numQuestions = questions.size;
            val score = numCorrectAnswers * 100 / numQuestions
            questionTextView.text = "You got $numCorrectAnswers out of $numQuestions!\n\nYour score: $score%"

            // Get the current user's ID and create a new child node in the database with their information
            val user = mAuth.currentUser
            if (user != null) {
                val userId = user.uid
                val userScore = score

                // Create a new child node in the database with the user's information
                val userRef = database.child(userId).child("quiz_results").child(quizName)
                userRef.child("score").setValue(userScore)
            }
        }


        updateQuestion()
        nextQuiz.setOnClickListener{
            val intent = Intent(this, Quiz2::class.java)
            startActivity(intent)
            finish()
        }
        option1Button.setOnClickListener {
            if (currentQuestionIndex < questions.size) {
                if (questions[currentQuestionIndex].answerIndex == 0) {
                    numCorrectAnswers++
                }
                currentQuestionIndex++
                updateQuestion()
            } else {
                showResults()
            }
        }
        option2Button.setOnClickListener {
            if (currentQuestionIndex < questions.size) {
                if (questions[currentQuestionIndex].answerIndex == 1) {
                    numCorrectAnswers++
                }
                currentQuestionIndex++
                updateQuestion()
            } else {
                showResults()
            }
        }
        option3Button.setOnClickListener {
            if (currentQuestionIndex < questions.size) {
                if (questions[currentQuestionIndex].answerIndex == 2) {
                    numCorrectAnswers++
                }
                currentQuestionIndex++
                updateQuestion()
            } else {
                showResults()
            }
        }
        option4Button.setOnClickListener {
            if (currentQuestionIndex < questions.size) {
                if (questions[currentQuestionIndex].answerIndex == 3) {
                    numCorrectAnswers++
                }
                currentQuestionIndex++
                updateQuestion()
            } else {
                showResults()
            }
        }



    }




}
