package ua.dolhanenko.repobrowser.utils.log


interface ILogger {
    fun d(tag: String, message: String)
    fun i(tag: String, message: String)
    fun e(tag: String, message: String)
}