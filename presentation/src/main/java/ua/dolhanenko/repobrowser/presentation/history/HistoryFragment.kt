package ua.dolhanenko.repobrowser.presentation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ua.dolhanenko.repobrowser.domain.model.IRepositoryModel
import ua.dolhanenko.repobrowser.presentation.base.BaseFragment
import ua.dolhanenko.repobrowser.presentation.common.RepositoriesAdapter
import ua.dolhanenko.repobrowser.presentation.databinding.FragmentHistoryBinding
import ua.dolhanenko.repobrowser.presentation.utils.openInDefaultBrowser
import javax.inject.Inject

@AndroidEntryPoint
internal class HistoryFragment : BaseFragment<FragmentHistoryBinding>(),
    RepositoriesAdapter.Callback {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHistoryBinding =
        FragmentHistoryBinding::inflate
    private val viewModel: HistoryVM by viewModels()

    @Inject
    lateinit var adapter: RepositoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeForData()
    }

    override fun initViews() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter.callback = this
        adapter.showPositions = false
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HistoryFragment.adapter
        }
    }

    override fun onItemClick(model: IRepositoryModel, position: Int) {
        viewModel.onItemClick(model)
    }

    private fun subscribeForData() {
        viewModel.getViewUrlEvent().observe(this) {
            it?.openInDefaultBrowser(requireContext())
        }
        viewModel.getCachedRepositories().observe(this) {
            it?.let {
                adapter.dataList = it
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onFragmentResume()
    }
}