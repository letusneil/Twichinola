package com.letusneil.twichinola.ui.browse_games

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.letusneil.twichinola.data.Top
import com.letusneil.twichinola.api.TwitchApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BrowseGamesViewModel @Inject constructor(
  private val twitchApi: TwitchApi
) : ViewModel() {

  private val disposables = CompositeDisposable()

  val viewEvent: LiveData<BrowseGamesUIState> get() = _viewEvent
  private val _viewEvent = MutableLiveData<BrowseGamesUIState>()

  val topGames: List<Top> get() = _topGames
  private val _topGames = mutableListOf<Top>()

  private var loadingNextPage = false

  fun loadGames() {
    if (loadingNextPage) return

    loadingNextPage = true
    _viewEvent.value = BrowseGamesUIState.Loading
    disposables.add(
      twitchApi.topGames(10, topGames.size)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ topgames ->
          _topGames.addAll(topgames.top)
          _viewEvent.value = BrowseGamesUIState.Successful(_topGames)
          loadingNextPage = false
        }, {
          _viewEvent.value = BrowseGamesUIState.Error
          loadingNextPage = false
        })
    )
  }

  override fun onCleared() {
    disposables.clear()
  }

  sealed class BrowseGamesUIState {
    object Error : BrowseGamesUIState()
    object Loading : BrowseGamesUIState()
    data class Successful(val topGames: List<Top>) : BrowseGamesUIState()
  }
}