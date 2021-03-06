package ua.dolhanenko.repobrowser.presentation.host

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ua.dolhanenko.repobrowser.presentation.R
import ua.dolhanenko.repobrowser.presentation.base.BaseActivity
import ua.dolhanenko.repobrowser.presentation.databinding.ActivityHostBinding
import ua.dolhanenko.repobrowser.presentation.login.LoginFragment

@AndroidEntryPoint
internal class HostActivity : BaseActivity<ActivityHostBinding>(), LoginFragment.Callback {
    override val bindingInflater: (LayoutInflater) -> ActivityHostBinding =
        ActivityHostBinding::inflate
    private val viewModel: HostVM by viewModels()
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeData()
    }

    override fun initViews() {
        LoginFragment().show()
    }


    override fun onLoginSuccess() {
        viewModel.onSuccessfulLogin()
        UserPagesHostFragment().show()
    }

    private fun Fragment.show() {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragmentContainer, this
        ).commit()
    }

    private fun subscribeData() {
        viewModel.getIsLogoutVisible().observe(this) {
            menu?.changeLogoutVisibility(it)
        }
        viewModel.getSuccessfulLogoutEvent().observe(this) {
            LoginFragment().show()
        }
    }

    private fun Menu.changeLogoutVisibility(isVisible: Boolean) {
        findItem(R.id.logout)?.isVisible = isVisible
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.changeLogoutVisibility(viewModel.getIsLogoutVisible().value ?: false)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.host_menu, menu)
        this.menu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            viewModel.onLogoutRequest()
        }
        return super.onOptionsItemSelected(item)
    }

}