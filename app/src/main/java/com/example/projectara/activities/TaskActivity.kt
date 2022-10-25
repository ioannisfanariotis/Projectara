package com.example.projectara.activities

import android.os.Bundle
import com.example.projectara.R
import com.example.projectara.databinding.ActivityTaskBinding
import com.example.projectara.firebase.FireStoreClass
import com.example.projectara.models.Board
import com.example.projectara.utils.Constants

class TaskActivity : BaseActivity() {

    private var binding: ActivityTaskBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        var boardDocument = ""
        if(intent.hasExtra(Constants.DOCUMENT_ID)){
            boardDocument = intent.getStringExtra(Constants.DOCUMENT_ID).toString()
        }

        startLoading(resources.getString(R.string.wait))
        FireStoreClass().getBoardDetails(this, boardDocument)
    }

    fun boardDetails(board: Board){
        cancelLoading()
        setSupportActionBar(binding?.taskToolbar)
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_white_back)
            actionbar.title = board.name
        }
        binding?.taskToolbar?.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}