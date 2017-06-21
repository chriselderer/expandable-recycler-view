package com.elder.expandablerecyclerview;

import android.view.View;

/**
 * Created by chris on 2017-06-15.
 *
 * Interface to be implemented by the class you want to respond to click events on the RecyclerView row child views.
 */
public interface ExpandableRowSubViewOnClickListener<H, SH, C>
{
    //================================================================================
    // region CLICK EVENTS

    /**
     * Called when a child view inside a header row is clicked
     *
     * @param view View that was clicked
     * @param header H - header data source of the clicked row
     * @param sectionIndex index of the section that was clicked
     * @param expansionState the expansion state of the section at sectionIndex
     */
    void onSubViewClickedInSectionHeader(View view, H header, int sectionIndex, int expansionState);

    /**
     * Called when a child view inside a sub header row is clicked
     *
     * @param view View that was clicked
     * @param subHeader SH - sub header data source of the clicked row
     * @param sectionIndex index of the section that was clicked
     * @param expansionState the expansion state of the section at sectionIndex
     */
    void onSubViewClickedInSectionSubHeader(View view, SH subHeader, int sectionIndex, int expansionState);

    /**
     * Called when a child view inside a content row is clicked
     *
     * @param view View that was clicked
     * @param content C - content object for the clicked row
     * @param sectionIndex index of the section that was clicked
     * @param sectionContentIndex index of the content in the data source
     */
    void onSubViewClickedInSectionContentRow(View view, C content, int sectionIndex, int sectionContentIndex);

    // endregion
    //================================================================================
}
