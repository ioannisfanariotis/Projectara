package com.example.projectara.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

import com.example.projectara.R
import com.example.projectara.databinding.ActivityUpdateBinding
import com.example.projectara.firebase.FireStoreClass
import com.example.projectara.models.User
import java.io.IOException

class UpdateActivity : BaseActivity() {

    private var binding: ActivityUpdateBinding? = null
    companion object{
        private const val READ_STORAGE_PERMISSION_CODE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2
    }

    private var selectedImageFile: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.updateToolbar)
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_white_back)
            actionbar.title = resources.getString(R.string.profile)
        }
        binding?.updateToolbar?.setNavigationOnClickListener {
            onBackPressed()
        }

        FireStoreClass().loadUserData(this)

        binding?.userImage?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                showImageChooser()
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_STORAGE_PERMISSION_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showImageChooser()
            }
        }else{
            Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showImageChooser(){
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST_CODE && data!!.data != null){
            selectedImageFile = data.data
            try {
                Glide.with(this).load(selectedImageFile).placeholder(R.drawable.ic_user_place_holder).into(binding?.userImage!!)
            }catch (e: IOException){
                e.printStackTrace()
            }

        }
    }

    fun setUserDataInUI(user: User){
        Glide.with(this).load(user.image).placeholder(R.drawable.ic_user_place_holder).into(binding?.userImage!!)
        binding?.username?.setText(user.username)
        binding?.email?.setText(user.email)
        if (user.number != 0L){
            binding?.number?.setText(user.number.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}