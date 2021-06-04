package net.codeinreal.projects.bestplace.list

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import net.codeinreal.projects.bestplace.BestPlace
import net.codeinreal.projects.bestplace.R
import net.codeinreal.projects.bestplace.listeners.OnRecyclerViewItemClicked
import java.io.File

class BestPlaceAdapter(var context: Context, var list: ArrayList<BestPlace>) :
    RecyclerView.Adapter<BestPlaceAdapter.BestPlaceViewHolder>() {
    var onRecyclerViewItemClicked: OnRecyclerViewItemClicked? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestPlaceViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_place, parent, false)
        return BestPlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: BestPlaceViewHolder, position: Int) {
        val bestPlace = list[position]
        holder.textViewTitle.text = bestPlace.title

        val imageBitmap = MediaStore.Images.Media.getBitmap(
            context.contentResolver,
            Uri.fromFile(File(bestPlace.image))
        )

        holder.imageViewPlace.setImageBitmap(imageBitmap)
        holder.itemView.setOnClickListener {
            onRecyclerViewItemClicked?.onBestPlaceItemClicked(position,bestPlace)//happened
        }
    }

    override fun getItemCount() = list.size


    class BestPlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView
        var imageViewPlace: ImageView

        init {
            textViewTitle = itemView.findViewById(R.id.textViewTitle)
            imageViewPlace = itemView.findViewById(R.id.imageViewPlace)
        }
    }

    fun setRecyclerViewItemClicked(onRecyclerViewItemClicked: OnRecyclerViewItemClicked){
        this.onRecyclerViewItemClicked = onRecyclerViewItemClicked
    }


    fun updateAdapter(list:ArrayList<BestPlace>){
        this.list = list
        notifyDataSetChanged()
    }
}