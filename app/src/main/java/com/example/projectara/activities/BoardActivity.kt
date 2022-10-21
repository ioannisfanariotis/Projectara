package com.example.projectara.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.projectara.R
import com.example.projectara.databinding.ActivityBoardBinding
import com.example.projectara.utils.Constants
import java.io.IOException

class BoardActivity : BaseActivity() {

    private var binding: ActivityBoardBinding? = null
    private var selectedImageFile: Uri? = null
    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.boardToolbar)
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_white_back)
            actionbar.title = resources.getString(R.string.board_title)
        }
        binding?.boardToolbar?.setNavigationOnClickListener {
            onBackPressed()
        }

        if(intent.hasExtra(Constants.NAME)){
            userName = intent.getStringExtra(Constants.NAME).toString()
        }

        binding?.boardImage?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), Constants.READ_STORAGE_PERMISSION_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
            }
        }else{
            Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_REQUEST_CODE && data!!.data != null){
            selectedImageFile = data.data
            try {
                Glide.with(this).load(selectedImageFile).placeholder(R.drawable.ic_board_place_holder).into(binding?.boardImage!!)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    fun boardCreatedSuccess(){
        cancelLoading()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}