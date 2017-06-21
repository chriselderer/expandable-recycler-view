package com.elder.expandablerecyclerview;

/**
 * Created by chris on 2017-06-15.
 *
 * Interface to be implemented by the class you want to respond to click events on the RecyclerView rows.
 */
public interface ExpandableRowOnClickListener<H, SH, C>
{
    //================================================================================
    // region CLICK EVENTS

    /**
     * Called when a header row is clicked
     *
     * @param header H - header data source of the clicked row
     * @param sectionIndex index of the section that was clicked
     * @param expansionState the expansion state of the section at sectionIndex
     */
    void sectionHeaderClicked(H header, int sectionIndex, int expansionState);

    /**
     * Called when a sub header row is clicked
     *
     * @param subHeader SH - sub header data source of the clicked row
     * @param sectionIndex index of the section that was clicked
     * @param expansionState the expansion state of the section at sectionIndex
     */
    void sectionSubHeaderClicked(SH subHeader, int sectionIndex, int expansionState);

    /**
     * Called when a content row is clicked
     *
     * @param content C - content object for the clicked row
     * @param sectionIndex index of the section that was clicked
     * @param sectionContentIndex index of the content in the data source
     */
    void sectionContentClicked(C content, int sectionIndex, int sectionContentIndex);

    // endregion
    //================================================================================

}
