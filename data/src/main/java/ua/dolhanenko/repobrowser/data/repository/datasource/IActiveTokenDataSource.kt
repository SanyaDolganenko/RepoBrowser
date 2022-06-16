package ua.dolhanenko.repobrowser.data.repository.datasource


interface IActiveTokenDataSource {
    fun getActiveToken(): String?

    fun putActiveToken(token: String?)
}