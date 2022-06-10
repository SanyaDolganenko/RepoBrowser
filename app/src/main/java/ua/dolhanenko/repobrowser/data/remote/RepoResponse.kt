package ua.dolhanenko.repobrowser.data.remote


data class FilteredReposResponse(val total_count: Long, val items: List<RepoResponse>)
data class RepoResponse(val id: String, val name: String, val full_name: String)