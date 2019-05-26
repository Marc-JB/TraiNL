package nl.marc_apps.ovgo.utils

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Picasso

@BindingAdapter("url")
fun loadImage(view: AppCompatImageView, url: String?) {
    view.visibility = if(url.isNullOrBlank()) View.GONE else View.VISIBLE
    if(url != null) Picasso.get().load(url).into(view)
}

inline fun <T, R> Array<out T>.takeFirstOrNull(predicate: (T) -> R?): R? {
    for (element in this) {
        val item = predicate(element)
        if(item != null) return item
    }
    return null
}

@BindingAdapter("layoutManager")
fun setLayoutManager(view: RecyclerView, manager: String){
    view.layoutManager = Class.forName(manager).constructors.takeFirstOrNull {
        try {
            it.newInstance(view.context) as? RecyclerView.LayoutManager
        } catch (err: Throwable){
            null
        }
    }
}

@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>){
    view.adapter = adapter
}

@BindingAdapter("goneUnless")
fun makeViewGoneUnless(view: View, visible: Boolean){
    view.visibility = if(visible) View.VISIBLE else View.GONE
}

@BindingAdapter("invisibleUnless")
fun makeViewInvisibleUnless(view: View, visible: Boolean){
    view.visibility = if(visible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("visibleUnless")
fun makeViewVisibleUnless(view: View, gone: Boolean){
    view.visibility = if(gone) View.GONE else View.VISIBLE
}

@BindingAdapter("isRefreshing")
fun setRefreshing(view: SwipeRefreshLayout, isRefreshing: Boolean){
    view.isRefreshing = isRefreshing
}