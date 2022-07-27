package nl.marc_apps.ovgo.ui.departure_board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.ImageLoader
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.databinding.FragmentDepartureBoardBinding
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.ui.theme.AppTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class DepartureBoardFragment : Fragment() {
    private val backStackEntry: NavBackStackEntry by lazy { findNavController().getBackStackEntry(R.id.departure_board) }
    private val viewModel by stateViewModel<DepartureBoardViewModel>(owner = { backStackEntry })

    private lateinit var binding: FragmentDepartureBoardBinding

    private val navigationArgs by navArgs<DepartureBoardFragmentArgs>()

    private val imageLoader by inject<ImageLoader>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDepartureBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.currentStation.collect {
                binding.toolbar.title = it?.fullName
            }
        }

        binding.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_change_station) {
                val action = DepartureBoardFragmentDirections.actionHomeToStationSearch()
                findNavController().navigate(action)
                true
            } else {
                false
            }
        }

        binding.listDepartures.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )

        binding.listDepartures.setContent {
            AppTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    DepartureBoardView(
                        departureBoardViewModel = viewModel,
                        navController = findNavController(),
                        imageLoader = imageLoader
                    )
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.departures.collect {
                loadNewDepartures(it)
            }
        }

        val station = navigationArgs.station
        if (station == null) {
            viewModel.loadDeparturesForLastKnownStation()
        } else {
            viewModel.loadDepartures(station)
        }
    }

    private fun loadNewDepartures(departures: Result<List<Departure>>?) {
        binding.listDepartures.visibility = View.GONE
        binding.partialImageWithLabelPlaceholder.root.visibility = View.GONE

        when {
            departures == null -> {
                binding.listDepartures.visibility = View.VISIBLE
            }
            departures.isFailure -> {
                Snackbar.make(binding.root, R.string.departure_board_loading_failure, Snackbar.LENGTH_INDEFINITE)
                    .also {
                        try {
                            it.setAnchorView(R.id.bottom_navigation)
                        } catch (error: IllegalArgumentException) {
                            error.printStackTrace()
                            Firebase.crashlytics.recordException(error)
                        }
                    }
                    .setAction(R.string.action_retry_loading) {
                        viewModel.reload()
                    }
                    .show()
            }
            departures.getOrThrow().isEmpty() -> {
                binding.partialImageWithLabelPlaceholder.image.setImageResource(R.drawable.va_stranded_traveler)
                binding.partialImageWithLabelPlaceholder.label.setText(R.string.no_departures)
                binding.partialImageWithLabelPlaceholder.root.visibility = View.VISIBLE
            }
            else -> {
                binding.listDepartures.visibility = View.VISIBLE
            }
        }
    }
}
