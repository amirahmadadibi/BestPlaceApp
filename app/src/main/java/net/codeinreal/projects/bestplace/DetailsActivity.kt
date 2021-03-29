package net.codeinreal.projects.bestplace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val data = intent?.extras?.get("bestPlace") as BestPlace
        Toast.makeText(this,data.title,Toast.LENGTH_SHORT).show()
    }
}