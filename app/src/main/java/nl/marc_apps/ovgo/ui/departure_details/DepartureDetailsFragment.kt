package nl.marc_apps.ovgo.ui.departure_details

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.ImageLoader
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.databinding.FragmentDepartureDetailsBinding
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.domain.TrainInfo
import nl.marc_apps.ovgo.ui.TrainImages
import nl.marc_apps.ovgo.utils.format
import nl.marc_apps.ovgo.utils.navGraphViewModel
import org.koin.android.ext.android.inject
import java.text.DateFormat

class DepartureDetailsFragment : Fragment() {
    private val viewModel by navGraphViewModel<DepartureDetailsViewModel>(R.id.departure_details)

    private lateinit var binding: FragmentDepartureDetailsBinding

    private val navigationArgs by navArgs<DepartureDetailsFragmentArgs>()

    private val imageLoader by inject<ImageLoader>()

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

        viewModel.loadStations(departure)

        binding.partialDepartureInformationCard.labelDepartureTime.text = if (departure.isDelayed) {
            view.context.resources.getQuantityString(
                R.plurals.departure_time_long_text_delayed,
                departure.delayInMinutesRounded,
                departure.plannedDepartureTime.format(timeStyle = DateFormat.SHORT),
                departure.delayInMinutesRounded
            )
        } else {
            view.context.getString(
                R.string.departure_time_long_text_no_delay,
                departure.plannedDepartureTime.format(timeStyle = DateFormat.SHORT)
            )
        }

        binding.partialDepartureInformationCard.labelDepartureTimeCountdown.text = DateUtils.getRelativeTimeSpanString(
            departure.actualDepartureTime.time,
            System.currentTimeMillis(),
            DateUtils.MINUTE_IN_MILLIS
        )

        binding.labelTitle.text = view.context.getString(R.string.departure_info_title, departure.categoryName, departure.actualDirection?.fullName)

        binding.partialRouteInformationCard.actionShowDestinationStation.text = departure.actualDirection?.fullName
        binding.partialRouteInformationCard.actionShowDestinationStation.setOnClickListener {
            if (departure.actualDirection != null) {
                val action = DepartureDetailsFragmentDirections
                    .actionDepartureDetailsToStationDepartureBoard(departure.actualDirection)
                findNavController().navigate(action)
            }
        }

        loadUpcomingStations(departure)

        binding.partialDepartureInformationCard.labelPlatform.text = view.context.getString(R.string.platform, departure.actualTrack)

        loadFacilities(departure, trainInfo)

        binding.partialTrainInformationCard.labelOperatorAndType.text = getString(
            R.string.departure_operator_and_type_single_line,
            departure.operator,
            departure.categoryName
        )

        loadTrainImages(departure, trainInfo)

        viewModel.journeyStops.observe(viewLifecycleOwner) {
            binding.partialRouteInformationCard.actionShowStops.visibility = View.GONE
            binding.partialRouteInformationCard.loadingIndicator.visibility = View.GONE

            if (it == null) {
                binding.partialRouteInformationCard.loadingIndicator.visibility = View.VISIBLE
            } else if (it.isNotEmpty()) {
                binding.partialRouteInformationCard.actionShowStops.visibility = View.VISIBLE
                binding.partialRouteInformationCard.actionShowStops.setOnClickListener { _ ->
                    val action = DepartureDetailsFragmentDirections
                        .actionDepartureDetailsToStops(it.toTypedArray())
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun loadUpcomingStations(departure: Departure) {
        val upcomingTrainStationAdapter = UpcomingTrainStationAdapter()
        binding.partialRouteInformationCard.listUpcomingStations.adapter = upcomingTrainStationAdapter

        upcomingTrainStationAdapter.submitList(departure.stationsOnRoute)

        binding.partialRouteInformationCard.labelUpcomingStations.visibility =
            if (departure.stationsOnRoute.isEmpty() || departure.isCancelled) View.GONE
            else View.VISIBLE
        binding.partialRouteInformationCard.listUpcomingStations.visibility =
            if (departure.stationsOnRoute.isEmpty() || departure.isCancelled) View.GONE
            else View.VISIBLE
    }

    private fun loadFacilities(departure: Departure, trainInfo: TrainInfo?) {
        val facilities = if (departure.isCancelled) {
            null
        } else {
            trainInfo?.facilities
        } ?: TrainInfo.TrainFacilities()

        binding.partialTrainInformationCard.iconWheelchairAccessible.visibility = if (facilities.isWheelChairAccessible) View.VISIBLE else View.GONE

        binding.partialTrainInformationCard.iconToilet.visibility = if (facilities.hasToilet) View.VISIBLE else View.GONE

        binding.partialTrainInformationCard.iconBicycles.visibility = if (facilities.hasBicycleCompartment) View.VISIBLE else View.GONE

        binding.partialTrainInformationCard.iconWifi.visibility = if (facilities.hasFreeWifi) View.VISIBLE else View.GONE

        binding.partialTrainInformationCard.iconPowerSockets.visibility = if (facilities.hasPowerSockets) View.VISIBLE else View.GONE

        binding.partialTrainInformationCard.iconSilenceCompartment.visibility = if (facilities.hasSilenceCompartment) View.VISIBLE else View.GONE

        binding.partialTrainInformationCard.iconFirstClass.visibility = if (facilities.hasFirstClass) View.VISIBLE else View.GONE

        binding.partialTrainInformationCard.iconBistro.visibility = if (facilities.hasBistro) View.VISIBLE else View.GONE
    }

    private fun loadTrainImages(departure: Departure, trainInfo: TrainInfo?) {
        binding.partialTrainInformationCard.holderTrainImagesScrollable.visibility =
            if (departure.isCancelled || trainInfo?.trainParts?.firstOrNull()?.imageUrl == null) View.GONE
            else View.VISIBLE

        binding.partialTrainInformationCard.holderTrainImages.removeAllViews()

        trainInfo?.trainParts?.mapNotNull { it.imageUrl }?.let {
            TrainImages.loadView(binding.partialTrainInformationCard.holderTrainImages, it, imageLoader)
        }
    }
}
