package com.example.relatome.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.relatome.R

/**
 * A simple [Fragment] subclass.
 * Use the [ReviseBNFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReviseBNFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_revise_b_n, container, false)
    }

}