package ua.dolhanenko.repobrowser.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View


fun Boolean.toVisibility(useGone: Boolean = false): Int =
    if (this) View.VISIBLE else if (useGone) View.GONE else View.INVISIBLE

fun String.toUri(): Uri? = try {
    Uri.parse(this)
} catch (e: Exception) {
    null
}

fun Uri?.openInDefaultBrowser(context: Context) {
    this?.let {
        val intent = Intent(Intent.ACTION_VIEW, this)
        context.startActivity(intent)
    }
}