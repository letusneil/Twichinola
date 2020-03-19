package com.letusneil.twichinola.ui.player

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
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
import timber.log.Timber
import javax.inject.Inject

class PlayerFragment : Fragment(R.layout.player_fragment), Player.EventListener {

  @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
  private val playerViewModel: PlayerViewModel by viewModels { viewModelFactory }

  private var binding by autoCleared<PlayerFragmentBinding>()
  private var qualityOptionsBottomSheet by autoCleared<QualityOptionsBottomSheetDialogFragment>()
  private var exoPlayer by autoCleared<ExoPlayer>()

  private var delayAnimationHandler = Handler()
  private val hideAnimationRunnable = Runnable {
    if (activity != null && isAdded && !isRemoving) {
      hidePlayerOverlay()
    }
  }

  private var landscape = false
  private var fullscreen = false

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    binding = PlayerFragmentBinding.bind(view)

    exoPlayer = SimpleExoPlayer.Builder(requireContext()).build()
    exoPlayer.addListener(this)
    binding.playerView.player = exoPlayer

//    binding.clickInterceptorView.setOnClickListener {
//      if (isPlayerControlsVisible()) {
//        hidePlayerOverlay()
//      } else {
//        showPlayerOverlay()
//      }
//    }
    binding.fullScreenButton.setOnClickListener { toggleFullScreen() }

    qualityOptionsBottomSheet = QualityOptionsBottomSheetDialogFragment(playerViewModel)
    binding.qualitySettingsButton.setOnClickListener {
      qualityOptionsBottomSheet.show(requireActivity().supportFragmentManager, null)
    }

    playerViewModel.streamQualities.observe(viewLifecycleOwner, Observer {
      preparePlayer(it[0].quality.url)
    })
    playerViewModel.qualityChangeEvent.observe(viewLifecycleOwner, Observer {
      preparePlayer(it)
    })

    val channelName = arguments?.getString("channelName") ?: ""
    playerViewModel.getStreamUrlAndQualityMap(channelName)
  }

  override fun onAttach(context: Context) {
    Twichinola.dependencyInjector().inject(this)
    super.onAttach(context)
  }

  private fun preparePlayer(url: String) {
    Timber.d("Preparing stream url $url")
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
    when (playbackState) {
      Player.STATE_READY -> {
        Timber.d("Player state ready")
        binding.playerLoadingIndicator.visibility = View.GONE

        hidePlayerOverlay()
      }
      Player.STATE_BUFFERING -> {
        Timber.d("Player state buffering")

        binding.playerLoadingIndicator.visibility = View.VISIBLE
      }
      Player.STATE_IDLE -> {
        Timber.d("Player state idle")
      }
      Player.STATE_ENDED -> {
        Timber.d("Player state ended")
      }
    }
  }

  override fun onPlayerError(error: ExoPlaybackException) {
    super.onPlayerError(error)
  }

  private fun resumeStream() {
    showPauseButton()
    hidePlayerOverlay()
  }

  private fun pauseStream() {
    showPlayButton()
  }

  private fun isPlayerControlsVisible() = binding.playerOverlayView.alpha == 1f

  private fun hidePlayerOverlay() {
//    delayAnimationHandler.removeCallbacks(hideAnimationRunnable)
//
//    binding.playerOverlayView.apply {
//      animate().alpha(0f).setInterpolator(AccelerateDecelerateInterpolator()).start()
//    }
  }

  private fun showPlayerOverlay() {
    binding.playerOverlayView.apply {
      animate().alpha(1f).setInterpolator(AccelerateDecelerateInterpolator()).start()
    }

    delayAnimationHandler.postDelayed(hideAnimationRunnable, HIDE_ANIMATION_DELAY)
  }

  private fun showPlayButton() {
    binding.playButton.visibility = View.VISIBLE
    binding.pauseButton.visibility = View.GONE
  }

  private fun showPauseButton() {
    binding.playButton.visibility = View.GONE
    binding.pauseButton.visibility = View.VISIBLE
  }

  private fun toggleFullScreen() {
    fullscreen = !fullscreen
    activity?.run {
      this.requestedOrientation =
        if (fullscreen) ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    updateFullscreenButtonState()
  }

  private fun updateFullscreenButtonState() {
    if (fullscreen) {
      binding.fullScreenButton.setImageResource(R.drawable.ic_fullscreen_exit_24px)
    } else {
      binding.fullScreenButton.setImageResource(R.drawable.ic_fullscreen_24px)
    }
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    landscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
  }

  companion object {
    const val HIDE_ANIMATION_DELAY = 3000L
  }
}
