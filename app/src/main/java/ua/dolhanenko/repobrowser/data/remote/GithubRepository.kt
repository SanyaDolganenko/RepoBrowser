package ua.dolhanenko.repobrowser.data.remote


class GithubRepository(val api: GithubApi) : BaseApiRepository() {
    fun browseRepositories(
        limit: Int = 15,
        page: Int,
        search: String? = null,
        onResult: (FilteredRepos?, String?) -> Unit
    ) {
        safeAsyncRequest(
            api.getAllRepositories(
                limit,
                page,
                search?.toNullIfEmpty()?.toSearchQuery()
            ), onResult
        )
    }

    private fun String.toSearchQuery(): String {
        return "$this in:name"
    }

    private fun String.toNullIfEmpty(): String? = ifEmpty { null }
}