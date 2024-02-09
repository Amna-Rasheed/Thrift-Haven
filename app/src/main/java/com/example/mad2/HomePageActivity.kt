package com.example.mad2

import Cloth
import ClothesAdapter
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomePageActivity : AppCompatActivity(), SensorEventListener{

    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var clothesAdapter: ClothesAdapter
    private lateinit var sensorManager: SensorManager
    private var proximitySensor: Sensor? = null
    private var proximitySensorListener: SensorEventListener? = null
    private lateinit var powerManager: PowerManager
    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        db = FirebaseFirestore.getInstance()

        setupBottomNavigationView()
        setupRecyclerView()
        fetchClothes()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "example:tag")

        if (proximitySensor == null){
            Toast.makeText(this, "Proximity sensor not available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBottomNavigationView() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> true
                R.id.navigation_shop -> {
                    startActivity(Intent(this, ShopPageActivity::class.java))
                    true
                }
                R.id.navigation_notifications -> {
                    startActivity(Intent(this, NotificationsPageActivity::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.navigation_home
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.clothesRecyclerView) // Add RecyclerView in your XML
        recyclerView.layoutManager = GridLayoutManager(this, 2) // 2 columns
        clothesAdapter = ClothesAdapter(listOf()) // Initialize your adapter
        recyclerView.adapter = clothesAdapter
    }

    private fun fetchClothes() {
        db.collection("clothes").get().addOnSuccessListener { documents ->
            val clothesList = documents.map { document ->
                // Correctly map all fields from the document to the Cloth constructor
                Cloth(
                    name = document.getString("name") ?: "",
                    price = document.getDouble("price") ?: 0.0, // Assuming price is stored as a Double in Firestore
                    image = document.getString("image") ?: "",
                    description = document.getString("description") ?: "",
                    category = document.getString("category") ?: ""
                )
            }
            clothesAdapter.updateData(clothesList)
        }.addOnFailureListener { exception ->
            // Handle any errors here
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        proximitySensor?.let {
            if (event.values[0] < it.maximumRange) {
                // Detected something nearby
//                Toast.makeText(this, "Near", Toast.LENGTH_SHORT).show()
                turnOffScreen()
            } else {
                // Nothing is nearby
//                Toast.makeText(this, "Far", Toast.LENGTH_SHORT).show()
                turnOnScreen()
            }
        }
    }
    private fun turnOffScreen() {
        if (!wakeLock.isHeld) {
            wakeLock.acquire()
        }
    }

    private fun turnOnScreen() {
        if (wakeLock.isHeld) {
            wakeLock.release()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}

