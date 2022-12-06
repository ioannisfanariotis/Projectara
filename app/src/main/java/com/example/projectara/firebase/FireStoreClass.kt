package com.example.projectara.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.projectara.activities.*
import com.example.projectara.models.Board
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

    fun loadUserData(activity: Activity, readBoardsList: Boolean = false){
        mFireStore.collection(Constants.USERS).document(getId()).get().addOnSuccessListener {
                document ->
                    val loggedUser = document.toObject(User::class.java)!!
                    when(activity){
                        is SignInActivity -> {
                            activity.successfullySignIn(loggedUser)
                        }
                        is MainActivity -> {
                            activity.updateNavigationUserDetails(loggedUser, readBoardsList)
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
            exception ->
                activity.cancelLoading()
                Log.e(activity.javaClass.simpleName, "Error on updating!", exception)
                Toast.makeText(activity, "Error on updating!", Toast.LENGTH_SHORT).show()
        }
    }

    fun createBoard(activity: BoardActivity, board: Board){
        mFireStore.collection(Constants.BOARDS).document().set(board, SetOptions.merge()).addOnSuccessListener {
            Log.e(activity.javaClass.simpleName, "Board Created!")
            Toast.makeText(activity, "Board Created!", Toast.LENGTH_SHORT).show()
            activity.boardCreatedSuccess()
        }.addOnFailureListener {
                exception ->
                    activity.cancelLoading()
                    Log.e(activity.javaClass.simpleName, "Error on board creation!", exception)
                    Toast.makeText(activity, "Error on board creation!", Toast.LENGTH_SHORT).show()
        }
    }

    fun getBoardList(activity: MainActivity){
        mFireStore.collection(Constants.BOARDS).whereArrayContains(Constants.ASSIGNED_TO, getId()).get().addOnSuccessListener {
            document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())
                val boardList: ArrayList<Board> = ArrayList()
                for (i in document.documents){
                    val board = i.toObject(Board::class.java)
                    board?.documentId = i.id
                    boardList.add(board!!)
                }
                activity.showBoardToUi(boardList)
        }.addOnFailureListener {
                exception ->
                    activity.cancelLoading()
                    Log.e(activity.javaClass.simpleName, "Error on board display!", exception)
                    Toast.makeText(activity, "Error on board display!", Toast.LENGTH_SHORT).show()
        }
    }

    fun getBoardDetails(activity: TaskActivity, documentID: String){
        mFireStore.collection(Constants.BOARDS).document(documentID).get().addOnSuccessListener {
                document ->
                    Log.i(activity.javaClass.simpleName, document.toString())
                    val board = document.toObject(Board::class.java)!!
                    board.documentId = document.id
                    activity.boardDetails(board)
        }.addOnFailureListener {
                exception ->
                    activity.cancelLoading()
                    Log.e(activity.javaClass.simpleName, "Error on board display!", exception)
                    Toast.makeText(activity, "Error on board display!", Toast.LENGTH_SHORT).show()
        }
    }
}