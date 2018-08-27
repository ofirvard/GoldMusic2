package com.example.ofir.goldmusic2;

import android.support.v7.widget.RecyclerView;

/**
 * Created by ofir on 21-Aug-18.
 */

public interface OnStartDragListener
{
    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
