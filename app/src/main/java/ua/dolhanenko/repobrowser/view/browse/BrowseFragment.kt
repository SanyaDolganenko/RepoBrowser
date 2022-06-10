package ua.dolhanenko.repobrowser.view.browse

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_browse.*
import kotlinx.android.synthetic.main.fragment_browse.view.*
import ua.dolhanenko.repobrowser.R
import ua.dolhanenko.repobrowser.application.RepoApp
import ua.dolhanenko.repobrowser.utils.Constants
import ua.dolhanenko.repobrowser.utils.runOnUiThread
import ua.dolhanenko.repobrowser.utils.toVisibility


class BrowseFragment : Fragment() {
    private val viewModel: BrowseVM by viewModels { RepoApp.vmFactory }
    private val adapter: BrowseAdapter = BrowseAdapter()
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
                viewModel.onFilterInput(it?.toString())
            }, Constants.FILTER_DELAY_MS)
        }
        initRecyclerView(root)
    }

    private fun initRecyclerView(root: View) {
        root.recyclerView.apply {
            layoutManager = object : LinearLayoutManager(requireContext()) {
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
        viewModel.isDataLoading.observe(this) {
            runOnUiThread {
                progressPar.visibility = it.toVisibility()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        inputHandler.removeCallbacksAndMessages(null)
    }
}