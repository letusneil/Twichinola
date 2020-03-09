package com.letusneil.twichinola.di

import android.app.Application

object Twichinola {

  private lateinit var appComponent: AppComponent

  fun initDependencies(app: Application) {
    appComponent = DaggerAppComponent.factory().create(app)
  }

  fun dependencyInjector(): AppComponent {
    return appComponent
  }
}