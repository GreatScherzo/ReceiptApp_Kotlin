package com.example.receiptapp_kotlin.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.receiptapp_kotlin.R

class AnalyticsOverviewFragment : Fragment()
{

    private lateinit var analyticsOverviewViewModel: AnalyticsOverviewViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View?
    {
        analyticsOverviewViewModel = ViewModelProviders.of(this).get(AnalyticsOverviewViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        analyticsOverviewViewModel.text.observe(viewLifecycleOwner, Observer
        {
            textView.text = it
        })
        return root
    }
}