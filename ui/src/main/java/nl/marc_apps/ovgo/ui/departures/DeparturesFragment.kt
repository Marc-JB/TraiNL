package nl.marc_apps.ovgo.ui.departures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import nl.marc_apps.ovgo.ui.GenericDataBindingAdapter
import nl.marc_apps.ovgo.ui.R
import nl.marc_apps.ovgo.ui.databinding.FragmentDeparturesBinding
import org.koin.android.viewmodel.ext.android.viewModel

class DeparturesFragment : Fragment() {
    private val viewModel by viewModel<DeparturesViewModel>()

    private val adapter by lazy {
        GenericDataBindingAdapter(viewModel.departures, R.layout.departure, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DataBindingUtil.inflate<FragmentDeparturesBinding>(layoutInflater, R.layout.fragment_departures, container, false).also {
            it.viewModel = viewModel
            it.adapter = adapter
            it.lifecycleOwner = this
        }.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.languageCode = resources.getString(R.string.languageCode)
        viewModel.station = "Dordrecht"
    }
}
