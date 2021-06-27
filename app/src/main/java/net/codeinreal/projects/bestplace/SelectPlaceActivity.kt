package net.codeinreal.projects.bestplace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.Style
import net.codeinreal.projects.bestplace.databinding.ActivitySelectPlaceBinding

class SelectPlaceActivity : AppCompatActivity() {
    lateinit var binding: ActivitySelectPlaceBinding
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

            }
        }
    }
}