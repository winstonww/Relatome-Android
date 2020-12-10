package com.example.relatome.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.relatome.R
import com.example.relatome.databinding.FragmentFillRelationshipBinding
import com.example.relatome.viewmodel.FillRelationshipViewModel
import com.example.relatome.viewmodel.HomeViewModel
import timber.log.Timber


class FillRelationshipFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var selectedItem : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentFillRelationshipBinding.inflate(inflater, container, false)

        val args = FillRelationshipFragmentArgs.fromBundle(requireArguments())

        ArrayAdapter.createFromResource(
                requireContext(),
        R.array.fill_relationship_spinner_array,
        R.layout.spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_dropdown)
            // Apply the adapter to the spinner
            binding.fillRelationshipSpinner.adapter = adapter
        }

        val viewModel = ViewModelProvider(this,
            FillRelationshipViewModel.Factory(requireActivity().application))
            .get(FillRelationshipViewModel::class.java)


        binding.fillRelationshipSpinner.setOnItemSelectedListener(this as AdapterView.OnItemSelectedListener)

        binding.nextButtonFillRelationship.setOnClickListener {
            viewModel.fillRelationship(selectedItem, args.id)

        }
        viewModel.navigateToContributeBN.observe(viewLifecycleOwner, Observer{
            if (it == true) {
                findNavController().navigate(FillRelationshipFragmentDirections.actionFillRelationshipFragmentToContributeBNFragment())
                viewModel.navgiateToContributeBNComplete()
            }
        })

        return binding.root
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Timber.i("In onItemSelected")
        selectedItem = parent?.getItemAtPosition(position) as String
        Timber.i("Selected Item: ${selectedItem.toString()}")

    }

}