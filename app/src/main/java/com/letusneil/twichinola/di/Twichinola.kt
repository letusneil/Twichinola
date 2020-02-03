package com.letusneil.twichinola.di

import android.app.Application

object Twichinola {

  lateinit var appComponent: AppComponent

  fun initDependencies(app: Application) {
    appComponent = DaggerAppComponent.factory().create(app)
  }
}