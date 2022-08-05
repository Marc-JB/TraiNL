package nl.marc_apps.ovgo.ui.disruptions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.databinding.ComposeLayoutBinding
import nl.marc_apps.ovgo.ui.theme.AppTheme
import org.koin.androidx.navigation.koinNavGraphViewModel

class DisruptionsFragment : Fragment() {
    private val viewModel by koinNavGraphViewModel<DisruptionsViewModel>(R.id.disruptions)

    private lateinit var binding: ComposeLayoutBinding

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
                    DisruptionsView(viewModel)
                }
            }
        }
    }
}
