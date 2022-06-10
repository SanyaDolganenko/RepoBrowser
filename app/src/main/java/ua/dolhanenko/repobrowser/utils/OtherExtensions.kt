package ua.dolhanenko.repobrowser.utils

import android.view.View


fun Boolean.toVisibility(useGone: Boolean = false): Int =
    if (this) View.VISIBLE else if (useGone) View.GONE else View.INVISIBLE