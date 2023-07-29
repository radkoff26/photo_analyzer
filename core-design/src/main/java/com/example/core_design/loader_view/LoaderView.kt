package com.example.core_design.loader_view

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat
import com.example.core_design.R

class LoaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    private val imageView = AppCompatImageView(context)

    init {
        backgroundTintList =
            ColorStateList.valueOf(resources.getColor(R.color.black_50_alpha, null))

        imageView.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.loader_48,
                null
            )
        )
        imageView.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.grey, null))
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        addView(imageView)
    }
}