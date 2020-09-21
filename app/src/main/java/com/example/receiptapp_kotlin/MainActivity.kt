package com.example.receiptapp_kotlin

import android.os.Bundle
import android.view.TextureView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

class MainActivity : AppCompatActivity()
{

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
        val textureView_Camera = findViewById<TextureView>(R.id.textureView_Camera)
//        textureView_Camera.surfaceTextureListener = surfaceTextureListener
//        startBackgroundThread()
    }

//
//    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener
//    {
//
//        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int)
//        {
//            val imageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, /*maxImages*/ 2)
//
//            openCamera()
//        }
//
//        override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture?, p1: Int, p2: Int)
//        {
//
//        }
//
//        override fun onSurfaceTextureUpdated(p0: SurfaceTexture?)
//        {
//
//        }
//
//        override fun onSurfaceTextureDestroyed(p0: SurfaceTexture?): Boolean
//        {
//            return false
//        }
//    }
//
//    fun openCamera()
//    {
//        var manager: CameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
//
//        try
//        {
//            var camerId: String = manager.getCameraIdList()[0]
//
//            val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//
//            if (permission != PackageManager.PERMISSION_GRANTED)
//            {
//                requestCameraPermission()
//                return
//            }
//            manager.openCamera(camerId, stateCallback, null)
//        }
//        catch (e: Exception)
//        {
//            e.printStackTrace()
//        }
//    }
//
//    //TODO: mungkin ni penyebab crash
//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun requestCameraPermission()
//    {
//        requestPermissions(arrayOf(Manifest.permission.CAMERA), 200)
//    }
//
//    private val stateCallback = object : CameraDevice.StateCallback()
//    {
//
//        override fun onOpened(cameraDevice: CameraDevice) {
//            MainActivity@this = cameraDevice
//            createCameraPreviewSession()
//        }
//
//        override fun onDisconnected(cameraDevice: CameraDevice) {
//            cameraDevice.close()
//            this@labe.cameraDevice = null
//        }
//
//        override fun onError(cameraDevice: CameraDevice, error: Int) {
//            onDisconnected(cameraDevice)
//            finish()
//        }
//
//    }



}