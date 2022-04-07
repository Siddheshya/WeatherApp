package com.example.weatherapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast

import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import java.util.jar.Manifest
import android.location.LocationRequest as LocationRequest1

class Splash_Screen : AppCompatActivity() {
    lateinit var mfused: FusedLocationProviderClient
    private var code = 1010
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mfused = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
    }


    private fun getLastLocation() {
        if (checkPermission()) {
            if (LocationEnable()) {
                mfused.lastLocation.addOnCompleteListener { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        Newlocation()
                    } else {
                        Handler(Looper.getMainLooper()).postDelayed({
                            var intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("latt",location.latitude.toString())
                            intent.putExtra("long",location.longitude.toString())
                            startActivity(intent)
                            finish()
                        }, 2000)
                    }
                }
            } else {
                Toast.makeText(this, "Turn on Your Location", Toast.LENGTH_LONG).show()
            }

        } else {
            RequestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun Newlocation() {
        var locationReq= LocationRequest()
        locationReq.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        locationReq.interval=0
        locationReq.fastestInterval=0
        locationReq.numUpdates=1
        mfused = LocationServices.getFusedLocationProviderClient(this)
        mfused.requestLocationUpdates(locationReq,locattionCallback, Looper.myLooper()!!)
    }
    private val locattionCallback= object :LocationCallback (){
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation:Location=p0.lastLocation
        }
    }

    private fun LocationEnable(): Boolean {
        var locationManager =getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun RequestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), code
        )

    }

    private fun checkPermission(): Boolean {
        if(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED
        ){
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == code){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLastLocation()
            }

        }

    }
}