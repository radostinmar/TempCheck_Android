package com.rmarinov.tempcheck.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.rmarinov.tempcheck.R
import com.rmarinov.tempcheck.databinding.ViewSkeletonBinding

class SkeletonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewSkeletonBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.view_skeleton,
        this,
        true
    )

    private val skeletonDrawable = SkeletonDrawable()

    init {
        setBackgroundResource(R.drawable.rounded_rectangle)
        clipToOutline = true
        skeletonDrawable.start()
        binding.ivSkeleton.setImageDrawable(skeletonDrawable)
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.VISIBLE) {
            skeletonDrawable.start()
        } else {
            skeletonDrawable.stop()
        }
    }
}