package com.example.relatome.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.relatome.R
import com.example.relatome.databinding.FragmentFillReviseRelationshipBinding
import timber.log.Timber


class FillReviseRelationshipFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentFillReviseRelationshipBinding.inflate(
            inflater, container, false)

        val args = FillReviseRelationshipFragmentArgs.fromBundle(requireArguments())

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

        Timber.i("args ${args}")

        return binding.root
    }

}