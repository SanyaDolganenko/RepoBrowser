package ua.dolhanenko.repobrowser.application

import android.content.Context
import ua.dolhanenko.repobrowser.data.local.AppDatabase
import ua.dolhanenko.repobrowser.data.local.datasource.RepositoriesCacheDataSource
import ua.dolhanenko.repobrowser.data.local.datasource.UsersCacheDataSource
import ua.dolhanenko.repobrowser.data.remote.ApiFactory
import ua.dolhanenko.repobrowser.data.remote.datasource.GithubDataSource
import ua.dolhanenko.repobrowser.data.repository.ReposRepository
import ua.dolhanenko.repobrowser.data.repository.UsersRepository
import ua.dolhanenko.repobrowser.data.repository.interfaces.IGithubDataSource
import ua.dolhanenko.repobrowser.data.repository.interfaces.IReposCacheDataSource
import ua.dolhanenko.repobrowser.data.repository.interfaces.IUsersCacheDataSource
import ua.dolhanenko.repobrowser.domain.interfaces.IReposRepository
import ua.dolhanenko.repobrowser.domain.interfaces.IUsersRepository
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