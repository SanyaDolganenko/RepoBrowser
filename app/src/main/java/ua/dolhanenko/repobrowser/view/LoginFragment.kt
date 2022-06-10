package ua.dolhanenko.repobrowser.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import ua.dolhanenko.repobrowser.R
import ua.dolhanenko.repobrowser.utils.runOnUiThread


class LoginFragment : Fragment() {

    private val viewModel: LoginVM by viewModels()

    override fun onStart() {
        super.onStart()
        subscribe()
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
            val pass = passwordInputEditText.text?.toString() ?: ""
            viewModel.onLoginClick(login, pass)
        }
    }

    private fun subscribe() {
        viewModel.result.observe(this) {
            it?.let {
                runOnUiThread {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}