package ua.dolhanenko.repobrowser.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.dolhanenko.repobrowser.core.ILogger
import ua.dolhanenko.repobrowser.utils.log.Logger
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideLogger(): ILogger {
        return Logger()
    }
}