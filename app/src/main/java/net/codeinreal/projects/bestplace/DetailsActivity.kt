package net.codeinreal.projects.bestplace

import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.Layer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import net.codeinreal.projects.bestplace.databinding.ActivityDetailsBinding
import java.io.File

class DetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailsBinding
    private var hoveringMarker: ImageView? = null
    private var droppedMarkerLayer: Layer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(
            this,
            "sk.eyJ1IjoiYW1pcmFobWFkYWRpYmkiLCJhIjoiY2twcXF1NDQzMDBuNTJ1bzBmendwcmZocyJ9.HopzCWV3KqzRi3alhhM7yA"
        )

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bestPlaceDetail = intent?.extras?.get("bestPlace") as BestPlace
        binding.mapView.onCreate(savedInstanceState)

        binding.mapView.getMapAsync { mapBoxMap ->
            mapBoxMap.setStyle(Style.MAPBOX_STREETS) { style ->
                goToCurrentLocation(mapBoxMap,bestPlaceDetail.latitude,bestPlaceDetail.longitude)
                showRedPickerImage()
            }
        }
        Toast.makeText(this, bestPlaceDetail.title, Toast.LENGTH_SHORT).show()


        binding.txtTitle.text = bestPlaceDetail.title

        val imageBitmap = MediaStore.Images.Media.getBitmap(
            this.contentResolver,
            Uri.fromFile(File(bestPlaceDetail.image))
        )

        binding.imgLocationThumbnail.setImageBitmap(imageBitmap)

        binding.txtDescription.text = "توضیحات : " + bestPlaceDetail.description

        binding.txtDate.text = "تاریخ : " + bestPlaceDetail.date

        binding.txtLocation.text =
            " مکان : ${bestPlaceDetail.latitude} |  ${bestPlaceDetail.longitude}"
    }


    private fun goToCurrentLocation(mapBoxMap: MapboxMap,lat:Double,lon:Double) {

        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(lat, lon))
            .zoom(17.0)
            .tilt(30.0)
            .build()

        mapBoxMap.animateCamera(
            CameraUpdateFactory.newCameraPosition(cameraPosition), 7000
        )
    }


    private fun showRedPickerImage() {
        hoveringMarker = ImageView(this@DetailsActivity)
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