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

public class ExpandableDecorationImplementationTwo extends ExpandableRecyclerViewItemDecoration
{
    //================================================================================
    // region VARIABLES

    private Drawable dividerWithInset;
    private Drawable dividerNoInset;
    private Drawable dividerNoInsetThick;

    // endregion
    //================================================================================

    //================================================================================
    // region CONSTRUCTOR

    public ExpandableDecorationImplementationTwo(Context context)
    {
        dividerWithInset = ContextCompat.getDrawable(context, R.drawable.list_divider_with_inset);
        dividerNoInset = ContextCompat.getDrawable(context, R.drawable.list_divider_no_inset);
        dividerNoInsetThick = ContextCompat.getDrawable(context, R.drawable.list_divider_no_inset_thick);
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
        // don't add a divider at the end
        if(parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount())
        {
            // if its loading use a thin divider, otherwise use a thick one
            if(expansionState == ExpandableRecyclerViewAdapter.EXPANSION_STATE_LOADING_CONTENT ||
                    expansionState == ExpandableRecyclerViewAdapter.EXPANSION_STATE_ERROR_LOADING)
            {
                outRect.bottom = dividerNoInset.getIntrinsicHeight();
            }
            else
            {
                outRect.bottom = dividerNoInsetThick.getIntrinsicHeight();
            }
        }
    }

    @Override
    public void getSubHeaderOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state,
                                    int sectionIndex,
                                    int expansionState,
                                    int sectionContentSize)
    {
        // Sub headers always have bottom dividers
        outRect.bottom = dividerNoInsetThick.getIntrinsicHeight();
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
        if(parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount())
        {
            return;
        }

        // For last content row we use a thick divider
        // For the others we use a divider with an inset
        if(sectionContentIndex == sectionContentSize - 1)
        {
            outRect.bottom = dividerNoInsetThick.getIntrinsicHeight();
        }
        else
        {
            outRect.bottom = dividerWithInset.getIntrinsicHeight();
        }
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
        // Only use the bottom if its not the last row
        if(parent.getChildAdapterPosition(childRow) != parent.getAdapter().getItemCount())
        {
            // if its loading or error use a thin divider, otherwise use a thick one
            if(expansionState == ExpandableRecyclerViewAdapter.EXPANSION_STATE_LOADING_CONTENT ||
                    expansionState == ExpandableRecyclerViewAdapter.EXPANSION_STATE_ERROR_LOADING)
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
            else
            {
                // Set the alpha for animation
                float alpha = childRow.getAlpha();
                dividerNoInsetThick.setAlpha((int)(alpha * 255));

                // Get translation for animations
                int translationY = (int)(childRow.getTranslationY() + 0.5);

                // Get padding on the row to position the divider
                int dividerLeft = parent.getPaddingLeft();
                int dividerRight = parent.getWidth() - parent.getPaddingRight();

                // get the recycler view layout parameters to position the divider
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childRow.getLayoutParams();
                int dividerTop = childRow.getBottom() + params.bottomMargin + translationY;
                int dividerBottom = dividerTop + dividerNoInsetThick.getIntrinsicHeight();

                // Draw the divider
                dividerNoInsetThick.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
                dividerNoInsetThick.draw(canvas);
            }


        }
    }

    @Override
    public void onDrawSectionSubHeader(Canvas canvas, RecyclerView parent, RecyclerView.State state,
                                       View childRow,
                                       int sectionIndex,
                                       int expansionState,
                                       int sectionContentSize)
    {
        // Set the alpha for animation
        float alpha = childRow.getAlpha();
        dividerNoInsetThick.setAlpha((int)(alpha * 255));

        // Get translation for animations
        int translationY = (int)(childRow.getTranslationY() + 0.5);

        // Get padding on the row to position the divider
        int dividerLeft = parent.getPaddingLeft();
        int dividerRight = parent.getWidth() - parent.getPaddingRight();

        // get the recycler view layout parameters to position the divider
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childRow.getLayoutParams();
        int dividerTop = childRow.getBottom() + params.bottomMargin + translationY;
        int dividerBottom = dividerTop + dividerNoInsetThick.getIntrinsicHeight();

        // Draw the divider
        dividerNoInsetThick.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
        dividerNoInsetThick.draw(canvas);
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
        // Use divider with no inset for the last item in the section
        if(sectionContentIndex == sectionContentSize - 1)
        {
            // Set the alpha for animation
            float alpha = childRow.getAlpha();
            dividerNoInsetThick.setAlpha((int)(alpha * 255));

            // Get translation for animations
            int translationY = (int)(childRow.getTranslationY() + 0.5);

            // Get padding on the row to position the divider
            int dividerLeft = parent.getPaddingLeft();
            int dividerRight = parent.getWidth() - parent.getPaddingRight();

            // get the recycler view layout parameters to position the divider
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childRow.getLayoutParams();
            int dividerTop = childRow.getBottom() + params.bottomMargin + translationY;
            int dividerBottom = dividerTop + dividerNoInsetThick.getIntrinsicHeight();

            // Draw the divider
            dividerNoInsetThick.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            dividerNoInsetThick.draw(canvas);
        }
        else
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
    }


    // endregion
    //================================================================================
}
