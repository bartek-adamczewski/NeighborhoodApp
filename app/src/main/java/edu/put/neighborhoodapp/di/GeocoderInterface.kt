package edu.put.neighborhoodapp.di

import android.content.Context
import android.location.Geocoder
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.util.*

interface GeocoderInterface {
    fun getLatLngFromAddress(address: String): Pair<Double, Double>?
}

class GeocoderInterfaceImpl(
    private val context: Context
) : GeocoderInterface {

    override fun getLatLngFromAddress( address: String): Pair<Double, Double>? {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val addressList = geocoder.getFromLocationName(address, 1)
            if (addressList?.isNotEmpty() == true) {
                val location = addressList[0]
                Pair(location.latitude, location.longitude)
            } else {
                null
            }
        } catch (e: IOException) {
            null
        }
    }
}