package com.example.receiptapp_kotlin.ui.home


import android.hardware.camera2.CameraManager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.receiptapp_kotlin.R

class HomeFragment : Fragment()

{

    private lateinit var homeViewModel: HomeViewModel

    // 必須*
    // Fragmentが初めてUIを描画する時にシステムが呼び出す
    override fun onCreateView( inflater: LayoutInflater,
                               container: ViewGroup?,
                               savedInstanceState: Bundle? ): View?
    {
        /****************************************************
         * initial scripts for text and tab fragment shit
         ****************************************************/
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        //layout utk text box tu
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        //text box
        val textView: TextView = root.findViewById(R.id.text_home)
        //data binding text tu versi android
        homeViewModel.text.observe(viewLifecycleOwner, Observer { textView.text = it })

        return root

        /****************************************************
         * initial scripts for text and tab fragment shit
         ****************************************************/

    }

//    /** A safe way to get an instance of the Camera object. */
//    fun getCameraInstance(): Camera? {
//        return try {
//            openCamera();
//        } catch (e: Exception) {
//            // Camera is not available (in use or does not exist)
//            null // returns null if camera is unavailable
//        }
//    }





}