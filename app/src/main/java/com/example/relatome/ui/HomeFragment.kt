package com.example.relatome.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.relatome.R
import com.example.relatome.databinding.FragmentHomeBinding
import com.example.relatome.viewmodel.HomeViewModel
import com.example.relatome.viewmodel.LoginViewModel
import com.example.relatome.viewmodel.MainViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel = ViewModelProvider(this,
            HomeViewModel.Factory(requireActivity().application))
            .get(HomeViewModel::class.java)

        homeViewModel.login.observe(viewLifecycleOwner, Observer {
            binding.helloUser.text = getString(R.string.hello_user_string, it.name)
        })


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.run {
            mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        } ?: throw Throwable("invalid activity")
        mainViewModel.updateToolbarTitle("Home")
    }

}