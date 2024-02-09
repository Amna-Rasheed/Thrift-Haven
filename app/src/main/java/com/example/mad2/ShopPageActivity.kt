package com.example.mad2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class ShopPageActivity : AppCompatActivity(){
    private val REQUEST_PHONE_CALL = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, HomePageActivity::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_shop -> {
                    // Current activity, no action needed or refresh the activity if needed
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    val intent = Intent(this, NotificationsPageActivity::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }

        bottomNavigationView.selectedItemId = R.id.navigation_shop

        setupCallButtons()
    }

    private fun setupCallButtons() {
        val phoneIconShop1: ImageView = findViewById(R.id.phone_icon_shop1)
        phoneIconShop1.setOnClickListener {
            dialPhoneNumber("+94763190565")
        }

        val phoneIconShop2: ImageView = findViewById(R.id.phone_icon_shop2)
        phoneIconShop2.setOnClickListener {
            dialPhoneNumber("+94763190565")
        }

        val phoneIconShop3: ImageView = findViewById(R.id.phone_icon_shop3)
        phoneIconShop3.setOnClickListener {
            dialPhoneNumber("+94763190565")
        }
    }

    private fun dialPhoneNumber(phoneNumber: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
        } else {
            startCall(phoneNumber)
        }
    }

    private fun startCall(phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$phoneNumber")
        startActivity(callIntent)
    }
}