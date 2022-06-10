package ua.dolhanenko.repobrowser

import android.app.Application


class RepoApp : Application() {
    companion object {
        var activeToken: String? = null
    }

}