package edu.put.neighborhoodapp.db.data

import androidx.room.Embedded
import androidx.room.Relation

data class PlaceWithLocation(
    @Embedded
    val location: LocationEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "parentLocationId"
    )
    val places: List<PlaceEntity>
)