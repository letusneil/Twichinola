package com.letusneil.twichinola.ui.game_streams

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import butterknife.BindView
import butterknife.ButterKnife
import com.airbnb.epoxy.EpoxyRecyclerView
import com.letusneil.twichinola.R
import com.letusneil.twichinola.data.Stream
import com.letusneil.twichinola.di.Twichinola
import timber.log.Timber
import javax.inject.Inject

class GameStreamFragment : Fragment() {

  @BindView(R.id.streams_list) lateinit var streamsRecyclerView: EpoxyRecyclerView

  @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

  private val viewModel: GameStreamViewModel by viewModels { viewModelFactory }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.game_streams_fragment, container, false)
    ButterKnife.bind(this, view)
    return view
  }

  override fun onAttach(context: Context) {
    Twichinola.dependencyInjector().inject(this)
    super.onAttach(context)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    viewModel.viewEvent.observe(viewLifecycleOwner, Observer {
      when (it) {
        is GameStreamsUIState.Successful -> setupGameStreamsRecyclerView(it.streams)
        is GameStreamsUIState.Loading -> Timber.d("Loading")
        is GameStreamsUIState.Error -> Timber.d("Error")
      }
    })

    val gameName = arguments?.getString("gameName") ?: ""
    viewModel.getGameStreams(gameName)
  }

  private fun setupGameStreamsRecyclerView(streams: List<Stream>) {
    streamsRecyclerView.withModels {
      streams.forEach { stream ->
        gameStreamsEpoxyHolder {
          id("top game ${stream.id}")
          gameName(stream.game)
          streamerName(stream.channel.display_name)
          streamPreviewImageUrl(stream.preview.template)
          streamerImageUrl(stream.channel.logo)
          streamDescription(stream.channel.status)
          streamType(stream.stream_type)
          streamLanguage(stream.channel.broadcaster_language)
          viewersCount(stream.viewers)
          listener {
            findNavController().navigate(GameStreamFragmentDirections.toPlayerFragment(stream.channel.name))
          }
        }
      }
    }
  }
}