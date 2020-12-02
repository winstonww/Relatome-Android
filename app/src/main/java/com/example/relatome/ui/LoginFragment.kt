package com.example.relatome.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.relatome.databinding.FragmentLoginBinding
import com.example.relatome.viewmodel.LoginStatus
import com.example.relatome.viewmodel.LoginViewModel
import com.example.relatome.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var mainViewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentLoginBinding.inflate(inflater, container, false)

        loginViewModel = ViewModelProvider(this,
            LoginViewModel.Factory(requireActivity().application))
            .get(LoginViewModel::class.java)

        binding.loginButton.setOnClickListener {
            loginViewModel.login(
                binding.editTextTextEmailAddress.text.toString(), binding.editTextTextPassword.text.toString())
        }
        binding.viewModel = loginViewModel

        loginViewModel.navigateToLoginStatus.observe(viewLifecycleOwner, Observer {

            // WARNING : PLEASE DON'T call setNavigateToLoginComplete outside of when, otherwise infinite loop
            when(it) {
                LoginStatus.ERROR -> {
                    Snackbar.make(binding.root, "Credentials given are invalid. Please try again.", Snackbar.LENGTH_LONG).show()
                    loginViewModel.setNavigateToLoginComplete()
                    binding.myProgressBar.visibility = View.GONE
                }
                LoginStatus.LOGGED_IN -> {
                    Toast.makeText(context, "Logged In!", Toast.LENGTH_LONG).show()
                    findNavController().navigate(LoginFragmentDirections.actionFragmentLoginToHomeFragment())

                    loginViewModel.setNavigateToLoginComplete()
                    binding.myProgressBar.visibility = View.GONE
                }
                LoginStatus.LOADING -> {
                    binding.myProgressBar.visibility = View.VISIBLE
                }
                LoginStatus.NOOP -> {
                    binding.myProgressBar.visibility = View.GONE
                }
                LoginStatus.TIMEOUT -> {
                    Snackbar.make(binding.root, "Connection timed out. Please try again.", Snackbar.LENGTH_LONG).show()
                    loginViewModel.setNavigateToLoginComplete()
                    binding.myProgressBar.visibility = View.GONE
                }
            }


        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.run {
            mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        } ?: throw Throwable("invalid activity")
        mainViewModel.updateToolbarTitle("Log in")
    }
}