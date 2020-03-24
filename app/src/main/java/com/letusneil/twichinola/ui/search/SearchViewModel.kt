package com.letusneil.twichinola.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.letusneil.twichinola.api.TwitchApi
import com.letusneil.twichinola.data.Game
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SearchViewModel @Inject constructor(
  private val twitchApi: TwitchApi
): ViewModel() {

  private val disposables = CompositeDisposable()

  val searchResults: LiveData<SearchResult> get() = _searchResults
  private val _searchResults = MutableLiveData<SearchResult>()

  fun search(key: String) {
    disposables.add(
      twitchApi.searchGames(key)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          _searchResults.value = SearchResult(it)
        }, {
          _searchResults.value = null
        })
    )
  }

  override fun onCleared() {
    disposables.clear()
  }
}
data class SearchResult(val games: List<Game>)