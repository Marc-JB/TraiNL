package nl.marc_apps.ovgo.ui.maintenance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import nl.marc_apps.ovgo.databinding.FragmentMaintenanceBinding
import nl.marc_apps.ovgo.ui.DisruptionsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MaintenanceFragment : Fragment() {
    private val viewModel by viewModel<MaintenanceViewModel>()

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
            if (it == null) {
                binding.placeholderListMaintenance.visibility = View.VISIBLE
                binding.listMaintenance.visibility = View.GONE
            } else {
                binding.placeholderListMaintenance.visibility = View.GONE
                binding.listMaintenance.visibility = View.VISIBLE
                binding.listMaintenance.scheduleLayoutAnimation()
                maintenanceAdapter.submitList(it)
            }
        }

        viewModel.loadMaintenance()
    }
}
