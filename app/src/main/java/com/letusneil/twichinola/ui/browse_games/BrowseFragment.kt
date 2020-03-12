package com.letusneil.twichinola.ui.browse_games

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import butterknife.BindView
import butterknife.ButterKnife
import com.airbnb.epoxy.EpoxyRecyclerView
import com.letusneil.twichinola.R
import com.letusneil.twichinola.data.Top
import com.letusneil.twichinola.di.Twichinola
import timber.log.Timber
import javax.inject.Inject

class BrowseFragment : Fragment() {

  @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

  private val viewModel: BrowseGamesViewModel by viewModels { viewModelFactory }

  @BindView(R.id.top_games_list) lateinit var topGamesRecyclerView: EpoxyRecyclerView

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.browse_fragment, container, false)
    ButterKnife.bind(this, view)
    return view
  }

  override fun onAttach(context: Context) {
    Twichinola.dependencyInjector().inject(this)
    super.onAttach(context)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
            Timber.d("Clicked game ${topGame.game.name}")
            findNavController().navigate(BrowseFragmentDirections.toGameFragment(topGame.game.name))
          }
        }
      }
    }
  }

}