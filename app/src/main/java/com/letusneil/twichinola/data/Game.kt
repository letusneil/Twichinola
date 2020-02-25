package com.letusneil.twichinola.data

import com.squareup.moshi.Json

data class Game(
  @Json(name = "_id") val id: Int,
  val box: Box,
  @Json(name = "giantbomb_id") val giantbombId: Int,
  val locale: String,
  val localized_name: String,
  val logo: Logo,
  val name: String,
  val popularity: Int
)