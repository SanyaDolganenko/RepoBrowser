package ua.dolhanenko.repobrowser.data.local

import ua.dolhanenko.repobrowser.data.repository.datasource.IActiveTokenDataSource


class ActiveTokenDataSource : IActiveTokenDataSource {
    private var token: String? = null
    override fun getActiveToken(): String? = token

    override fun putActiveToken(token: String?) {
        this.token = token
    }
}