package com.letusneil.twichinola.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.letusneil.twichinola.ui.browse_games.BrowseGamesViewModel
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
  internal abstract fun mainViewModel(viewModel: BrowseGamesViewModel): ViewModel
}