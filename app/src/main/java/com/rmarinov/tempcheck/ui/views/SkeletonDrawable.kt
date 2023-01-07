package com.rmarinov.tempcheck.ui.views

import android.graphics.Color.rgb
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.GradientDrawable

class SkeletonDrawable : AnimationDrawable() {

    private val start: Int = rgb(197, 198, 199)
    private val center: Int = rgb(126, 137, 142)
    private val end: Int = rgb(80, 87, 91)
    private val frameDuration: Int = 1000
    private val enterFadeDuration: Int = 500
    private val exitFadeDuration: Int = 1000

    private val gradientStart = GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, intArrayOf(start, center, end))
        .apply {
            shape = GradientDrawable.RECTANGLE
            gradientType = GradientDrawable.LINEAR_GRADIENT
        }

    private val gradientCenter = GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, intArrayOf(center, end, start))
        .apply {
            shape = GradientDrawable.RECTANGLE
            gradientType = GradientDrawable.LINEAR_GRADIENT
        }

    private val gradientEnd = GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, intArrayOf(end, start, center))
        .apply {
            shape = GradientDrawable.RECTANGLE
            gradientType = GradientDrawable.LINEAR_GRADIENT
        }

    init {
        addFrame(gradientStart, frameDuration)
        addFrame(gradientCenter, frameDuration)
        addFrame(gradientEnd, frameDuration)
        setEnterFadeDuration(enterFadeDuration)
        setExitFadeDuration(exitFadeDuration)
        isOneShot = false
    }
}