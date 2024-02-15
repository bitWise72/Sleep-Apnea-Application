package com.example.sleepapnea

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sleepapnea.ml.BestFp16
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer

class MainActivity : AppCompatActivity() {

    private var permissionsLauncher: ActivityResultLauncher<Set<String>>? = null
    private lateinit var healthConnectManager: HealthConnectManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        healthConnectManager = HealthConnectManager(this)

        val viewModel = ViewModelProvider(this,
            SleepSessionViewModelFactory(healthConnectManager))[SleepSessionViewModel::class.java]

        val onPermissionsResult = { viewModel.initialLoad() }

        // Initialize permissions launcher
        permissionsLauncher = registerForActivityResult(viewModel.permissionsLauncher) {
            onPermissionsResult
        }

        viewModel.getPermissionsGrantedValue().observe(this, Observer {
            if(it==true)
                Log.d("TAG_1", "onCreate: permission granted")
            else
                permissionsLauncher!!.launch(viewModel.permissions)
        })
        val nav_bar= findViewById<BottomNavigationView>(R.id.main_bottom_nav)
        nav_bar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    true
                }

                R.id.nav_drowsy -> {
                    true
                }

                R.id.nav_diet -> {
                    true
                }

                else -> {
                    false
                }
            }
        }




    }

}