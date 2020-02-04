package com.letusneil.twichinola.ui.browse_games

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.letusneil.twichinola.R
import com.letusneil.twichinola.di.Twichinola

class MainActivity : AppCompatActivity() {

  private lateinit var browseGamesViewModel: BrowseGamesViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    browseGamesViewModel =
      ViewModelProviders.of(this, Twichinola.viewModelFactory())[BrowseGamesViewModel::class.java]
  }

  private fun observeTopGames() {
  }
}
