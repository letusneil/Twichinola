package com.letusneil.twichinola.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.letusneil.twichinola.BuildConfig
import com.letusneil.twichinola.api.TwitchApi
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
class AppModule {

  @Provides
  @Singleton
  fun providesOkHttpClient(): OkHttpClient {
    val builder = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
      val logging = HttpLoggingInterceptor(
        HttpLoggingInterceptor.Logger { message: String? -> Timber.tag("OkHttp").d(message) }
      )
      logging.level = HttpLoggingInterceptor.Level.BASIC
      builder.addInterceptor(logging)
      builder.addNetworkInterceptor(StethoInterceptor())
    }
    builder.addInterceptor { chain: Interceptor.Chain ->
      val request = chain.request().newBuilder()
        .build()
      chain.proceed(request)
    }
    return builder.build()
  }

  @Provides
  @Singleton
  fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .addConverterFactory(MoshiConverterFactory.create())
      .baseUrl(TWITCH_BASE_URL)
      .client(okHttpClient)
      .build()
  }

  @Provides
  @Singleton
  fun providesTwitchApi(retrofit: Retrofit): TwitchApi {
    return retrofit.create<TwitchApi>(
      TwitchApi::class.java
    )
  }

  companion object {
    private const val TWITCH_BASE_URL = "https://api.twitch.tv/"
  }
}