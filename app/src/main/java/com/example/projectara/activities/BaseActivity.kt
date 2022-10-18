package com.example.projectara.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.projectara.R
import com.example.projectara.databinding.ActivityBaseBinding
import com.example.projectara.databinding.LoadingSpinnerBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

open class BaseActivity : AppCompatActivity() {

    private var binding: ActivityBaseBinding? = null
    private var doubleBackToExitPressedOnce = false
    private lateinit var myLoader: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    fun startLoading(text: String){
        myLoader = Dialog(this)
        val loaderBinding = LoadingSpinnerBinding.inflate(layoutInflater)
        myLoader.setContentView(loaderBinding.root)
        myLoader.setCanceledOnTouchOutside(false)
        loaderBinding.text.text = text
        myLoader.show()
    }

    fun cancelLoading(){
        myLoader.dismiss()
    }

    fun getCurrentUserId(): String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun doubleBackToExit(){
        if(doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Click back again to exit", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({doubleBackToExitPressedOnce = false}, 2000)
    }

    fun showError(message: String){
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.error))
        snackBar.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}