package ua.dolhanenko.repobrowser.core

class NetworkException(val code: Int, message: String) : Exception(message)