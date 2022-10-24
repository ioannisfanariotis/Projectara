package com.example.projectara.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.projectara.R
import com.example.projectara.databinding.ActivityUpdateBinding
import com.example.projectara.firebase.FireStoreClass
import com.example.projectara.models.User
import com.example.projectara.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException

class UpdateActivity : BaseActivity() {

    private var binding: ActivityUpdateBinding? = null

    private var selectedImageFile: Uri? = null
    private var profileImageURL: String = ""
    private lateinit var userDetails: User

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
                Constants.showImageChooser(this)
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), Constants.READ_STORAGE_PERMISSION_CODE)
            }
        }

        binding?.update?.setOnClickListener {
            if (selectedImageFile != null){
                uploadProfileImage()
            }else{
                startLoading(resources.getString(R.string.wait))
                updateUser()
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
                Glide.with(this).load(selectedImageFile).placeholder(R.drawable.ic_user_place_holder).into(binding?.userImage!!)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    fun setUserDataInUI(user: User){
        userDetails = user
        Glide.with(this).load(user.image).placeholder(R.drawable.ic_user_place_holder).into(binding?.userImage!!)
        binding?.username?.setText(user.username)
        binding?.email?.setText(user.email)
        if (user.number != 0L){
            binding?.number?.setText(user.number.toString())
        }
    }

    private fun uploadProfileImage(){
        startLoading(resources.getString(R.string.wait))
        if (selectedImageFile != null){
            val reference: StorageReference = FirebaseStorage.getInstance().reference.child("USER_IMAGE" + System.currentTimeMillis() + "." + Constants.getFileExtension(this, selectedImageFile))
            reference.putFile(selectedImageFile!!).addOnSuccessListener {
                task ->
                    Log.e("Firebase Image URL", task.metadata!!.reference!!.downloadUrl.toString())
                    task.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        uri ->
                            Log.i("Downloadable Image URL", uri.toString())
                        profileImageURL = uri.toString()
                        updateUser()
                    }
            }.addOnFailureListener{
                exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                cancelLoading()
            }
        }
    }

    private fun updateUser(){
        val userHashMap = HashMap<String, Any>()
        var anyChanges = false
        if(profileImageURL.isNotEmpty() && profileImageURL != userDetails.image){
            userHashMap[Constants.IMAGE] = profileImageURL
            anyChanges = true
        }
        if(binding?.username?.text.toString() != userDetails.username){
            userHashMap[Constants.NAME] = binding?.username?.text.toString()
            anyChanges = true
        }
        if (binding?.number?.text.toString() != userDetails.number.toString()){
            userHashMap[Constants.NUMBER] = binding?.number?.text.toString().toLong()
            anyChanges = true
        }
        if (anyChanges) {
            FireStoreClass().updateUser(this, userHashMap)
        }
    }

    fun profileUpdateSuccess(){
        cancelLoading()
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}