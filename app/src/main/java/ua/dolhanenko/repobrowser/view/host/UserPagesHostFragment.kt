package ua.dolhanenko.repobrowser.view.host

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_pages_host.view.*
import ua.dolhanenko.repobrowser.R
import ua.dolhanenko.repobrowser.view.browse.BrowseFragment
import ua.dolhanenko.repobrowser.view.history.HistoryFragment


class UserPagesHostFragment : Fragment() {
    private val browseFragment = BrowseFragment()
    private val historyFragment = HistoryFragment()
    private lateinit var adapter: UserPagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_pages_host, container, false)
        initAdapter()
        initViews(root)
        return root
    }

    private fun initAdapter() {
        adapter = UserPagesAdapter(
            childFragmentManager,
            lifecycle,
            listOf(browseFragment, historyFragment)
        )
    }

    private fun initViews(root: View) {
        root.viewPager.adapter = adapter
        TabLayoutMediator(root.tabLayout, root.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_browse)
                else -> getString(R.string.tab_history)
            }
        }.attach()
    }
}