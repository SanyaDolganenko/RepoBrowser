package ua.dolhanenko.repobrowser.utils

import android.content.Context
import android.content.res.TypedArray
import android.util.TypedValue


var Context.colorPrimary: Int
    get() {
        return getStyleColor(android.R.attr.colorPrimary)
    }
    private set(value) {}

private fun Context.getStyleColor(attrRes: Int): Int {
    val typedValue = TypedValue()
    val a: TypedArray = obtainStyledAttributes(typedValue.data, intArrayOf(attrRes))
    val color = a.getColor(0, 0)
    a.recycle()
    return color
}