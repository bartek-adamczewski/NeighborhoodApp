package edu.put.neighborhoodapp.di

import edu.put.neighborhoodapp.BuildConfig

class PhotoLinkProvider()  {
    companion object {
        fun getPhotoLink(width: String, height: String, url: String): String {
            return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=${width}&maxheight=${height}&photo_reference=${url}&key=${BuildConfig.PLACES_API_KEY}"
        }
    }
}