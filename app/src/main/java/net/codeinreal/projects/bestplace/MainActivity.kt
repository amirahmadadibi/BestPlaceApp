package net.codeinreal.projects.bestplace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import net.codeinreal.projects.bestplace.databinding.ActivityMainBinding
import net.codeinreal.projects.bestplace.list.BestPlaceAdapter
import org.koin.android.ext.android.bind

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this@MainActivity))
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val db = DatabaseHandler(this)


        val bestPlaceAdapter = BestPlaceAdapter(this,db.getAllPlaces())
        binding.recyclerViewMain.adapter = bestPlaceAdapter
        binding.recyclerViewMain.layoutManager  = LinearLayoutManager(this)


        binding.fabAddPlaceActivity.setOnClickListener {
            val intentToAddPlaceActivity = Intent(this@MainActivity,AddPlaceActivity::class.java)
            startActivity(intentToAddPlaceActivity)
        }


    }
}