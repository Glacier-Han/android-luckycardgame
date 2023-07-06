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

        // 6개일때는 겹치지 않음 (0), 7개일때부터 조금씩 간격 추가
        overlapParam += ((state.itemCount - 6) * (-25))
        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == 0) {
            return
        }
        outRect[overlapParam, 0, 0] = 0
    }
}
