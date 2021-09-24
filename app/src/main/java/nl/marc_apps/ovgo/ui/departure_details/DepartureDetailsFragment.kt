package nl.marc_apps.ovgo.ui.departure_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDeparture
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysTrainInfo
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysTrainInfo.TrainPart.Facility.*
import nl.marc_apps.ovgo.databinding.FragmentDepartureDetailsBinding
import nl.marc_apps.ovgo.databinding.PartialTrainImageBinding
import nl.marc_apps.ovgo.domain.TrainInfo

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
        val trainInfo = navigationArgs.trainInfo

        binding.labelDepartureTime.text = if(departure.isDelayed) {
            view.context.getString(R.string.departure_time_delayed, departure.departureTimeText, departure.delayInMinutesRounded)
        } else {
            departure.departureTimeText
        }

        binding.labelDepartureTime.setTextColor(
            ContextCompat.getColor(
                view.context,
                if(departure.isDelayed || departure.cancelled) R.color.colorError else R.color.colorPrimary
            )
        )

        binding.labelDirection.text = (departure.direction ?: departure.routeStations.lastOrNull()?.mediumName).toString()
        binding.labelDirection.setTextColor(
            ContextCompat.getColor(
                binding.labelDirection.context,
                if(departure.cancelled) R.color.colorError else R.color.colorPrimary
            )
        )

        val upcomingStations = departure.routeStations.joinToString { it.mediumName }
        binding.labelUpcomingStations.text = if(departure.routeStations.isNotEmpty()) {
            view.context.getString(R.string.departure_via_stations, upcomingStations)
        } else {
            ""
        }
        binding.labelUpcomingStations.visibility =
            if(departure.routeStations.isEmpty() || departure.cancelled) View.GONE
            else View.VISIBLE

        binding.labelCancelled.visibility = if(departure.cancelled) View.VISIBLE else View.GONE

        binding.labelPlatform.setBackgroundResource(
            if(departure.platformChanged || departure.cancelled) R.drawable.platform_background_style_red
            else R.drawable.platform_background_style
        )
        binding.labelPlatform.text = departure.actualTrack
        binding.labelPlatform.visibility = if(departure.cancelled) View.GONE else View.VISIBLE

        loadFacilities(departure, trainInfo)

        binding.labelOperatorAndType.text = getString(R.string.departure_operator_and_type_single_line, departure.product.correctedOperatorName, departure.product.longCategoryName)

        loadTrainImages(departure, trainInfo)
    }

    private fun loadFacilities(departure: DutchRailwaysDeparture, trainInfo: DutchRailwaysTrainInfo) {
        binding.iconWheelchairAccessible.visibility =
            if(!departure.cancelled && trainInfo.actualTrainParts.any { WHEELCHAIR_ACCESSIBLE in it.facilities }) View.VISIBLE
            else View.GONE

        binding.iconToilet.visibility =
            if(!departure.cancelled && trainInfo.actualTrainParts.any { TOILET in it.facilities }) View.VISIBLE
            else View.GONE

        binding.iconBicycles.visibility =
            if(!departure.cancelled && trainInfo.actualTrainParts.any { BICYCLE_COMPARTMENT in it.facilities }) View.VISIBLE
            else View.GONE

        binding.iconWifi.visibility =
            if(!departure.cancelled && trainInfo.actualTrainParts.any { WIFI in it.facilities }) View.VISIBLE
            else View.GONE

        binding.iconPowerSockets.visibility =
            if(!departure.cancelled && trainInfo.actualTrainParts.any { POWER_SOCKETS in it.facilities }) View.VISIBLE
            else View.GONE

        binding.iconSilenceCompartment.visibility =
            if(!departure.cancelled && trainInfo.actualTrainParts.any { SILENCE_COMPARTMENT in it.facilities }) View.VISIBLE
            else View.GONE

        binding.labelFirstClass.visibility =
            if(!departure.cancelled && trainInfo.seatCountFirstClass > 0) View.VISIBLE
            else View.GONE
    }

    private fun loadTrainImages(departure: DutchRailwaysDeparture, trainInfo: DutchRailwaysTrainInfo) {
        binding.holderTrainImagesScrollable.visibility =
            if(departure.cancelled || trainInfo.actualTrainParts.firstOrNull()?.imageUrl == null) View.GONE
            else View.VISIBLE

        binding.holderTrainImages.removeAllViews()
        trainInfo.actualTrainParts.forEach {
            val imageView = PartialTrainImageBinding.inflate(
                LayoutInflater.from(binding.holderTrainImages.context),
                binding.holderTrainImages,
                true
            )
            imageView.root.load(it.imageUrl)
        }
    }
}
