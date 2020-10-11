package nl.marc_apps.ovgo.ui.departures

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import nl.marc_apps.ovgo.domain.models.Departure
import nl.marc_apps.ovgo.ui.R
import nl.marc_apps.ovgo.ui.databinding.DepartureBinding

class DeparturesAdapter(
    private val departures: LiveData<Set<Departure>>
) : RecyclerView.Adapter<DeparturesAdapter.ArticleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = DepartureBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val departure = departures.value?.elementAt(position) ?: return
        val binding = holder.binding

        binding.departureTime.text = departure.departureTimeText
        binding.departureTime.setTextColor(
            ContextCompat.getColor(
                binding.departureTime.context,
                if(departure.isDelayed || departure.cancelled) R.color.colorError else R.color.colorOK
            )
        )

        binding.delay.text = departure.delayText
        binding.delay.visibility = if(departure.isDelayed) View.VISIBLE else View.INVISIBLE

        binding.direction.text = departure.direction.toString()
        binding.direction.setTextColor(
            ContextCompat.getColor(
                binding.direction.context,
                if(departure.cancelled) R.color.colorError else R.color.colorPrimary
            )
        )

        binding.routeStations.text = departure.routeStationsText
        binding.routeStations.visibility =
            if(departure.routeStationsText.isEmpty() || departure.cancelled) View.GONE
            else View.VISIBLE

        binding.cancelled.visibility = if(departure.cancelled) View.VISIBLE else View.GONE

        binding.operator.text = departure.operator + "\n" + departure.category
        binding.operator.visibility = if(departure.cancelled) View.VISIBLE else View.GONE

        binding.platform.setBackgroundResource(
            if(departure.platformChanged || departure.cancelled) R.drawable.platform_background_style_red
            else R.drawable.platform_background_style
        )
        binding.platform.text = departure.actualPlatform
        binding.platform.visibility = if(departure.cancelled) View.GONE else View.VISIBLE

        binding.facilities.visibility = if(departure.cancelled) View.GONE else View.VISIBLE

        binding.wheelchairAccessible.visibility =
            if(departure.trainComposition.facilities.wheelchairAccessible) View.VISIBLE
            else View.GONE

        binding.toilet.visibility =
            if(departure.trainComposition.facilities.toilet) View.VISIBLE
            else View.GONE

        binding.bicycles.visibility =
            if(departure.trainComposition.facilities.bicycles) View.VISIBLE
            else View.GONE

        binding.wifi.visibility =
            if(departure.trainComposition.facilities.wifi) View.VISIBLE
            else View.GONE

        binding.powerSockets.visibility =
            if(departure.trainComposition.facilities.powerSockets) View.VISIBLE
            else View.GONE

        binding.silenceCompartment.visibility =
            if(departure.trainComposition.facilities.silenceCompartment) View.VISIBLE
            else View.GONE

        binding.firstClass.visibility =
            if(departure.trainComposition.facilities.firstClass) View.VISIBLE
            else View.GONE

        binding.operatorAndCategory.text = departure.operator + " • " + departure.category

        binding.trainImagesScrollView.visibility =
            if(departure.cancelled || departure.trainComposition.images.firstOrNull() == null) View.GONE
            else View.VISIBLE

        binding.trainImagesContainer.removeAllViews()
        departure.trainComposition.images.forEach {
            val imageView = ImageView(binding.trainImagesContainer.context).apply {
                layoutParams = LinearLayoutCompat.LayoutParams(
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                    LinearLayoutCompat.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.CENTER_VERTICAL
                }
                adjustViewBounds = true
            }
            binding.trainImagesContainer.addView(imageView)
            Picasso.get().load(it).into(imageView)
        }
    }

    override fun getItemCount() = departures.value?.size ?: 0

    class ArticleViewHolder(val binding: DepartureBinding): RecyclerView.ViewHolder(binding.root)
}