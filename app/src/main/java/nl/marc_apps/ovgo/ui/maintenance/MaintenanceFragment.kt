package nl.marc_apps.ovgo.ui.maintenance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.databinding.FragmentMaintenanceBinding
import nl.marc_apps.ovgo.ui.DisruptionsAdapter
import nl.marc_apps.ovgo.ui.DividerItemDecoration
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

        val maintenanceAdapter = DisruptionsAdapter()
        binding.listMaintenance.adapter = maintenanceAdapter
        binding.listMaintenance.addItemDecoration(
            DividerItemDecoration(binding.listMaintenance.context, DividerItemDecoration.VERTICAL)
        )

        viewModel.maintenanceList.observe(viewLifecycleOwner) {
            binding.partialImageWithLabelPlaceholder.root.visibility = View.GONE
            binding.placeholderListMaintenance.visibility = View.GONE
            binding.listMaintenance.visibility = View.GONE

            when {
                it == null -> {
                    binding.placeholderListMaintenance.visibility = View.VISIBLE
                }
                it.isEmpty() -> {
                    binding.partialImageWithLabelPlaceholder.image.setImageResource(R.drawable.va_travelling)
                    binding.partialImageWithLabelPlaceholder.label.setText(R.string.no_maintenance)
                    binding.partialImageWithLabelPlaceholder.root.visibility = View.VISIBLE
                }
                else -> {
                    binding.listMaintenance.visibility = View.VISIBLE
                    binding.listMaintenance.scheduleLayoutAnimation()
                    maintenanceAdapter.submitList(it)
                }
            }
        }

        viewModel.loadMaintenance()
    }
}
