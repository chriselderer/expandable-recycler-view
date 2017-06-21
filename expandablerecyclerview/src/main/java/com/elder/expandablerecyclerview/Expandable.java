package com.elder.expandablerecyclerview;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;


/**
 * Created by chris on 2017-06-15.
 * Expandable interface, implemented by ExpandableRecyclerViewAdapter
 *
 * @param <H> Header model class
 * @param <SH> SubHeader model class
 * @param <C> Content model class
 * @param <HVH> HeaderViewHolder class
 * @param <SVH> SubHeaderViewHolder class
 * @param <CVH> ContentViewHolder class
 */
public interface Expandable <H, SH, C, HVH extends RecyclerView.ViewHolder, SVH extends RecyclerView.ViewHolder, CVH extends RecyclerView.ViewHolder>
{
    //================================================================================
    // region CREATING SECTIONS

    /**
     * Returns the number of sections in the recycler view.
     */
    int getNumberOfSections();

    /**
     * Returns a Header object for a specific section.
     * This will be passed automatically to the corresponding BindView method.
     *
     * @param sectionIndex index of the specific section.
     * @return Header object.
     */
    H getHeaderForSection(int sectionIndex);

    /**
     * Returns a Sub Header object for a specific section.
     * This will be passed automatically to the corresponding BindView method.
     *
     * @param sectionIndex index of the specific section.
     * @return Sub Header object.
     */
    @Nullable SH getSubHeaderForSection(int sectionIndex);

    /**
     * Returns an ArrayList of section content object for a specific section.
     * Each item in the ArrayList will be passed automatically to the corresponding BindView method.
     *
     * @param sectionIndex index of the specific section.
     * @return Content ArrayList.
     */
    List<C> getContentForSection(int sectionIndex);

    // endregion
    //================================================================================

    //================================================================================
    // region CREATING VIEW HOLDERS

    /**
     * Provide a Header ViewHolder.
     *
     * @param parent parent ViewGroup of the new View
     * @return HVH - header view holder
     */
    HVH createSectionHeaderViewHolder(ViewGroup parent);

    /**
     * Provide a Sub Header ViewHolder.
     *
     * @param parent parent ViewGroup of the new View
     * @return SVH - sub header view holder
     */
    SVH createSectionSubHeaderViewHolder(ViewGroup parent);

    /**
     * Provide a Content ViewHolder.
     *
     * @param parent parent ViewGroup of the new View
     * @return CVH - content view holder
     */
    CVH createSectionContentViewHolder(ViewGroup parent);


    // endregion
    //================================================================================

    //================================================================================
    // region BINDING VIEW HOLDERS

    /**
     * Bind the view for a section header view holder.
     *
     * @param holder HVH - header view holder.
     * @param header H - the header object to bind with.
     * @param expansionState the expansion state of the section at sectionIndex
     */
    void bindSectionHeaderViewHolder(HVH holder,
                                     H header,
                                     int sectionIndex,
                                     int expansionState);

    /**
     * Bind the view for a sub header view holder.
     *
     * @param holder SVH - sub header view holder.
     * @param subHeader SH - the subHeader object to bind with.
     * @param expansionState the expansion state of the section at sectionIndex
     */
    void bindSectionSubHeaderViewHolder(SVH holder,
                                        SH subHeader,
                                        int sectionIndex,
                                        int expansionState);

    /**
     * Bind the view for a content view holder.
     *
     * @param holder CVH - content view holder.
     * @param content C - the content object to bind with.
     */
    void bindSectionContentViewHolder(CVH holder,
                                      C content,
                                      int sectionIndex,
                                      int expansionState);

    // endregion
    //================================================================================

    //================================================================================
    // region SECTION STATE

    /**
     * Returns the default expansion state for the section at sectionIndex
     * Expansion states can be:
     * ExpandableRecyclerViewSection.EXPANSION_STATE_MINIMIZED
     * ExpandableRecyclerViewSection.EXPANSION_STATE_EXPANDED
     * ExpandableRecyclerViewSection.EXPANSION_STATE_LOADING_CONTENT
     * ExpandableRecyclerViewSection.EXPANSION_STATE_ERROR_LOADING
     *
     * @param sectionIndex the index of the section.
     * @return the expansionState for the section.
     */

    int getDefaultExpansionStateForSection(int sectionIndex);

    /**
     * Return false if the sub header should not be shown.
     * Return true if the sub header should be shown.
     * By default, this method returns true.
     *
     * @param sectionIndex index of the section.
     * @return true for showing the sub header.
     */
    boolean shouldShowSectionSubHeader(int sectionIndex, int expansionState);

    // endregion
    //================================================================================

    //================================================================================
    // region SAVED INSTANCE STATE

    /**
     * Provide the state you want to save for each section.
     * Only called when you call the adapters onSaveInstanceState method.
     * This allows you to for example, default a loading state to a minimized state or to save it as loading.
     *
     * @param sectionIndex index of the section
     * @param expansionState expansion state of the section
     */
    int getSavedStateForSection(int sectionIndex, int expansionState);

    // endregion
    //================================================================================
}
