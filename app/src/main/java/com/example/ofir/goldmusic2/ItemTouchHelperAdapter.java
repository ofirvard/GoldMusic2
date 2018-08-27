package com.example.ofir.goldmusic2;

/**
 * Created by ofir on 21-Aug-18.
 */

public interface ItemTouchHelperAdapter
{
    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
