package com.letusneil.twichinola.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.jakewharton.rxbinding3.widget.textChanges
import com.letusneil.twichinola.R
import com.letusneil.twichinola.databinding.SearchFragmentBinding
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchFragment : Fragment(R.layout.search_fragment) {

  @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

  private val viewModel: SearchViewModel by viewModels { viewModelFactory }

  private lateinit var binding: SearchFragmentBinding

  private val disposables = CompositeDisposable()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.search_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    binding = SearchFragmentBinding.bind(view)
    binding.backButton.setOnClickListener {
      findNavController().navigateUp()
    }

    viewModel.searchResults.observe(viewLifecycleOwner, Observer {

    })

    disposables.add(
      binding.inputSearch.textChanges()
        .filter { s -> s.length > 3 }
        .debounce(3, TimeUnit.SECONDS)
        .map { charSequence -> charSequence.toString() }
        .subscribe {
          viewModel.search(it)
        }
    )
  }

  override fun onDestroy() {
    disposables.clear()
    super.onDestroy()
  }
}