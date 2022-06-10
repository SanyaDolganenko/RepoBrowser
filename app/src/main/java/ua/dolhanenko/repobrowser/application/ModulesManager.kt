package ua.dolhanenko.repobrowser.application

import ua.dolhanenko.repobrowser.data.remote.ApiFactory
import ua.dolhanenko.repobrowser.data.remote.GithubDataSource
import ua.dolhanenko.repobrowser.domain.interfaces.IGithubDataSource


class ModulesManager(private val apiFactory: ApiFactory) {
    var githubDataSource: IGithubDataSource = GithubDataSource(apiFactory.githubApi)

}