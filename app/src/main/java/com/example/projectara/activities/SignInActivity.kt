package com.example.projectara.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.projectara.R
import com.example.projectara.databinding.ActivitySignInBinding
import com.example.projectara.firebase.FireStoreClass
import com.example.projectara.models.User
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : BaseActivity() {

    private var binding: ActivitySignInBinding? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        auth = FirebaseAuth.getInstance()

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setSupportActionBar(binding?.toolbarSignIn)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back)
        }
        binding?.toolbarSignIn?.setNavigationOnClickListener{
            onBackPressed()
        }

        binding?.signIn?.setOnClickListener {
            signInUser()
        }
    }

    private fun signInUser(){
        val email: String = binding?.siEmail?.text.toString().trim { it <= ' ' }
        val password: String = binding?.siPassword?.text.toString().trim { it <= ' ' }

        if (validateForm(email, password)){
            startLoading(resources.getString(R.string.wait))
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    FireStoreClass().signInUser(this)
                }else{
                    cancelLoading()
                    Log.w("Sign in", "failure", task.exception)
                    Toast.makeText(this, "The user doesn't exist!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateForm(email: String, password: String):Boolean{
        return when{
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

    fun successfullySignIn(user: User){
        cancelLoading()
        Log.d("Sign in", "success")
        Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}