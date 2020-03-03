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

  fun loadGames() {
    _viewEvent.value = BrowseGamesUIState.Loading
    disposables.add(
      twitchApi.topGames(10, 0)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ topgames ->
          _viewEvent.value = BrowseGamesUIState.Successful(topGames = topgames.top)
        }, {
          _viewEvent.value = BrowseGamesUIState.Error
        })
    )
  }

  override fun onCleared() {
    if (!disposables.isDisposed) {
      disposables.dispose()
    }
  }

  sealed class BrowseGamesUIState {
    object Error : BrowseGamesUIState()
    object Loading : BrowseGamesUIState()
    data class Successful(val topGames: List<Top>) : BrowseGamesUIState()
  }
}