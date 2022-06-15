package ua.dolhanenko.repobrowser.application

import android.content.Context
import ua.dolhanenko.repobrowser.data.local.dao.AppDatabase
import ua.dolhanenko.repobrowser.data.local.RepositoriesCacheDataSource
import ua.dolhanenko.repobrowser.data.local.UsersCacheDataSource
import ua.dolhanenko.repobrowser.data.remote.api.ApiFactory
import ua.dolhanenko.repobrowser.data.remote.GithubDataSource
import ua.dolhanenko.repobrowser.data.repository.ReposRepository
import ua.dolhanenko.repobrowser.data.repository.UsersRepository
import ua.dolhanenko.repobrowser.data.repository.datasource.IGithubDataSource
import ua.dolhanenko.repobrowser.data.repository.datasource.IReposCacheDataSource
import ua.dolhanenko.repobrowser.data.repository.datasource.IUsersCacheDataSource
import ua.dolhanenko.repobrowser.domain.repository.IReposRepository
import ua.dolhanenko.repobrowser.domain.repository.IUsersRepository
import ua.dolhanenko.repobrowser.utils.Constants


class ModulesManager(private val context: Context, private val apiFactory: ApiFactory) {

    private val appDatabase = AppDatabase.getInstance(context)
    private val repositoriesCacheDataSource: IReposCacheDataSource =
        RepositoriesCacheDataSource(appDatabase.repositoriesCacheDao())
    private val usersCacheDataSource: IUsersCacheDataSource =
        UsersCacheDataSource(appDatabase.usersCacheDao())
    private var githubDataSource: IGithubDataSource = GithubDataSource(apiFactory.githubApi)

    val reposRepository: IReposRepository =
        ReposRepository(Constants.PAGE_SIZE, githubDataSource, repositoriesCacheDataSource)
    val usersRepository: IUsersRepository = UsersRepository(githubDataSource, usersCacheDataSource)
}