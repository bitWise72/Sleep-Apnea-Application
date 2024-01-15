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
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer

class MainActivity : AppCompatActivity() {

    private var permissionsLauncher: ActivityResultLauncher<Set<String>>? = null
    private lateinit var healthConnectManager: HealthConnectManager

    lateinit var imageProcessor:ImageProcessor
    lateinit var tensorImage:TensorImage
    lateinit var bitmap: Bitmap
    lateinit var model:BestFp16

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

        imageProcessor= ImageProcessor.Builder()
            .add(ResizeOp(416,416,ResizeOp.ResizeMethod.BILINEAR))
            .build()
        tensorImage= TensorImage(DataType.FLOAT32)

        bitmap= BitmapFactory.decodeResource(this.resources,R.drawable.test3)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 416, 416, true)
        tensorImage.load(resizedBitmap)
        tensorImage = imageProcessor.process(tensorImage);
        Log.d("TAG_1", "onCreate Height: ${tensorImage.tensorBuffer.shape[2]}  ${tensorImage.width}")
        model = BestFp16.newInstance(this)

// Creates inputs for reference.
//        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 416, 416, 3), DataType.FLOAT32)
//        inputFeature0.loadBuffer(ByteBuffer.allocate(Int.MAX_VALUE))

// Runs model inference and gets result.
        val reshapedShape = intArrayOf(1, 416, 416, 3)
        val outputs = model.process(reshapeTensorBuffer(tensorImage.tensorBuffer,reshapedShape))
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        Log.d("TAG_1", "onCreate feature: ${outputFeature0.shape[1]}")

    }

    fun reshapeTensorBuffer(original: TensorBuffer, newShape: IntArray): TensorBuffer {
        // Check if the number of elements matches in both shapes
        require(original.flatSize == newShape.reduce(Int::times)) { "Number of elements must match for reshaping." }

        // Create a new TensorBuffer with the desired shape
        val reshapedTensorBuffer = TensorBuffer.createFixedSize(newShape, original.dataType)

        // Copy the data from the original TensorBuffer to the reshaped TensorBuffer
        reshapedTensorBuffer.loadArray(original.floatArray)

        return reshapedTensorBuffer
    }
}