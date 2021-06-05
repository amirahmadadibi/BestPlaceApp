package net.codeinreal.projects.bestplace

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.happyplaces.utils.SwipeToDeleteCallback
import net.codeinreal.projects.bestplace.databinding.ActivityMainBinding
import net.codeinreal.projects.bestplace.list.BestPlaceAdapter
import net.codeinreal.projects.bestplace.listeners.OnRecyclerViewItemClicked
import net.codeinreal.projects.bestplace.listeners.SwipeToEditCallback
import org.koin.android.ext.android.bind

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    companion object {
        const val REQUEST_ADD_PLACE = 12129
        const val REQUEST_EDIT_PLACE = 12122
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this@MainActivity))
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupRecyclerView()

        binding.fabAddPlaceActivity.setOnClickListener {
            val intentToAddPlaceActivity = Intent(this@MainActivity, AddPlaceActivity::class.java)
            startActivityForResult(intentToAddPlaceActivity, REQUEST_ADD_PLACE)
        }


    }

    fun setupRecyclerView() {
        val db = DatabaseHandler(this)

        val bestPlaceAdapter = BestPlaceAdapter(this, db.getAllPlaces())
        binding.recyclerViewMain.adapter = bestPlaceAdapter
        binding.recyclerViewMain.layoutManager = LinearLayoutManager(this)

        bestPlaceAdapter.setRecyclerViewItemClicked(object : OnRecyclerViewItemClicked {
            override fun onBestPlaceItemClicked(position: Int, bestPlace: BestPlace) {
                val detailIntent = Intent(this@MainActivity, DetailsActivity::class.java)
                detailIntent.putExtra("bestPlace", bestPlace)
                startActivity(detailIntent)
            }

        })

        val editSwipeCallback = object : SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val id = bestPlaceAdapter.list.get(viewHolder.absoluteAdapterPosition).id
                val intentEdit = Intent(this@MainActivity, AddPlaceActivity::class.java)
                intentEdit.putExtra("id", id)
                startActivityForResult(intentEdit, REQUEST_EDIT_PLACE)
            }

        }

        val itemTouchHelper = ItemTouchHelper(editSwipeCallback)

        itemTouchHelper.attachToRecyclerView(binding.recyclerViewMain)


        val deleteSwipeCallback = object: SwipeToDeleteCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }

        }

        val itemDeleteTouchHelper = ItemTouchHelper(deleteSwipeCallback)
        itemDeleteTouchHelper.attachToRecyclerView(binding.recyclerViewMain)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_ADD_PLACE) {
                updateRecyclerveiwAdapter()
            }

            if (requestCode == REQUEST_EDIT_PLACE) {
                updateRecyclerveiwAdapter()
            }
        }
    }


    fun updateRecyclerveiwAdapter(){
        val db = DatabaseHandler(this)
        val adapter = binding.recyclerViewMain.adapter as BestPlaceAdapter

        adapter.updateAdapter(db.getAllPlaces())
    }
}