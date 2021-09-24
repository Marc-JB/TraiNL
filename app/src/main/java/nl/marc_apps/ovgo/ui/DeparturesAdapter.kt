package nl.marc_apps.ovgo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDeparture
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysTrainInfo
import nl.marc_apps.ovgo.databinding.ListItemDepartureBinding
import nl.marc_apps.ovgo.databinding.ListItemDepartureCancelledBinding
import nl.marc_apps.ovgo.databinding.PartialTrainImageBinding
import nl.marc_apps.ovgo.ui.home.DepartureBoardFragmentDirections

class DeparturesAdapter : ListAdapter<Pair<DutchRailwaysDeparture, DutchRailwaysTrainInfo?>, DeparturesAdapter.DepartureViewHolder>(DiffCallback) {
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
            onBindRegularDepartureViewHolder(departure.first, departure.second!!, holder)
        } else if (holder is DepartureViewHolder.CancelledDepartureViewHolder) {
            onBindCancelledDepartureViewHolder(departure.first, holder)
        }
    }

    private fun onBindRegularDepartureViewHolder(
        departure: DutchRailwaysDeparture,
        trainInfo: DutchRailwaysTrainInfo,
        holder: DepartureViewHolder.RegularDepartureViewHolder
    ) {
        val binding = holder.binding
        val context = binding.root.context

        val departureTimeWithDelayText = if(departure.isDelayed) {
            context.getString(R.string.departure_time_delayed, departure.departureTimeText, departure.delayInMinutesRounded)
        } else {
            departure.departureTimeText
        }
        binding.labelDepartureTime.text = departureTimeWithDelayText
        binding.labelDepartureTimeAlignment.text = departureTimeWithDelayText

        binding.labelDepartureTime.setTextColor(
            ContextCompat.getColor(
                binding.labelDepartureTime.context,
                if(departure.isDelayed) R.color.colorError else R.color.colorPrimary
            )
        )

        binding.labelDirection.text = (departure.direction ?: departure.routeStations.lastOrNull()?.mediumName)?.toString()

        val upcomingStations = departure.routeStations.joinToString(limit = MAX_STATIONS_DISPLAYED_ON_ROUTE) {
            it.mediumName
        }
        binding.labelUpcomingStations.text = if(departure.routeStations.isNotEmpty()) {
            context.getString(R.string.departure_via_stations, upcomingStations)
        } else {
            ""
        }
        binding.labelUpcomingStations.visibility =
            if(departure.routeStations.isEmpty()) View.GONE
            else View.VISIBLE

        binding.labelPlatform.setBackgroundResource(
            if(departure.platformChanged) R.drawable.platform_background_style_red
            else R.drawable.platform_background_style
        )
        binding.labelPlatform.text = departure.actualTrack

        binding.labelOperatorAndType.text = binding.root.resources.getString(R.string.departure_operator_and_type_multi_line, departure.product.correctedOperatorName, departure.product.longCategoryName)

        loadTrainImages(binding, trainInfo)

        binding.root.setOnClickListener {
            val action = DepartureBoardFragmentDirections.actionDepartureBoardToDetails(departure, trainInfo)
            binding.root.findNavController().navigate(action)
        }
    }

    private fun loadTrainImages(binding: ListItemDepartureBinding, trainInfo: DutchRailwaysTrainInfo) {
        binding.holderTrainImagesScrollable.visibility = if(trainInfo.actualTrainParts.firstOrNull()?.imageUrl == null) {
            View.GONE
        } else {
            View.VISIBLE
        }

        if (binding.holderTrainImages.childCount > LAYOUT_CHILD_COUNT_SINGLE_VIEW) {
            binding.holderTrainImages.removeViews(LAYOUT_CHILD_COUNT_SINGLE_VIEW, binding.holderTrainImages.childCount - LAYOUT_CHILD_COUNT_SINGLE_VIEW)
        }

        trainInfo.actualTrainParts.forEach {
            val imageView = PartialTrainImageBinding.inflate(
                LayoutInflater.from(binding.holderTrainImages.context),
                binding.holderTrainImages,
                true
            )
            imageView.root.load(it.imageUrl)
        }
    }

    private fun onBindCancelledDepartureViewHolder(departure: DutchRailwaysDeparture, holder: DepartureViewHolder.CancelledDepartureViewHolder) {
        val binding = holder.binding

        binding.labelDepartureTime.text = departure.departureTimeText
        binding.labelDepartureTime.setTextColor(
            ContextCompat.getColor(
                binding.labelDepartureTime.context,
                if(departure.isDelayed || departure.cancelled) R.color.colorError else R.color.colorOK
            )
        )

        binding.labelDirection.text = (departure.direction ?: departure.routeStations.lastOrNull()?.mediumName).toString()
        binding.labelDirection.setTextColor(
            ContextCompat.getColor(
                binding.labelDirection.context,
                if(departure.cancelled) R.color.colorError else R.color.colorPrimary
            )
        )

        binding.labelCancelled.visibility = if(departure.cancelled) View.VISIBLE else View.GONE

        binding.labelOperatorAndType.text = binding.root.resources.getString(R.string.departure_operator_and_type_multi_line, departure.product.correctedOperatorName, departure.product.longCategoryName)
    }

    @Type
    override fun getItemViewType(position: Int): Int {
        val departure = currentList.elementAtOrNull(position)?.first
        return if(departure?.cancelled == false) TYPE_REGULAR else TYPE_CANCELLED
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

        @IntDef(TYPE_REGULAR, TYPE_CANCELLED)
        @Retention(AnnotationRetention.SOURCE)
        private annotation class Type
    }

    object DiffCallback : DiffUtil.ItemCallback<Pair<DutchRailwaysDeparture, DutchRailwaysTrainInfo?>>() {
        override fun areItemsTheSame(
            oldItem: Pair<DutchRailwaysDeparture, DutchRailwaysTrainInfo?>,
            newItem: Pair<DutchRailwaysDeparture, DutchRailwaysTrainInfo?>
        ): Boolean {
            return oldItem.first.product.number == newItem.first.product.number
        }

        override fun areContentsTheSame(
            oldItem: Pair<DutchRailwaysDeparture, DutchRailwaysTrainInfo?>,
            newItem: Pair<DutchRailwaysDeparture, DutchRailwaysTrainInfo?>
        ): Boolean {
            return oldItem.first == newItem.first && newItem.second == oldItem.second
        }
    }
}
