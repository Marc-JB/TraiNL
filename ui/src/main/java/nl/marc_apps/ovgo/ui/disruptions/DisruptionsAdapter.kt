package nl.marc_apps.ovgo.ui.disruptions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import nl.marc_apps.ovgo.domain.models.Disruption
import nl.marc_apps.ovgo.ui.R
import nl.marc_apps.ovgo.ui.databinding.DisruptionBinding

class DisruptionsAdapter(
    private val disruptions: LiveData<Set<Disruption>>
) : RecyclerView.Adapter<DisruptionsAdapter.ArticleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = DisruptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val disruption = disruptions.value?.elementAt(position) ?: return
        val binding = holder.binding

        val drawableStart = if(disruption.warningLevel > 0) R.drawable.ic_error else R.drawable.ic_warning
        binding.title.setCompoundDrawablesWithIntrinsicBounds(drawableStart, 0, 0, 0)
        binding.title.text = disruption.title
        binding.title.setTextColor(
            ContextCompat.getColor(
                binding.title.context,
                if(disruption.warningLevel > 0) R.color.colorError else R.color.black
            )
        )

        binding.description.text = disruption.description

        binding.dateAndTime.text = disruption.startDateText + if(disruption.endDateText == null) "" else "\n" + disruption.endDateText
        binding.dateAndTime.visibility = if(disruption.startDate == null) View.GONE else View.VISIBLE
    }

    override fun getItemCount() = disruptions.value?.size ?: 0

    class ArticleViewHolder(val binding: DisruptionBinding): RecyclerView.ViewHolder(binding.root)
}