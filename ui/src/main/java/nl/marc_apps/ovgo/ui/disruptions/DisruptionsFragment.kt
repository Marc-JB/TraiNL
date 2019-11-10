package nl.marc_apps.ovgo.ui.disruptions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import nl.marc_apps.ovgo.ui.R
import nl.marc_apps.ovgo.ui.GenericDataBindingAdapter
import nl.marc_apps.ovgo.ui.databinding.FragmentDisruptionsBinding
import org.koin.android.viewmodel.ext.android.viewModel

class DisruptionsFragment : Fragment() {
    private val viewModel by viewModel<DisruptionsViewModel>()

    private val adapter by lazy {
        GenericDataBindingAdapter(viewModel.disruptions, R.layout.disruption, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<FragmentDisruptionsBinding>(layoutInflater, R.layout.fragment_disruptions, container, false).also {
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
