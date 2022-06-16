package ua.dolhanenko.repobrowser.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.dolhanenko.repobrowser.presentation.common.RepositoriesAdapter
import ua.dolhanenko.repobrowser.presentation.utils.loader.IImageLoader
import ua.dolhanenko.repobrowser.presentation.utils.loader.ImageLoader
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object PresentationModule {

    @Provides
    @Singleton
    internal fun provideImageLoader(): IImageLoader = ImageLoader()

    @Provides
    internal fun provideRepositoriesAdapter(imageLoader: IImageLoader): RepositoriesAdapter =
        RepositoriesAdapter(imageLoader)
}