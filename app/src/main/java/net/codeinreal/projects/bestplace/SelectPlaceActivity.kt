package net.codeinreal.projects.bestplace

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.Style
import net.codeinreal.projects.bestplace.databinding.ActivitySelectPlaceBinding


class SelectPlaceActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelectPlaceBinding
    private var hoveringMarker: ImageView? = null

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
        }
    }
}