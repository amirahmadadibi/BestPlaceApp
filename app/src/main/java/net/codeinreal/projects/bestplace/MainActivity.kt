package net.codeinreal.projects.bestplace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import net.codeinreal.projects.bestplace.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this@MainActivity))
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val db = DatabaseHandler(this)
        val array  = db.getAllPlaces()
        for (bestPlace in array) {
            Log.d("tagx", "onCreate: ${bestPlace.id}")
        }

        binding.fabAddPlaceActivity.setOnClickListener {
            val intentToAddPlaceActivity = Intent(this@MainActivity,AddPlaceActivity::class.java)
            startActivity(intentToAddPlaceActivity)
        }
    }
}