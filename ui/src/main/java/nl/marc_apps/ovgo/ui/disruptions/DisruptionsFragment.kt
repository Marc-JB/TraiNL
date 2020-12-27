package nl.marc_apps.ovgo.ui.disruptions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import nl.marc_apps.ovgo.ui.databinding.FragmentDisruptionsBinding
import org.koin.android.viewmodel.ext.android.viewModel

class DisruptionsFragment : Fragment() {
    private val viewModel by viewModel<DisruptionsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentDisruptionsBinding.inflate(layoutInflater, container, false)

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.swipeRefreshLayout.isRefreshing = it
        }

        val adapter = DisruptionsAdapter(viewModel.disruptions)
        binding.recyclerView.adapter = adapter
        viewModel.disruptions.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }
}
