package ua.dolhanenko.repobrowser.data.remote


data class FilteredRepos(val total_count: Long, val items: List<RepoResponse>)
data class RepoResponse(val id: Long, val name: String, val full_name: String)