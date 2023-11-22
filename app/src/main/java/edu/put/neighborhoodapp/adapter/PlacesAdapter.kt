package edu.put.neighborhoodapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import edu.put.neighborhoodapp.R
import edu.put.neighborhoodapp.db.data.LocationEntity
import edu.put.neighborhoodapp.db.data.PlaceEntity
import edu.put.neighborhoodapp.di.PhotoLinkProvider
import edu.put.neighborhoodapp.fragments.FragmentType

class PlacesAdapter(private val onItemClickListener: (location : String) -> Unit, private val type: FragmentType?) : androidx.recyclerview.widget.ListAdapter<PlaceEntity, PlacesAdapter.PlaceViewHolder>(GameDiffCallback()) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.place_recycler_view, parent, false)
        return PlaceViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = getItem(position)
        context = holder.itemView.context
        holder.bind(place)
    }

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val mark = itemView.findViewById<TextView>(R.id.markTextView)
        private val name = itemView.findViewById<TextView>(R.id.nameTextView)
        private val photo = itemView.findViewById<ImageView>(R.id.photoImageView)
        private val distance = itemView.findViewById<TextView>(R.id.distanceTextView)
        private lateinit var argument: String

        init {
            itemView.setOnClickListener {
                onItemClickListener(argument)
            }
        }


        @SuppressLint("SetTextI18n")
        fun bind(place: PlaceEntity) {
            argument = place.name
            mark.text = "Ocena: ${place.rating}"
            name.text = place.name
            if(place.url != null) {
                val link = PhotoLinkProvider.getPhotoLink(place.photoWidth.toString(),place.photoHeight.toString(),place.url)
                Glide.with(context)
                    .load(link)
                    .override(330, 330)
                    .centerCrop()
                    .into(photo)
            } else {
                val image = when (type) {
                    FragmentType.STORES -> R.drawable.store
                    FragmentType.GYMS -> R.drawable.gym
                    FragmentType.BUSES -> R.drawable.bus
                    FragmentType.TRAMS -> R.drawable.tram
                    FragmentType.PARKS -> R.drawable.park
                    FragmentType.RESTAURANTS -> R.drawable.restaurant
                    else -> { R.drawable.people }
                }
                Glide.with(context)
                    .load(image)
                    .override(400, 400)
                    .centerCrop()
                    .into(photo)
            }
            //distance.text = place.distance.toString()
        }
    }

    class GameDiffCallback : DiffUtil.ItemCallback<PlaceEntity>() {
        override fun areItemsTheSame(oldItem: PlaceEntity, newItem: PlaceEntity): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: PlaceEntity, newItem: PlaceEntity): Boolean {
            return oldItem == newItem
        }
    }

}