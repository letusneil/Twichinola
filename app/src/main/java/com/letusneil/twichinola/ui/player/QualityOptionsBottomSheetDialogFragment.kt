package com.letusneil.twichinola.ui.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import com.letusneil.twichinola.R
import com.letusneil.twichinola.databinding.MenuBottomSheetDialogLayoutBinding
import timber.log.Timber
import androidx.lifecycle.Observer
import java.util.*

class QualityOptionsBottomSheetDialogFragment(
  private val playerViewModel: PlayerViewModel
) : BottomSheetDialogFragment() {

  private lateinit var binding: MenuBottomSheetDialogLayoutBinding
  private lateinit var navigationView: NavigationView

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.menu_bottom_sheet_dialog_layout, container, false)
  }

  @ExperimentalStdlibApi
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    binding = MenuBottomSheetDialogLayoutBinding.bind(view)

    navigationView = binding.navigationView
    navigationView.apply {
      setNavigationItemSelectedListener { menuItem ->
        val streamUrl =
          playerViewModel.streamQualities.value?.firstOrNull {
            it.name.equals(menuItem.title.toString(), true)
          }?.quality?.url ?: ""
        Timber.d("stream url $streamUrl")
        playerViewModel.qualityChangeEvent.postValue(streamUrl)
        true
      }
    }

    playerViewModel.streamQualities.observe(viewLifecycleOwner, Observer {
      it.forEach { quality -> navigationView.menu.add(quality.name.capitalize(Locale.ROOT)) }
    })
  }
}