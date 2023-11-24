package edu.put.neighborhoodapp.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DistanceEntity(

    @PrimaryKey(autoGenerate = false) val placeName: String,

    val placeMain: String,

    val distance: Int,

    val time: Int

)
