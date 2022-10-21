package com.example.projectara.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.example.projectara.R
import com.example.projectara.databinding.ActivityMainBinding
import com.example.projectara.firebase.FireStoreClass
import com.example.projectara.models.User
import com.example.projectara.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var binding: ActivityMainBinding? = null
    companion object{
        private const val PROFILE_REQUEST_CODE: Int = 100
    }
    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.barMain?.mainToolbar)
        binding?.barMain?.mainToolbar?.setNavigationIcon(R.drawable.ic_navigation_menu)
        binding?.barMain?.mainToolbar?.setNavigationOnClickListener {
            toggle()
        }
        binding?.navView?.setNavigationItemSelectedListener(this)

        binding?.barMain?.fab?.setOnClickListener {
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

    fun updateNavigationUserDetails(user: User){
        userName = user.username
        binding?.navView?.getHeaderView(0)?.findViewById<TextView>(R.id.username)?.let {
            it.text = user.username
        }
        val userImage = binding?.navView?.getHeaderView(0)?.findViewById<CircleImageView>(R.id.picture)
        Glide.with(this).load(user.image).placeholder(R.drawable.ic_background).into(userImage!!)
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
        }else{
            Log.e("Cancelled", "Cancelled")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}