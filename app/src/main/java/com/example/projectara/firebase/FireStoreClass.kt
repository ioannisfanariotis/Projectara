package com.example.projectara.firebase

import android.util.Log
import com.example.projectara.activities.SignInActivity
import com.example.projectara.activities.SignUpActivity
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

    fun signInUser(activity: SignInActivity){
        mFireStore.collection(Constants.USERS).document(getId()).get().addOnSuccessListener { document ->
            val loggedUser = document.toObject(User::class.java)!!
            activity.successfullySignIn(loggedUser)
        }.addOnFailureListener {
            Log.e(activity.javaClass.simpleName, "Error")
        }
    }

    fun getId(): String{
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }
}