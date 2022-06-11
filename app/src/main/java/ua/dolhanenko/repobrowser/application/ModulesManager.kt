package ua.dolhanenko.repobrowser.application

import android.content.Context
import ua.dolhanenko.repobrowser.data.local.AppDatabase
import ua.dolhanenko.repobrowser.data.local.RepositoriesCacheDataSource
import ua.dolhanenko.repobrowser.data.local.UsersCacheDataSource
import ua.dolhanenko.repobrowser.data.remote.ApiFactory
import ua.dolhanenko.repobrowser.data.remote.datasource.GithubDataSource
import ua.dolhanenko.repobrowser.data.remote.repository.GithubRepository
import ua.dolhanenko.repobrowser.domain.interfaces.IGithubDataSource
import ua.dolhanenko.repobrowser.domain.interfaces.IGithubRepository
import ua.dolhanenko.repobrowser.domain.interfaces.IRepositoriesCacheDataSource
import ua.dolhanenko.repobrowser.domain.interfaces.IUsersCacheDataSource
import ua.dolhanenko.repobrowser.utils.Constants


class ModulesManager(private val context: Context, private val apiFactory: ApiFactory) {
    var githubDataSource: IGithubDataSource = GithubDataSource(apiFactory.githubApi)
    var githubRepository: IGithubRepository =
        GithubRepository(Constants.PAGE_SIZE, githubDataSource)

    private val appDatabase = AppDatabase.getInstance(context)

    val repositoriesCacheDataSource: IRepositoriesCacheDataSource =
        RepositoriesCacheDataSource(appDatabase.repositoriesCacheDao())

    val usersCacheDataSource: IUsersCacheDataSource =
        UsersCacheDataSource(appDatabase.usersCacheDao())
}