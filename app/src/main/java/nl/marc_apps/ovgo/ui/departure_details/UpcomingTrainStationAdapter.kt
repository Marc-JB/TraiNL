package nl.marc_apps.ovgo.ui.departure_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import nl.marc_apps.ovgo.databinding.ListItemStationButtonBinding
import nl.marc_apps.ovgo.domain.TrainStation

class UpcomingTrainStationAdapter : ListAdapter<TrainStation, UpcomingTrainStationAdapter.ViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemStationButtonBinding.inflate(
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
        val station = currentList.elementAtOrNull(position) ?: return

        holder.binding.root.text = buildString {
            append(station.fullName)

            if (isForeignStation(station)) {
                append(" ")
                append(station.country?.flag)
            }
        }

        holder.binding.root.setOnClickListener {
            val action = DepartureDetailsFragmentDirections
                .actionDepartureDetailsToStationDepartureBoard(station)
            holder.binding.root.findNavController().navigate(action)
        }
    }

    class ViewHolder(val binding: ListItemStationButtonBinding) : RecyclerView.ViewHolder(binding.root)

    object DiffCallback : DiffUtil.ItemCallback<TrainStation>() {
        override fun areItemsTheSame(oldItem: TrainStation, newItem: TrainStation): Boolean {
            return oldItem.uicCode == newItem.uicCode
        }

        override fun areContentsTheSame(oldItem: TrainStation, newItem: TrainStation): Boolean {
            return oldItem == newItem
        }
    }
}
