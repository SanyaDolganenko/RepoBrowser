package ua.dolhanenko.repobrowser.presentation.utils.loader

import android.widget.ImageView


internal interface IImageLoader {
    fun load(url: String?, into: ImageView)
}