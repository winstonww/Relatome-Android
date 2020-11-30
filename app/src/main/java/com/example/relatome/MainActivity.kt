package com.example.relatome

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.relatome.databinding.ActivityMainBinding
import com.example.relatome.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel : MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.toolbarTitle.observe(this, Observer { str ->
            supportActionBar?.title = str
        })

        setSupportActionBar(findViewById(R.id.my_toolbar))


    }
}