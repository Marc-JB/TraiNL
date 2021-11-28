package nl.marc_apps.ovgo.ui.departure_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.databinding.ListItemDepartureBinding
import nl.marc_apps.ovgo.databinding.ListItemDepartureCancelledBinding
import nl.marc_apps.ovgo.databinding.ListItemStationButtonBinding
import nl.marc_apps.ovgo.domain.Departure
import nl.marc_apps.ovgo.domain.TrainInfo
import nl.marc_apps.ovgo.domain.TrainStation
import nl.marc_apps.ovgo.ui.TrainImages
import nl.marc_apps.ovgo.ui.departure_board.DepartureBoardFragmentDirections
import nl.marc_apps.ovgo.utils.format
import java.text.DateFormat

class UpcomingTrainStationAdapter : ListAdapter<TrainStation, UpcomingTrainStationAdapter.ViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemStationButtonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val station = currentList.elementAtOrNull(position) ?: return

        holder.binding.root.text = station.name

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
