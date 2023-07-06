package edu.put.neighborhoodapp.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationEntity(

    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val location: String,

    val rating: Double? = null

)