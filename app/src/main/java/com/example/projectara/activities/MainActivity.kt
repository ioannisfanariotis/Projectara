package com.example.projectara.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.projectara.R
import com.example.projectara.adapters.BoardItemAdapter
import com.example.projectara.databinding.ActivityMainBinding
import com.example.projectara.firebase.FireStoreClass
import com.example.projectara.models.Board
import com.example.projectara.models.User
import com.example.projectara.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var binding: ActivityMainBinding? = null
    companion object{
        const val PROFILE_REQUEST_CODE: Int = 100
        const val CREATE_BOARD_REQUEST_CODE: Int = 200
    }
    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.mainContent?.mainToolbar)
        binding?.mainContent?.mainToolbar?.setNavigationIcon(R.drawable.ic_navigation_menu)
        binding?.mainContent?.mainToolbar?.setNavigationOnClickListener {
            toggle()
        }
        binding?.navView?.setNavigationItemSelectedListener(this)

        binding?.mainContent?.fab?.setOnClickListener {
            val intent = Intent(this, BoardActivity::class.java)
            intent.putExtra(Constants.NAME, userName)
            startActivity(intent)
        }

        FireStoreClass().loadUserData(this)
    }

    private fun toggle(){
        if(binding?.drawer!!.isDrawerOpen(GravityCompat.START)){
            binding?.drawer!!.closeDrawer(GravityCompat.START)
        }else{
            binding?.drawer!!.openDrawer(GravityCompat.START)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.my_profile -> {
                val intent = Intent(this, UpdateActivity::class.java)
                startActivityForResult(intent, PROFILE_REQUEST_CODE)
            }
            R.id.sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            }
        }
        binding?.drawer!!.closeDrawer(GravityCompat.START)
        return true
    }

    fun updateNavigationUserDetails(user: User, readBoardsList: Boolean){
        cancelLoading()
        userName = user.username
        binding?.navView?.getHeaderView(0)?.findViewById<TextView>(R.id.username)?.let {
            it.text = user.username
        }
        val userImage = binding?.navView?.getHeaderView(0)?.findViewById<CircleImageView>(R.id.picture)
        Glide.with(this).load(user.image).placeholder(R.drawable.ic_background).into(userImage!!)

        if (readBoardsList) {
            startLoading(resources.getString(R.string.wait))
            FireStoreClass().getBoardList(this)
        }
    }

    fun showBoardToUi(boardsList: ArrayList<Board>){
        cancelLoading()

        if (boardsList.size > 0) {
            binding?.mainContent?.noBoards?.visibility = View.GONE
            binding?.mainContent?.rvBoard?.visibility = View.VISIBLE
            binding?.mainContent?.rvBoard?.layoutManager = LinearLayoutManager(this)
            binding?.mainContent?.rvBoard?.setHasFixedSize(true)

            val adapter = BoardItemAdapter(this, boardsList)
            binding?.mainContent?.rvBoard?.adapter = adapter
        } else {
            binding?.mainContent?.noBoards?.visibility = View.VISIBLE
            binding?.mainContent?.rvBoard?.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        if(binding?.drawer!!.isDrawerOpen(GravityCompat.START)){
            binding?.drawer!!.closeDrawer(GravityCompat.START)
        }else{
            doubleBackToExit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PROFILE_REQUEST_CODE){
            FireStoreClass().loadUserData(this)
        }else if(resultCode == Activity.RESULT_OK && requestCode == CREATE_BOARD_REQUEST_CODE){
            FireStoreClass().getBoardList(this)
        }else{
            Log.e("Cancelled", "Cancelled")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}