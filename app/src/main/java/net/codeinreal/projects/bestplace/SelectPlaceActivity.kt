package net.codeinreal.projects.bestplace

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.Layer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import net.codeinreal.projects.bestplace.databinding.ActivitySelectPlaceBinding
import net.codeinreal.projects.bestplace.R


class SelectPlaceActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelectPlaceBinding
    private var hoveringMarker: ImageView? = null
    private var droppedMarkerLayer: Layer? = null

    companion object {
        const val DROPPED_MARKER_LAYER_ID = "DROPPED_MARKER_LAYER_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(
            this,
            "sk.eyJ1IjoiYW1pcmFobWFkYWRpYmkiLCJhIjoiY2twcXF1NDQzMDBuNTJ1bzBmendwcmZocyJ9.HopzCWV3KqzRi3alhhM7yA"
        )
        binding = ActivitySelectPlaceBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync { mapBoxMap ->
            mapBoxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                showRedPickerImage()
                creteGreenSelectedMark(style)

                binding.fabCurrentLocation.setOnClickListener {

                    if(!checkLocationPermission()){
                        Toast.makeText(this@SelectPlaceActivity,"permission error", Toast.LENGTH_SHORT).show()
                    }
                }

                binding.buttonSelectCurrentPlace.setOnClickListener {
                    if (hoveringMarker!!.visibility == View.VISIBLE) {
                        val mapTargetLatLan = mapBoxMap!!.cameraPosition.target
                        hoveringMarker!!.visibility = View.INVISIBLE

                        binding.buttonSelectCurrentPlace.setBackgroundColor(
                            ContextCompat.getColor(this, R.color.mapbox_blue)!!
                        )

                        binding.buttonSelectCurrentPlace.setText("ویرایش موقعیت انتخاب شده")

                        val source = style.getSourceAs<GeoJsonSource>("dropped-icon-image")

                        val targetPoint =
                            Point.fromLngLat(mapTargetLatLan.longitude, mapTargetLatLan.latitude)

                        source!!.setGeoJson(targetPoint)

                        droppedMarkerLayer = style.getLayer(DROPPED_MARKER_LAYER_ID)
                        droppedMarkerLayer!!.setProperties(PropertyFactory.visibility(Property.VISIBLE))
                    } else {
                        binding.buttonSelectCurrentPlace.setBackgroundColor(
                            ContextCompat.getColor(this, R.color.primaryColor)!!
                        )

                        binding.buttonSelectCurrentPlace.setText("انتخاب موقعیت مکان")

                        hoveringMarker!!.visibility = View.VISIBLE

                        droppedMarkerLayer = style.getLayer(DROPPED_MARKER_LAYER_ID)
                        droppedMarkerLayer!!.setProperties(PropertyFactory.visibility(Property.NONE))
                    }

                }
            }
        }
    }

    private fun showRedPickerImage() {
        hoveringMarker = ImageView(this@SelectPlaceActivity)
        hoveringMarker!!.setBackgroundResource(R.drawable.ic_marker_red)

        val imageLocation = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER
        )

        hoveringMarker!!.layoutParams = imageLocation

        binding.mapView.addView(hoveringMarker)
    }


    private fun creteGreenSelectedMark(loadedStyle: Style) {
        loadedStyle.addImage(
            "dropped-icon-image",
            ContextCompat.getDrawable(this, R.drawable.ic_pin)!!
        )

        loadedStyle.addSource(GeoJsonSource("dropped-icon-image"))

        val symbol = SymbolLayer(
            DROPPED_MARKER_LAYER_ID, "dropped-icon-image"
        )

        symbol.withProperties(
            PropertyFactory.iconImage("dropped-icon-image"),
            PropertyFactory.visibility(Property.NONE),
            PropertyFactory.iconAllowOverlap(true),
            PropertyFactory.iconIgnorePlacement(true)
        )

        loadedStyle.addLayer(symbol)
    }


    private fun checkLocationPermission(): Boolean {
        var isPermissionGranted = false
        Dexter.withContext(this@SelectPlaceActivity)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    isPermissionGranted = true
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    isPermissionGranted = false
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    showRationalPermiison()
                }

            }).check()

        return isPermissionGranted
    }


    private fun showRationalPermiison() {
        AlertDialog.Builder(this@SelectPlaceActivity)
            .setTitle("اجازه دسترسی به موقعیت فعلی")
            .setPositiveButton("برو به تنظیمات") { _, _ ->
                val settinsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("packge", packageName, null)
                settinsIntent.data = uri
                startActivity(settinsIntent)
            }
            .setNegativeButton("اجازه نمیدم") { dialog, _ ->
                finish()
            }
            .show()
    }


}