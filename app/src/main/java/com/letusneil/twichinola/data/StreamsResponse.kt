package com.letusneil.twichinola.data

import com.squareup.moshi.Json

data class StreamsResponse(
  val streams: List<Stream>
)

data class Stream(
  @Json(name = "_id") val id: Long,
  val average_fps: Int,
  val broadcast_platform: String,
  val channel: Channel,
  val community_id: String,
  val community_ids: List<Any>,
  val created_at: String,
  val delay: Int,
  val game: String,
  val is_playlist: Boolean,
  val preview: Preview,
  val stream_type: String,
  val video_height: Int,
  val viewers: Int
)

data class Channel(
  @Json(name = "_id") val id: Long,
  val broadcaster_language: String,
  val broadcaster_software: String,
  val broadcaster_type: String,
  val created_at: String,
  val description: String,
  val display_name: String,
  val followers: Int,
  val game: String,
  val language: String,
  val logo: String,
  val mature: Boolean,
  val name: String,
  val partner: Boolean,
  val privacy_options_enabled: Boolean,
  val private_video: Boolean,
  val profile_banner: String,
  val profile_banner_background_color: String,
  val status: String,
  val updated_at: String,
  val url: String,
  val video_banner: String,
  val views: Int
)

data class Preview(
  val large: String,
  val medium: String,
  val small: String,
  val template: String
)