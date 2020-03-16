package com.letusneil.twichinola.ui.player

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.letusneil.twichinola.R
import com.letusneil.twichinola.api.TwitchApi
import com.letusneil.twichinola.data.LiveStreamQuality
import com.letusneil.twichinola.data.Quality
import com.letusneil.twichinola.util.QUALITY_AUTO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.regex.Pattern
import javax.inject.Inject

class PlayerViewModel @Inject constructor(
  private val context: Context,
  private val twitchApi: TwitchApi
) : ViewModel() {

  private val disposables = CompositeDisposable()

  val viewEvent: LiveData<List<LiveStreamQuality>> get() = _viewEvent
  private val _viewEvent = MutableLiveData<List<LiveStreamQuality>>()

  private var autoQualityUrl = ""

  fun getStreamUrlAndQualityMap(channelName: String) {
    disposables.add(
      twitchApi.channelsAccessToken(channelName)
        .flatMap { accessToken ->
          autoQualityUrl = context.getString(
            R.string.quality_auto_base_url,
            channelName,
            accessToken.token,
            accessToken.sig,
            System.currentTimeMillis().toString()
          )

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

  private fun getStreamQualityUrlMap(
    nakedUrl: String
  ): List<LiveStreamQuality> {

    val results = mutableListOf<LiveStreamQuality>()
    results.add(
      LiveStreamQuality(
        name = QUALITY_AUTO,
        quality = Quality(
          name = "AUTO",
          url = autoQualityUrl
        )
      )
    )

    val p = Pattern.compile("GROUP-ID=\"(.+)\",NAME=\"(.+)\".+\\n.+\\n(http://\\S+)")
    val m = p.matcher(nakedUrl)
    while (m.find()) {
      results.add(
        LiveStreamQuality(
          name = m.group(1) ?: "",
          quality = Quality(
            name = m.group(2) ?: "",
            url = m.group(3) ?: ""
          )
        )
      )
    }
    return results
  }

  override fun onCleared() {
    disposables.clear()
  }
}