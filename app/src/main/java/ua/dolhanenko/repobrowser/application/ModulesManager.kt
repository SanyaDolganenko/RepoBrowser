package ua.dolhanenko.repobrowser.application

import android.content.Context
import ua.dolhanenko.repobrowser.data.local.AppDatabase
import ua.dolhanenko.repobrowser.data.local.RepositoriesCacheDataSource
import ua.dolhanenko.repobrowser.data.local.UsersCacheDataSource
import ua.dolhanenko.repobrowser.data.remote.ApiFactory
import ua.dolhanenko.repobrowser.data.remote.GithubDataSource
import ua.dolhanenko.repobrowser.domain.interfaces.IGithubDataSource
import ua.dolhanenko.repobrowser.domain.interfaces.IRepositoriesCacheDataSource
import ua.dolhanenko.repobrowser.domain.interfaces.IUsersCacheDataSource


class ModulesManager(private val context: Context, private val apiFactory: ApiFactory) {
    var githubDataSource: IGithubDataSource = GithubDataSource(apiFactory.githubApi)

    private val appDatabase = AppDatabase.getInstance(context)

    val repositoriesCacheDataSource: IRepositoriesCacheDataSource =
        RepositoriesCacheDataSource(appDatabase.repositoriesCacheDao())

    val usersCacheDataSource: IUsersCacheDataSource =
        UsersCacheDataSource(appDatabase.usersCacheDao())
}