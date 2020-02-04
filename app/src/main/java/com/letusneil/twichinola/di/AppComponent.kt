package com.letusneil.twichinola.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.letusneil.twichinola.ui.browse_games.MainActivity
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Component(modules = [AppModule::class, ViewModelModule::class])
@Singleton
interface AppComponent {

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance applicationContext: Context): AppComponent
  }

  val viewModelFactory: ViewModelProvider.Factory
}