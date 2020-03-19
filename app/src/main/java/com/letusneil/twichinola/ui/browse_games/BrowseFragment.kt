package com.letusneil.twichinola.ui.browse_games

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.letusneil.twichinola.R
import com.letusneil.twichinola.data.Top
import com.letusneil.twichinola.databinding.BrowseFragmentBinding
import com.letusneil.twichinola.di.Twichinola
import com.letusneil.twichinola.util.autoCleared
import timber.log.Timber
import javax.inject.Inject

class BrowseFragment : Fragment(R.layout.browse_fragment) {

  @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

  private val viewModel: BrowseGamesViewModel by viewModels { viewModelFactory }

  private var binding by autoCleared<BrowseFragmentBinding>()

  override fun onAttach(context: Context) {
    Twichinola.dependencyInjector().inject(this)
    super.onAttach(context)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    binding = BrowseFragmentBinding.bind(view)
    initGames()
  }

  private fun initGames() {
    binding.topGamesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val lastPosition = layoutManager.findLastVisibleItemPosition()
        if (lastPosition == viewModel.topGames.size - 1) {
          viewModel.loadGames()
        }
      }
    })

    viewModel.viewEvent.observe(viewLifecycleOwner, Observer {
      when (it) {
        is BrowseGamesViewModel.BrowseGamesUIState.Successful -> setTopGames(it.topGames)
        is BrowseGamesViewModel.BrowseGamesUIState.Loading -> Timber.d("Loading")
        is BrowseGamesViewModel.BrowseGamesUIState.Error -> Timber.d("Error")
      }
    })
    viewModel.loadGames()
  }

  private fun setTopGames(topGames: List<Top>) {
    binding.topGamesList.withModels {
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