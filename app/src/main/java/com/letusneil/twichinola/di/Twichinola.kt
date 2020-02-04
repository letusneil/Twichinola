package com.letusneil.twichinola.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider

object Twichinola {

  lateinit var appComponent: AppComponent

  fun initDependencies(app: Application) {
    appComponent = DaggerAppComponent.factory().create(app)
  }

  fun viewModelFactory(): ViewModelProvider.Factory {
    return appComponent.viewModelFactory
  }
}