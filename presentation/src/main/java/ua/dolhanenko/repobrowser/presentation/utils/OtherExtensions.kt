package ua.dolhanenko.repobrowser.presentation.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View


internal fun Boolean.toVisibility(useGone: Boolean = false): Int =
    if (this) View.VISIBLE else if (useGone) View.GONE else View.INVISIBLE

internal fun String.toUri(): Uri? = try {
    Uri.parse(this)
} catch (e: Exception) {
    null
}

internal fun Uri?.openInDefaultBrowser(context: Context) {
    this?.let {
        val intent = Intent(Intent.ACTION_VIEW, this)
        context.startActivity(intent)
    }
}