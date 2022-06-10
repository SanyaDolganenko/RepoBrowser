package ua.dolhanenko.repobrowser.view.browse

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ua.dolhanenko.repobrowser.data.remote.ApiFactory
import ua.dolhanenko.repobrowser.data.remote.GithubRepository
import ua.dolhanenko.repobrowser.data.remote.RepoResponse


class BrowseVM : ViewModel() {
    private val repository: GithubRepository = GithubRepository(ApiFactory.githubApi)
    val filteredRepositories: MutableLiveData<List<RepoResponse>?> = MutableLiveData()

    fun onSearchInput(search: String?) {
        if (search.isNullOrEmpty()) {
            filteredRepositories.postValue(listOf())
            return
        }
        repository.browseRepositories(search = search, limit = 15, page = 1) { data, error ->
            error?.let {
                Log.e("BROWSE_VM", it)
            }
            data?.let {
                filteredRepositories.postValue(it.items)
            }
        }
    }

}