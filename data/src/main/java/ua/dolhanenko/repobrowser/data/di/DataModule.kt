package ua.dolhanenko.repobrowser.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.dolhanenko.repobrowser.core.Constants
import ua.dolhanenko.repobrowser.core.ILogger
import ua.dolhanenko.repobrowser.data.local.ActiveTokenDataSource
import ua.dolhanenko.repobrowser.data.local.ReposCacheDataSource
import ua.dolhanenko.repobrowser.data.local.UsersCacheDataSource
import ua.dolhanenko.repobrowser.data.local.dao.AppDatabase
import ua.dolhanenko.repobrowser.data.remote.GithubDataSource
import ua.dolhanenko.repobrowser.data.remote.api.ApiFactory
import ua.dolhanenko.repobrowser.data.remote.api.GithubApi
import ua.dolhanenko.repobrowser.data.remote.api.IApiFactory
import ua.dolhanenko.repobrowser.data.repository.ReposRepository
import ua.dolhanenko.repobrowser.data.repository.UsersRepository
import ua.dolhanenko.repobrowser.data.repository.datasource.IActiveTokenDataSource
import ua.dolhanenko.repobrowser.data.repository.datasource.IGithubDataSource
import ua.dolhanenko.repobrowser.data.repository.datasource.IReposCacheDataSource
import ua.dolhanenko.repobrowser.data.repository.datasource.IUsersCacheDataSource
import ua.dolhanenko.repobrowser.domain.repository.IReposRepository
import ua.dolhanenko.repobrowser.domain.repository.IUsersRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataModule {

    @Singleton
    @Provides
    internal fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    internal fun provideReposCacheDataSource(database: AppDatabase): IReposCacheDataSource {
        return ReposCacheDataSource(database.repositoriesCacheDao())
    }

    @Singleton
    @Provides
    internal fun provideUsersCacheDataSource(database: AppDatabase): IUsersCacheDataSource {
        return UsersCacheDataSource(database.usersCacheDao())
    }

    @Singleton
    @Provides
    internal fun provideActiveTokenDataSource(): IActiveTokenDataSource {
        return ActiveTokenDataSource()
    }

    @Singleton
    @Provides
    internal fun provideApiFactory(activeTokenDataSource: IActiveTokenDataSource): IApiFactory {
        return ApiFactory(activeTokenDataSource, Constants.API_BASE_URL)
    }

    @Singleton
    @Provides
    internal fun provideGithubApi(factory: IApiFactory): GithubApi {
        return factory.createDefaultRetrofit().create(GithubApi::class.java)
    }

    @Singleton
    @Provides
    internal fun provideGithubDataSource(api: GithubApi): IGithubDataSource {
        return GithubDataSource(api)
    }

    @Provides
    internal fun provideReposRepository(
        githubDataSource: IGithubDataSource,
        reposCacheDataSource: IReposCacheDataSource,
        logger: ILogger
    ): IReposRepository {
        return ReposRepository(Constants.PAGE_SIZE, githubDataSource, reposCacheDataSource, logger)
    }

    @Provides
    internal fun provideUsersRepository(
        activeTokenDataSource: IActiveTokenDataSource,
        githubDataSource: IGithubDataSource,
        usersCacheDataSource: IUsersCacheDataSource
    ): IUsersRepository {
        return UsersRepository(activeTokenDataSource, githubDataSource, usersCacheDataSource)
    }
}