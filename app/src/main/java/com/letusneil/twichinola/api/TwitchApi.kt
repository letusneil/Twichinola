package com.letusneil.twichinola.api

import com.letusneil.twichinola.BuildConfig
import com.letusneil.twichinola.data.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface TwitchApi {

  @GET("kraken/games/top")
  @Headers(value = [HEADER_TWITCH_API_VERSION, HEADER_PUBLIC_CLIENT_ID])
  fun topGames(
    @Query("limit") limit: Int,
    @Query("offset") offset: Int
  ): Single<TopGamesReponse>

  @GET("kraken/streams")
  @Headers(value = [HEADER_TWITCH_API_VERSION, HEADER_PUBLIC_CLIENT_ID])
  fun streams(
    @Query("game") game: String,
    @Query("limit") limit: Int,
    @Query("offset") offset: Int
  ): Single<StreamsResponse>

  @GET("https://api.twitch.tv/api/channels/{channelName}/access_token")
  @Headers(value = [HEADER_TWITCH_API_VERSION, HEADER_PRIVATE_CLIENT_ID])
  fun channelsAccessToken(
    @Path("channelName") name: String
  ): Single<AccessToken>

  @GET("http://usher.twitch.tv/api/channel/hls/{channelName}.m3u8")
  fun streamFilesAndQuality(
    @Path("channelName") name: String,
    @Query("player") player: String = "twitchweb",
    @Query("token") token: String,
    @Query("sig") sig: String,
    @Query("allow_audio_only") audioOnly: Boolean = true,
    @Query("allow_source") source: Boolean = true,
    @Query("type") type: String = "any",
    @Query("p") p: Long = System.currentTimeMillis()
  ): Single<String>

  @GET("kraken/search/games")
  @Headers(value = [HEADER_TWITCH_API_VERSION, HEADER_PUBLIC_CLIENT_ID])
  fun searchGames(@Query("query") queryString: String): Single<SearchResult>

  companion object {
    const val HEADER_TWITCH_API_VERSION = "Accept: application/vnd.twitchtv.v5+json"
    const val HEADER_PUBLIC_CLIENT_ID = "Client-ID: ${BuildConfig.TWICHINOLA_CLIENT_ID}"
    const val HEADER_PRIVATE_CLIENT_ID = "Client-ID: ${BuildConfig.TWITCH_CLIENT_ID}"
  }
}