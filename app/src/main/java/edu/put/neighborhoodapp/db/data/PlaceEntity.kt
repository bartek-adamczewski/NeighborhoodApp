package edu.put.neighborhoodapp.db.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = LocationEntity::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("parentLocationId"),
    onDelete = ForeignKey.CASCADE)]
)
data class PlaceEntity(

    @PrimaryKey(autoGenerate = false) val placeId: String,

    val address: String,

    val name: String,

    val rating: Double,

    val url: String?,

    val photoHeight: Int?,

    val photoWidth: Int?,

    val userRatingsTotal: Int,

    val parentLocationId: Long,

    var distance: Int,

    var time: Int

)