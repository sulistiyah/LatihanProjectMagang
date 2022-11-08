package com.magang.projectmaganglatihan.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.magang.projectmaganglatihan.R
import com.magang.projectmaganglatihan.databinding.ActivityHomeMapsBinding
import java.security.Permission

class HomeMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityHomeMapsBinding


    private lateinit var mMap: GoogleMap
    private lateinit var currentlocation:Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val  permissioncode= 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchlocation()

    }

    private fun fetchlocation() {
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
        !=PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),permissioncode)
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if(location != null){
                currentlocation = location
                Toast.makeText(this,
                    currentlocation.latitude.toString()+""+
                        currentlocation.longitude.toString(),Toast.LENGTH_SHORT).show()

                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.map)as
                        SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val latLng = LatLng(currentlocation.latitude, currentlocation.longitude)
        val makerOptions = MarkerOptions().position(latLng).title("Hello i am here")
        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,5f))
        googleMap?.addMarker(makerOptions)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            permissioncode -> if(grantResults.isEmpty()&& grantResults[0] ==
                PackageManager.PERMISSION_GRANTED){
                fetchlocation()
            }
        }
    }


}