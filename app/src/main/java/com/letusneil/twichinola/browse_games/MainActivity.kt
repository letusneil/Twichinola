package com.letusneil.twichinola.browse_games

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.letusneil.twichinola.BuildConfig
import com.letusneil.twichinola.R
import com.letusneil.twichinola.di.Twichinola
import com.letusneil.twichinola.di.TwitchApi
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  @Inject
  lateinit var twitchApi: TwitchApi

  override fun onCreate(savedInstanceState: Bundle?) {
    Twichinola.appComponent.inject(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }
}
