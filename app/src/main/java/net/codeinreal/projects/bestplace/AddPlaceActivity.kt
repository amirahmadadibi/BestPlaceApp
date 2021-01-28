package net.codeinreal.projects.bestplace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import net.codeinreal.projects.bestplace.databinding.ActivityAddPlaceBinding

class AddPlaceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddPlaceBinding.inflate(LayoutInflater.from(this@AddPlaceActivity))
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}