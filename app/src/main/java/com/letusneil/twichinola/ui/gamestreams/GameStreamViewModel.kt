package com.letusneil.twichinola.ui.gamestreams

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.letusneil.twichinola.api.TwitchApi
import com.letusneil.twichinola.data.Stream
import com.letusneil.twichinola.util.LIMIT
import com.letusneil.twichinola.util.offset
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

  val streams: List<Stream> get() = _streams
  private val _streams = mutableListOf<Stream>()

  fun getGameStreams(name: String) {
    if (_viewEvent.value == GameStreamsUIState.Loading) return

    disposables.add(
      twitchApi.streams(name, LIMIT, _streams.offset())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ data ->
          _streams.addAll(data.streams)
          _viewEvent.value = GameStreamsUIState.Successful(_streams)
        }, {
          _viewEvent.value = GameStreamsUIState.Error
        })
    )
  }

  override fun onCleared() {
    disposables.clear()
  }
}

sealed class GameStreamsUIState {
  object Error : GameStreamsUIState()
  object Loading : GameStreamsUIState()
  data class Successful(val streams: List<Stream>) : GameStreamsUIState()
}