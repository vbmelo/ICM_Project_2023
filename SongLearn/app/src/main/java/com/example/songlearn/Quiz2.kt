package com.example.songlearn

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

class Quiz2 : AppCompatActivity() {

    data class Question(
        val questionText: String,
        val options: List<String>,
        val answerIndex: Int
    )
    val quizName= "MusicTheoryQuiz"
    var currentQuestionIndex = 0
    var numCorrectAnswers = 0
    val questions = listOf(
        Question("What does the term 'piano' mean in music?", listOf("Soft", "Loud", "Fast", "Slow"), 0),
        Question("What is the term used for a musical note that is sustained or held?", listOf("Tie", "Rest", "Fermata", "Staccato"), 2),
        Question("What is the term for a musical composition for two voices or instruments?", listOf("Solo", "Duet", "Trio", "Quartet"), 1),
        Question("What is a chord that is made up of three notes called?", listOf("Major chord", "Minor chord", "Triad", "Seventh chord"), 2),
        Question("What is the term for a gradual increase in volume?", listOf("Decrescendo", "Crescendo", "Diminuendo", "Rallentando"), 1),
        Question("What is the term for the pattern of beats in a musical composition?", listOf("Melody", "Harmony", "Rhythm", "Texture"), 2),
        Question("What is the term for a musical composition for three voices or instruments?", listOf("Trio", "Quartet", "Quintet", "Sextet"), 0),
        Question("What is the term for the musical distance between two notes?", listOf("Chord", "Interval", "Scale", "Key"), 1),
        Question("What is the term for a musical composition for four voices or instruments?", listOf("Quartet", "Quintet", "Sextet", "Octet"), 0),
        Question("What is the term for the highest female singing voice?", listOf("Alto", "Soprano", "Tenor", "Baritone"), 1)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        var  database: DatabaseReference;
        database = FirebaseDatabase.getInstance("https://songlearn-default-rtdb.europe-west1.firebasedatabase.app/").getReference("quiz_results")
        val mAuth = FirebaseAuth.getInstance()
        val questionTextView = findViewById<TextView>(R.id.questionText)
        val option1Button = findViewById<Button>(R.id.option1)
        val option2Button = findViewById<Button>(R.id.option2)
        val option3Button = findViewById<Button>(R.id.option3)
        val option4Button = findViewById<Button>(R.id.option4)
        fun updateQuestion() {
            if(currentQuestionIndex != 10){
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
            var numQuestions = questions.size;
            val score = numCorrectAnswers * 100 / numQuestions
            questionTextView.text = "You got $numCorrectAnswers out of $numQuestions!\n\nYour score: $score%"

            // Get the current user's ID and create a new child node in the database with their information
            val user = mAuth.currentUser
            if (user != null) {
                val userId = user.uid
                val userScore = score

                // Create a new child node in the database with the user's information

                val userRef = database.child("users").child(userId).child(quizName)
                userRef.child("score").setValue(userScore)
            }
        }


        updateQuestion()

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
