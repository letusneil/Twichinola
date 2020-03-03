package com.letusneil.twichinola.ui.game_streams

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.letusneil.twichinola.api.TwitchApi
import com.letusneil.twichinola.data.Stream
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GameStreamViewModel @Inject constructor(
  private val twitchApi: TwitchApi
) : ViewModel() {

  private val disposables = CompositeDisposable()

  val viewEvent: LiveData<GameStreamsUIState> get() = _viewEvent
  private val _viewEvent = MutableLiveData<GameStreamsUIState>()

  fun getGameStreams(name: String) {
    disposables.add(
      twitchApi.streams(name)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ data ->
          _viewEvent.value = GameStreamsUIState.Successful(data.streams)
        }, {
          _viewEvent.value = GameStreamsUIState.Error
        })
    )
  }

  override fun onCleared() {
    if (!disposables.isDisposed) {
      disposables.dispose()
    }
  }
}

sealed class GameStreamsUIState {
  object Error : GameStreamsUIState()
  object Loading : GameStreamsUIState()
  data class Successful(val streams: List<Stream>) : GameStreamsUIState()
}