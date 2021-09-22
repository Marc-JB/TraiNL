package nl.marc_apps.ovgo.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDeparture
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysTrainInfo
import nl.marc_apps.ovgo.databinding.ListItemDisruptionBinding
import nl.marc_apps.ovgo.utils.format
import java.text.DateFormat
import java.util.*

class DisruptionsAdapter : ListAdapter<DutchRailwaysDisruption.DisruptionOrMaintenance, DisruptionsAdapter.DisruptionViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisruptionViewHolder {
        val binding = ListItemDisruptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DisruptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DisruptionViewHolder, position: Int) {
        val disruption = currentList.elementAtOrNull(position) ?: return

        holder.binding.labelTitle.text = disruption.title

        val currentDate = Date()
        val activeTimeSpans = disruption.timespans.filterNot { currentDate.after(it.start) && currentDate.before(it.end) }

        holder.binding.labelDescription.text = listOfNotNull(
            activeTimeSpans.firstOrNull()?.situation?.label,
            disruption.summaryAdditionalTravelTime?.label,
            disruption.expectedDuration?.description
        ).joinToString(separator = " ")

        holder.binding.labelTimerange.text = if (disruption.end == null) {
            disruption.start.format(DateFormat.MEDIUM, DateFormat.SHORT)
        } else {
            disruption.start.format(DateFormat.MEDIUM, DateFormat.SHORT) + "\n" + disruption.end.format(DateFormat.MEDIUM, DateFormat.SHORT)
        }
    }

    class DisruptionViewHolder(val binding: ListItemDisruptionBinding) : RecyclerView.ViewHolder(binding.root)

    object DiffCallback : DiffUtil.ItemCallback<DutchRailwaysDisruption.DisruptionOrMaintenance>() {
        override fun areItemsTheSame(
            oldItem: DutchRailwaysDisruption.DisruptionOrMaintenance,
            newItem: DutchRailwaysDisruption.DisruptionOrMaintenance
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DutchRailwaysDisruption.DisruptionOrMaintenance,
            newItem: DutchRailwaysDisruption.DisruptionOrMaintenance
        ): Boolean {
            return oldItem == newItem
        }
    }
}
