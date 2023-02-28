package com.rodionovmax.onedron

import android.app.Application
import com.rodionovmax.onedron.repo.Repo
import com.rodionovmax.onedron.repo.RepoImpl
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BaseApp : Application() {
    @Inject lateinit var repo: Repo
    private lateinit var appInstance: BaseApp

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        appInstance = this
    }
}