package ua.dolhanenko.repobrowser.data.repository.datasource


internal interface IActiveTokenDataSource {
    fun getActiveToken(): String?

    fun putActiveToken(token: String?)
}