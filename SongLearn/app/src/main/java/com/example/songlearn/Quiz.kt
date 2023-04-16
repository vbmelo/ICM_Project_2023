package com.example.songlearn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class Quiz : AppCompatActivity() {
    data class Question(
        val questionText: String,
        val options: List<String>,
        val answerIndex: Int
    )

    var currentQuestionIndex = 0
    var numCorrectAnswers = 0
    val questions = listOf(
        Question("What is the capital of France?", listOf("Paris", "Madrid", "Rome", "Berlin"), 0),
        Question("What is the largest country in the world by land area?", listOf("Russia", "China", "USA", "Canada"), 0),
        Question("What is the currency of Japan?", listOf("Yen", "Dollar", "Euro", "Pound"), 0),
        Question("Who painted the Mona Lisa?", listOf("Leonardo da Vinci", "Vincent van Gogh", "Pablo Picasso", "Claude Monet"), 0),
        Question("Which planet is known as the Red Planet?", listOf("Mars", "Venus", "Jupiter", "Mercury"), 0)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val questionTextView = findViewById<TextView>(R.id.questionText)
        val option1Button = findViewById<Button>(R.id.option1)
        val option2Button = findViewById<Button>(R.id.option2)
        val option3Button = findViewById<Button>(R.id.option3)
        val option4Button = findViewById<Button>(R.id.option4)
        fun updateQuestion() {
            if(currentQuestionIndex != 5){
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
