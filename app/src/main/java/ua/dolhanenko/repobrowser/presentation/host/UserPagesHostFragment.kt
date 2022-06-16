package ua.dolhanenko.repobrowser.presentation.host

import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ua.dolhanenko.repobrowser.R
import ua.dolhanenko.repobrowser.databinding.FragmentPagesHostBinding
import ua.dolhanenko.repobrowser.presentation.browse.BrowseFragment
import ua.dolhanenko.repobrowser.presentation.base.BaseFragment
import ua.dolhanenko.repobrowser.presentation.history.HistoryFragment

@AndroidEntryPoint
class UserPagesHostFragment : BaseFragment<FragmentPagesHostBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPagesHostBinding =
        FragmentPagesHostBinding::inflate
    private val browseFragment = BrowseFragment()
    private val historyFragment = HistoryFragment()
    private lateinit var adapter: UserPagesAdapter

    override fun initViews() {
        initAdapter()
        initViewPager()
    }

    private fun initAdapter() {
        adapter = UserPagesAdapter(
            childFragmentManager,
            lifecycle,
            listOf(browseFragment, historyFragment)
        )
    }

    private fun initViewPager() {
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_browse)
                else -> getString(R.string.tab_history)
            }
        }.attach()
    }
}