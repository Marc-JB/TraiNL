package nl.marc_apps.ovgo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import nl.marc_apps.ovgo.databinding.FragmentDisruptionsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DisruptionsFragment : Fragment() {
    private val viewModel by viewModel<HomeViewModel>()

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
            if (it == null) {
                binding.placeholderListDisruptions.visibility = View.VISIBLE
                binding.listDisruptions.visibility = View.GONE
            } else {
                binding.placeholderListDisruptions.visibility = View.GONE
                binding.listDisruptions.visibility = View.VISIBLE
                binding.listDisruptions.scheduleLayoutAnimation()
                disruptionsAdapter.submitList(it.toList())
            }
        }

        viewModel.loadDisruptionsAndMaintenance()
    }
}
