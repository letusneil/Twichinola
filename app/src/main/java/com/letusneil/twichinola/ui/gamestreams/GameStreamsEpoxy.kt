package com.letusneil.twichinola.ui.gamestreams

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.letusneil.twichinola.R
import com.letusneil.twichinola.data.GameStream
import com.letusneil.twichinola.util.BaseEpoxyHolder
import com.letusneil.twichinola.util.TEMPLATE_IMAGE_HEIGHT
import com.letusneil.twichinola.util.TEMPLATE_IMAGE_WIDTH
import java.text.NumberFormat
import java.util.*

@EpoxyModelClass(layout = R.layout.item_game_stream)
abstract class GameStreamsEpoxyHolder : EpoxyModelWithHolder<GameStreamsEpoxyHolder.Holder>() {

  @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
  lateinit var listener: (GameStream, FragmentNavigator.Extras) -> Unit

  @EpoxyAttribute lateinit var channelName: String
  @EpoxyAttribute lateinit var streamPreviewImageUrl: String
  @EpoxyAttribute lateinit var streamerImageUrl: String
  @EpoxyAttribute lateinit var gameName: String
  @EpoxyAttribute lateinit var streamerName: String
  @EpoxyAttribute lateinit var streamDescription: String
  @EpoxyAttribute open var viewersCount: Int = 0
  @EpoxyAttribute lateinit var streamType: String
  @EpoxyAttribute lateinit var streamLanguage: String

  override fun bind(holder: Holder) {
    holder.apply {
      val context = gameStreamView.context
      val screenWidth = context.resources.displayMetrics.widthPixels
      val streamPreviewUrl = streamPreviewImageUrl
        .replace(TEMPLATE_IMAGE_WIDTH, screenWidth.toString(), true)
        .replace(TEMPLATE_IMAGE_HEIGHT, ((screenWidth / 16) * 9).toString(), true)
      Glide.with(context)
        .load(streamPreviewUrl)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(streamPreview)
      streamPreview.transitionName = streamPreviewUrl

      Glide.with(context)
        .load(streamerImageUrl)
        .apply(
          RequestOptions
            .centerCropTransform()
            .circleCrop()
        )
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(streamerPicView)
      gameNameView.text = gameName
      streamerNameView.text = streamerName
      if (streamDescription.isNotEmpty()) {
        descriptionView.text = streamDescription
      } else {
        descriptionView.visibility = View.GONE
      }
      viewerCountView.text =
        context.getString(R.string.watching, NumberFormat.getIntegerInstance().format(viewersCount))
      streamTypeIndicatorView.text = streamType.toUpperCase(Locale.ENGLISH)
      streamLanguageIndicatorView.text = streamLanguage.toUpperCase(Locale.ENGLISH)

      gameStreamView.setOnClickListener {
        listener.invoke(
          GameStream(
            channelName = channelName,
            game = gameName,
            streamer = streamerName,
            previewImageUrl = streamPreviewUrl,
            streamerImageUrl = streamerImageUrl,
            description = streamDescription,
            streamType = streamType.toUpperCase(Locale.ENGLISH),
            language = streamLanguage.toUpperCase(Locale.ENGLISH),
            viewersCount = viewersCount
          ),
          FragmentNavigatorExtras(
            streamPreview to streamPreviewUrl
          )
        )
      }
    }
  }

  class Holder : BaseEpoxyHolder() {
    val gameStreamView by bind<View>(R.id.game_stream_view)
    val streamPreview by bind<ImageView>(R.id.game_stream_preview)
    val streamerPicView by bind<ImageView>(R.id.game_streamer_pic)
    val gameNameView by bind<TextView>(R.id.game_name)
    val streamerNameView by bind<TextView>(R.id.game_streamer_name)
    val descriptionView by bind<TextView>(R.id.game_stream_description)
    val viewerCountView by bind<TextView>(R.id.game_stream_viewer_count)
    val streamTypeIndicatorView by bind<TextView>(R.id.game_stream_type_indicator)
    val streamLanguageIndicatorView by bind<TextView>(R.id.game_stream_language)
  }
}

