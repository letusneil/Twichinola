package com.letusneil.twichinola.ui.browse_games

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.letusneil.twichinola.R
import com.letusneil.twichinola.util.BaseEpoxyHolder
import java.text.NumberFormat

@EpoxyModelClass(layout = R.layout.item_top_game)
abstract class TopGameEpoxyHolder : EpoxyModelWithHolder<TopGameEpoxyHolder.Holder>() {

  @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) lateinit var listener: () -> Unit

  @EpoxyAttribute lateinit var title: String
  @EpoxyAttribute lateinit var imageUrl: String
  @EpoxyAttribute open var viewersCount: Int = 0

  override fun bind(holder: Holder) {
    holder.titleView.text = title
    holder.viewersCountView.text = NumberFormat.getIntegerInstance().format(viewersCount)
    Glide.with(holder.posterView.context)
      .load(imageUrl)
      .transition(DrawableTransitionOptions.withCrossFade())
      .into(holder.posterView)
    holder.parentView.setOnClickListener { listener.invoke() }
  }

  class Holder : BaseEpoxyHolder() {
    val parentView by bind<View>(R.id.item_view)
    val titleView by bind<TextView>(R.id.game_item_title)
    val posterView by bind<ImageView>(R.id.game_item_poster)
    val viewersCountView by bind<TextView>(R.id.game_viewers_count)
  }
}

