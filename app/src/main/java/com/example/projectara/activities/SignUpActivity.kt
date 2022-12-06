package com.example.projectara.activities

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.projectara.R
import com.example.projectara.databinding.ActivitySignUpBinding
import com.example.projectara.firebase.FireStoreClass
import com.example.projectara.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

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
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_back)
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
        val email: String = binding?.suEmail?.text.toString().trim { it <= ' ' }
        val password: String = binding?.suPassword?.text.toString().trim { it <= ' ' }

        if(validateForm(username, email, password)){
            startLoading(resources.getString(R.string.wait))
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    task ->
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            val registeredEmail = firebaseUser.email
                            val user = User(firebaseUser.uid, username, registeredEmail!!)
                            FireStoreClass().registerUser(this, user)
                        } else {
                            cancelLoading()
                            Log.w("Sign up", "failure", task.exception)
                            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                        }
            }
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

    fun successfulRegistration(){
        cancelLoading()
        Log.d("Sign up", "success")
        Toast.makeText(this, "Successfully registered!", Toast.LENGTH_SHORT).show()
        FirebaseAuth.getInstance().signOut()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}