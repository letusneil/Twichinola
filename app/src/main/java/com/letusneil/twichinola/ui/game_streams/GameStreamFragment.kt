package com.letusneil.twichinola.ui.game_streams

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.letusneil.twichinola.R
import com.letusneil.twichinola.data.Stream
import com.letusneil.twichinola.databinding.GameStreamsFragmentBinding
import com.letusneil.twichinola.di.Twichinola
import timber.log.Timber
import javax.inject.Inject

class GameStreamFragment : Fragment(R.layout.game_streams_fragment) {

  @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

  private lateinit var binding: GameStreamsFragmentBinding

  private val viewModel: GameStreamViewModel by viewModels { viewModelFactory }

  override fun onAttach(context: Context) {
    Twichinola.dependencyInjector().inject(this)
    super.onAttach(context)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    binding = GameStreamsFragmentBinding.bind(view)
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
    binding.streamsList.withModels {
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