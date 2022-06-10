package ua.dolhanenko.repobrowser.data.remote


data class FilteredReposResponse(val total_count: Long, val items: List<RepoResponse>)
data class RepoResponse(
    val id: String, val name: String, val full_name: String?,
    val description: String?,
    val stargazers_count: Long?, val watchers_count: Long?,
    val language: String?,
    val url: String?,
    val owner: Owner?
)

data class Owner(val login: String, val avatar_url: String)