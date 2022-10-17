package com.example.parsetagram

import android.content.ClipDescription
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.parsetagram.fragments.ComposeFragment
import com.example.parsetagram.fragments.HomeFragment
import com.example.parsetagram.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.*
import java.io.File

//TODO: Move logout to appbar temporarily
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager: FragmentManager = supportFragmentManager
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)

        toolbar.inflateMenu(R.menu.menu)

        findViewById<BottomNavigationView>(R.id.bottom_nav_bar).setOnItemSelectedListener {

            item ->

            var currentFragment: Fragment? = null
            when (item.itemId) {

                R.id.bottom_nav_home -> {
                    currentFragment = HomeFragment()
                }
                R.id.bottom_nav_compose -> {
                    currentFragment = ComposeFragment()
                }
                R.id.bottom_nav_profile -> {
                    currentFragment = ProfileFragment()
                }
            }

            if (currentFragment != null) {
                fragmentManager.beginTransaction().replace(R.id.frame_layout_container, currentFragment).commit()
            }
            // This true signifies we handled the user interaction
            true
        }

        findViewById<BottomNavigationView>(R.id.bottom_nav_bar).selectedItemId = R.id.bottom_nav_home
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        const val TAG = "MainActivity"
    }
}