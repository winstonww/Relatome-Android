package com.example.relatome.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ListAdapter
import com.example.relatome.R
import com.example.relatome.databinding.FragmentAs1NameInputBinding
import timber.log.Timber


class As1NameInputFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentAs1NameInputBinding.inflate(inflater, container, false)

        binding.nextButtonAs1NameInput.setOnClickListener {
            findNavController().navigate(
                As1NameInputFragmentDirections.actionAs1NameInputFragmentToAs1NameSuggestionFragment(binding.as1nameEdittext.text.toString())
            )
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


