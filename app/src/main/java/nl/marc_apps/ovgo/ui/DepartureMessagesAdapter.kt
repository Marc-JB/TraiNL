package nl.marc_apps.ovgo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.databinding.ListItemDepartureMessageBinding

class DepartureMessagesAdapter : ListAdapter<Pair<String, Int>, DepartureMessagesAdapter.DepartureMessageViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartureMessageViewHolder {
        val binding = ListItemDepartureMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DepartureMessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DepartureMessageViewHolder, position: Int) {
        val (message, type) = currentList.elementAtOrNull(position) ?: return

        val context = holder.binding.root.context

        holder.binding.root.setTextColor(
            ContextCompat.getColor(context, if(type == TYPE_WARNING) {
                R.color.departureMessageWarningColor
            } else {
                R.color.departureMessageColor
            })
        )

        holder.binding.root.text = message
    }

    class DepartureMessageViewHolder(val binding: ListItemDepartureMessageBinding) : RecyclerView.ViewHolder(binding.root)

    object DiffCallback : DiffUtil.ItemCallback<Pair<String, Int>>() {
        override fun areItemsTheSame(
            oldItem: Pair<String, Int>,
            newItem: Pair<String, Int>
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Pair<String, Int>,
            newItem: Pair<String, Int>
        ): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        const val TYPE_WARNING = 1
        const val TYPE_MESSAGE = 0
    }
}
