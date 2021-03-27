package net.codeinreal.projects.bestplace

import java.util.*

class BestPlace constructor(
    var title: String,
    val image: String,
    val description: String,
    val date: String,
    val location: String,
    val latitude: Double,
    val longitude: Double
) {
    var id: String? = null

    init{
        this.id = UUID.randomUUID().toString()
    }
}