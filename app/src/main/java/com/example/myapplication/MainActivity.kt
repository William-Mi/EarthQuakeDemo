package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EarthquakeAdapter
    private lateinit var viewModel: EarthquakeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showLoading()
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val spaceHeight = resources.getDimensionPixelSize(R.dimen.item_space)
        val itemDecoration = EarthquakeItemDecoration(spaceHeight, this@MainActivity)
        recyclerView.addItemDecoration(itemDecoration)

        viewModel = ViewModelProvider(this).get(EarthquakeViewModel::class.java)

        viewModel.earthquakeData.observe(this, { earthquakes ->
            adapter = EarthquakeAdapter(earthquakes)
            recyclerView.adapter = adapter
            hideLoading()
        })
    }


    private fun showLoading() {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.GONE
    }

}