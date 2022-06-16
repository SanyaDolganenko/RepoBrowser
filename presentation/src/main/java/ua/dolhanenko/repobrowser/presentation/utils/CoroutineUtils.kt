package ua.dolhanenko.repobrowser.presentation.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Deprecated(
    "do not use", ReplaceWith(
        "CoroutineScope(Dispatchers.Main).launch { block.invoke() }",
        "kotlinx.coroutines.CoroutineScope",
        "kotlinx.coroutines.Dispatchers",
        "kotlinx.coroutines.launch"
    )
)
fun runOnUiThread(block: () -> Unit) {
    CoroutineScope(Dispatchers.Main).launch { block.invoke() }
}