package nl.marc_apps.ovgo.ui.departure_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.databinding.FragmentDepartureDetailsBinding
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.domain.TrainInfo
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.ui.TrainImages
import nl.marc_apps.ovgo.ui.departure_board.DeparturesAdapter
import nl.marc_apps.ovgo.utils.format
import java.text.DateFormat

class DepartureDetailsFragment : Fragment() {
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

        val departure = navigationArgs.departure
        val trainInfo = departure.trainInfo

        binding.labelDepartureTime.text = if (departure.isDelayed) {
            view.context.getString(
                R.string.departure_time_delayed,
                departure.actualDepartureTime.format(timeStyle = DateFormat.SHORT),
                departure.delayInMinutesRounded
            )
        } else {
            departure.actualDepartureTime.format(timeStyle = DateFormat.SHORT)
        }

        binding.labelDepartureTime.setTextColor(
            ContextCompat.getColor(
                view.context,
                if (departure.isDelayed || departure.isCancelled) R.color.sectionTitleWarningColor
                else R.color.sectionTitleColor
            )
        )

        binding.labelDirection.text = departure.direction?.name
        binding.labelDirection.setTextColor(
            ContextCompat.getColor(
                binding.labelDirection.context,
                if (departure.isCancelled) R.color.sectionTitleWarningColor
                else R.color.sectionTitleColor
            )
        )

        binding.labelDirection.setOnClickListener {
            if (departure.direction != null) {
                val action = DepartureDetailsFragmentDirections
                    .actionDepartureDetailsToStationDepartureBoard(departure.direction)
                findNavController().navigate(action)
            }
        }

        loadUpcomingStations(departure)

        binding.labelCancelled.visibility = if (departure.isCancelled) View.VISIBLE else View.GONE

        binding.labelPlatform.setBackgroundResource(
            if (departure.platformChanged || departure.isCancelled) R.drawable.platform_background_style_red
            else R.drawable.platform_background_style
        )
        binding.labelPlatform.text = departure.actualTrack
        binding.labelPlatform.visibility = if (departure.isCancelled) View.GONE else View.VISIBLE

        loadFacilities(departure, trainInfo)

        binding.labelOperatorAndType.text = getString(
            R.string.departure_operator_and_type_single_line,
            departure.operator,
            departure.categoryName
        )

        loadTrainImages(departure, trainInfo)
    }

    private fun loadUpcomingStations(departure: Departure) {
        val upcomingTrainStationAdapter = UpcomingTrainStationAdapter()
        binding.listUpcomingStations.adapter = upcomingTrainStationAdapter
        upcomingTrainStationAdapter.submitList(departure.stationsOnRoute)

        binding.labelUpcomingStations.visibility =
            if (departure.stationsOnRoute.isEmpty() || departure.isCancelled) View.GONE
            else View.VISIBLE
        binding.listUpcomingStations.visibility =
            if (departure.stationsOnRoute.isEmpty() || departure.isCancelled) View.GONE
            else View.VISIBLE
    }

    private fun loadFacilities(departure: Departure, trainInfo: TrainInfo?) {
        val facilities = if (departure.isCancelled) {
            null
        } else {
            trainInfo?.facilities
        } ?: TrainInfo.TrainFacilities()

        binding.iconWheelchairAccessible.visibility = if (facilities.isWheelChairAccessible) View.VISIBLE else View.GONE

        binding.iconToilet.visibility = if (facilities.hasToilet) View.VISIBLE else View.GONE

        binding.iconBicycles.visibility = if (facilities.hasBicycleCompartment) View.VISIBLE else View.GONE

        binding.iconWifi.visibility = if (facilities.hasFreeWifi) View.VISIBLE else View.GONE

        binding.iconPowerSockets.visibility = if (facilities.hasPowerSockets) View.VISIBLE else View.GONE

        binding.iconSilenceCompartment.visibility = if (facilities.hasSilenceCompartment) View.VISIBLE else View.GONE

        binding.iconFirstClass.visibility = if (facilities.hasFirstClass) View.VISIBLE else View.GONE

        binding.iconBistro.visibility = if (facilities.hasBistro) View.VISIBLE else View.GONE
    }

    private fun loadTrainImages(departure: Departure, trainInfo: TrainInfo?) {
        binding.holderTrainImagesScrollable.visibility =
            if (departure.isCancelled || trainInfo?.trainParts?.firstOrNull()?.imageUrl == null) View.GONE
            else View.VISIBLE

        binding.holderTrainImages.removeAllViews()

        trainInfo?.trainParts?.mapNotNull { it.imageUrl }?.let {
            TrainImages.loadView(binding.holderTrainImages, it)
        }
    }
}
