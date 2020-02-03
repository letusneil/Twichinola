package com.letusneil.twichinola.di

import android.content.Context
import com.letusneil.twichinola.browse_games.MainActivity
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance applicationContext: Context): AppComponent
  }

  fun inject(mainActivity: MainActivity)

  val retrofit: Retrofit
}