package com.example.relatome.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.relatome.databinding.FragmentAs2NameSuggestionBinding
import com.example.relatome.viewmodel.As2NameViewModel
import com.example.relatome.viewmodel.AsLoadingStatus
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber


class As2NameSuggestionFragment : Fragment() {

    private lateinit var viewModel : As2NameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentAs2NameSuggestionBinding.inflate(inflater, container, false)
        val args = As2NameSuggestionFragmentArgs.fromBundle(requireArguments())


        viewModel = ViewModelProvider(this,
            As2NameViewModel.Factory(requireActivity().application, args.as2nameInput))
            .get(As2NameViewModel::class.java)

        val adapter = AsNameSuggestionAdapter(viewModel)
        binding.as2NameRecycler.adapter = adapter

        viewModel.asNameList.observe(viewLifecycleOwner, Observer {
            Timber.i("submit list ${it.toString()}")
            adapter.data = it
        })

        viewModel.loadingStatus.observe(viewLifecycleOwner, Observer {
            when(it){
                AsLoadingStatus.LOADING -> binding.homeProgressBar.visibility = View.VISIBLE
                AsLoadingStatus.TIMEOUT -> {
                    Snackbar.make(binding.root, "Connection timeout. Retrying...", Snackbar.LENGTH_LONG).show()
                    viewModel.refreshAsNames(args.as2nameInput)
                }
                else -> binding.homeProgressBar.visibility = View.GONE
            }
        })

        binding.nextButtonAs2NameSuggestion.setOnClickListener {
            val item = adapter.getSelectedItem()
            if(item == null) {
                Snackbar.make(binding.root, "Please select one anatomical structure from the above list.", Snackbar.LENGTH_LONG).show()
            } else {
                viewModel.saveAsId(item.id, requireActivity().getSharedPreferences("Share", Context.MODE_PRIVATE))
                viewModel.addRelationship(requireActivity().getSharedPreferences("Share", Context.MODE_PRIVATE))
                findNavController().navigate(As2NameSuggestionFragmentDirections.actionAs2NameSuggestionFragmentToHomeFragment())
            }
        }

        return binding.root
    }

}