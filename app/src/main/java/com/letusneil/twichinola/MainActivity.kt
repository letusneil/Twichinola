package com.letusneil.twichinola

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.letusneil.twichinola.databinding.MainActivityBinding

class MainActivity : AppCompatActivity(),
  Toolbar.OnMenuItemClickListener,
  NavController.OnDestinationChangedListener {

  private lateinit var binding: MainActivityBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = MainActivityBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)

    binding.run {
      findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener(
        this@MainActivity
      )

      bottomAppBar.apply {
        replaceMenu(R.menu.bottom_app_bar_menu)
        setOnMenuItemClickListener(this@MainActivity)
      }
    }
  }

  override fun onDestinationChanged(
    controller: NavController,
    destination: NavDestination,
    arguments: Bundle?
  ) {
    when (destination.id) {
      R.id.browseFragment -> {
        binding.bottomAppBar.visibility = View.VISIBLE
        binding.bottomAppBar.performShow()
      }
      else -> {
        binding.bottomAppBar.performHide()
        binding.bottomAppBar.visibility = View.GONE
      }
    }
  }

  override fun onMenuItemClick(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.menu_main_search -> {
        findNavController(R.id.nav_host_fragment)
          .navigate(R.id.global_action_search_fragment)
      }
    }
    return true
  }
}
