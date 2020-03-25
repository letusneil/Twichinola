package com.letusneil.twichinola.ui.search

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

@EpoxyModelClass(layout = R.layout.item_game_search)
abstract class SearchEpoxyHolder : EpoxyModelWithHolder<SearchEpoxyHolder.Holder>() {

  @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash) lateinit var listener: () -> Unit

  @EpoxyAttribute lateinit var gameName: String
  @EpoxyAttribute lateinit var imageUrl: String

  override fun bind(holder: Holder) {
    holder.titleView.text = gameName
    Glide.with(holder.posterView.context)
      .load(imageUrl)
      .transition(DrawableTransitionOptions.withCrossFade())
      .into(holder.posterView)
    holder.rootView.setOnClickListener { listener.invoke() }
  }

  class Holder : BaseEpoxyHolder() {
    val titleView by bind<TextView>(R.id.game_name)
    val posterView by bind<ImageView>(R.id.game_item_poster)
    val rootView by bind<View>(R.id.rootView)
  }
}
