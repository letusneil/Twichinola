package com.letusneil.twichinola.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.letusneil.twichinola.ui.browse_games.BrowseGamesViewModel
import com.letusneil.twichinola.ui.game_streams.GameStreamViewModel
import com.letusneil.twichinola.ui.player.PlayerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
  @Binds
  @Suppress("unused")
  internal abstract fun viewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

  @Binds
  @IntoMap
  @ViewModelKey(BrowseGamesViewModel::class)
  @Suppress("unused")
  internal abstract fun browseViewModel(viewModel: BrowseGamesViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(GameStreamViewModel::class)
  @Suppress("unused")
  internal abstract fun gameStreamViewModel(viewModel: GameStreamViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(PlayerViewModel::class)
  @Suppress("unused")
  internal abstract fun playerViewModel(viewModel: PlayerViewModel): ViewModel
}