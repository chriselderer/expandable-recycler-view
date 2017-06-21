package com.elder.expandablerecyclerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by chris on 2016-12-10.
 *
 * This abstract class adds expandable section functionality to RecyclerView.ItemDecoration
 */
public abstract class ExpandableRecyclerViewItemDecoration extends RecyclerView.ItemDecoration
{
    //================================================================================
    // region DECORATION OVERRIDES

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        super.getItemOffsets(outRect, view, parent, state);

        // get reference to the adapter
        ExpandableRecyclerViewAdapter adapter = (ExpandableRecyclerViewAdapter) parent.getAdapter();

        // find the recycler view index
        int recyclerViewIndex = parent.getChildAdapterPosition(view);

        // get the section index for this position
        int sectionIndex = adapter.getSectionIndexForPosition(recyclerViewIndex);

        // check if sub header exists
        boolean subHeaderExists = adapter.sectionSubHeaderExists(sectionIndex);

        // get expansion state of section
        int expansionState = ((ExpandableRecyclerViewSection) adapter.sections.get(sectionIndex)).getSectionExpansionState();

        // get section content size
        int contentSize = ((ExpandableRecyclerViewSection) adapter.sections.get(sectionIndex)).getSectionContent() != null ?
                ((ExpandableRecyclerViewSection) adapter.sections.get(sectionIndex)).getSectionContent().size() : 0;

