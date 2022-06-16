package ua.dolhanenko.repobrowser.presentation.base

import androidx.lifecycle.ViewModel
import ua.dolhanenko.repobrowser.utils.log.ILogger
import javax.inject.Inject


abstract class BaseVM : ViewModel() {
    @Inject
    protected lateinit var logger: ILogger
}