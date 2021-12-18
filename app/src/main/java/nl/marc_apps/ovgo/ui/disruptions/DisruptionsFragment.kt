package nl.marc_apps.ovgo.ui.disruptions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.data.api.dutch_railways.models.DutchRailwaysDisruption
import nl.marc_apps.ovgo.databinding.FragmentDisruptionsBinding
import nl.marc_apps.ovgo.ui.DisruptionsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class DisruptionsFragment : Fragment() {
    private val viewModel by viewModel<DisruptionsViewModel>()

    private lateinit var binding: FragmentDisruptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDisruptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val disruptionsAdapter = DisruptionsAdapter()
        binding.listDisruptions.adapter = disruptionsAdapter
        binding.listDisruptions.addItemDecoration(
            DividerItemDecoration(binding.listDisruptions.context, DividerItemDecoration.VERTICAL)
        )

        viewModel.disruptions.observe(viewLifecycleOwner) {
            binding.partialImageWithLabelPlaceholder.root.visibility = View.GONE
            binding.placeholderListDisruptions.visibility = View.GONE
            binding.listDisruptions.visibility = View.GONE

            when {
                it == null -> {
                    binding.placeholderListDisruptions.visibility = View.VISIBLE
                }
                it.isEmpty() -> {
                    binding.partialImageWithLabelPlaceholder.image.setImageResource(R.drawable.va_travelling)
                    binding.partialImageWithLabelPlaceholder.label.setText(R.string.no_disruptions)
                    binding.partialImageWithLabelPlaceholder.root.visibility = View.VISIBLE
                }
                else -> {
                    binding.listDisruptions.visibility = View.VISIBLE
                    binding.listDisruptions.scheduleLayoutAnimation()
                    disruptionsAdapter.submitList(it.sortedByDescending {
                        when((it as? DutchRailwaysDisruption.Calamity)?.priority) {
                            DutchRailwaysDisruption.Calamity.Priority.PRIO_1 -> 3
                            DutchRailwaysDisruption.Calamity.Priority.PRIO_2 -> 2
                            DutchRailwaysDisruption.Calamity.Priority.PRIO_3 -> 1
                            null -> 0
                        }
                    })
                }
            }
        }

        viewModel.loadDisruptions()
    }
}
