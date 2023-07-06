package com.glacier.luckycardgamesofteer

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class OverlapDecoration() : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        var overlapParam = 0
        overlapParam += ((state.itemCount - 6) * (-25))
        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == 0) {
            return
        }
        outRect[overlapParam, 0, 0] = 0
    }
}
