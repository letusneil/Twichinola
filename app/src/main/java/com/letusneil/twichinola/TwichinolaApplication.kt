package com.letusneil.twichinola

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

class TwichinolaApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) {
      Timber.plant(DebugTree())
    }
  }
}