package ua.dolhanenko.repobrowser.view.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_browse.view.*
import ua.dolhanenko.repobrowser.R
import ua.dolhanenko.repobrowser.utils.runOnUiThread


class BrowseFragment : Fragment() {
    private val viewModel: BrowseVM by viewModels()
    private val adapter: BrowseAdapter = BrowseAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeForData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_browse, container, false)
        initViews(root)
        return root
    }

    private fun initViews(root: View) {
        root.searchEditText.addTextChangedListener {
            viewModel.onSearchInput(it?.toString() )
        }
        initRecyclerView(root)
    }

    private fun initRecyclerView(root: View) {
        root.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@BrowseFragment.adapter
        }
    }

    private fun subscribeForData() {
        viewModel.filteredRepositories.observe(this) {
            it?.let {
                runOnUiThread {
                    adapter.dataList = it
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}