package ua.dolhanenko.repobrowser.domain.model


data class FilteredRepositoriesModel(
    val pageNumber: Int,
    val foundInTotal: Long,
    val items: List<RepositoryModel>
)