package nl.marc_apps.ovgo.ui.search_station

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.composethemeadapter.MdcTheme
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.databinding.FragmentSearchStationBinding
import org.koin.androidx.navigation.koinNavGraphViewModel

class SearchStationFragment : Fragment() {
    private val viewModel by koinNavGraphViewModel<SearchStationViewModel>(R.id.search_station)

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

        binding.root.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )

        binding.root.setContent {
            MdcTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    SearchStationView(viewModel) {
                        val action = SearchStationFragmentDirections.actionStationSearchToHome(it)
                        binding.root.findNavController().navigate(action)
                    }
                }
            }
        }
    }
}
