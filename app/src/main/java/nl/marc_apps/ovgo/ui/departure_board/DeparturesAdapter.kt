package nl.marc_apps.ovgo.ui.departure_board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.databinding.ListItemDepartureBinding
import nl.marc_apps.ovgo.databinding.ListItemDepartureCancelledBinding
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.domain.TrainInfo
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.ui.DepartureMessagesAdapter
import nl.marc_apps.ovgo.ui.TrainImages
import nl.marc_apps.ovgo.utils.format
import java.text.DateFormat

class DeparturesAdapter(
    private val imageLoader: ImageLoader
) : ListAdapter<Departure, DeparturesAdapter.DepartureViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, @Type viewType: Int): DepartureViewHolder {
        return when(viewType) {
            TYPE_REGULAR -> {
                val binding = ListItemDepartureBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                DepartureViewHolder.RegularDepartureViewHolder(binding)
            }
            TYPE_CANCELLED -> {
                val binding = ListItemDepartureCancelledBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                DepartureViewHolder.CancelledDepartureViewHolder(binding)
            }
            else -> throw IllegalStateException("Type $viewType is not supported")
        }
    }

    override fun onBindViewHolder(holder: DepartureViewHolder, position: Int) {
        val departure = currentList.elementAtOrNull(position) ?: return

        if (holder is DepartureViewHolder.RegularDepartureViewHolder) {
            onBindRegularDepartureViewHolder(departure, holder)
        } else if (holder is DepartureViewHolder.CancelledDepartureViewHolder) {
            onBindCancelledDepartureViewHolder(departure, holder)
        }
    }

    private fun isForeignStation(trainStation: TrainStation): Boolean {
        return trainStation.country != null && trainStation.country != TrainStation.Country.THE_NETHERLANDS
    }

    private fun createDisplayName(trainStation: TrainStation, characterLimit: Int): String {
        val effectiveCharacterLimit = if (isForeignStation(trainStation)) {
            characterLimit - STATION_NAME_FLAG_CHARACTER_COUNT
        } else {
            characterLimit
        }

        return buildString {
            append(
                if (trainStation.fullName.length > effectiveCharacterLimit) {
                    trainStation.shortenedName
                } else {
                    trainStation.fullName
                }
            )

            if (isForeignStation(trainStation)) {
                append(" ")
                append(trainStation.country?.flag)
            }
        }
    }

    private fun onBindRegularDepartureViewHolder(
        departure: Departure,
        holder: DepartureViewHolder.RegularDepartureViewHolder
    ) {
        val binding = holder.binding
        val context = binding.root.context

        val departureTimeWithDelayText = if (departure.isDelayed) {
            context.getString(
                R.string.departure_time_delayed,
                departure.plannedDepartureTime.format(timeStyle = DateFormat.SHORT),
                departure.delay.inWholeMinutes
            )
        } else {
            departure.plannedDepartureTime.format(timeStyle = DateFormat.SHORT)
        }
        binding.labelDepartureTime.text = departureTimeWithDelayText
        binding.labelDepartureTimeAlignment.text = departureTimeWithDelayText

        binding.labelDepartureTime.setTextColor(
            ContextCompat.getColor(
                context,
                if (departure.isDelayed) R.color.sectionTitleWarningColor else R.color.sectionTitleOkColor
            )
        )

        binding.labelDirection.text = departure.actualDirection?.let {
            createDisplayName(it, STATION_NAME_CHARACTER_LIMIT)
        }

        val upcomingStations = departure.stationsOnRoute.joinToString(limit = MAX_STATIONS_DISPLAYED_ON_ROUTE) {
            createDisplayName(it, STATION_NAME_CHARACTER_LIMIT)
        }
        binding.labelUpcomingStations.text = if (departure.stationsOnRoute.isNotEmpty()) {
            context.getString(R.string.departure_via_stations, upcomingStations)
        } else {
            ""
        }
        binding.labelUpcomingStations.visibility =
            if (departure.stationsOnRoute.isEmpty()) View.GONE
            else View.VISIBLE

        binding.labelPlatform.setBackgroundResource(
            if (departure.platformChanged) R.drawable.platform_background_style_red
            else R.drawable.platform_background_style
        )
        binding.labelPlatform.text = departure.actualTrack

        binding.labelOperatorAndType.text = if (departure.operator == departure.categoryName) {
            departure.operator
        } else {
            binding.root.resources.getString(
                R.string.departure_operator_and_type_multi_line,
                departure.operator,
                departure.categoryName
            )
        }

        loadTrainImages(binding, departure.trainInfo)

        loadMessages(binding, departure)

        binding.root.setOnClickListener {
            val action = DepartureBoardFragmentDirections.actionDepartureBoardToDetails(departure)
            binding.root.findNavController().navigate(action)
        }
    }

    private fun loadTrainImages(binding: ListItemDepartureBinding, trainInfo: TrainInfo?) {
        binding.holderTrainImagesScrollable.visibility = if (trainInfo?.trainParts?.firstOrNull()?.imageUrl == null) {
            View.GONE
        } else {
            View.VISIBLE
        }

        if (binding.holderTrainImages.childCount > LAYOUT_CHILD_COUNT_SINGLE_VIEW) {
            binding.holderTrainImages.removeViews(LAYOUT_CHILD_COUNT_SINGLE_VIEW, binding.holderTrainImages.childCount - LAYOUT_CHILD_COUNT_SINGLE_VIEW)
        }

        trainInfo?.trainParts?.mapNotNull { it.imageUrl }?.let {
            TrainImages.loadView(binding.holderTrainImages, it, imageLoader)
        }
    }

    private fun loadMessages(binding: ListItemDepartureBinding, departure: Departure) {
        if (departure.messages.isEmpty() && departure.warnings.isEmpty()) {
            binding.listMessages.visibility = View.GONE
        } else {
            binding.listMessages.visibility = View.VISIBLE

            val messagesAdapter = (binding.listMessages.adapter as? DepartureMessagesAdapter)
                ?: DepartureMessagesAdapter().also {
                    binding.listMessages.adapter = it
                }

            messagesAdapter.submitList(departure.warnings.map {
                it to DepartureMessagesAdapter.TYPE_WARNING
            } + departure.messages.map {
                it to DepartureMessagesAdapter.TYPE_MESSAGE
            })
        }
    }

    private fun onBindCancelledDepartureViewHolder(departure: Departure, holder: DepartureViewHolder.CancelledDepartureViewHolder) {
        val binding = holder.binding

        val departureTimeText = departure.actualDepartureTime.format(timeStyle = DateFormat.SHORT)
        binding.labelDepartureTime.text = departureTimeText
        binding.labelDepartureTimeAlignment.text = departureTimeText

        binding.labelDirection.text = departure.actualDirection?.fullName

        binding.labelCancelled.visibility = if (departure.isCancelled) View.VISIBLE else View.GONE

        binding.labelOperatorAndType.text = if (departure.operator == departure.categoryName) {
            departure.operator
        } else {
            binding.root.resources.getString(
                R.string.departure_operator_and_type_multi_line,
                departure.operator,
                departure.categoryName
            )
        }
    }

    @Type
    override fun getItemViewType(position: Int): Int {
        val departure = currentList.elementAtOrNull(position)
        return if (departure?.isCancelled == true) TYPE_CANCELLED else TYPE_REGULAR
    }

    sealed class DepartureViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        class RegularDepartureViewHolder(val binding: ListItemDepartureBinding) : DepartureViewHolder(binding.root)
        class CancelledDepartureViewHolder(val binding: ListItemDepartureCancelledBinding) : DepartureViewHolder(binding.root)
    }

    companion object {
        private const val TYPE_REGULAR = 0
        private const val TYPE_CANCELLED = 1

        private const val LAYOUT_CHILD_COUNT_SINGLE_VIEW = 1

        private const val MAX_STATIONS_DISPLAYED_ON_ROUTE = 2

        private const val STATION_NAME_CHARACTER_LIMIT = 18

        private const val STATION_NAME_FLAG_CHARACTER_COUNT = 2

        @IntDef(TYPE_REGULAR, TYPE_CANCELLED)
        @Retention(AnnotationRetention.SOURCE)
        private annotation class Type
    }

    object DiffCallback : DiffUtil.ItemCallback<Departure>() {
        override fun areItemsTheSame(oldItem: Departure, newItem: Departure): Boolean {
            return oldItem.journeyId == newItem.journeyId
        }

        override fun areContentsTheSame(oldItem: Departure, newItem: Departure): Boolean {
            return oldItem == newItem
        }
    }
}
