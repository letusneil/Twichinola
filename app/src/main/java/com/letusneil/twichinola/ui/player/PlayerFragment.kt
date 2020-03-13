package com.letusneil.twichinola.ui.player

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.letusneil.twichinola.R
import com.letusneil.twichinola.databinding.PlayerFragmentBinding
import com.letusneil.twichinola.di.Twichinola
import javax.inject.Inject

class PlayerFragment : Fragment(R.layout.player_fragment) {

  @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

  private val viewModel: PlayerViewModel by viewModels { viewModelFactory }

  private lateinit var binding: PlayerFragmentBinding

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    binding = PlayerFragmentBinding.bind(view)
    viewModel.viewEvent.observe(viewLifecycleOwner, Observer {

    })

    val channelName = arguments?.getString("channelName") ?: ""
    viewModel.getStreamUrlAndQualityMap(channelName)
  }

  override fun onAttach(context: Context) {
    Twichinola.dependencyInjector().inject(this)
    super.onAttach(context)
  }
}
