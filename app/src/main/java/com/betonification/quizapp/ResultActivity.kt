package com.betonification.quizapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.betonification.quizapp.Constants.ANSWERS_NUMBER
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        val username = intent.getStringExtra(Constants.USER_NAME)
        tvUserName.text = username
        val correctAnswers = intent.getIntExtra(Constants.ANSWERS_NUMBER, 0)

        tvScore.text = "Your score is $correctAnswers out of 10"

        btnFinish.setOnClickListener(){
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}