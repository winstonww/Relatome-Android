package com.example.relatome.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.relatome.databinding.FragmentAs1NameSuggestionBinding
import com.example.relatome.viewmodel.As1NameViewModel
import com.example.relatome.viewmodel.AsLoadingStatus
import com.example.relatome.viewmodel.HomeStatus
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Use the [As1NameSuggestionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class As1NameSuggestionFragment : Fragment() {

    private lateinit var viewModel : As1NameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        val binding = FragmentAs1NameSuggestionBinding.inflate(inflater, container, false)
        val args = As1NameSuggestionFragmentArgs.fromBundle(requireArguments())


        viewModel = ViewModelProvider(this,
            As1NameViewModel.Factory(requireActivity().application, args.as1nameInput))
            .get(As1NameViewModel::class.java)

        val adapter = AsNameSuggestionAdapter(viewModel)
        binding.as1NameRecycler.adapter = adapter

        viewModel.asNameList.observe(viewLifecycleOwner, Observer {
            Timber.i("submit list ${it.toString()}")
            adapter.data = it
        })

        viewModel.loadingStatus.observe(viewLifecycleOwner, Observer {
            when(it){
                AsLoadingStatus.LOADING -> binding.homeProgressBar.visibility = View.VISIBLE
                AsLoadingStatus.TIMEOUT -> {
                    Snackbar.make(binding.root, "Connection timeout. Please try again.", Snackbar.LENGTH_LONG).show()
                    viewModel.refreshAsNames(args.as1nameInput)
                }
                else -> binding.homeProgressBar.visibility = View.GONE
            }
        })


        binding.nextButtonAs1NameSuggestion.setOnClickListener {
            val item = adapter.getSelectedItem()
            if(item == null) {
                Snackbar.make(binding.root, "Please select one anatomical structure from the above list.", Snackbar.LENGTH_LONG).show()
            } else {
                viewModel.saveAsId(item!!.id, requireActivity().getSharedPreferences("Share", Context.MODE_PRIVATE))
                findNavController().navigate(As1NameSuggestionFragmentDirections.actionAs1NameSuggestionFragmentToAs2NameInputFragment())
            }

        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        activity?.run {
//            mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
//        } ?: throw Throwable("invalid activity")
//        mainViewModel.updateToolbarTitle("Home")
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

}
