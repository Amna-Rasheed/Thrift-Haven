package com.example.mad2

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PERMISSION = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val ivProfileIcon = findViewById<ImageView>(R.id.ivProfileIcon)

        ivProfileIcon.setOnClickListener {
            // Check for camera permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_PERMISSION)
            } else {
                // Permission has already been granted
                dispatchTakePictureIntent()
            }
        }

        val tvLogout = findViewById<TextView>(R.id.tvLogout)
        tvLogout.setOnClickListener {
            // Call your logout method here
            logoutUser()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener  { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, HomePageActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                    }
                    startActivity(intent)
                    return@setOnItemSelectedListener  true
                }
                R.id.navigation_shop -> {
                    val intent = Intent(this, ShopPageActivity::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener  true
                }
                R.id.navigation_notifications -> {
                    val intent = Intent(this, NotificationsPageActivity::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener  true
                }
                R.id.navigation_profile -> {
                    // Current activity, no action needed or refresh the activity if needed
                    return@setOnItemSelectedListener  true
                }
            }
            false
        }

        // Optionally, highlight the current activity in the BottomNavigationView
        bottomNavigationView.selectedItemId = R.id.navigation_profile
    }

    private fun logoutUser() {
         FirebaseAuth.getInstance().signOut()

        // Redirect to the login activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val imageView = findViewById<ImageView>(R.id.ivProfileIcon)
            // Convert bitmap to a circular bitmap and set it to ImageView
            val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, imageBitmap)
            roundedBitmapDrawable.isCircular = true
            imageView.setImageDrawable(roundedBitmapDrawable)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent()
        } else {
            // Permission denied. Handle the failure as appropriate
        }
    }
}


