package com.elder.expandablerecyclerview;

import java.util.ArrayList;
import java.util.List;

import static com.elder.expandablerecyclerview.ExpandableRecyclerViewAdapter.EXPANSION_STATE_ERROR_LOADING;
import static com.elder.expandablerecyclerview.ExpandableRecyclerViewAdapter.EXPANSION_STATE_EXPANDED;
import static com.elder.expandablerecyclerview.ExpandableRecyclerViewAdapter.EXPANSION_STATE_LOADING_CONTENT;
import static com.elder.expandablerecyclerview.ExpandableRecyclerViewAdapter.EXPANSION_STATE_MINIMIZED;

/**
 * Created by Chris Elder on 2017-06-13.
 *
 * This is a model class used to hold the data displayed in individual sections of the RecyclerView.
 *
 * @param <H> The section 'SectionHeader' type, used to populate 'SectionHeader' rows in each section.
 * @param <SH> The section 'SubHeader' type, used to populate 'SubHeader' rows in each section.
 * @param <C> The section 'Content' type, used to populate 'Content' rows in each section.
 *
 */
public class ExpandableRecyclerViewSection<H, SH, C>
{

    //================================================================================
    // region VARIABLES

    /**
     * Expansion state of the section.
     * Used to determine how to display each section (ie. expanded, minimized, error loading etc.)
     */
    private int sectionExpansionState;

    /**
     * Data source used to populate the SectionHeader for this section.
     */
    private H header;

    /**
     * Data source used to populate the subHeader for this section.
     */
    private SH subHeader;

    /**
     * ArrayList of section content 'C'.
     * Used to populate the content rows of each section.
     */
    private List<C> sectionContent = new ArrayList<>();



    // endregion
    //================================================================================

    //================================================================================
    // region CONSTRUCTOR

    public ExpandableRecyclerViewSection(H header, SH subHeader, List<C> sectionContent, int defaultSectionExpansionState)
    {
        this.header = header;
        this.subHeader = subHeader;
        this.sectionContent = sectionContent;

        if(defaultSectionExpansionState == EXPANSION_STATE_MINIMIZED ||
                defaultSectionExpansionState == EXPANSION_STATE_EXPANDED ||
                defaultSectionExpansionState == EXPANSION_STATE_LOADING_CONTENT ||
                defaultSectionExpansionState == EXPANSION_STATE_ERROR_LOADING)
        {
            sectionExpansionState = defaultSectionExpansionState;
        }
        else
        {
            sectionExpansionState = EXPANSION_STATE_MINIMIZED;
        }
    }

    // endregion
    //================================================================================

    //================================================================================
    // region GETTERS AND SETTERS

    public int getSectionExpansionState()
    {
        return sectionExpansionState;
    }

    public void setSectionExpansionState(int sectionExpansionState)
    {
        this.sectionExpansionState = sectionExpansionState;
    }

    public H getHeader() {
        return header;
    }

    public void setHeader(H header) {
        this.header = header;
    }

    public SH getSubHeader() {
        return subHeader;
    }

    public void setSubHeader(SH subHeader) {
        this.subHeader = subHeader;
    }

    public List<C> getSectionContent()
    {
        return sectionContent;
    }

    public void setSectionContent(List<C> sectionContent)
    {
        this.sectionContent = sectionContent;
    }

    // endregion
    //================================================================================

    //================================================================================
    // region CHECK EXPANSION STATE

    /**
     * Check if the section is expanded.
     * @return true if section is expanded, false otherwise.
     */
    protected boolean sectionIsExpanded()
    {
        return(sectionExpansionState == EXPANSION_STATE_EXPANDED);
    }

    /**
     * Check if the section is minimized.
     * @return true if section is minimized, false otherwise.
     */
    protected boolean sectionIsMinimized()
    {
        return(sectionExpansionState == EXPANSION_STATE_MINIMIZED);
    }

    /**
     * Check if the section is loading.
     * @return true if section is loading, false otherwise.
     */
    protected boolean sectionIsLoading()
    {
        return(sectionExpansionState == EXPANSION_STATE_LOADING_CONTENT);
    }

    /**
     * Check if the section had a loading error.
     * @return true if an error occurred, false otherwise.
     */
    protected boolean sectionLoadingErrorOccurred()
    {
        return(sectionExpansionState == EXPANSION_STATE_ERROR_LOADING);
    }

    // endregion
    //================================================================================
}
