package com.letusneil.twichinola.ui.game_streams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import com.airbnb.epoxy.EpoxyRecyclerView
import com.letusneil.twichinola.R
import com.letusneil.twichinola.data.Game
import com.letusneil.twichinola.di.Twichinola
import com.letusneil.twichinola.ui.browse_games.BrowseGamesViewModel

class GameStreamFragment : Fragment() {

  @BindView(R.id.streams_list) lateinit var streamsRecyclerView: EpoxyRecyclerView

  private lateinit var gameStreamViewModel: GameStreamViewModel

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_game, container, false)
    ButterKnife.bind(this, view)
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    gameStreamViewModel =
      ViewModelProviders.of(this, Twichinola.viewModelFactory())[GameStreamViewModel::class.java]

  }
}