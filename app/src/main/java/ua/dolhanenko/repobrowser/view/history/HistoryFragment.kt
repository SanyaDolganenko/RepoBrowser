package ua.dolhanenko.repobrowser.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ua.dolhanenko.repobrowser.application.RepoApp
import ua.dolhanenko.repobrowser.databinding.FragmentHistoryBinding
import ua.dolhanenko.repobrowser.domain.model.IRepositoryModel
import ua.dolhanenko.repobrowser.utils.openInDefaultBrowser
import ua.dolhanenko.repobrowser.view.common.BaseFragment
import ua.dolhanenko.repobrowser.view.common.RepositoriesAdapter


class HistoryFragment : BaseFragment<FragmentHistoryBinding>(), RepositoriesAdapter.Callback {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHistoryBinding =
        FragmentHistoryBinding::inflate
    private val viewModel: HistoryVM by viewModels { RepoApp.vmFactory }
    private val adapter: RepositoriesAdapter = RepositoriesAdapter(false, this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeForData()
    }

    override fun initViews() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
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