package com.letusneil.twichinola.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.letusneil.twichinola.ui.browse_games.BrowseFragment
import com.letusneil.twichinola.ui.game_streams.GameStreamFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, ViewModelModule::class])
@Singleton
interface AppComponent {

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance applicationContext: Context): AppComponent
  }

  fun inject(browseFragment: BrowseFragment)

  fun inject(gameStreamFragment: GameStreamFragment)

  val viewModelFactory: ViewModelProvider.Factory
}