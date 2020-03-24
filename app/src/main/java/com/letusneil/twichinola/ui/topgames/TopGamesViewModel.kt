package com.letusneil.twichinola.ui.topgames

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.letusneil.twichinola.data.Top
import com.letusneil.twichinola.api.TwitchApi
import com.letusneil.twichinola.util.LIMIT
import com.letusneil.twichinola.util.offset
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TopGamesViewModel @Inject constructor(
  private val twitchApi: TwitchApi
) : ViewModel() {

  private val disposables = CompositeDisposable()

  val viewEvent: LiveData<BrowseGamesUIState> get() = _viewEvent
  private val _viewEvent = MutableLiveData<BrowseGamesUIState>()

  val topGames: List<Top> get() = _topGames
  private val _topGames = mutableListOf<Top>()

  fun loadGames() {
    if (_viewEvent.value == BrowseGamesUIState.Loading) return

    _viewEvent.value = BrowseGamesUIState.Loading
    disposables.add(
      twitchApi.topGames(LIMIT, _topGames.offset())
        .map { it.top }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ result ->
          _topGames.addAll(result)
          _viewEvent.value = BrowseGamesUIState.Successful(_topGames)
        }, {
          _viewEvent.value = BrowseGamesUIState.Error
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