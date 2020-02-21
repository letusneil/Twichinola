package com.letusneil.twichinola.ui.launcher

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.letusneil.twichinola.R

class LauncherFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_launcher, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    findNavController().navigate(
      R.id.actionBrowse,
      null,
      NavOptions.Builder().setPopUpTo(
        R.id.launcherFragment,
        true
      ).build()
    )
  }
}