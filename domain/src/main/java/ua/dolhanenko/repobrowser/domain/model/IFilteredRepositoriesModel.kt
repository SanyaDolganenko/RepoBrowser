package ua.dolhanenko.repobrowser.domain.model


interface IFilteredRepositoriesModel {
    var pageNumber: Int
    val foundInTotal: Long
    val items: List<IRepositoryModel>
}
