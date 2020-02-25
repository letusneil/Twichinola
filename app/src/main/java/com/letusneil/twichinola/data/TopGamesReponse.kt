package com.letusneil.twichinola.data

import com.squareup.moshi.Json

data class TopGamesReponse(
  @Json(name = "_total") val total: Int,
  val top: List<Top>
)

data class Top(
  val channels: Int,
  val game: Game,
  val viewers: Int
)

data class Box(
  val large: String,
  val medium: String,
  val small: String,
  val template: String
)

data class Logo(
  val large: String,
  val medium: String,
  val small: String,
  val template: String
)