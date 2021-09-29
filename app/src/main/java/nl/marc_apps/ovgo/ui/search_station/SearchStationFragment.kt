package nl.marc_apps.ovgo.ui.search_station

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import nl.marc_apps.ovgo.databinding.FragmentSearchStationBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchStationFragment : Fragment() {
    private val viewModel by viewModel<SearchStationViewModel>()

    private lateinit var binding: FragmentSearchStationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchStationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoadingData.observe(viewLifecycleOwner) {
            if (it == false) {
                binding.listStationSuggestions.visibility = View.VISIBLE
                binding.placeholderListStationSuggestions.visibility = View.GONE
            } else {
                binding.listStationSuggestions.visibility = View.GONE
                binding.placeholderListStationSuggestions.visibility = View.VISIBLE
            }
        }

        binding.inputStationName.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.updateAutocompleteList(text.toString())
        }

        val stationSuggestionsAdapter = StationSuggestionsAdapter()
        binding.listStationSuggestions.adapter = stationSuggestionsAdapter
        binding.listStationSuggestions.addItemDecoration(
            DividerItemDecoration(binding.listStationSuggestions.context, DividerItemDecoration.VERTICAL)
        )

        viewModel.stationSuggestions.observe(viewLifecycleOwner) {
            stationSuggestionsAdapter.submitList(it)
            binding.listStationSuggestions.layoutManager?.scrollToPosition(FIRST_ELEMENT_IN_LIST_POSITION)
        }
    }

    companion object {
        private const val FIRST_ELEMENT_IN_LIST_POSITION = 0
    }
}
