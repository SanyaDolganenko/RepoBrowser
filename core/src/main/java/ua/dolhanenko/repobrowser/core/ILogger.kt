package ua.dolhanenko.repobrowser.core


interface ILogger {
    fun d(tag: String, message: String)
    fun i(tag: String, message: String)
    fun e(tag: String, message: String)
}