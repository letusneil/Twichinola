package com.letusneil.twichinola.ui.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.letusneil.twichinola.api.TwitchApi
import com.letusneil.twichinola.data.Quality
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class PlayerViewModel @Inject constructor(
  private val twitchApi: TwitchApi
) : ViewModel() {

  private val disposables = CompositeDisposable()

  val viewEvent: LiveData<HashMap<String, Quality>> get() = _viewEvent
  private val _viewEvent = MutableLiveData<HashMap<String, Quality>>()

  fun getStreamUrlAndQualityMap(channelName: String) {
    disposables.add(
      twitchApi.channelsAccessToken(channelName)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          Timber.d("Access token ${it.token}")
          getStreamFilesPerQuality(channelName, it.token, it.sig)
        }, {

        })
    )
  }

  fun getStreamFilesPerQuality(channelName: String, token: String, sig: String) {
    disposables.add(
      twitchApi.streamFilesAndQuality(
        name = channelName,
        sig = sig,
        token = token
      )
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          Timber.d("Stream files token $it")
        }, {

        })
    )
  }

  override fun onCleared() {
    disposables.clear()
    super.onCleared()
  }
}