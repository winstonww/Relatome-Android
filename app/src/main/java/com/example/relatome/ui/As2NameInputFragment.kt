package com.example.relatome.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.relatome.R
import com.example.relatome.databinding.FragmentAs1NameInputBinding
import com.example.relatome.databinding.FragmentAs2NameInputBinding


class As2NameInputFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentAs2NameInputBinding.inflate(inflater, container, false)

        binding.nextButtonAs2NameInput.setOnClickListener {
            findNavController().navigate(
                As2NameInputFragmentDirections.actionAs2NameInputFragmentToAs2NameSuggestionFragment(binding.as2nameEdittext.text.toString())
            )
        }
        return binding.root
    }

}