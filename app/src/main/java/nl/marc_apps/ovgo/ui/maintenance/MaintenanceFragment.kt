package nl.marc_apps.ovgo.ui.maintenance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.databinding.FragmentMaintenanceBinding
import nl.marc_apps.ovgo.ui.theme.AppTheme
import org.koin.androidx.navigation.koinNavGraphViewModel

class MaintenanceFragment : Fragment() {
    private val viewModel by koinNavGraphViewModel<MaintenanceViewModel>(R.id.maintenance)

    private lateinit var binding: FragmentMaintenanceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMaintenanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.holderCompose.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
        )

        binding.holderCompose.setContent {
            AppTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    MaintenanceView(viewModel)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.maintenanceList.collect {
                binding.partialImageWithLabelPlaceholder.root.visibility = View.GONE

                when {
                    it == null -> {}
                    it.isEmpty() -> {
                        binding.partialImageWithLabelPlaceholder.image.setImageResource(R.drawable.va_travelling)
                        binding.partialImageWithLabelPlaceholder.label.setText(R.string.no_maintenance)
                        binding.partialImageWithLabelPlaceholder.root.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}
