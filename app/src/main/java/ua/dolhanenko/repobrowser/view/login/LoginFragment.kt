package ua.dolhanenko.repobrowser.view.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import ua.dolhanenko.repobrowser.R
import ua.dolhanenko.repobrowser.application.RepoApp
import ua.dolhanenko.repobrowser.utils.runOnUiThread
import ua.dolhanenko.repobrowser.utils.toVisibility


class LoginFragment : Fragment() {
    private val viewModel: LoginVM by viewModels { RepoApp.vmFactory }
    private var callback: Callback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribe()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Callback) {
            callback = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        initViews(root)
        return root
    }

    private fun initViews(root: View) {
        root.buttonLogin.setOnClickListener {
            viewModel.onLoginClick(requireActivity(), "")
        }
    }

    private fun subscribe() {
        viewModel.result.observe(this) {
            it?.let {
                runOnUiThread {
                    if (it) callback?.onLoginSuccess()
                }
            }
        }
        viewModel.isLoggingIn.observe(this) {
            runOnUiThread {
                loadingLayout.visibility = it.toVisibility(true)
            }
        }
    }

    interface Callback {
        fun onLoginSuccess()
    }
}