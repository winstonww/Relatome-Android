package com.example.relatome.ui

import com.example.relatome.databinding.AddAs2DialogBinding
import com.example.relatome.viewmodel.AddAs2ViewModel


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.relatome.viewmodel.LoadingStatus
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class AddAs2Dialog: BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = AddAs2DialogBinding.inflate(inflater, container, false)

        val viewModel = ViewModelProvider(
            this, AddAs2ViewModel.Factory(requireActivity().application))
            .get(AddAs2ViewModel::class.java)

        val args = AddAs2DialogArgs.fromBundle(requireArguments())

        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.addButton.setOnClickListener {
            viewModel.addAnatomicalStructure(binding.editTextAs.text.toString())
        }


        viewModel.navigateToAsSuggestion.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(AddAs2DialogDirections.actionAddAs2DialogToAs2NameSuggestionFragment(args.as2NameInput))
                viewModel.navigateToAsSuggestionComplete()
            }
        })

        viewModel.loadingStatus.observe(viewLifecycleOwner, Observer { loadingStatus ->
            when (loadingStatus) {
                is LoadingStatus.Loading -> {
                    binding.addDialogProgressBar.visibility = View.VISIBLE
                }
                is LoadingStatus.Noop -> {
                    binding.addDialogProgressBar.visibility = View.GONE
                }
                is LoadingStatus.Error -> {
                    binding.addDialogProgressBar.visibility = View.GONE
                    Timber.i("loading status error")
                    Snackbar.make(binding.addAsDialogCoordinatorLayout, "Anatomical Structure already exists", Snackbar.LENGTH_LONG).show()
                }
                is LoadingStatus.Timeout -> {
                    binding.addDialogProgressBar.visibility = View.GONE
                    Snackbar.make(binding.addAsDialogCoordinatorLayout, "Connection timeout", Snackbar.LENGTH_LONG).show()
                }
                is LoadingStatus.NoConnection -> {
                    binding.addDialogProgressBar.visibility = View.GONE
                    Snackbar.make(binding.addAsDialogCoordinatorLayout, "No Connection", Snackbar.LENGTH_LONG).show()
                }
            }
        })

        return binding.root
    }
}