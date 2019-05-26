package nl.marc_apps.ovgo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import nl.marc_apps.ovgo.BR

class GenericDataBindingAdapter<T>(private val myDataset: MutableList<T>, private val layoutId: Int, private val lifecycleOwner: LifecycleOwner? = null): RecyclerView.Adapter<GenericDataBindingAdapter.MyViewHolder>() {
    class MyViewHolder(val dataBinder: ViewDataBinding) : RecyclerView.ViewHolder(dataBinder.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), layoutId, parent, false).also {
            it.lifecycleOwner = lifecycleOwner
        })
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.dataBinder.setVariable(BR.viewModel, myDataset[position])
    }

    override fun getItemCount() = myDataset.size
}