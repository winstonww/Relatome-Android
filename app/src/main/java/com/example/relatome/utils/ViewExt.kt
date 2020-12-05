package com.example.relatome.utils

import android.view.View
import android.view.animation.AnimationUtils
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.example.relatome.R

fun View.slideUp(animTime: Long, startOffset: Long) {
    val slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up).apply {
        duration = animTime
        interpolator = FastOutSlowInInterpolator()
        this.startOffset = startOffset
    }
    startAnimation(slideUp)
}