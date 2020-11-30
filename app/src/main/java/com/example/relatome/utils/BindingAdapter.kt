package com.example.relatome.utils

import android.opengl.Visibility
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.relatome.viewmodel.LoginStatus
import timber.log.Timber

@BindingAdapter("helloUserBinding")
fun bindHelloUser(textView: TextView, str: String?) {
    textView.text = str
}