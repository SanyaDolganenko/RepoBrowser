package ua.dolhanenko.repobrowser.domain.interfaces

import kotlinx.coroutines.flow.Flow
import ua.dolhanenko.repobrowser.domain.model.FilteredRepositoriesModel
import ua.dolhanenko.repobrowser.domain.model.Resource


interface IGithubRepository {
    fun getPagesAsync(
        filter: String,
        pageNumbers: IntArray
    ): Flow<Resource<FilteredRepositoriesModel?>>
}