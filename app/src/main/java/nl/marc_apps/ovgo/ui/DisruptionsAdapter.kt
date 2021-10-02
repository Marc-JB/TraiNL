package nl.marc_apps.ovgo.ui

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption
import nl.marc_apps.ovgo.databinding.ListItemDisruptionBinding
import nl.marc_apps.ovgo.utils.format
import java.text.DateFormat
import java.util.*

class DisruptionsAdapter : ListAdapter<DutchRailwaysDisruption, DisruptionsAdapter.DisruptionViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisruptionViewHolder {
        val binding = ListItemDisruptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DisruptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DisruptionViewHolder, position: Int) {
        val disruption = currentList.elementAtOrNull(position) ?: return

        val context = holder.binding.root.context

        holder.binding.labelTitle.text = disruption.title

        if (disruption is DutchRailwaysDisruption.DisruptionOrMaintenance) {
            holder.binding.labelTitle.setTextColor(context.getColor(R.color.sectionTitleColor))

            val currentDate = Date()
            val activeTimeSpans = disruption.timespans.filterNot {
                currentDate.after(it.start) && currentDate.before(it.end)
            }
            val activeAlternativeTransportTimeSpans = disruption.alternativeTransportTimespans.filterNot {
                (it.start == null || currentDate.after(it.start)) && (it.end == null || currentDate.before(it.end))
            }

            val description = listOfNotNull(
                activeTimeSpans.firstOrNull()?.situation?.label,
                disruption.summaryAdditionalTravelTime?.label ?: activeTimeSpans.firstOrNull()?.additionalTravelTime?.label,
                disruption.expectedDuration?.description,
                activeAlternativeTransportTimeSpans.firstOrNull()?.alternativeTransport?.label ?: activeTimeSpans.firstOrNull()?.alternativeTransport?.label
            ).joinToString(separator = " ")

            holder.binding.labelDescription.text = description

            holder.binding.labelLastUpdate.visibility = View.GONE

            holder.binding.labelTimerange.visibility = View.VISIBLE

            holder.binding.labelTimerange.text = if (disruption.end == null) {
                disruption.start.format(DateFormat.MEDIUM, DateFormat.SHORT)
            } else {
                disruption.start.format(DateFormat.MEDIUM, DateFormat.SHORT) + "\n" + disruption.end.format(DateFormat.MEDIUM, DateFormat.SHORT)
            }

            holder.binding.root.setOnClickListener {
                MaterialAlertDialogBuilder(context)
                    .setTitle(disruption.title)
                    .setMessage(description)
                    .setPositiveButton(R.string.ok) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        } else if (disruption is DutchRailwaysDisruption.Calamity) {
            holder.binding.labelTitle.setTextColor(context.getColor(R.color.sectionTitleWarningColor))

            holder.binding.labelDescription.text = disruption.description

            holder.binding.labelTimerange.visibility = View.GONE

            if (disruption.lastUpdated == null) {
                holder.binding.labelLastUpdate.visibility = View.GONE
            } else {
                holder.binding.labelLastUpdate.visibility = View.VISIBLE
                holder.binding.labelLastUpdate.text = DateUtils.getRelativeTimeSpanString(
                    disruption.lastUpdated.time,
                    System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS
                )
            }

            holder.binding.root.setOnClickListener {
                MaterialAlertDialogBuilder(context)
                    .setTitle(disruption.title)
                    .setMessage(disruption.description)
                    .setPositiveButton(R.string.ok) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    class DisruptionViewHolder(val binding: ListItemDisruptionBinding) : RecyclerView.ViewHolder(binding.root)

    object DiffCallback : DiffUtil.ItemCallback<DutchRailwaysDisruption>() {
        override fun areItemsTheSame(
            oldItem: DutchRailwaysDisruption,
            newItem: DutchRailwaysDisruption
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DutchRailwaysDisruption,
            newItem: DutchRailwaysDisruption
        ): Boolean {
            return oldItem == newItem
        }
    }
}
