package com.example.receiptapp_kotlin

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class MainActivity : AppCompatActivity()
{
    private lateinit var textureView: TextureView
    private var captureSession: CameraCaptureSession? = null
    private var cameraDevice: CameraDevice? = null
    private val previewSize: Size = Size(300, 300) // FIXME: for now.
    private lateinit var previewRequestBuilder: CaptureRequest.Builder
    private var imageReader: ImageReader? = null
    private lateinit var previewRequest: CaptureRequest
    private var backgroundThread: HandlerThread? = null
    private var backgroundHandler: Handler? = null


    //TODO: make prototype without bottom navigation
    override fun onCreate(savedInstanceState: Bundle?)
    {
        /****************************************************
         * initial intent for onCreate activity
         ****************************************************/
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //mmg kena set content kat main, not supposed to be kat fragments (kalau buat camtu dia crash)
        //setContentView(R.layout.fragment_home)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        /****************************************************
         * menatang utk bottom navigator
         ****************************************************/
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_AnalyticsOverview, R.id.navigation_History))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        /****************************************************
         * menatang camera
         ****************************************************/

        textureView = findViewById<TextureView>(R.id.textureView_Camera)
        textureView.surfaceTextureListener = surfaceTextureListener
        startBackgroundThread()
    }


    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener
    {

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int)
        {
            val imageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, /*maxImages*/ 2)

            openCamera()
        }

        override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture?, p1: Int, p2: Int)
        {

        }

        override fun onSurfaceTextureUpdated(p0: SurfaceTexture?)
        {

        }

        override fun onSurfaceTextureDestroyed(p0: SurfaceTexture?): Boolean
        {
            return false
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun openCamera()
    {
        var manager: CameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        try
        {
            var camerId: String = manager.getCameraIdList()[0]

            val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

            if (permission != PackageManager.PERMISSION_GRANTED)
            {
                requestCameraPermission()
                return
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                manager.openCamera(camerId, stateCallback, null)
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }


//    private fun requestCameraPermission()
//    {
//        requestPermissions(arrayOf(Manifest.permission.CAMERA), 200)
//    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun requestCameraPermission()
    {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
        {
            AlertDialog.Builder(baseContext)
                .setMessage("Permission Here")
                .setPositiveButton(android.R.string.ok)
                { _, _ ->
                    requestPermissions(arrayOf(Manifest.permission.CAMERA),200)
                }
                .setNegativeButton(android.R.string.cancel)
                { _, _ ->
                    finish()
                }
                .create()
        }
        else
        {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 200)
        }
    }

    private val stateCallback = object : CameraDevice.StateCallback() {

        override fun onOpened(cameraDevice: CameraDevice) {
            this@MainActivity.cameraDevice = cameraDevice
            createCameraPreviewSession()
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {
            cameraDevice.close()
            this@MainActivity.cameraDevice = null
        }

        override fun onError(cameraDevice: CameraDevice, error: Int) {
            onDisconnected(cameraDevice)
            finish()
        }

    }

    private fun createCameraPreviewSession()
    {
        try {
            val texture = textureView.surfaceTexture
            texture.setDefaultBufferSize(previewSize.width, previewSize.height)
            val surface = Surface(texture)
            previewRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            previewRequestBuilder.addTarget(surface)
            cameraDevice?.createCaptureSession(Arrays.asList(surface, imageReader?.surface), object : CameraCaptureSession.StateCallback()
            {

                    override fun onConfigured(cameraCaptureSession: CameraCaptureSession)
                    {

                        if (cameraDevice == null) return
                        captureSession = cameraCaptureSession
                        try {
                            previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                            previewRequest = previewRequestBuilder.build()
                            captureSession?.setRepeatingRequest(previewRequest,
                                null, Handler(backgroundThread?.looper))
                        } catch (e: CameraAccessException) {
                            Log.e("erfs", e.toString())
                        }

                    }

                    override fun onConfigureFailed(session: CameraCaptureSession)
                    {
                        //Tools.makeToast(baseContext, "Failed")
                    }
            }, null)
        } catch (e: CameraAccessException) {
            Log.e("erf", e.toString())
        }

    }

    private fun startBackgroundThread()
    {
        backgroundThread = HandlerThread("CameraBackground").also { it.start() }
        backgroundHandler = Handler(backgroundThread?.looper)
    }



}