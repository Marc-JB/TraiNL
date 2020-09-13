package nl.marc_apps.ovgo.ui.extensions

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Picasso

class ImageList @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr)

@BindingAdapter("images")
fun loadImages(imageList: ImageList, list: List<String>?) {
    imageList.removeAllViews()
    list?.forEach {
        val imageView = ImageView(imageList.context).apply {
            layoutParams = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_VERTICAL
            }
            adjustViewBounds = true
        }
        imageList.addView(imageView)
        Picasso.get().load(it).into(imageView)
    }
}

/**
 * A combination of [Array.map] and [Array.firstOrNull].
 * Returns the result of [predicate] on the first item that is not null.
 */
inline fun <T, R> Array<out T>.takeFirstOrNull(predicate: (T) -> R?): R? {
    for (element in this) {
        val item = predicate(element)
        if(item != null) return item
    }
    return null
}

/**
 * Binding adapter for [RecyclerView.setLayoutManager].
 * Creates a class using the path specified at [manager],
 * or uses null as layout manager when the class could not be found or created.
 */
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

/**
 * Binding adapter for [RecyclerView.setAdapter].
 */
@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>){
    view.adapter = adapter
}

/**
 * Calls [View.setVisibility] of the [view] with [View.VISIBLE] when [visible] is true, otherwise [View.GONE].
 */
@BindingAdapter("goneUnless")
fun makeViewGoneUnless(view: View, visible: Boolean){
    view.visibility = if(visible) View.VISIBLE else View.GONE
}

/**
 * Calls [View.setVisibility] of the [view] with [View.VISIBLE] when [visible] is true, otherwise [View.INVISIBLE].
 */
@BindingAdapter("invisibleUnless")
fun makeViewInvisibleUnless(view: View, visible: Boolean){
    view.visibility = if(visible) View.VISIBLE else View.INVISIBLE
}

/**
 * Calls [View.setVisibility] of the [view] with [View.GONE] when [gone] is true, otherwise [View.VISIBLE].
 */
@BindingAdapter("visibleUnless")
fun makeViewVisibleUnless(view: View, gone: Boolean){
    view.visibility = if(gone) View.GONE else View.VISIBLE
}

/**
 * Binding adapter for [SwipeRefreshLayout.setRefreshing].
 */
@BindingAdapter("isRefreshing")
fun setRefreshing(view: SwipeRefreshLayout, isRefreshing: Boolean){
    view.isRefreshing = isRefreshing
}