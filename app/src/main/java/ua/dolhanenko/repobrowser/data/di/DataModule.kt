package ua.dolhanenko.repobrowser.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ua.dolhanenko.repobrowser.BuildConfig
import ua.dolhanenko.repobrowser.core.Constants
import ua.dolhanenko.repobrowser.data.local.ReposCacheDataSource
import ua.dolhanenko.repobrowser.data.local.UsersCacheDataSource
import ua.dolhanenko.repobrowser.data.local.dao.AppDatabase
import ua.dolhanenko.repobrowser.data.remote.GithubDataSource
import ua.dolhanenko.repobrowser.data.remote.api.ApiFactory
import ua.dolhanenko.repobrowser.data.remote.api.GithubApi
import ua.dolhanenko.repobrowser.data.remote.api.IApiFactory
import ua.dolhanenko.repobrowser.data.repository.ReposRepository
import ua.dolhanenko.repobrowser.data.repository.UsersRepository
import ua.dolhanenko.repobrowser.data.repository.datasource.IGithubDataSource
import ua.dolhanenko.repobrowser.data.repository.datasource.IReposCacheDataSource
import ua.dolhanenko.repobrowser.data.repository.datasource.IUsersCacheDataSource
import ua.dolhanenko.repobrowser.domain.repository.IReposRepository
import ua.dolhanenko.repobrowser.domain.repository.IUsersRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideReposCacheDataSource(database: AppDatabase): IReposCacheDataSource {
        return ReposCacheDataSource(database.repositoriesCacheDao())
    }

    @Singleton
    @Provides
    fun provideUsersCacheDataSource(database: AppDatabase): IUsersCacheDataSource {
        return UsersCacheDataSource(database.usersCacheDao())
    }

    @Singleton
    @Provides
    fun provideApiFactory(): IApiFactory {
        return ApiFactory(BuildConfig.API_BASE_URL)
    }

    @Singleton
    @Provides
    fun provideGithubApi(factory: IApiFactory): GithubApi {
        return factory.createDefaultRetrofit().create(GithubApi::class.java)
    }

    @Singleton
    @Provides
    fun provideGithubDataSource(api: GithubApi): IGithubDataSource {
        return GithubDataSource(api)
    }

    @Provides
    fun provideReposRepository(
        githubDataSource: IGithubDataSource,
        reposCacheDataSource: IReposCacheDataSource
    ): IReposRepository {
        return ReposRepository(Constants.PAGE_SIZE, githubDataSource, reposCacheDataSource)
    }

    @Provides
    fun provideUsersRepository(
        githubDataSource: IGithubDataSource,
        usersCacheDataSource: IUsersCacheDataSource
    ): IUsersRepository {
        return UsersRepository(githubDataSource, usersCacheDataSource)
    }
}