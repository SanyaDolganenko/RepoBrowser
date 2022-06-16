package ua.dolhanenko.repobrowser.utils.log

import android.util.Log
import javax.inject.Inject


class Logger @Inject constructor() : ILogger {
    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun i(tag: String, message: String) {
        Log.i(tag, message)
    }

    override fun e(tag: String, message: String) {
        Log.e(tag, message)
    }
}