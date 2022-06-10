package ua.dolhanenko.repobrowser.view.browse

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_browse.*
import kotlinx.android.synthetic.main.fragment_browse.view.*
import ua.dolhanenko.repobrowser.R
import ua.dolhanenko.repobrowser.application.RepoApp
import ua.dolhanenko.repobrowser.domain.model.RepositoryModel
import ua.dolhanenko.repobrowser.utils.*


class BrowseFragment : Fragment(), BrowseAdapter.Callback {
    private val viewModel: BrowseVM by viewModels { RepoApp.vmFactory }
    private val adapter: BrowseAdapter = BrowseAdapter(this)
    private val inputHandler = Handler(Looper.getMainLooper())
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
            inputHandler.removeCallbacksAndMessages(null)
            inputHandler.postDelayed({
                Log.d("BROWSE_FR", "Executing timer to apply filter")
                adapter.currentFilter = it?.toString()
                viewModel.onFilterInput(it?.toString())
            }, Constants.FILTER_DELAY_MS)
        }
        initRecyclerView(root)
    }

    private fun initRecyclerView(root: View) {
        root.recyclerView.apply {
            layoutManager = createPaginationAwareLayoutManager()
            adapter = this@BrowseFragment.adapter
        }
    }

    private fun createPaginationAwareLayoutManager(): RecyclerView.LayoutManager {
        return object : LinearLayoutManager(requireContext()) {
            override fun onScrollStateChanged(state: Int) {
                super.onScrollStateChanged(state)
                if (state == RecyclerView.SCROLL_STATE_IDLE) {
                    if (findLastCompletelyVisibleItemPosition() ==
                        this@BrowseFragment.adapter.dataList.lastIndex
                    ) {
                        Log.d("BROWSE_FR", "Reached end of list. Notifying VM.")
                        viewModel.onPageEndReached()
                    }
                }
            }
        }
    }

    override fun onItemClick(model: RepositoryModel, position: Int) {
        //TODO notify VM so that the model is saved to DB
        //TODO visually mark the item as "read"
        Toast.makeText(requireContext(), position.toString(), Toast.LENGTH_SHORT).show()
        model.url.toUri()?.openInDefaultBrowser(requireContext())
    }

    private fun subscribeForData() {
        viewModel.filteredRepositories.observe(this) {
            it?.let {
                runOnUiThread {
                    adapter.dataList = it
                }
            }
        }
        viewModel.isDataLoading.observe(this) {
            runOnUiThread {
                progressPar.visibility = it.toVisibility()
            }
        }
        viewModel.filteredFound.observe(this) {
            runOnUiThread {
                countTextView.text = it?.toString()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        inputHandler.removeCallbacksAndMessages(null)
    }
}