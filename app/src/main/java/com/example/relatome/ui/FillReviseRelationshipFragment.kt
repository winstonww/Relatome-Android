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
import com.example.relatome.databinding.FragmentFillReviseRelationshipBinding
import com.example.relatome.viewmodel.FillReviseViewModel
import timber.log.Timber


class FillReviseRelationshipFragment : Fragment(), AdapterView.OnItemSelectedListener  {

    private lateinit var selectedItem : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentFillReviseRelationshipBinding.inflate(
            inflater, container, false)

        val args = FillReviseRelationshipFragmentArgs.fromBundle(requireArguments())

        val viewModel = ViewModelProvider(this,
            FillReviseViewModel.Factory(requireActivity().application)).get(
            FillReviseViewModel::class.java
        )

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

        binding.nextButtonFillRelationship.setOnClickListener {
            viewModel.reviseRelationship(selectedItem, args.relationshipId)
        }

        binding.fillRelationshipSpinner.setOnItemSelectedListener(this as AdapterView.OnItemSelectedListener)

        viewModel.navigate.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(FillReviseRelationshipFragmentDirections.actionFillReviseRelationshipFragmentToReviseBNFragment())
                viewModel.navigateComplete()
            }
        })


        Timber.i("args ${args}")

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