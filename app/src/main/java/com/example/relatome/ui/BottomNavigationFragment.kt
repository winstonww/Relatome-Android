package com.example.relatome.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.relatome.R
import com.google.android.material.bottomnavigation.BottomNavigationView

open class BottomNavigationFragment : Fragment() {

    val FRAGMENT_OTHER = "FRAGMENT_OTHER"
    val FRAGMENT_HOME = "FRAGMENT_HOME"

    fun viewFragment(fragment: Fragment, name: String) {
        // https://stackoverflow.com/questions/43870485/how-to-handle-bottom-navigation-perfectly-with-back-pressed
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()

        fragmentTransaction?.replace(R.id.myNavHostFragment, fragment)
        fragmentTransaction?.commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.homeBNFragment -> {
                    // Respond to navigation item 1 reselection
                    viewFragment(HomeBNFragment(), FRAGMENT_HOME)
                    true
                }
                R.id.contributeBNFragment -> {
                    // Respond to navigation item 2 reselection
                    viewFragment(ContributeBNFragment(), FRAGMENT_OTHER)
                    true
                }
                else -> true
            }
        }
    }
}