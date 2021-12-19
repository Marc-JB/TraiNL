package nl.marc_apps.ovgo.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.res.use
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import kotlin.math.roundToInt

class DividerItemDecoration(context: Context, orientation: Int) : ItemDecoration() {
    lateinit var drawable: Drawable

    private var orientation = orientation
        set(value) {
            require(value == HORIZONTAL || value == VERTICAL) {
                "Invalid orientation. It should be either HORIZONTAL or VERTICAL"
            }

            field = value
        }

    private val bounds = Rect()

    init {
        context.obtainStyledAttributes(ATTRS).use {
            drawable = it.getDrawable(0)!!
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null) {
            return
        }

        canvas.save()

        if (orientation == VERTICAL) {
            drawVertical(canvas, parent)
        } else {
            drawHorizontal(canvas, parent)
        }

        canvas.restore()
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight

            canvas.clipRect(
                left, parent.paddingTop, right,
                parent.height - parent.paddingBottom
            )
        } else {
            left = 0
            right = parent.width
        }

        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, bounds)
            val bottom = bounds.bottom + child.translationY.roundToInt()
            val top = bottom - drawable.intrinsicHeight
            drawable.setBounds(left, top, right, bottom)
            drawable.draw(canvas)
        }
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val top: Int
        val bottom: Int
        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom

            canvas.clipRect(
                parent.paddingLeft, top,
                parent.width - parent.paddingRight, bottom
            )
        } else {
            top = 0
            bottom = parent.height
        }

        val childCount = parent.childCount
        val spanCount = (parent.layoutManager as? GridLayoutManager)?.spanCount
        for (i in 0 until childCount) {
            if (spanCount == null || i % spanCount != spanCount - 1) {
                val child = parent.getChildAt(i)
                parent.layoutManager!!.getDecoratedBoundsWithMargins(child, bounds)
                val right = bounds.right + child.translationX.roundToInt()
                val left = right - drawable.intrinsicWidth
                drawable.setBounds(left, top, right, bottom)
                drawable.draw(canvas)
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (orientation == VERTICAL) {
            if (parent.getChildAdapterPosition(view) == state.itemCount - 1) {
                outRect.setEmpty()
            } else {
                outRect.set(0, 0, 0, drawable.intrinsicHeight)
            }
        } else {
            val spanCount = (parent.layoutManager as? GridLayoutManager)?.spanCount
            if (spanCount != null && parent.getChildAdapterPosition(view) % spanCount == spanCount - 1) {
                outRect.setEmpty()
            } else {
                outRect.set(0, 0, drawable.intrinsicWidth, 0)
            }
        }
    }

    companion object {
        const val HORIZONTAL = LinearLayout.HORIZONTAL

        const val VERTICAL = LinearLayout.VERTICAL

        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }
}
