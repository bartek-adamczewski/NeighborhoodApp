package edu.put.neighborhoodapp.di

class PhotoLinkProvider()  {
    companion object {
        fun getPhotoLink(width: String, height: String, url: String): String {
            return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=${width}&maxheight=${height}&photo_reference=${url}&key=AIzaSyDxtJjgi6UfDQrRUQ89FsIFWBAQWAawUwQ"
        }
    }
}