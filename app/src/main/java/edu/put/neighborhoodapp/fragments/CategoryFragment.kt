package edu.put.neighborhoodapp.fragments

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.put.neighborhoodapp.R
import edu.put.neighborhoodapp.adapter.PlacesAdapter
import edu.put.neighborhoodapp.adapter.SavedPlacesAdapter
import edu.put.neighborhoodapp.databinding.FragmentAddBinding
import edu.put.neighborhoodapp.databinding.FragmentCategoryBinding
import edu.put.neighborhoodapp.viewmodel.MainViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CategoryFragment : Fragment() {

    private var fragmentType: FragmentType? = null

    companion object {
        fun newInstance(type: FragmentType) = CategoryFragment().apply {
            arguments = Bundle().apply {
                putSerializable("type", type)
            }
        }
    }

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fragmentType = it.getSerializable("type") as FragmentType?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.uiState.onEach { state ->
            binding.typeTextView.text = when (fragmentType) {
                FragmentType.STORES -> "Sklepy"
                FragmentType.GYMS -> "Siłownie"
                FragmentType.BUSES -> "Przystanki autobusowe"
                FragmentType.TRAMS -> "Przystanki tramwajowe"
                FragmentType.PARKS -> "Parki"
                FragmentType.RESTAURANTS -> "Restauracje"
                null -> "Nie Wybrano żadnych obiektów do wyświetlenia"
            }
        }.launchIn(lifecycleScope)

        observeAddress()

        val adapter = PlacesAdapter(
            onItemClickListener = { argument ->
                Toast.makeText(view.context,"Nakliknąłeś na adres $argument", Toast.LENGTH_SHORT).show()
            },
            fragmentType
        )

        binding.placesRecyclerView.adapter = adapter
        binding.placesRecyclerView.addItemDecoration(VerticalSpaceItemDecoration(16))
        binding.placesRecyclerView.layoutManager = LinearLayoutManager(view.context)

        observeData(adapter)
    }

    private fun observeAddress() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                state.address?.let { address ->
                    when (fragmentType) {
                        FragmentType.STORES -> if(state.storesData == null) viewModel.getData("Grocery store")
                        FragmentType.GYMS -> if(state.gymsData == null) viewModel.getData("Gym")
                        FragmentType.BUSES -> if(state.busesData == null) viewModel.getData("Bus stop")
                        FragmentType.TRAMS -> if(state.tramsData == null) viewModel.getData("Tram stop")
                        FragmentType.PARKS -> if(state.parksData == null) viewModel.getData("Park")
                        FragmentType.RESTAURANTS -> if(state.restaurantsData == null) viewModel.getData("Restaurant")
                        null -> "Nie Wybrano żadnych obiektów do wyświetlenia"
                    }
                }
            }
        }
    }

    private fun observeData(adapter: PlacesAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (fragmentType) {
                    FragmentType.STORES ->
                        state.storesData?.let { data ->
                            adapter.submitList(data)
                        }
                    FragmentType.GYMS ->
                        state.gymsData?.let { data ->
                            adapter.submitList(data)
                        }
                    FragmentType.BUSES ->
                        state.busesData?.let { data ->
                            adapter.submitList(data)
                        }
                    FragmentType.TRAMS ->
                        state.tramsData?.let { data ->
                            adapter.submitList(data)
                        }
                    FragmentType.PARKS ->
                        state.parksData?.let { data ->
                            adapter.submitList(data)
                        }
                    FragmentType.RESTAURANTS ->
                        state.restaurantsData?.let { data ->
                            adapter.submitList(data)
                        }
                    null -> Toast.makeText(context, "Error with loading data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = verticalSpaceHeight
    }
}


