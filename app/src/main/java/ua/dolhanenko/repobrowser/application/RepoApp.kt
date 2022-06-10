package ua.dolhanenko.repobrowser.application

import android.app.Application
import ua.dolhanenko.repobrowser.data.remote.ApiFactory
import ua.dolhanenko.repobrowser.view.common.ModuledViewModelFactory


class RepoApp : Application() {
    companion object {
        var activeToken: String? = null
        lateinit var vmFactory: ModuledViewModelFactory
        private lateinit var apiFactory: ApiFactory
        private lateinit var modulesManager: ModulesManager
    }

    override fun onCreate() {
        super.onCreate()
        apiFactory = ApiFactory()
        modulesManager = ModulesManager(apiFactory)
        vmFactory = ModuledViewModelFactory(modulesManager)
    }
}