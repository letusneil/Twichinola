package com.letusneil.twichinola.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.letusneil.twichinola.ui.topgames.TopGamesFragment
import com.letusneil.twichinola.ui.gamestreams.GameStreamFragment
import com.letusneil.twichinola.ui.player.PlayerFragment
import com.letusneil.twichinola.ui.player.QualityOptionsBottomSheetDialogFragment
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

  fun inject(target: TopGamesFragment)

  fun inject(target: GameStreamFragment)

  fun inject(target: PlayerFragment)

  fun inject(target: QualityOptionsBottomSheetDialogFragment)

  val viewModelFactory: ViewModelProvider.Factory
}