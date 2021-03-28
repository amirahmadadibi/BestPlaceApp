package net.codeinreal.projects.bestplace.listeners

import net.codeinreal.projects.bestplace.BestPlace

interface OnRecyclerViewItemClicked {
    fun onBestPlaceItemClicked(position:Int,bestPlace:BestPlace)
}