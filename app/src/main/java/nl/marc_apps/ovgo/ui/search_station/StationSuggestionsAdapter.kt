package nl.marc_apps.ovgo.ui.search_station

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import nl.marc_apps.ovgo.databinding.ListItemStationSuggestionBinding
import nl.marc_apps.ovgo.domain.TrainStation

class StationSuggestionsAdapter : ListAdapter<TrainStation, StationSuggestionsAdapter.StationSuggestionViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationSuggestionViewHolder {
        val binding = ListItemStationSuggestionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StationSuggestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StationSuggestionViewHolder, position: Int) {
        val stationSuggestion = currentList.elementAtOrNull(position) ?: return

        holder.binding.labelPrimaryName.text = stationSuggestion.name

        if(stationSuggestion.alternativeNames.isEmpty()) {
            holder.binding.labelSecondaryNames.visibility = View.GONE
        } else {
            holder.binding.labelSecondaryNames.text = stationSuggestion.alternativeNames.joinToString()
            holder.binding.labelSecondaryNames.visibility = View.VISIBLE
        }

        holder.binding.root.setOnClickListener {
            val action = SearchStationFragmentDirections.actionStationSearchToHome(stationSuggestion)
            holder.binding.root.findNavController().navigate(action)
        }
    }

    class StationSuggestionViewHolder(val binding: ListItemStationSuggestionBinding) : RecyclerView.ViewHolder(binding.root)

    object DiffCallback : DiffUtil.ItemCallback<TrainStation>() {
        override fun areItemsTheSame(
            oldItem: TrainStation,
            newItem: TrainStation
        ): Boolean {
            return oldItem.uicCode == newItem.uicCode
        }

        override fun areContentsTheSame(
            oldItem: TrainStation,
            newItem: TrainStation
        ): Boolean {
            return oldItem == newItem
        }
    }
}
