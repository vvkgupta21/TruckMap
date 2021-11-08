package com.example.trucksmap.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.trucksmap.R
import com.example.trucksmap.activity.api.TrucksDataService
import com.example.trucksmap.activity.model.Data
import com.example.trucksmap.activity.repository.Repository
import com.example.trucksmap.activity.viewmodel.MainViewModel
import com.example.trucksmap.databinding.ActivityMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory

import android.graphics.Bitmap
import android.graphics.Canvas

import androidx.core.content.ContextCompat

import android.graphics.drawable.Drawable

import com.google.android.gms.maps.model.BitmapDescriptor




class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repo = Repository(TrucksDataService)
        viewModel = ViewModelProvider(this,ViewModelFactory(repo)).get(MainViewModel::class.java)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
                    }
                    StateData.DataStatus.ERROR -> {
                        Toast.makeText(this, stateData.error?.message.toString(), Toast.LENGTH_LONG).show()
                    }
                    StateData.DataStatus.LOADING -> {
//
                    }
                }
            }
        })

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
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

                        for (i in 0 until data.size){
                            val sydney = LatLng(data[i].lastWaypoint.lat, data[i].lastWaypoint.lng)
                            if (data[i].lastRunningState.truckRunningState == 1){
                                mMap.addMarker(MarkerOptions().position(sydney)
                                    .icon(BitmapFromVector(applicationContext, R.drawable.car_green)))
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                            }else if (data[i].lastRunningState.truckRunningState == 0 && !data[i].lastWaypoint.ignitionOn){
                                mMap.addMarker(MarkerOptions().position(sydney)
                                    .icon(BitmapFromVector(applicationContext, R.drawable.blue_car)))
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                            }else if (data[i].lastRunningState.truckRunningState == 0 && data[i].lastWaypoint.ignitionOn){
                                mMap.addMarker(MarkerOptions().position(sydney)
                                    .icon(BitmapFromVector(applicationContext, R.drawable.yellow_car)))
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                            }
                        }
                    }
                    StateData.DataStatus.ERROR -> {
                        Toast.makeText(this, stateData.error?.message.toString(), Toast.LENGTH_LONG).show()
                    }
                    StateData.DataStatus.LOADING -> {
//
                    }
                }
            }
        })
    }

    private fun BitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        // below line is use to generate a drawable.
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

        // below line is use to set bounds to our vector drawable.
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )

        // below line is use to create a bitmap for our
        // drawable which we have added.
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // below line is use to add bitmap in our canvas.
        val canvas = Canvas(bitmap)

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas)

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}