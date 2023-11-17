package edu.put.neighborhoodapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.put.neighborhoodapp.R
import edu.put.neighborhoodapp.adapter.SavedPlacesAdapter
import edu.put.neighborhoodapp.db.data.LocationEntity
import edu.put.neighborhoodapp.viewmodel.MainViewModel

class SavedFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerViewSavedPlaces = view.findViewById<RecyclerView>(R.id.savedPlacesRecyclerView)
        val adapter = SavedPlacesAdapter(
            onItemClickListener = { location ->
                Toast.makeText(view.context,"Nakliknąłeś na adres $location", Toast.LENGTH_SHORT).show()
            }
        )
        recyclerViewSavedPlaces.adapter = adapter
        recyclerViewSavedPlaces.layoutManager = LinearLayoutManager(view.context)

        val list = mutableListOf<LocationEntity>(
            LocationEntity(0,"Mrotecka 5a Nakło nad Notecią", 4.5),
            LocationEntity(1,"Osiedle Rusa 18/6 Poznań", 3.8),
            LocationEntity(1,"Osiedle Rusa 18/6 Poznań", 3.8),
            LocationEntity(1,"Osiedle Rusa 18/6 Poznań", 3.8),
            LocationEntity(1,"Osiedle Rusa 18/6 Poznań", 3.8),
            LocationEntity(1,"Osiedle Rusa 18/6 Poznań", 3.8)
        )
        adapter.submitList(list)
    }
}