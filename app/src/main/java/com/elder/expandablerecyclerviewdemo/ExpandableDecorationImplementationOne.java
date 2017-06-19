package com.elder.expandablerecyclerviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.elder.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.elder.expandablerecyclerview.ExpandableRecyclerViewItemDecoration;


/**
 * Created by chris on 2017-06-14.
 */

public class ExpandableDecorationImplementationOne extends ExpandableRecyclerViewItemDecoration
{
    //================================================================================
    // region VARIABLES

    private Drawable dividerWithInset;
    private Drawable dividerNoInset;

    // endregion
    //================================================================================

    //================================================================================
    // region CONSTRUCTOR

    public ExpandableDecorationImplementationOne(Context context)
    {
        dividerWithInset = ContextCompat.getDrawable(context, R.drawable.list_divider_with_inset);
        dividerNoInset = ContextCompat.getDrawable(context, R.drawable.list_divider_no_inset);
    }

    // endregion
    //================================================================================

    //================================================================================
    // region GET OFFSETS

    @Override
    public void getHeaderOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state,
                                 boolean subHeaderExists,
                                 int sectionIndex,
                                 int expansionState,
                                 int sectionContentSize)
    {
        // Headers don't have any decoration - they are a different color for this implementation
    }

    @Override
    public void getSubHeaderOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state,
                                    int sectionIndex,
                                    int expansionState,
                                    int sectionContentSize)
    {
        // Sub header - only has a bottom divider if there is content showing below it
        if(expansionState == ExpandableRecyclerViewAdapter.EXPANSION_STATE_EXPANDED &&
                sectionContentSize != 0)
        {
            // Headers have a solid bottom border
            outRect.bottom = dividerNoInset.getIntrinsicHeight();
        }
    }

    @Override
    public void getContentRowOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state,
                                     boolean subHeaderExists,
                                     int sectionIndex,
                                     int expansionState,
                                     int sectionContentSize,
                                     int sectionContentIndex)
    {
        // Don't put any decoration on the last row in the recycler view
        // Also don't put any decoration on the last content row of any section
        if(parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() ||
                sectionContentIndex == sectionContentSize - 1)
        {
            return;
        }

        // use the divider with inset
        outRect.bottom = dividerWithInset.getIntrinsicHeight();
    }

    // endregion
    //================================================================================

    //================================================================================
    // region ON DRAW

    @Override
    public void onDrawSectionHeader(Canvas canvas, RecyclerView parent, RecyclerView.State state,
                                    View childRow,
                                    boolean subHeaderExists,
                                    int sectionIndex,
                                    int expansionState,
                                    int sectionContentSize)
    {
        // No decoration for the header in this implementation
    }

    @Override
    public void onDrawSectionSubHeader(Canvas canvas, RecyclerView parent, RecyclerView.State state,
                                       View childRow,
                                       int sectionIndex,
                                       int expansionState,
                                       int sectionContentSize)
    {

        // Only draw when its expanded and there is content in the section
        if(expansionState == ExpandableRecyclerViewAdapter.EXPANSION_STATE_EXPANDED &&
                sectionContentSize != 0)
        {
            // Set the alpha for animation
            float alpha = childRow.getAlpha();
            dividerNoInset.setAlpha((int)(alpha * 255));

            // Get translation for animations
            int translationY = (int)(childRow.getTranslationY() + 0.5);

            // Get padding on the row to position the divider
            int dividerLeft = parent.getPaddingLeft();
            int dividerRight = parent.getWidth() - parent.getPaddingRight();

            // get the recycler view layout parameters to position the divider
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childRow.getLayoutParams();
            int dividerTop = childRow.getBottom() + params.bottomMargin + translationY;
            int dividerBottom = dividerTop + dividerNoInset.getIntrinsicHeight();

            // Draw the divider
            dividerNoInset.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            dividerNoInset.draw(canvas);
        }
    }

    @Override
    public void onDrawContent(Canvas canvas, RecyclerView parent, RecyclerView.State state,
                              View childRow,
                              boolean subHeaderExists,
                              int sectionIndex,
                              int expansionState,
                              int sectionContentSize,
                              int sectionContentIndex)
    {
        // Set the alpha for animation
        float alpha = childRow.getAlpha();
        dividerWithInset.setAlpha((int)(alpha * 255));

        // Get translation for animations
        int translationY = (int)(childRow.getTranslationY() + 0.5);

        // Get padding on the row to position the divider
        int dividerLeft = parent.getPaddingLeft();
        int dividerRight = parent.getWidth() - parent.getPaddingRight();

        // get the recycler view layout parameters to position the divider
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childRow.getLayoutParams();
        int dividerTop = childRow.getBottom() + params.bottomMargin + translationY;
        int dividerBottom = dividerTop + dividerWithInset.getIntrinsicHeight();

        // Draw the divider
        dividerWithInset.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
        dividerWithInset.draw(canvas);
    }


    // endregion
    //================================================================================
}
