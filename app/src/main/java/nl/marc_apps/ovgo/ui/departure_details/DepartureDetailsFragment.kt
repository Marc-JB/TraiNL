package nl.marc_apps.ovgo.ui.departure_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.databinding.FragmentDepartureDetailsBinding
import nl.marc_apps.ovgo.ui.theme.AppTheme
import org.koin.androidx.navigation.koinNavGraphViewModel

class DepartureDetailsFragment : Fragment() {
    private val viewModel by koinNavGraphViewModel<DepartureDetailsViewModel>(R.id.departure_details)

    private lateinit var binding: FragmentDepartureDetailsBinding

    private val navigationArgs by navArgs<DepartureDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDepartureDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadStations(navigationArgs.departure)

        binding.root.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )

        binding.root.setContent {
            AppTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    DepartureDetailsView(
                        departure = navigationArgs.departure,
                        departureDetailsViewModel = viewModel,
                        navController = findNavController()
                    )
                }
            }
        }
    }
}
