package com.letusneil.twichinola

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import com.facebook.stetho.Stetho
import com.letusneil.twichinola.di.Twichinola
import timber.log.Timber
import timber.log.Timber.DebugTree

class TwichinolaApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    Twichinola.initDependencies(this)

    if (BuildConfig.DEBUG) {
      Timber.plant(DebugTree())
    }

    if (BuildConfig.DEBUG) {
      Stetho.initializeWithDefaults(this)
      StrictMode.setThreadPolicy(
        StrictMode.ThreadPolicy.Builder()
          .detectAll()
          .build()
      )
      StrictMode.setVmPolicy(
        VmPolicy.Builder()
          .detectLeakedSqlLiteObjects()
          .penaltyLog()
          .penaltyDeath()
          .build()
      )
    }
  }
}