package net.codeinreal.projects.bestplace

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class BestPlace constructor(
    var title: String,
    val image: String,
    val description: String,
    val date: String,
    val location: String,
    val latitude: Double,
    val longitude: Double
):Parcelable {
    var id: String? = null

    init{
        this.id = UUID.randomUUID().toString()
    }
}