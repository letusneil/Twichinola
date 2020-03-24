package com.letusneil.twichinola.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.letusneil.twichinola.ui.topgames.TopGamesViewModel
import com.letusneil.twichinola.ui.gamestreams.GameStreamViewModel
import com.letusneil.twichinola.ui.player.PlayerViewModel
import com.letusneil.twichinola.ui.search.SearchViewModel
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
  @ViewModelKey(TopGamesViewModel::class)
  @Suppress("unused")
  internal abstract fun browseViewModel(viewModel: TopGamesViewModel): ViewModel

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

  @Binds
  @IntoMap
  @ViewModelKey(SearchViewModel::class)
  @Suppress("unused")
  internal abstract fun searchViewModel(viewModel: SearchViewModel): ViewModel
}