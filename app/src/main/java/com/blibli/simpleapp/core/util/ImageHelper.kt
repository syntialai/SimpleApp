package com.blibli.simpleapp.core.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions

object ImageHelper {

    private fun imageBuilder(
        context: Context,
        imageUrl: String
    ): RequestBuilder<Drawable> {

        return Glide.with(context)
            .load(imageUrl)
    }

    fun resizeAndBuildImage(
        context: Context,
        imageUrl: String,
        imageView: ImageView,
        sizeId: Int
    ) {
        val size = ResourcesHelper.getDimen(context, sizeId)

        imageBuilder(context, imageUrl)
            .apply(RequestOptions().override(size, size))
            .centerCrop()
            .into(imageView)
    }
}