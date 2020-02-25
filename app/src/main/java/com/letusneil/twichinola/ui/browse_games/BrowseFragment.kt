package com.letusneil.twichinola.ui.browse_games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import com.airbnb.epoxy.EpoxyRecyclerView
import com.letusneil.twichinola.R
import com.letusneil.twichinola.data.Top
import com.letusneil.twichinola.di.Twichinola
import timber.log.Timber

class BrowseFragment : Fragment() {

  private lateinit var viewModel: BrowseGamesViewModel

  @BindView(R.id.top_games_list) lateinit var topGamesRecyclerView: EpoxyRecyclerView

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_browse, container, false)
    ButterKnife.bind(this, view)
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    viewModel =
      ViewModelProviders.of(this, Twichinola.viewModelFactory())[BrowseGamesViewModel::class.java]

    initGames()
  }

  private fun initGames() {
    viewModel.viewEvent.observe(viewLifecycleOwner, Observer {
      when (it) {
        is BrowseGamesViewModel.BrowseGamesUIState.Successful -> setupTopGamesRecyclerView(it.topGames)
        is BrowseGamesViewModel.BrowseGamesUIState.Loading -> Timber.d("Loading")
        is BrowseGamesViewModel.BrowseGamesUIState.Error -> Timber.d("Error")
      }
    })

    viewModel.loadGames()
  }

  private fun setupTopGamesRecyclerView(topGames: List<Top>) {
    topGamesRecyclerView.withModels {
      topGames.forEach { topGame ->
        topGameEpoxyHolder {
          id("top game $topGame.game.id")
          title(topGame.game.localized_name)
          viewersCount(topGame.viewers)
          imageUrl(topGame.game.box.medium)
          listener {

          }
        }
      }
    }
  }

}