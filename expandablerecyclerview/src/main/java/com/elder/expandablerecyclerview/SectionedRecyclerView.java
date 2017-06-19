package com.elder.expandablerecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Created by chris on 2016-12-11.
 */

public class SectionedRecyclerView extends RecyclerView
{
    //================================================================================
    // region CONSTRUCTOR

    public SectionedRecyclerView(Context context)
    {
        super(context);
    }

    // endregion
    //================================================================================

    //================================================================================
    // region SCROLL LISTENER

    private void addScrollListener()
    {
        this.addOnScrollListener(new OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    // endregion
    //================================================================================

    //================================================================================
    // region UPDATING METHODS

    public void updateRecyclerView()
    {

        //----------------- update decorations -----------------\\
        invalidateItemDecorations();

    }

    // endregion
    //================================================================================
}
