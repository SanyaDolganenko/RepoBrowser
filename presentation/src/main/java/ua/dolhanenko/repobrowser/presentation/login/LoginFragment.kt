package ua.dolhanenko.repobrowser.presentation.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
import ua.dolhanenko.repobrowser.presentation.base.BaseFragment
import ua.dolhanenko.repobrowser.presentation.databinding.FragmentLoginBinding
import ua.dolhanenko.repobrowser.presentation.utils.toVisibility

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLoginBinding =
        FragmentLoginBinding::inflate
    private val viewModel: LoginVM by viewModels()
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

    override fun initViews() {
        binding.buttonLogin.setOnClickListener {
            viewModel.onLoginClick(requireActivity(), "")
        }
    }

    private fun subscribe() {
        viewModel.getAuthResult().observe(this) {
            it?.let {
                if (it) callback?.onLoginSuccess()
            }
        }
        viewModel.getIsLoggingIn().observe(this) {
            loadingLayout.visibility = it.toVisibility(true)
        }
    }

    interface Callback {
        fun onLoginSuccess()
    }
}