        if(adapter.recyclerViewIndexIsSectionHeader(recyclerViewIndex))
        {
            // Header view
            getHeaderOffsets(outRect, view, parent, state,
                    subHeaderExists,
                    sectionIndex,
                    expansionState,
                    contentSize);
        }
        else if(adapter.recyclerViewIndexIsSectionSubHeader(recyclerViewIndex))
        {
            // Sub header view
            getSubHeaderOffsets(outRect, view, parent, state,
                    sectionIndex,
                    expansionState,
                    contentSize);
        }
        else
        {
            int contentIndex = adapter.getContentIndexForSectionWithRecyclerViewIndex(sectionIndex, recyclerViewIndex);

            // Content view
            getContentRowOffsets(outRect, view, parent, state,
                    subHeaderExists,
                    sectionIndex,
                    expansionState,
                    contentSize,
                    contentIndex);
        }
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state)
    {
        // get reference to the adapter
        ExpandableRecyclerViewAdapter adapter = (ExpandableRecyclerViewAdapter) parent.getAdapter();

        // Get the first visible view
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        int firstVisible = layoutManager.findFirstVisibleItemPosition();

        // Get count of items in the recycler view
        int childCount = layoutManager.getChildCount(); //parent.getChildCount();

        // Add decorations to all visible rows
        for (int i = 0; i < childCount - 1; i++)
        {
            // get the current view
            View child = parent.getChildAt(i);

            // remove old decoration
            child.refreshDrawableState();

            // get index of the recycler view for this child
            int recyclerViewIndex = i + firstVisible;

            // get the section index for this position
            int sectionIndex = adapter.getSectionIndexForPosition(recyclerViewIndex);

            // check if sub header exists
            boolean subHeaderExists = adapter.sectionSubHeaderExists(sectionIndex);

            // get expansion state of section
            int expansionState = ((ExpandableRecyclerViewSection) adapter.sections.get(sectionIndex)).getSectionExpansionState();

            // get section content size
            int contentSize = ((ExpandableRecyclerViewSection) adapter.sections.get(sectionIndex)).getSectionContent() != null ?
                    ((ExpandableRecyclerViewSection) adapter.sections.get(sectionIndex)).getSectionContent().size() : 0;

            // Check what kind of view it is, and call the specific draw method
            if(adapter.recyclerViewIndexIsSectionHeader(recyclerViewIndex))
            {
                // Header view
                onDrawSectionHeader(canvas, parent, state,
                        child,
                        subHeaderExists,
                        sectionIndex,
                        expansionState,
                        contentSize);
            }
            else if(adapter.recyclerViewIndexIsSectionSubHeader(recyclerViewIndex))
            {
                // Sub header view
                onDrawSectionSubHeader(canvas, parent, state,
                        child,
                        sectionIndex,
                        expansionState,
                        contentSize);
            }
            else
            {
                int contentIndex = adapter.getContentIndexForSectionWithRecyclerViewIndex(sectionIndex, recyclerViewIndex);

                // Content view
                onDrawContent(canvas, parent, state,
                        child,
                        subHeaderExists,
                        sectionIndex,
                        expansionState,
                        contentSize,
                        contentIndex);
            }
        }
    }

    // endregion
    //================================================================================

    //================================================================================
    // region SPECIFIC ITEM OFFSET METHODS

    /**
     * Used to set the offsets of each header view in the RecyclerView
     *
     * @param outRect used to set offsets
     * @param view row view for which offsets are being set
     * @param parent parent view (RecyclerView)
     * @param state RecyclerView state
     * @param subHeaderExists true if this section includes a sub header (row below the header)
     * @param sectionIndex index of the section this header belongs to
     * @param expansionState expansion state of this section
     * @param sectionContentSize size of the content List (ie. 0 if there is no content)
     */
    public abstract void getHeaderOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state,
                                          boolean subHeaderExists,
                                          int sectionIndex,
                                          int expansionState,
                                          int sectionContentSize);


    /**
     * Used to set the offsets of each sub header view in the RecyclerView
     *
     * @param outRect used to set offsets
     * @param view row view for which offsets are being set
     * @param parent parent view (RecyclerView)
     * @param state RecyclerView state
     * @param sectionIndex index of the section this header belongs to
     * @param expansionState expansion state of this section
     * @param sectionContentSize size of the content List (ie. 0 if there is no content)
     */
    public abstract void getSubHeaderOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state,
                                             int sectionIndex,
                                             int expansionState,
                                             int sectionContentSize);

    /**
     * Used to set the offsets of each section content row in the RecyclerView
     *
     * @param outRect used to set offsets
     * @param view row view for which offsets are being set
     * @param parent parent view (RecyclerView)
     * @param state RecyclerView state
     * @param subHeaderExists true if this section includes a sub header (row below the header)
     * @param sectionIndex index of the section this header belongs to
     * @param expansionState expansion state of this section
     * @param sectionContentSize size of the content List (ie. 0 if there is no content)
     * @param sectionContentIndex index of the data source object for this row, in the content List for this section.
     *                            (ie. index == 2 --> 3rd content item in this section).
     */
    public abstract void getContentRowOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state,
                                              boolean subHeaderExists,
                                              int sectionIndex,
                                              int expansionState,
                                              int sectionContentSize,
                                              int sectionContentIndex);

    // endregion
    //================================================================================

    //================================================================================
    // region SPECIFIC ON DRAW METHODS

    /**
     * Used to provide a decoration for a Header row.
     *
     * Information about the section is provided to facilitate changing the decoration for different
     * states. (ex. change the decoration when a sub header row is displayed below the header).
     *
     * @param canvas Canvas used to draw the decoration
     * @param parent parent view (RecyclerView)
     * @param state RecyclerView state
     * @param childRow The child view of the recyclerView corresponding to this row
     * @param subHeaderExists true if this section includes a sub header (row below the header)
     * @param sectionIndex index of the section this header belongs to
     * @param expansionState expansion state of this section
     * @param sectionContentSize size of the content List (ie. 0 if there is no content)
     */
    public abstract void onDrawSectionHeader(Canvas canvas, RecyclerView parent, RecyclerView.State state,
                                             View childRow,
                                             boolean subHeaderExists,
                                             int sectionIndex,
                                             int expansionState,
                                             int sectionContentSize);


    /**
     *
     * Used to provide a decoration for a Sub Header row.
     *
     * Information about the section is provided to facilitate changing the decoration for different
     * states.
     * Ex. change the decoration when the section is expanded or minimized
     *
     * @param canvas Canvas used to draw the decoration
     * @param parent parent view (RecyclerView)
     * @param state RecyclerView state
     * @param childRow The child view of the recyclerView corresponding to this row
     * @param sectionIndex index of the section this sub header belongs to
     * @param expansionState expansion state of this section
     * @param sectionContentSize size of the content List (ie. 0 if there is no content)
     */
    public abstract void onDrawSectionSubHeader(Canvas canvas, RecyclerView parent, RecyclerView.State state,
                                                View childRow,
                                                int sectionIndex,
                                                int expansionState,
                                                int sectionContentSize);

    /**
     *
     * Used to provide a decoration for a Content row.
     *
     * Information about the section is provided to facilitate changing the decoration for different
     * states.
     * Ex. Don't draw a decoration on the first or last row of the content (calculated using the
     * sectionContentSize and sectionContentIndex).
     *
     * @param canvas Canvas used to draw the decoration
     * @param parent parent view (RecyclerView)
     * @param state RecyclerView state
     * @param childRow The child view of the recyclerView corresponding to this row
     * @param subHeaderExists true if this section contains a sub header
     * @param sectionIndex index of the section this content row belongs to
     * @param expansionState expansion state of this section
     * @param sectionContentSize size of the content List (ie. 0 if there is no content)
     * @param sectionContentIndex index of the data source object for this row, in the content List for this section.
     *                            (ie. index == 2 --> 3rd content item in this section).
     */
    public abstract void onDrawContent(Canvas canvas, RecyclerView parent, RecyclerView.State state,
                                       View childRow,
                                       boolean subHeaderExists,
                                       int sectionIndex,
                                       int expansionState,
                                       int sectionContentSize,
                                       int sectionContentIndex);

    // endregion
    //================================================================================
}
