package com.betonification.quizapp

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_quiz_questions.*

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private var mCurrentPosition: Int = 1
    private var mQuestionsList: MutableList<Question>? = null
    private var mSelectedOptionPosition: Int = 0
    private var mSubmittedAnswer = false
    private var mSelectedAnswer = false
    private var mNumberOfCorrectAnswers = 10
    private var mUserName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        mQuestionsList = Constants.getQuestions().toMutableList()

        mQuestionsList!!.shuffle()


        setQuestion()

        tvOptionOne.setOnClickListener(this)
        tvOptionTwo.setOnClickListener(this)
        tvOptionThree.setOnClickListener(this)
        tvOptionFour.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
    }

    private fun setQuestion(){

        val question = mQuestionsList!![mCurrentPosition - 1]

        defaultOptionsView()
        mSubmittedAnswer = false
        mSelectedAnswer = false

        if(mCurrentPosition == mQuestionsList!!.size){
            btnSubmit.text = "FINISH"
        }else{
            btnSubmit.text = "SUBMIT"
        }

        pbProgressBar.progress = mCurrentPosition
        tvProgress.text = "$mCurrentPosition" + "/" + pbProgressBar.max
        tvQuestion.text = question!!.question
        ivImage.setImageResource(question.image)
        tvOptionOne.text = question.optionOne
        tvOptionTwo.text = question.optionTwo
        tvOptionThree.text = question.optionThree
        tvOptionFour.text = question.optionFour
    }

    private fun defaultOptionsView(){
        val options = ArrayList<TextView>()
        options.add(0, tvOptionOne)
        options.add(1, tvOptionTwo)
        options.add(2, tvOptionThree)
        options.add(3, tvOptionFour)

        for (option in options){
            option.setTextColor(Color.parseColor("#363A43"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
        }
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNumber: Int){
        defaultOptionsView()
        mSelectedOptionPosition = selectedOptionNumber
        mSelectedAnswer = true
        tv.setTextColor(Color.parseColor("#000000"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border_bg)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tvOptionOne ->{
                if (!mSubmittedAnswer) selectedOptionView(tvOptionOne, 1)
            }
            R.id.tvOptionTwo ->{
                if (!mSubmittedAnswer) selectedOptionView(tvOptionTwo, 2)
            }
            R.id.tvOptionThree ->{
                if (!mSubmittedAnswer) selectedOptionView(tvOptionThree, 3)
            }
            R.id.tvOptionFour ->{
                if (!mSubmittedAnswer) selectedOptionView(tvOptionFour, 4)
            }
            R.id.btnSubmit ->{
                if(mSelectedOptionPosition == 0 && mSelectedAnswer){

                    when{
                        mCurrentPosition <= mQuestionsList!!.size ->{
                            setQuestion()
                        }else ->{
                        val intent = Intent(this, ResultActivity::class.java)
                        intent.putExtra(Constants.USER_NAME, mUserName)
                        intent.putExtra(Constants.ANSWERS_NUMBER, mNumberOfCorrectAnswers)
                        startActivity(intent)
                        finish()
                        }
                    }
                }else if (mSelectedAnswer){
                    val question = mQuestionsList?.get(mCurrentPosition - 1)
                    if(question!!.correctAnswer != mSelectedOptionPosition){
                        answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                        mNumberOfCorrectAnswers--
                    }
                    answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

                    if(mCurrentPosition == mQuestionsList!!.size){
                        btnSubmit.text = "FINISH"
                        mCurrentPosition++
                    }else{
                        btnSubmit.text = "GO TO NEXT QUESTION"
                        mCurrentPosition++
                    }
                    mSubmittedAnswer = true
                    mSelectedOptionPosition = 0
                }else{
                    Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun answerView(answer: Int, drawableView: Int){
        when (answer){
            1 ->{
                tvOptionOne.background = ContextCompat.getDrawable(this, drawableView)
            }
            2 ->{
                tvOptionTwo.background = ContextCompat.getDrawable(this, drawableView)
            }
            3 ->{
                tvOptionThree.background = ContextCompat.getDrawable(this, drawableView)
            }
            4 ->{
                tvOptionFour.background = ContextCompat.getDrawable(this, drawableView)
            }
        }
    }
}