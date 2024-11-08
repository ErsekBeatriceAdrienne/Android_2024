package com.tasty.recipesapp.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.tasty.recipesapp.R

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    Glide.with(view.context)
        .load(imageUrl)
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.error_image)
        .centerCrop()
        .into(view)
}
