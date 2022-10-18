package com.example.projectara.activities

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.projectara.R
import com.example.projectara.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity() {

    private var binding: ActivitySignUpBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setSupportActionBar(binding?.toolbarSignUp)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
        }
        binding?.toolbarSignUp?.setNavigationOnClickListener{
            onBackPressed()
        }

        binding?.signUp?.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser(){
        val username: String = binding?.username?.text.toString().trim { it <= ' ' }
        val email: String = binding?.email?.text.toString().trim { it <= ' ' }
        val password: String = binding?.password?.text.toString().trim { it <= ' ' }

        if(validateForm(username, email, password)){
            Toast.makeText(this, "Completed registration!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateForm(username: String, email: String, password: String):Boolean{
        return when{
            TextUtils.isEmpty(username)-> {
                showError("Please enter a username")
                false
            }
            TextUtils.isEmpty(email)-> {
                showError("Please enter an email")
                false
            }
            TextUtils.isEmpty(password)-> {
                showError("Please enter a password")
                false
            }else -> {
                true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}