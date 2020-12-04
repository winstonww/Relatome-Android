package com.example.relatome.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.relatome.R
import com.example.relatome.databinding.FragmentContributeBNBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ContributeBNFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContributeBNFragment : BottomNavigationFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentContributeBNBinding.inflate(inflater, container, false)
        binding.bottomNavigation.setSelectedItemId(R.id.contributeBNFragment)


        return binding.root
    }

}