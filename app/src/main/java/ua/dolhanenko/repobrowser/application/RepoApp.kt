package ua.dolhanenko.repobrowser.application

import android.app.Application
import ua.dolhanenko.repobrowser.data.remote.ApiFactory
import ua.dolhanenko.repobrowser.view.common.ModuledViewModelFactory


class RepoApp : Application() {
    companion object {
        var activeToken: String? = null
        lateinit var vmFactory: ModuledViewModelFactory
    }

    override fun onCreate() {
        super.onCreate()
        val apiFactory = ApiFactory()
        val modulesManager = ModulesManager(applicationContext, apiFactory)
        vmFactory = ModuledViewModelFactory(modulesManager)
    }
}