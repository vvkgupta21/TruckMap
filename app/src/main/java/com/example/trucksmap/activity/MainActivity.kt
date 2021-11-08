package com.example.trucksmap.activity

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.trucksmap.R
import com.example.trucksmap.activity.adapter.AdapterTruckslist
import com.example.trucksmap.activity.api.TrucksDataService
import com.example.trucksmap.activity.model.Data
import com.example.trucksmap.activity.repository.Repository
import com.example.trucksmap.activity.viewmodel.MainViewModel
import com.example.trucksmap.databinding.ActivityMainBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    lateinit var adapter: AdapterTruckslist

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val repo = Repository(TrucksDataService)
        viewModel = ViewModelProvider(this,ViewModelFactory(repo)).get(MainViewModel::class.java)
        initAdapter()
        fetchData()

        binding.mapImg.setOnClickListener {
            startActivity(Intent(this@MainActivity,MapsActivity::class.java))
        }
    }

    private fun initAdapter(){
        adapter = AdapterTruckslist(arrayListOf())
        binding.trucksRecycler.adapter = adapter
    }

    private fun fetchData(){
        viewModel.getTrucksData("PCH",
            33,
            false ,
            "g2qb5jvucg7j8skpu5q7ria0mu",
            true,
            "lastRunningState,lastWaypoint"
        ).observe(this, Observer {
                it?.let { stateData ->
                when(stateData.status){
                    StateData.DataStatus.SUCCESS -> {
                        val data = stateData.data?.data as ArrayList<Data>
                        adapter.initList(data)
                    }
                    StateData.DataStatus.ERROR -> {
                        Toast.makeText(this, stateData.error?.message.toString(), Toast.LENGTH_LONG).show()
                    }
                    StateData.DataStatus.LOADING -> {

                    }
                }
                }
        })

    }

}