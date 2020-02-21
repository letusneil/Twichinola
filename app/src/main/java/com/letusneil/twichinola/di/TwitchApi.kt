package com.letusneil.twichinola.di

import com.letusneil.twichinola.data.TopGamesReponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface TwitchApi {

  @GET("kraken/games/top")
  fun topGames(
    @Query("limit") limit: Int,
    @Query("offset") offset: Int
  ): Single<TopGamesReponse>

}