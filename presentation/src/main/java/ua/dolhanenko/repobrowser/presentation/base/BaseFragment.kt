package ua.dolhanenko.repobrowser.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import ua.dolhanenko.repobrowser.core.ILogger
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    protected abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    @Inject
    protected lateinit var logger: ILogger

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return requireNotNull(_binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    protected abstract fun initViews()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}