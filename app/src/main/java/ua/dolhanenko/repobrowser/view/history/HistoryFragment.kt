package ua.dolhanenko.repobrowser.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_browse.view.*
import ua.dolhanenko.repobrowser.R
import ua.dolhanenko.repobrowser.application.RepoApp
import ua.dolhanenko.repobrowser.domain.model.RepositoryModel
import ua.dolhanenko.repobrowser.utils.runOnUiThread
import ua.dolhanenko.repobrowser.view.common.RepositoriesAdapter


class HistoryFragment : Fragment(), RepositoriesAdapter.Callback {
    private val viewModel: HistoryVM by viewModels { RepoApp.vmFactory }
    private val adapter: RepositoriesAdapter = RepositoriesAdapter(false, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeForData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_history, container, false)
        initViews(root)
        return root
    }

    private fun initViews(root: View) {
        initRecyclerView(root)
    }

    private fun initRecyclerView(root: View) {
        root.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HistoryFragment.adapter
        }
    }

    override fun onItemClick(model: RepositoryModel, position: Int) {

    }

    private fun subscribeForData() {
        viewModel.cachedRepositories.observe(this) {
            it?.let {
                runOnUiThread {
                    adapter.dataList = it
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onFragmentResume()
    }
}