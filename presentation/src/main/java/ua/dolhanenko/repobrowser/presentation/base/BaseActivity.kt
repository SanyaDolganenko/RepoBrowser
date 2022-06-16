package ua.dolhanenko.repobrowser.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import ua.dolhanenko.repobrowser.core.ILogger
import javax.inject.Inject

internal abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    @Inject
    protected lateinit var logger: ILogger

    private var _binding: VB? = null
    abstract val bindingInflater: (LayoutInflater) -> VB

    protected val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingInflater.invoke(layoutInflater)
        setContentView(requireNotNull(_binding).root)
        initViews()
    }

    protected abstract fun initViews()
}