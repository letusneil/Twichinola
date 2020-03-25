package com.letusneil.twichinola.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Game(
  @Json(name = "_id") val id: Int,
  val box: Box,
  @Json(name = "giantbomb_id") val giantbombId: Int,
  val locale: String,
  val localized_name: String,
  val logo: Logo,
  val name: String,
  val popularity: Int
) : Parcelable

@Parcelize
data class GameStream(
  val channelName: String,
  val game: String,
  val streamer: String,
  val previewImageUrl: String,
  val streamerImageUrl: String,
  val description: String,
  val language: String,
  val streamType: String,
  val viewersCount: Int
) : Parcelable