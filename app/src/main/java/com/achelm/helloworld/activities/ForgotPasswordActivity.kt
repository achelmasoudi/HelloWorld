package com.achelm.helloworld.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.achelm.helloworld.R

class ForgotPasswordActivity : AppCompatActivity() {

    private var arrowBackButton: CardView? = null
    private  var sendButton: CardView? = null
    private var emailEditText: TextInputLayout? = null
    private var sendTextFromSendBtn: TextView? = null
    private var loadingAnimation: LottieAnimationView? = null

    private lateinit var toolbar: Toolbar

    //Firebase
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        sendButton = findViewById(R.id.forgotPasswordActivity_sendButtonId)
        sendTextFromSendBtn = findViewById(R.id.forgotPasswordActivity_sendButtonId_textView)
        loadingAnimation = findViewById(R.id.forgotPasswordActivity_sendButtonId_loadingLottie)

        sendButton!!.setOnClickListener {
            //Signup Function
            forgotPassProcess()
        }

        toolbar = findViewById(R.id.forgotPasswordActivity_toolBarId)
        // Set arrow back button to Toolbar
        toolbar.setNavigationIcon(R.drawable.arrow_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun forgotPassProcess() {
        //Initialize the variables
        emailEditText = findViewById(R.id.forgotPasswordActivity_emailId)
        var email: String = emailEditText!!.editText!!.text.toString().trim()

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
        } else {
            loadingAnimation!!.visibility = View.VISIBLE
            sendTextFromSendBtn!!.visibility = View.GONE

            // Hide the keyboard
            var inputMethodManager: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(sendButton!!.getWindowToken(), 0);

            mAuth = FirebaseAuth.getInstance()

            mAuth!!.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "An email to reset your password has been sent. Please check your inbox.",
                        Toast.LENGTH_LONG
                    ).show()
                    loadingAnimation!!.visibility = View.GONE
                    sendTextFromSendBtn!!.visibility = View.VISIBLE
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Failed to send password reset email. Please ensure your email address is correct and try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadingAnimation!!.visibility = View.GONE
                    sendTextFromSendBtn!!.visibility = View.VISIBLE
                }
            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}