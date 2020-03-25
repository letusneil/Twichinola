package com.letusneil.twichinola.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

data class TopGamesReponse(
  @Json(name = "_total") val total: Int,
  val top: List<Top>
)

data class Top(
  val channels: Int,
  val game: Game,
  val viewers: Int
)

@Parcelize
data class Box(
  val large: String,
  val medium: String,
  val small: String,
  val template: String
) : Parcelable

@Parcelize
data class Logo(
  val large: String,
  val medium: String,
  val small: String,
  val template: String
) : Parcelable

data class SearchResult(
  val games: List<Game>
)