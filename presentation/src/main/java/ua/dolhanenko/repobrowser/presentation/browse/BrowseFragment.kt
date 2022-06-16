package ua.dolhanenko.repobrowser.presentation.browse

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_browse.*
import ua.dolhanenko.repobrowser.core.Constants
import ua.dolhanenko.repobrowser.domain.model.IRepositoryModel
import ua.dolhanenko.repobrowser.presentation.base.BaseFragment
import ua.dolhanenko.repobrowser.presentation.common.RepositoriesAdapter
import ua.dolhanenko.repobrowser.presentation.databinding.FragmentBrowseBinding
import ua.dolhanenko.repobrowser.presentation.utils.openInDefaultBrowser
import ua.dolhanenko.repobrowser.presentation.utils.toVisibility

@AndroidEntryPoint
class BrowseFragment : BaseFragment<FragmentBrowseBinding>(), RepositoriesAdapter.Callback {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBrowseBinding =
        FragmentBrowseBinding::inflate
    private val viewModel: BrowseVM by viewModels()
    private val adapter: RepositoriesAdapter = RepositoriesAdapter(true, this)
    private val inputHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeForData()
    }

    override fun initViews() {
        binding.searchEditText.addTextChangedListener {
            inputHandler.removeCallbacksAndMessages(null)
            inputHandler.postDelayed({
                Log.d("BROWSE_FR", "Executing timer to apply filter")
                adapter.currentFilter = it?.toString()
                viewModel.onFilterInput(it?.toString())
            }, Constants.FILTER_DELAY_MS)
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = createPaginationAwareLayoutManager()
            adapter = this@BrowseFragment.adapter
        }
    }

    private fun createPaginationAwareLayoutManager(): RecyclerView.LayoutManager {
        return object : LinearLayoutManager(requireContext()) {
            private fun isLastItemVisible(): Boolean =
                findLastCompletelyVisibleItemPosition() == this@BrowseFragment.adapter.dataList.lastIndex

            override fun onScrollStateChanged(state: Int) {
                super.onScrollStateChanged(state)
                if (state == RecyclerView.SCROLL_STATE_IDLE) {
                    if (isLastItemVisible()) {
                        Log.d("BROWSE_FR", "Reached end of list. Notifying VM.")
                        viewModel.onPageEndReached()
                    }
                }
            }
        }
    }

    override fun onItemClick(model: IRepositoryModel, position: Int) {
        viewModel.onRepositoryClick(model, position)
    }

    private fun subscribeForData() {
        viewModel.getViewUrlEvent().observe(this) {
            it?.openInDefaultBrowser(requireContext())
        }
        viewModel.getFilteredRepositories().observe(this) {
            it?.let {
                adapter.dataList = it
            }
        }
        viewModel.getIsDataLoading().observe(this) {
            progressPar.visibility = it.toVisibility()
        }
        viewModel.getFilteredFound().observe(this) {
            countTextView.text = it?.toString()
        }
    }

    override fun onPause() {
        super.onPause()
        inputHandler.removeCallbacksAndMessages(null)
    }
}