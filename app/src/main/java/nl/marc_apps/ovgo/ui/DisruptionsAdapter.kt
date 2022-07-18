package nl.marc_apps.ovgo.ui

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption
import nl.marc_apps.ovgo.databinding.ListItemDisruptionBinding
import nl.marc_apps.ovgo.utils.format
import java.text.DateFormat

class DisruptionsAdapter : ListAdapter<DutchRailwaysDisruption, DisruptionsAdapter.DisruptionViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisruptionViewHolder {
        val binding = ListItemDisruptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DisruptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DisruptionViewHolder, position: Int) {
        val disruption = currentList.elementAtOrNull(position) ?: return

        holder.binding.labelTitle.text = disruption.title

        if (disruption is DutchRailwaysDisruption.DisruptionOrMaintenance) {
            loadDisruption(holder.binding, disruption)
        } else if (disruption is DutchRailwaysDisruption.Calamity) {
            loadCalamity(holder.binding, disruption)
        }
    }

    private fun loadDisruption(binding: ListItemDisruptionBinding, disruption: DutchRailwaysDisruption.DisruptionOrMaintenance) {
        val context = binding.root.context

        binding.labelTitle.setTextColor(ContextCompat.getColor(context, R.color.sectionTitleColor))

        val currentDate = Clock.System.now()
        val activeTimeSpan = disruption.timespans.filter {
            it.end != null && currentDate < it.end
        }.minByOrNull {
            it.start
        }

        val activeAlternativeTransportTimeSpan = disruption.alternativeTransportTimespans.filter {
            (it.end == null && it.start != null && currentDate > it.start) || (it.end != null && currentDate < it.end)
        }.minByOrNull {
            it.start ?: Instant.DISTANT_FUTURE
        }

        val description = listOfNotNull(
            activeTimeSpan?.situation?.label,
            disruption.summaryAdditionalTravelTime?.label ?: activeTimeSpan?.additionalTravelTime?.label,
            disruption.expectedDuration?.description,
            activeAlternativeTransportTimeSpan?.alternativeTransport?.label ?: activeTimeSpan?.alternativeTransport?.label
        ).joinToString(separator = " ")

        binding.labelDescription.text = description

        binding.labelLastUpdate.visibility = View.GONE

        binding.labelTimerange.visibility = View.VISIBLE

        binding.labelTimerange.text = when {
            disruption.end == null && currentDate > disruption.start -> {
                context.getString(R.string.disruption_end_time_unknown)
            }
            disruption.end != null && currentDate > disruption.start -> {
                context.getString(R.string.disruption_end_time, disruption.end.format(DateFormat.MEDIUM, DateFormat.SHORT))
            }
            disruption.end == null -> {
                disruption.start.format(DateFormat.MEDIUM, DateFormat.SHORT)
            }
            else -> {
                disruption.start.format(DateFormat.MEDIUM, DateFormat.SHORT) + "\n" + disruption.end.format(DateFormat.MEDIUM, DateFormat.SHORT)
            }
        }

        binding.root.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle(disruption.title)
                .setMessage(description)
                .setPositiveButton(R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun loadCalamity(binding: ListItemDisruptionBinding, calamity: DutchRailwaysDisruption.Calamity) {
        val context = binding.root.context

        binding.labelTitle.setTextColor(
            ContextCompat.getColor(
                context,
                if (calamity.priority == DutchRailwaysDisruption.Calamity.Priority.PRIO_1) {
                    R.color.sectionTitleWarningColor
                } else {
                    R.color.sectionTitleColor
                }
            )
        )

        binding.labelDescription.text = calamity.description

        binding.labelTimerange.visibility = View.GONE

        if (calamity.lastUpdated == null) {
            binding.labelLastUpdate.visibility = View.GONE
        } else {
            binding.labelLastUpdate.visibility = View.VISIBLE
            binding.labelLastUpdate.text = DateUtils.getRelativeTimeSpanString(
                calamity.lastUpdated.toEpochMilliseconds(),
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
            )
        }

        binding.root.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle(calamity.title)
                .setMessage(calamity.description)
                .setPositiveButton(R.string.ok) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
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
