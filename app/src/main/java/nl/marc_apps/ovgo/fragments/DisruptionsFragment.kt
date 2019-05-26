package nl.marc_apps.ovgo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import nl.marc_apps.ovgo.R
import nl.marc_apps.ovgo.adapters.GenericDataBindingAdapter
import nl.marc_apps.ovgo.databinding.DisruptionsFragmentBinding
import nl.marc_apps.ovgo.viewmodels.DisruptionsViewModel

class DisruptionsFragment : Fragment() {
    companion object {
        fun newInstance() = DisruptionsFragment()
    }

    private val viewModel by viewModels<DisruptionsViewModel>()

    private val adapter by lazy {
        GenericDataBindingAdapter(viewModel.disruptions, R.layout.disruption, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<DisruptionsFragmentBinding>(layoutInflater, R.layout.disruptions_fragment, container, false).also {
            it.viewModel = viewModel
            it.adapter = adapter
            it.lifecycleOwner = this
        }.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.languageCode = resources.getString(R.string.languageCode)
        viewModel.loadDisruptions()
    }
}
