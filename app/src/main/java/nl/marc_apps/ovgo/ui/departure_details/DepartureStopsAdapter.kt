package nl.marc_apps.ovgo.ui.departure_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.datetime.Instant
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.databinding.ListItemJourneyStopBinding
import nl.marc_apps.ovgo.domain.JourneyStop
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.utils.format
import java.text.DateFormat

class DepartureStopsAdapter(
    private val navigateToStation: ((TrainStation) -> Unit)? = null
) : ListAdapter<JourneyStop, DepartureStopsAdapter.ViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemJourneyStopBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    private fun isForeignStation(trainStation: TrainStation): Boolean {
        return trainStation.country != null && trainStation.country != TrainStation.Country.THE_NETHERLANDS
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stop = currentList.elementAtOrNull(position) ?: return

        if (stop.actualArrivalTime == stop.actualDepartureTime) {
            holder.binding.labelArrivalTime.visibility = View.GONE
        } else {
            loadTimeView(holder.binding.labelArrivalTime, stop.plannedArrivalTime,
                stop.arrivalDelay.inWholeMinutes
            )
        }

        loadTimeView(holder.binding.labelDepartureTime, stop.plannedDepartureTime,
            stop.departureDelay.inWholeMinutes
        )

        holder.binding.labelStationName.text = buildString {
            append(stop.station.fullName)

            if (isForeignStation(stop.station)) {
                append(" ")
                append(stop.station.country?.flag)
            }
        }

        holder.binding.labelStationName.setTextColor(
            ContextCompat.getColor(
                holder.binding.labelStationName.context,
                if (stop.isCancelled) R.color.sectionTitleWarningColor else R.color.sectionTitleColor
            )
        )

        holder.binding.root.setOnClickListener {
            navigateToStation?.invoke(stop.station)
        }
    }

    private fun loadTimeView(view: TextView, plannedTime: Instant?, delayInMinutesRounded: Long) {
        val context = view.context

        if (plannedTime == null) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
            val isDelayed = delayInMinutesRounded > 0

            val departureTimeWithDelayText = if (isDelayed) {
                context.getString(
                    R.string.departure_time_delayed,
                    plannedTime.format(timeStyle = DateFormat.SHORT),
                    delayInMinutesRounded
                )
            } else {
                plannedTime.format(timeStyle = DateFormat.SHORT)
            }
            view.text = departureTimeWithDelayText

            view.setTextColor(
                ContextCompat.getColor(
                    context,
                    if (isDelayed) R.color.sectionTitleWarningColor else R.color.sectionTitleOkColor
                )
            )
        }
    }

    class ViewHolder(val binding: ListItemJourneyStopBinding) : RecyclerView.ViewHolder(binding.root)

    object DiffCallback : DiffUtil.ItemCallback<JourneyStop>() {
        override fun areItemsTheSame(oldItem: JourneyStop, newItem: JourneyStop): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: JourneyStop, newItem: JourneyStop): Boolean {
            return oldItem == newItem
        }
    }
}
