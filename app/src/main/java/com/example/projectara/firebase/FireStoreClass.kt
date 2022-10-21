package com.example.projectara.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.projectara.activities.MainActivity
import com.example.projectara.activities.SignInActivity
import com.example.projectara.activities.SignUpActivity
import com.example.projectara.activities.UpdateActivity
import com.example.projectara.models.User
import com.example.projectara.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User){
        mFireStore.collection(Constants.USERS).document(getId()).set(userInfo, SetOptions.merge()).addOnSuccessListener {
            activity.successfulRegistration()
        }.addOnFailureListener {
            Log.e(activity.javaClass.simpleName, "Error")
        }
    }

    fun loadUserData(activity: Activity){
        mFireStore.collection(Constants.USERS).document(getId()).get().addOnSuccessListener { document ->
            val loggedUser = document.toObject(User::class.java)!!
            when(activity){
                is SignInActivity -> {
                    activity.successfullySignIn(loggedUser)
                }
                is MainActivity -> {
                    activity.updateNavigationUserDetails(loggedUser)
                }
                is UpdateActivity -> {
                    activity.setUserDataInUI(loggedUser)
                }
            }
        }.addOnFailureListener {
            when(activity){
                is SignInActivity -> {
                    activity.cancelLoading()
                }
                is MainActivity -> {
                    activity.cancelLoading()
                }
                is UpdateActivity -> {
                    activity.cancelLoading()
                }
            }
            Log.e(activity.javaClass.simpleName, "Error")
        }
    }

    fun getId(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun updateUser(activity: UpdateActivity, userHashMap: HashMap<String, Any>){
        mFireStore.collection(Constants.USERS).document(getId()).update(userHashMap).addOnSuccessListener {
            Log.i(activity.javaClass.simpleName, "Success!")
            Toast.makeText(activity, "Success!", Toast.LENGTH_SHORT).show()
            activity.profileUpdateSuccess()
        }.addOnFailureListener {
            e -> activity.cancelLoading()
            Log.e(activity.javaClass.simpleName, "Error on updating!")
            Toast.makeText(activity, "Error on updating!", Toast.LENGTH_SHORT).show()
        }
    }


}