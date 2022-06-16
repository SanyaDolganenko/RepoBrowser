package ua.dolhanenko.repobrowser.presentation.utils.loader

import android.widget.ImageView
import com.bumptech.glide.Glide
import javax.inject.Inject


internal class ImageLoader @Inject constructor() : IImageLoader {
    override fun load(url: String?, into: ImageView) {
        Glide.with(into).load(url).into(into)
    }
}