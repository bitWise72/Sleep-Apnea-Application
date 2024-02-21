package com.example.sleepapnea.DrowsinessDetection

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.sleepapnea.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DorwsinessFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DorwsinessFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var textureView: TextureView
    private lateinit var cameraDevice:CameraDevice


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dorwsiness, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textureView= view.findViewById<TextureView>(R.id.drowsy_textureView)
        val surfaceTextureListener by lazy {
            object : TextureView.SurfaceTextureListener{
                @RequiresApi(Build.VERSION_CODES.P)
                override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
                    openCamera()
                }

                override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {
                    TODO("Not yet implemented")
                }

                override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
                    TODO("Not yet implemented")
                }

                override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {
                    TODO("Make call to the backend AI Model")
                }

            }
        }

        textureView.surfaceTextureListener=surfaceTextureListener
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }

            val cameraManager= activity?.getSystemService(Context.CAMERA_SERVICE) as CameraManager

            val handlerThread = HandlerThread("Video Stream")
            handlerThread.start()
            val handler = Handler.createAsync(handlerThread.looper)

            cameraManager.openCamera(
                cameraManager.cameraIdList[1],
                object : CameraDevice.StateCallback() {
                    override fun onOpened(cameraDevice: CameraDevice) {
                        this@DorwsinessFragment.cameraDevice = cameraDevice
                        val surfaceTexture: SurfaceTexture = textureView.surfaceTexture!!
                        val surface = Surface(surfaceTexture)
                        try {
                            val captureRequest: CaptureRequest.Builder =
                                cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                            captureRequest.addTarget(surface)
                            cameraDevice.createCaptureSession(
                                ArrayList<Surface>(listOf<Surface>(surface)),
                                object : CameraCaptureSession.StateCallback() {
                                    override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                                        try {
                                            cameraCaptureSession.setRepeatingRequest(
                                                captureRequest.build(),
                                                null,
                                                null
                                            )
                                        } catch (e: CameraAccessException) {
                                            e.printStackTrace()
                                        }
                                    }

                                    override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {}
                                }, handler
                            )
                        } catch (e: CameraAccessException) {
                            e.printStackTrace()
                        }
                    }

                    override fun onDisconnected(cameraDevice: CameraDevice) {}
                    override fun onError(cameraDevice: CameraDevice, i: Int) {}
                },
                handler
            )
        }catch (e:Exception){
            e.printStackTrace()
        }


    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DorwsinessFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DorwsinessFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}