package com.letusneil.twichinola.ui.player

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.letusneil.twichinola.R
import com.letusneil.twichinola.databinding.PlayerFragmentBinding
import com.letusneil.twichinola.di.Twichinola
import com.letusneil.twichinola.util.autoCleared
import javax.inject.Inject

class PlayerFragment : Fragment(R.layout.player_fragment), Player.EventListener {

  @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
  private val viewModel: PlayerViewModel by viewModels { viewModelFactory }

  private var binding by autoCleared<PlayerFragmentBinding>()
  private var exoPlayer by autoCleared<ExoPlayer>()

  private var landscape = false

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    binding = PlayerFragmentBinding.bind(view)

    exoPlayer = SimpleExoPlayer.Builder(requireContext()).build()
    exoPlayer.addListener(this)
    binding.playerView.player = exoPlayer

    binding.fullScreenButton.setOnClickListener { toggleFullScreen() }

    viewModel.viewEvent.observe(viewLifecycleOwner, Observer {
      preparePlayer(it.get("360p30")?.url ?: "")
    })
    val channelName = arguments?.getString("channelName") ?: ""
    viewModel.getStreamUrlAndQualityMap(channelName)
  }

  override fun onAttach(context: Context) {
    Twichinola.dependencyInjector().inject(this)
    super.onAttach(context)
  }

  private fun preparePlayer(url: String) {
    val dataSourceFactory = DefaultDataSourceFactory(requireContext(), getString(R.string.app_name))
    val mediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url))
    exoPlayer.prepare(mediaSource)
    exoPlayer.playWhenReady = true
  }

  override fun onStop() {
    exoPlayer.stop()
    exoPlayer.release()
    super.onStop()
  }

  override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
    super.onPlayerStateChanged(playWhenReady, playbackState)
  }

  override fun onPlayerError(error: ExoPlaybackException) {
    super.onPlayerError(error)
  }

  private fun toggleFullScreen() {
    activity?.run {

      this.requestedOrientation =
        if (landscape) ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        else ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    landscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
    if (landscape) {
      enterFullScreenUi()
    } else {
      exitFullScreenUi()
    }
  }

  private fun enterFullScreenUi() {
    binding.fullScreenButton.setImageDrawable(
      ContextCompat.getDrawable(requireContext(), R.drawable.ic_fullscreen_exit_24px)
    )
  }

  private fun exitFullScreenUi() {
    binding.fullScreenButton.setImageDrawable(
      ContextCompat.getDrawable(requireContext(), R.drawable.ic_fullscreen_24px)
    )
  }
}
