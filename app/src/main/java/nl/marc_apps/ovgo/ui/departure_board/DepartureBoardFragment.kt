package nl.marc_apps.ovgo.ui.departure_board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import nl.marc_apps.ovgo.databinding.FragmentDepartureBoardBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DepartureBoardFragment : Fragment() {
    private val viewModel by viewModel<DepartureBoardViewModel>()

    private lateinit var binding: FragmentDepartureBoardBinding

    private val navigationArgs by navArgs<DepartureBoardFragmentArgs>()

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

        viewModel.currentStation.observe(viewLifecycleOwner) {
            binding.labelStation.text = it.name
        }

        binding.actionChangeStation.setOnClickListener {
            val action = DepartureBoardFragmentDirections.actionHomeToStationSearch()
            findNavController().navigate(action)
        }

        val departuresAdapter = DeparturesAdapter()
        binding.listDepartures.adapter = departuresAdapter
        binding.listDepartures.addItemDecoration(
            DividerItemDecoration(binding.listDepartures.context, DividerItemDecoration.VERTICAL)
        )

        viewModel.departures.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.placeholderListDepartures.visibility = View.VISIBLE
                binding.listDepartures.visibility = View.GONE
            } else {
                binding.placeholderListDepartures.visibility = View.GONE
                binding.listDepartures.visibility = View.VISIBLE
                departuresAdapter.submitList(it.toList()) {
                    binding.listDepartures.scheduleLayoutAnimation()
                }
            }
        }

        val station = navigationArgs.station
        if (station != null) {
            viewModel.loadDepartures(station)
        } else {
            viewModel.loadDeparturesForLastKnownStation()
        }
    }
}
