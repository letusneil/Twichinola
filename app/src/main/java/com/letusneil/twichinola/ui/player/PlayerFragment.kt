package com.letusneil.twichinola.ui.player

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.transition.ChangeBounds
import android.transition.TransitionInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.jakewharton.rxbinding3.view.systemUiVisibilityChanges
import com.jakewharton.rxbinding3.view.visibility
import com.letusneil.twichinola.R
import com.letusneil.twichinola.data.GameStream
import com.letusneil.twichinola.databinding.PlayerFragmentBinding
import com.letusneil.twichinola.di.Twichinola
import com.letusneil.twichinola.util.TEMPLATE_IMAGE_HEIGHT
import com.letusneil.twichinola.util.TEMPLATE_IMAGE_WIDTH
import com.letusneil.twichinola.util.autoCleared
import timber.log.Timber
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class PlayerFragment : Fragment(R.layout.player_fragment), Player.EventListener {

  @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
  private val playerViewModel: PlayerViewModel by viewModels { viewModelFactory }

  private var binding by autoCleared<PlayerFragmentBinding>()
  private var qualityOptionsBottomSheet by autoCleared<QualityOptionsBottomSheetDialogFragment>()
  private var exoPlayer by autoCleared<ExoPlayer>()

  private var landscape = false
  private var fullscreen = false

  private val args: PlayerFragmentArgs by navArgs()
  private var gameStream: GameStream? = null

  private var delayAnimationHandler = Handler()
  private val hideAnimationRunnable = Runnable {
    if (activity != null && isAdded && !isRemoving) {
      togglePlayerControlsVisibility(false)
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    binding = PlayerFragmentBinding.bind(view)
    gameStream = args.channel

    exoPlayer = SimpleExoPlayer.Builder(requireContext()).build()
    exoPlayer.addListener(this)

    qualityOptionsBottomSheet = QualityOptionsBottomSheetDialogFragment(playerViewModel)

    binding.run {
      playerView.player = exoPlayer

      playerOverlayView.setOnClickListener {
        if (fullscreen) toggleLeanbackMode()
        togglePlayerControlsVisibility(!isPlayerControlsVisible())
      }
      fullScreenButton.setOnClickListener { toggleFullScreen() }

      qualitySettingsButton.setOnClickListener {
        qualityOptionsBottomSheet.show(requireActivity().supportFragmentManager, null)
      }

      backButton.setOnClickListener {
        if (landscape) {
          toggleFullScreen()
        } else {
          findNavController().navigateUp()
        }
      }
    }

    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      landscape = true
    }

    requireActivity().onBackPressedDispatcher.addCallback(
      viewLifecycleOwner, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
          if (fullscreen) toggleFullScreen() else findNavController().navigateUp()
        }
      }
    )

    setupStreamUi()
    setupStream()
  }

  override fun onResume() {
    keepScreen(true)
    super.onResume()
  }

  override fun onAttach(context: Context) {
    Twichinola.dependencyInjector().inject(this)
    super.onAttach(context)
  }

  private fun keepScreen(on: Boolean) {
    if (on) {
      activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    } else {
      activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
  }

  private fun setupStreamUi() {
    gameStream?.let { stream ->
      binding.run {
        val context = gameStreamPreview.context
        gameStreamPreview.transitionName = stream.previewImageUrl
        Glide.with(context)
          .load(stream.previewImageUrl)
          .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
          .transition(DrawableTransitionOptions.withCrossFade())
          .into(gameStreamPreview)
        Glide.with(context)
          .load(stream.streamerImageUrl)
          .apply(
            RequestOptions
              .centerCropTransform()
              .circleCrop()
          )
          .transition(DrawableTransitionOptions.withCrossFade())
          .into(streamerPictureView)
        gameName.text = stream.game
        streamerName.text = stream.streamer
        if (stream.description.isNotEmpty()) {
          streamDescription.text = stream.description
        } else {
          streamDescription.visibility = View.GONE
        }
        gameStreamViewerCount.text =
          context.getString(
            R.string.watching,
            NumberFormat.getIntegerInstance().format(stream.viewersCount)
          )
        gameStreamTypeIndicator.text = stream.streamType
        gameStreamLanguage.text = stream.language
      }
    }
  }

  private fun setupStream() {
    playerViewModel.urlToPlay.observe(viewLifecycleOwner, Observer { preparePlayer(it) })

    gameStream?.run {
      playerViewModel.getStreamUrlAndQualityMap(channelName)
    }
  }

  private fun preparePlayer(url: String) {
    Timber.d("Preparing stream url $url")
    val dataSourceFactory = DefaultDataSourceFactory(requireContext(), getString(R.string.app_name))
    val mediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url))
    exoPlayer.prepare(mediaSource)
    exoPlayer.playWhenReady = true
  }

  override fun onStop() {
    keepScreen(false)
    exoPlayer.stop()
    exoPlayer.release()
    super.onStop()
  }

  override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
    when (playbackState) {
      Player.STATE_READY -> {
        Timber.d("Player state ready")
        binding.playerLoadingIndicator.visibility = View.GONE

        togglePlayerControlsVisibility(false)
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
    togglePlayerControlsVisibility(false)
  }

  private fun pauseStream() {
    showPlayButton()
  }

  private fun isPlayerControlsVisible() = binding.playerOverlayView.alpha == 1f

  private fun togglePlayerControlsVisibility(show: Boolean) {
    if (show) {
      binding.playerOverlayView
        .animate()
        .alpha(1f)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .start()

      delayAnimationHandler.postDelayed(hideAnimationRunnable, HIDE_ANIMATION_DELAY)
    } else {
      binding.gameStreamPreview.visibility = View.GONE
      binding.playerOverlayView
        .animate()
        .alpha(0f)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .start()
    }
    changeOverlayClickable(show)
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
      requestedOrientation =
        if (fullscreen) ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    toggleLeanbackMode()
    updateFullscreenButtonState()
    setVideoLayout()
  }

  private fun changeOverlayClickable(clickable: Boolean) {
    binding.clickInterceptorView.run {
      visibility = (if (clickable) View.GONE else View.VISIBLE)
      setOnClickListener { binding.playerOverlayView.performClick() }
    }
  }

  private fun updateFullscreenButtonState() {
    if (fullscreen) {
      binding.fullScreenButton.setImageResource(R.drawable.ic_fullscreen_exit_24px)
    } else {
      binding.fullScreenButton.setImageResource(R.drawable.ic_fullscreen_24px)
    }
  }

  private fun setVideoLayout() {
    val contentFrame = binding.root.findViewById<AspectRatioFrameLayout>(R.id.exo_content_frame)
    if (landscape) {
      contentFrame.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
    } else {
      contentFrame.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
    }
  }

  private fun toggleLeanbackMode() {
    activity?.window?.decorView?.systemUiVisibility =
      if (fullscreen) (
          View.SYSTEM_UI_FLAG_LAYOUT_STABLE
              or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
              or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
              or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
              or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
              or View.SYSTEM_UI_FLAG_IMMERSIVE
          ) else (
          View.SYSTEM_UI_FLAG_VISIBLE
          )
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    landscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
  }

  companion object {
    const val HIDE_ANIMATION_DELAY = 3000L
  }
}
