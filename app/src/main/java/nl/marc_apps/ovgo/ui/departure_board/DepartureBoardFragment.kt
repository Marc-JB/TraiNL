package nl.marc_apps.ovgo.ui.departure_board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.NavBackStackEntry
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.databinding.ComposeLayoutBinding
import nl.marc_apps.ovgo.ui.theme.AppTheme
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class DepartureBoardFragment : Fragment() {
    private val backStackEntry: NavBackStackEntry by lazy { findNavController().getBackStackEntry(R.id.departure_board) }
    private val viewModel by stateViewModel<DepartureBoardViewModel>(owner = { backStackEntry })

    private lateinit var binding: ComposeLayoutBinding

    private val navigationArgs by navArgs<DepartureBoardFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ComposeLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )

        binding.root.setContent {
            AppTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    DepartureBoardView(
                        stationId = navigationArgs.stationId,
                        departureBoardViewModel = viewModel,
                        navController = findNavController()
                    )
                }
            }
        }
    }
}
