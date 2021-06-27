package net.codeinreal.projects.bestplace

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
    private var droppedMarkerLayer:Layer? = null
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
                binding.buttonSelectCurrentPlace.setOnClickListener {
                    val mapTargetLatLan = mapBoxMap!!.cameraPosition.target
                    hoveringMarker!!.visibility = View.INVISIBLE

                    binding.buttonSelectCurrentPlace.setBackgroundColor(
                        ContextCompat.getColor(this,R.color.mapbox_blue)!!)

                    binding.buttonSelectCurrentPlace.setText("موقعیت انتخاب شد !")

                    val source = style.getSourceAs<GeoJsonSource>("dropped-icon-image")

                    val targetPoint = Point.fromLngLat(mapTargetLatLan.longitude,mapTargetLatLan.latitude)

                    source!!.setGeoJson(targetPoint)

                    droppedMarkerLayer = style.getLayer(DROPPED_MARKER_LAYER_ID)
                    droppedMarkerLayer!!.setProperties(PropertyFactory.visibility(Property.VISIBLE))
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
}