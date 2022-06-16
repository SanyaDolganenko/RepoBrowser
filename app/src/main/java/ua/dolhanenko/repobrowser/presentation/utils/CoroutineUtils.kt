package ua.dolhanenko.repobrowser.presentation.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun runOnUiThread(block: () -> Unit) {
    CoroutineScope(Dispatchers.Main).launch { block.invoke() }
}