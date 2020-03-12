package com.letusneil.twichinola.ui.player

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.letusneil.twichinola.R
import com.letusneil.twichinola.di.Twichinola
import javax.inject.Inject

class PlayerFragment : Fragment(R.layout.play_fragment) {

  @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

  private val viewModel: PlayerViewModel by viewModels { viewModelFactory }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    viewModel.viewEvent.observe(viewLifecycleOwner, Observer {

    })
    viewModel.getStreamUrlAndQualityMap("")
  }



  override fun onAttach(context: Context) {
    Twichinola.dependencyInjector().inject(this)
    super.onAttach(context)
  }
}
