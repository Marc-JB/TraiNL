package nl.marc_apps.ovgo.ui.departure_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.databinding.FragmentDepartureStopsBinding
import nl.marc_apps.ovgo.ui.DividerItemDecoration

class DepartureStopsFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentDepartureStopsBinding

    private val navigationArgs by navArgs<DepartureStopsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDepartureStopsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val departureStopsAdapter = DepartureStopsAdapter {
            val action = DepartureStopsFragmentDirections
                .actionDepartureStopsToStationDepartureBoard(it)
            activity?.findNavController(R.id.holder_fragment)?.navigate(action)
        }
        binding.listStops.adapter = departureStopsAdapter
        binding.listStops.addItemDecoration(
            DividerItemDecoration(
                view.context,
                DividerItemDecoration.VERTICAL
            ).apply {
                AppCompatResources.getDrawable(view.context, R.drawable.divider)?.let {
                    drawable = it
                }
            }
        )
        departureStopsAdapter.submitList(navigationArgs.stops.toList())
    }
}
