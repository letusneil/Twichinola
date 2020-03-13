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
import java.util.regex.Pattern
import javax.inject.Inject

class PlayerViewModel @Inject constructor(
  private val twitchApi: TwitchApi
) : ViewModel() {

  private val disposables = CompositeDisposable()

  val viewEvent: LiveData<LinkedHashMap<String, Quality>> get() = _viewEvent
  private val _viewEvent = MutableLiveData<LinkedHashMap<String, Quality>>()

  fun getStreamUrlAndQualityMap(channelName: String) {
    disposables.add(
      twitchApi.channelsAccessToken(channelName)
        .flatMap { accessToken ->
          twitchApi.streamFilesAndQuality(
            name = channelName,
            sig = accessToken.sig,
            token = accessToken.token
          )
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          _viewEvent.value = getStreamQualityUrlMap(it)
        }, {
          Timber.e(it)
        })
    )
  }

  private fun getStreamQualityUrlMap(nakedUrl: String): LinkedHashMap<String, Quality> {
    val results = linkedMapOf<String, Quality>()
    val p = Pattern.compile("GROUP-ID=\"(.+)\",NAME=\"(.+)\".+\\n.+\\n(http://\\S+)")
    val m = p.matcher(nakedUrl)
    while (m.find()) {
      results[m.group(1)] = Quality(m.group(2), m.group(3))
    }
    return results
  }

  override fun onCleared() {
    disposables.clear()
  }
}