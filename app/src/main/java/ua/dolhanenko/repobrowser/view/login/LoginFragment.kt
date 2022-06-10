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
import ua.dolhanenko.repobrowser.utils.runOnUiThread


class LoginFragment : Fragment() {
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
            val login = loginInputEditText.text?.toString() ?: ""
            viewModel.onLoginClick(requireActivity(), login)
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
    }

    interface Callback {
        fun onLoginSuccess()
    }
}