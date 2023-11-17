package edu.put.neighborhoodapp.adapter

import android.health.connect.datatypes.ExerciseRoute.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.put.neighborhoodapp.R
import edu.put.neighborhoodapp.db.data.LocationEntity
import edu.put.neighborhoodapp.db.data.PlaceWithLocation

class SavedPlacesAdapter(private val onItemClickListener: (location : String) -> Unit) : androidx.recyclerview.widget.ListAdapter<LocationEntity, SavedPlacesAdapter.GameViewHolder>(GameDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.saved_places_recycler_view, parent, false)
        return GameViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place)
    }

    inner class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val mark = itemView.findViewById<TextView>(R.id.markTextView)
        private val location = itemView.findViewById<TextView>(R.id.locationTextView)
        private lateinit var model : String

        init {
            itemView.setOnClickListener {
                onItemClickListener(model)
            }
        }

        fun bind(place: LocationEntity) {
            model = place.location.toString()
            mark.text = "${place.rating}"
            location.text = "${place.location}"
        }
    }

    class GameDiffCallback : DiffUtil.ItemCallback<LocationEntity>() {
        override fun areItemsTheSame(oldItem: LocationEntity, newItem: LocationEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LocationEntity, newItem: LocationEntity): Boolean {
            return oldItem == newItem
        }
    }

}