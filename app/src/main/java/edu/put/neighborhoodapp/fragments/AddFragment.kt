package edu.put.neighborhoodapp.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import edu.put.neighborhoodapp.R
import edu.put.neighborhoodapp.databinding.DialogCheckboxesBinding
import edu.put.neighborhoodapp.databinding.FragmentAddBinding
import edu.put.neighborhoodapp.viewmodel.MainViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialogBinding = DialogCheckboxesBinding.inflate(LayoutInflater.from(context), null, false)
        val dialog = AlertDialog.Builder(context, R.style.RoundedDialog)
            .setView(dialogBinding.root)  // Use the root view of the binding
            .create()

        Glide.with(this)
            .load(R.drawable.add_button_image)
            .into(binding.imageViewAdd)

        val places = mutableListOf<FragmentType>()

        val checkboxActions = mapOf(
                dialogBinding.storesCheckBox to { places.add(FragmentType.STORES) },
                dialogBinding.gymsCheckBox to { places.add(FragmentType.GYMS) },
                dialogBinding.busesCheckBox to { places.add(FragmentType.BUSES) },
                dialogBinding.tramsCheckBox to { places.add(FragmentType.TRAMS) },
                dialogBinding.parksCheckBox to { places.add(FragmentType.PARKS)  },
                dialogBinding.restaurantsCheckBox to { places.add(FragmentType.RESTAURANTS) }
        )

        dialogBinding.confirmButton.setOnClickListener {
            checkboxActions.forEach { (checkbox, action) ->
                if (checkbox.isChecked) {
                    action()
                }
            }
            dialog.dismiss()
            buttonClickedListener?.onButtonClicked(places)
        }

        binding.imageViewAdd.setOnClickListener {
            var address = binding.editTextAddress.text
            if(address.toString() == "") {
                Toast.makeText(context, "Please, insert text", Toast.LENGTH_SHORT).show()
            } else {
                dialog.show()
                viewModel.updateAddress(address.toString())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is MainViewModel.Event.ShowToast -> Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    interface OnButtonClickedListener {
        fun onButtonClicked(fragmentTypes: MutableList<FragmentType>)
    }

    private var buttonClickedListener: OnButtonClickedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnButtonClickedListener) {
            buttonClickedListener = context
        } else {
            throw RuntimeException("$context must implement OnButtonClickedListener")
        }
    }
}