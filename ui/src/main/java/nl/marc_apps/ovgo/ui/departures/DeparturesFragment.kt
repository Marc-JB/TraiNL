package nl.marc_apps.ovgo.ui.departures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import nl.marc_apps.ovgo.ui.databinding.FragmentDeparturesBinding
import org.koin.android.viewmodel.ext.android.viewModel

class DeparturesFragment : Fragment() {
    private val viewModel by viewModel<DeparturesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDeparturesBinding.inflate(layoutInflater, container, false)

        viewModel.station.observe(viewLifecycleOwner) {
            binding.toolbar.title = it
            viewModel.loadDepartures()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.swipeRefreshLayout.isRefreshing = it
        }

        val adapter = DeparturesAdapter(viewModel.departures)
        binding.recyclerView.adapter = adapter
        viewModel.departures.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }

        binding.recyclerView.addItemDecoration(DividerItemDecoration(
            binding.recyclerView.context,
            (binding.recyclerView.layoutManager as LinearLayoutManager).orientation
        ))

        return binding.root
    }
}
