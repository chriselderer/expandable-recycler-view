package com.elder.expandablerecyclerview;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 2016-12-05.
 *
 * This abstract class adds expandable section functionality to RecyclerView.Adapter
 */
public abstract class ExpandableRecyclerViewAdapter
        <H, SH, C, HVH extends RecyclerView.ViewHolder, SVH extends RecyclerView.ViewHolder, CVH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener, Expandable<H, SH, C, HVH, SVH, CVH>
{

    //================================================================================
    // region SECTION EXPANSION STATE CONSTANTS
    // -------------------------------------------------------------
    // sections can have 4 states:
    // 1) minimized - the section is minimized
    // 2) expanded  - the section is expanded
    // 3) loading   - the section content is currently loading
    // 4) error     - there was an error loading the section content
    // -------------------------------------------------------------

    /**
     * Section state value for when the section is minimized.
     */
    public static final int EXPANSION_STATE_MINIMIZED = 0;

    /**
     * Section state value for when the section is Expanded.
     */
    public static final int EXPANSION_STATE_EXPANDED = 1;
    /**
     * Section state value for when the section is loading.
     */
    public static final int EXPANSION_STATE_LOADING_CONTENT = 2;

    /**
     * Section state value for when an error occurred in loading.
     */
    public static final int EXPANSION_STATE_ERROR_LOADING = 3;

    // endregion
    //================================================================================

    //================================================================================
    // region VIEW CONSTANTS

    /**
     * Value representing a header row.
     */
    private final static int HEADER = 0;
    /**
     * Value representing a sub header row.
     */
    private final static int SUB_HEADER = 1;
    /**
     * Value representing a content row
     */
    private final static int SECTION_CONTENT = 2;

    // endregion
    //================================================================================

    //================================================================================
    // region SAVED STATE CONSTANT

    private static final String SAVE_STATE_KEY = "EXPANDABLE_RECYCLER_VIEW_SAVE_STATE";

    // endregion
    //================================================================================

    //================================================================================
    // region VARIABLES

    private Class<HVH> hvhClass;
    private Class<SVH> svhClass;
    private Class<CVH> cvhClass;

    /**
     * Internal reference to the recycler view
     */
    private RecyclerView recyclerView;

    /**
     * ArrayList of sections - used to populate the RecyclerView
     */
    protected ArrayList<ExpandableRecyclerViewSection<H,SH,C>> sections = new ArrayList<>();

    /**
     * ArrayList of indexes of section header rows (position 0 --> header index for section 0)
     */
    protected ArrayList<Integer> headerRowIndexes = new ArrayList<>();

    /**
     * ArrayList of indexes of section subheader rows
     */
    protected ArrayList<Integer> subHeaderRowIndexes = new ArrayList<>();

    /**
     * Interface implementation used to respond to row click events
     */
    public ExpandableRowOnClickListener<H, SH, C> expandableRowOnClickListener;

    /**
     * Interface implementation used to respond to click events on child views in each row
     */
    public ExpandableRowSubViewOnClickListener<H, SH, C> expandableRowSubViewOnClickListener;

    // endregion
    //================================================================================

    //================================================================================
    // region CONSTRUCTOR

    public ExpandableRecyclerViewAdapter(Class<HVH> hvhClass, Class<SVH> svhClass, Class<CVH> cvhClass)
    {
        // Set references to view holder classes
        this.hvhClass = hvhClass;
        this.svhClass = svhClass;
        this.cvhClass = cvhClass;
    }

    // endregion
    //================================================================================

    //================================================================================
    // region ADAPTER OVERRIDES

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);

        this.recyclerView = recyclerView;

        // Create each section
        for(int i = 0; i<getNumberOfSections(); i++)
        {
            // Map the section to our model
            sections.add(createSection(i));
        }

        // Calculate the header locations
        updateSectionHeaderAndSubHeaderLocations();
    }

    @Override
    public int getItemCount()
    {
        // Add up the number of rows in each section
        // getNumberOfSections() will be implemented by the concrete subclass

        int count = 0;

        for(int i = 0; i < getNumberOfSections(); i++)
        {
            count += getSectionSize(i);
        }

        return count;
    }

    @Override
    public int getItemViewType(int position)
    {
        // Checks to see if a row should display as a HEADER, SUB_HEADER, CONTENT.
        // The result of this method is used to call a specific CreateViewHolder method for each
        // type of view. The custom methods are implemented by the concrete base class to provide
        // ViewHolders.

        if(recyclerViewIndexIsSectionHeader(position))
        {
            return HEADER;
        }
        else if(recyclerViewIndexIsSectionSubHeader(position))
        {
            return SUB_HEADER;
        }
        else
        {
            return SECTION_CONTENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // Calls specific CreateViewHolder methods for each type of view.
        // The custom methods are implemented by the concrete base class to provide ViewHolders.

        if(viewType == HEADER)
        {
            return createSectionHeaderViewHolder(parent);
        }
        else if(viewType == SUB_HEADER)
        {
            return createSectionSubHeaderViewHolder(parent);
        }
        else
        {
            return createSectionContentViewHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int recyclerViewIndex)
    {
        // Find the section that this row is in.
        int sectionIndex = getSectionIndexForPosition(recyclerViewIndex);

        // Get expansion state
        int expansionState = sections.get(sectionIndex).getSectionExpansionState();

        // Checks the type of the view holder and call a specific method to bind the view for
        // each type. Specific binding methods are implemented by the concrete subclass.
        if(holder.getClass() == hvhClass)
        {
            bindSectionHeaderViewHolder(hvhClass.cast(holder),
                    sections.get(sectionIndex).getHeader(),
                    sectionIndex,
                    expansionState);
        }
        else if(recyclerViewIndexIsSectionSubHeader(recyclerViewIndex))
        {
            bindSectionSubHeaderViewHolder(svhClass.cast(holder),
                    sections.get(sectionIndex).getSubHeader(),
                    sectionIndex,
                    expansionState);
        }
        else
        {
            // Find where in the Content ArrayList the row is in.
            int newPosition = getContentIndexForSectionWithRecyclerViewIndex(sectionIndex, recyclerViewIndex);

            // Get the content object at that location
            C content = sections.get(sectionIndex).getSectionContent().get(newPosition);

            // Bind the view holder with the provided content
            bindSectionContentViewHolder(cvhClass.cast(holder),
                    content,
                    sectionIndex,
                    expansionState);
        }

        if(expandableRowOnClickListener != null)
        {
            // Set on click listener
            holder.itemView.setOnClickListener(this);
        }
    }

    // endregion
    //================================================================================

    //================================================================================
    // region CREATING SECTIONS

    /**
     * Creates each section.
     * The header, sub header and content are provided by the concrete base class.
     */
    private ExpandableRecyclerViewSection<H,SH,C> createSection(int sectionIndex)
    {
        // Get the header for this section
        H header = getHeaderForSection(sectionIndex);

        // Get the sub header for this section
        SH subHeader = getSubHeaderForSection(sectionIndex);

        // Get the content list for this section
        List<C> content = getContentForSection(sectionIndex);

        // Set the initial expansion state
        return new ExpandableRecyclerViewSection<>(header, subHeader, content, getDefaultExpansionStateForSection(sectionIndex));
    }

    // endregion
    //================================================================================

    //================================================================================
    // region ON CLICK LISTENER

    @Override
    public void onClick(View v)
    {
        if(v.getParent() == recyclerView)
        {
            // THIS IS A ROW THAT WAS CLICKED

            // Get the index for the recycler view
            int recyclerViewIndex = recyclerView.getLayoutManager().getPosition(v);

            // Find what section was clicked
            int sectionIndex = getSectionIndexForPosition(recyclerViewIndex);

            // Get expansion state of the section
            int expansionState = sections.get(sectionIndex).getSectionExpansionState();

            // Check if a header, sub header or content row was clicked, and call the corresponding method
            if(recyclerViewIndexIsSectionHeader(recyclerViewIndex))
            {
                expandableRowOnClickListener.sectionHeaderClicked(sections.get(sectionIndex).getHeader(),
                        sectionIndex,
                        expansionState);
            }
            else if(recyclerViewIndexIsSectionSubHeader(recyclerViewIndex))
            {
                expandableRowOnClickListener.sectionSubHeaderClicked(sections.get(sectionIndex).getSubHeader(),
                        sectionIndex,
                        expansionState);
            }
            else
            {
                int contentIndex = getContentIndexForSectionWithRecyclerViewIndex(sectionIndex, recyclerViewIndex);
                expandableRowOnClickListener.sectionContentClicked(sections.get(sectionIndex).getSectionContent().get(contentIndex),
                        sectionIndex,
                        contentIndex);
            }
        }
        else
        {
            // THIS IS A SUBVIEW THAT WAS CLICKED

            // Check if we implemented expandable sub view on click
            if(this instanceof ExpandableRowSubViewOnClickListener)
            {
                // Get the row view
                View rowView = (View) v.getParent();

                // Get the index for the recycler view
                int recyclerViewIndex = recyclerView.getLayoutManager().getPosition(rowView);

                // Find what section was clicked
                int sectionIndex = getSectionIndexForPosition(recyclerViewIndex);

                // Get expansion state of the section
                int expansionState = sections.get(sectionIndex).getSectionExpansionState();

                // Check if a header, sub header or content row was clicked, and call the corresponding method
                if(recyclerViewIndexIsSectionHeader(recyclerViewIndex))
                {
                    expandableRowSubViewOnClickListener.onSubViewClickedInSectionHeader(v,
                            sections.get(sectionIndex).getHeader(),
                            sectionIndex,
                            expansionState);
                }
                else if(recyclerViewIndexIsSectionSubHeader(recyclerViewIndex))
                {
                    expandableRowSubViewOnClickListener.onSubViewClickedInSectionSubHeader(v,
                            sections.get(sectionIndex).getSubHeader(),
                            sectionIndex,
                            expansionState);
                }
                else
                {
                    int contentIndex = getContentIndexForSectionWithRecyclerViewIndex(sectionIndex, recyclerViewIndex);
                    expandableRowSubViewOnClickListener.onSubViewClickedInSectionContentRow(v,
                            sections.get(sectionIndex).getSectionContent().get(contentIndex),
                            sectionIndex,
                            contentIndex);
                }
            }
        }

    }


    // endregion
    //================================================================================

    //================================================================================
    // region INTERNAL METHODS FOR MANAGING THE SECTIONS

    /**
     * Returns the number of rows currently displayed in a section
     */
    protected int getSectionSize(int sectionIndex)
    {
        // Start with 1 for the section header
        int count = 1;

        // Check if sub header is included, if it is add 1
        if(sectionSubHeaderExists(sectionIndex))
        {
            count += 1;
        }

        // Check if the section is expanded, if it is, add the size of the section content ArrayList
        if(sections.get(sectionIndex).sectionIsExpanded())
        {
            count += sections.get(sectionIndex).getSectionContent().size();
        }

        return count;
    }

    /**
     * This finds and tracks the index of each header and sub header row.
     * Called each time there is an expansion event and when notified of a data set change.
     */
    protected void updateSectionHeaderAndSubHeaderLocations()
    {
        Integer recyclerViewIndex = 0;

        // Track sub header rows needing inserting or removing
        ArrayList<Integer> rowsToInsert = new ArrayList<>();
        ArrayList<Integer> rowsToRemove = new ArrayList<>();

        // Loop through every section and find the index locations of the header views
        for (int i = 0; i<getNumberOfSections(); i++)
        {
            //----------------- header indexes -----------------\\
            // First row in each section is a header
            if(i < headerRowIndexes.size())
            {
                headerRowIndexes.set(i, recyclerViewIndex);
            }
            else
            {
                headerRowIndexes.add(recyclerViewIndex);
            }

            // Move to the next index in the RecyclerView
            recyclerViewIndex ++;

            //----------------- sub header indexes -----------------\\
            // Check if the sub header exists, if it does, track its index
            // If not, set the tracked index to -1

            if(sectionSubHeaderExists(i))
            {
                // check if the sub didn't exist on the last pass
                // if it didn't then were going to insert it with animation
                if(i < subHeaderRowIndexes.size() && subHeaderRowIndexes.get(i) == -1)
                {
                    rowsToInsert.add(recyclerViewIndex);
                }

                // track the new index of the sub header
                if(i < subHeaderRowIndexes.size())
                {
                    subHeaderRowIndexes.set(i, recyclerViewIndex);
                }
                else
                {
                    subHeaderRowIndexes.add(recyclerViewIndex);
                }

                // Move to next index
                recyclerViewIndex ++;
            }
            else
            {
                // Sub header doesn't exist

                // check if the sub header existed on the last pass
                // if it did then were going to remove it with animation
                if(i < subHeaderRowIndexes.size() && subHeaderRowIndexes.get(i) != -1)
                {
                    rowsToRemove.add(recyclerViewIndex);
                }

                // Set sub header tracking to -1, indicating that it does not exist
                if(i < subHeaderRowIndexes.size())
                {
                    subHeaderRowIndexes.set(i, -1);
                }
                else
                {
                    subHeaderRowIndexes.add(-1);
                }
            }

            //----------------- content rows -----------------\\
            // Check if the section is expanded
            // If it is move the recyclerViewIndex past each content row for the section
            if(sections.get(i).sectionIsExpanded())
            {
                // COUNT THE CONTENT
                recyclerViewIndex += sections.get(i).getSectionContent().size();
            }
        }

        // Inserting and Removing sub header rows
        // We will insert new sub headers and remove old ones

        for(int i = 0; i<rowsToInsert.size(); i++)
        {
            notifyItemInserted(rowsToInsert.get(i));
        }

        for(int i = 0; i<rowsToRemove.size(); i++)
        {
            notifyItemRemoved(rowsToRemove.get(i));
        }

    }

    /**
     * Will calculate the relative index of a content row in a section according to the given recyclerViewIndex
     *
     *
     * @param sectionIndex section index
     * @param recyclerViewIndex Index representing a row of the RecyclerView
     * @return the index of the content object in the Content ArrayList held by the Section represented by sectionIndex
     *
     */
    protected int getContentIndexForSectionWithRecyclerViewIndex(int sectionIndex, int recyclerViewIndex)
    {
        // Check if this section has a subHeader
        // If it does use the location of that to find the index of the content
        // Else, use the location of the header to find the index of the content
        if(sectionSubHeaderExists(sectionIndex))
        {
            return recyclerViewIndex - subHeaderRowIndexes.get(sectionIndex)-1;
        }
        else
        {
            return recyclerViewIndex - headerRowIndexes.get(sectionIndex)-1;
        }
    }

    /**
     * For a specific index/position/row in the recycler view, find the index representing the
     * section that the row belongs too.
     *
     * @param recyclerViewIndex Index representing a row of the RecyclerView
     * @return Section index, representing a specific section
     */
    protected int getSectionIndexForPosition(int recyclerViewIndex)
    {
        for(int i = 0; i<getNumberOfSections(); i++)
        {
            // Check if this is the last section
            // If it is, then the row for recyclerViewIndex has to belong to it
            if(i == getNumberOfSections()-1)
            {
                return i;
            }

            // Check if its in the current section

            // Get the start of the current section
            int sectionHeaderIndex = headerRowIndexes.get(i);

            // Get the start of the next section
            int followingSectionIndex = headerRowIndexes.get(i+1);

            // Check if the row is between the start of this section and start of next.
            if(recyclerViewIndex >= sectionHeaderIndex && recyclerViewIndex < followingSectionIndex)
            {
                return i;
            }
        }

        return getNumberOfSections()-1;
    }

    /**
     * Checks if an index/position in the recycler view is a section header
     */
    protected boolean recyclerViewIndexIsSectionHeader(int index)
    {
        for(Integer sectionHeaderIndex : headerRowIndexes)
        {
            if(sectionHeaderIndex.equals(index))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if an index/position in the recycler view is a section sub header
     */
    protected boolean recyclerViewIndexIsSectionSubHeader(int index)
    {
        for(Integer sectionSubHeaderIndex : subHeaderRowIndexes)
        {
            if(sectionSubHeaderIndex.equals(index))
            {
                return true;
            }
        }

        return false;
    }

    // endregion
    //================================================================================

    //================================================================================
    // region UPDATING THE RECYCLER VIEW

    /**
     * Updates the entire expandable recycler view
     */
    public void notifyExpandableDataSetChanged()
    {
        // update header and sub header locations
        updateSectionHeaderAndSubHeaderLocations();

        // update the recycler view
        notifyDataSetChanged();
    }

    /**
     * Updates a section header
     *
     * @param sectionIndex index of the section to update
     */
    public void notifySectionHeaderChanged(int sectionIndex)
    {
        // update header and sub header locations
        updateSectionHeaderAndSubHeaderLocations();

        // notify item changed
        notifyItemChanged(headerRowIndexes.get(sectionIndex));
    }

    /**
     * Updates a section sub header.
     *
     * @param sectionIndex index of the section to update
     */
    public void notifySectionSubHeaderChanged(int sectionIndex)
    {
        // update header and sub header locations
        updateSectionHeaderAndSubHeaderLocations();

        if(sectionSubHeaderExists(sectionIndex))
        {
            notifyItemChanged(subHeaderRowIndexes.get(sectionIndex));
        }
    }

    /**
     * Updates the content of a section.
     *
     * @param sectionIndex index of the section to update
     */
    public void notifySectionContentChanged(int sectionIndex)
    {
        // update header and sub header locations
        updateSectionHeaderAndSubHeaderLocations();

        if(sections.get(sectionIndex).sectionIsExpanded())
        {
            int startOfRange;

            if(sectionSubHeaderExists(sectionIndex))
            {
                startOfRange = subHeaderRowIndexes.get(sectionIndex) + 1;
            }
            else
            {
                startOfRange = headerRowIndexes.get(sectionIndex) + 1;
            }

            int itemCount = sections.get(sectionIndex).getSectionContent().size();

            notifyItemRangeChanged(startOfRange, itemCount, null);
        }
    }

    /**
     * Updates the header, sub header & content of a section.
     *
     * @param sectionIndex index of the section to update
     */
    public void notifySectionChanged(int sectionIndex)
    {
        // update header and sub header locations
        updateSectionHeaderAndSubHeaderLocations();

        int start = headerRowIndexes.get(sectionIndex);
        int itemCount = 1;

        if(sectionSubHeaderExists(sectionIndex))
        {
            itemCount = itemCount + 1;
        }

        if(sections.get(sectionIndex).sectionIsExpanded())
        {
            itemCount = itemCount + sections.get(sectionIndex).getSectionContent().size();
        }

        notifyItemRangeChanged(start, itemCount, null);
    }

    // endregion
    //================================================================================

    //================================================================================
    // region CHANGING EXPANSION STATE

    /**
     * Minimize the section with sectionIndex
     *
     * @param sectionIndex index of the section to minimize
     */
    public void setExpansionStateMinimized(int sectionIndex)
    {
        if(sections.get(sectionIndex).sectionIsMinimized())
        {
            return;
        }

        // set state
        sections.get(sectionIndex).setSectionExpansionState(EXPANSION_STATE_MINIMIZED);

        // remove rows from section
        int startOfRange;

        if(sectionSubHeaderExists(sectionIndex))
        {
            startOfRange = subHeaderRowIndexes.get(sectionIndex) + 1;
        }
        else
        {
            startOfRange = headerRowIndexes.get(sectionIndex) + 1;
        }

        int itemCount = sections.get(sectionIndex).getSectionContent().size();

        // update section header and sub header
        notifySectionHeaderChanged(sectionIndex);
        notifySectionSubHeaderChanged(sectionIndex);

        // update the recycler view
        notifyItemRangeRemoved(startOfRange, itemCount);

        // update header and sub header index tracking
        updateSectionHeaderAndSubHeaderLocations();

    }

    /**
     * Expand the section with sectionIndex
     *
     * @param sectionIndex index of the section to expand
     */
    public void setExpansionStateExpanded(int sectionIndex)
    {
        if(sections.get(sectionIndex).sectionIsExpanded())
        {
            return;
        }

        // set state
        sections.get(sectionIndex).setSectionExpansionState(EXPANSION_STATE_EXPANDED);

        // update header and sub header index tracking
        updateSectionHeaderAndSubHeaderLocations();

        // add rows to section
        int startOfRange;

        if(sectionSubHeaderExists(sectionIndex))
        {
            startOfRange = subHeaderRowIndexes.get(sectionIndex) + 1;
        }
        else
        {
            startOfRange = headerRowIndexes.get(sectionIndex) + 1;
        }

        int itemCount = sections.get(sectionIndex).getSectionContent().size();

        // add items
        notifyItemRangeInserted(startOfRange, itemCount);

        // update section header and sub header
        notifySectionHeaderChanged(sectionIndex);
        notifySectionSubHeaderChanged(sectionIndex);
    }

    /**
     * Set expansion state to loading
     *
     * @param sectionIndex index of the section
     */
    public void setExpansionStateLoading(int sectionIndex)
    {
        if(sections.get(sectionIndex).sectionIsLoading())
        {
            return;
        }

        // set state
        sections.get(sectionIndex).setSectionExpansionState(EXPANSION_STATE_LOADING_CONTENT);

        // update header and sub header index tracking
        updateSectionHeaderAndSubHeaderLocations();

        // update section header and sub header
        notifySectionHeaderChanged(sectionIndex);
        notifySectionSubHeaderChanged(sectionIndex);
    }

    /**
     * Set expansion state to loading
     *
     * @param sectionIndex index of the section
     */
    public void setExpansionStateError(int sectionIndex)
    {
        if(sections.get(sectionIndex).sectionLoadingErrorOccurred())
        {
            return;
        }

        // set state
        sections.get(sectionIndex).setSectionExpansionState(EXPANSION_STATE_ERROR_LOADING);

        // update header and sub header index tracking
        updateSectionHeaderAndSubHeaderLocations();

        // update section header and sub header
        notifySectionHeaderChanged(sectionIndex);
        notifySectionSubHeaderChanged(sectionIndex);
    }

    // endregion
    //================================================================================

    //================================================================================
    // region SAVING INSTANCE STATE

    /**
     * Call this from the activities onSaveInstanceState method to save the expansion state
     * of each section.
     *
     * @param savedInstanceState saved instance state Bundle
     */
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        int[] expansionStates = new int[sections.size()];

        for(int i = 0; i<sections.size(); i++)
        {
            expansionStates[i] = getSavedStateForSection(i, sections.get(i).getSectionExpansionState());
        }

        savedInstanceState.putIntArray(SAVE_STATE_KEY, expansionStates);
    }

    /**
     * Call this from the activities onRestoreInstanceState method to restore the expansion state
     * of each section.  If this is called the result of getDefaultExpansionStateForSection
     * will be ignored.
     *
     * @param savedInstanceState
     */
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        if (savedInstanceState != null && savedInstanceState.containsKey(SAVE_STATE_KEY))
        {
            int[] expansionStates = savedInstanceState.getIntArray(SAVE_STATE_KEY);

            if(expansionStates != null)
            {
                for(int i=0; i<expansionStates.length; i++)
                {
                    // Set the expansion state
                    sections.get(i).setSectionExpansionState(expansionStates[i]);
                }

                // update the view
                notifyExpandableDataSetChanged();
            }

        }
    }

    // endregion
    //================================================================================

    //================================================================================
    // region HELPER METHODS

    /**
     * Helper method to check if a section sub header exists.
     * Not to be overridden.
     */
    protected boolean sectionSubHeaderExists(int sectionIndex)
    {
        int expansionState = sections.get(sectionIndex).getSectionExpansionState();

        return sections.get(sectionIndex) != null &&
                shouldShowSectionSubHeader(sectionIndex, expansionState);
    }

    /**
     * Checks to see if there is anything in the content List of a section
     * Not to be overridden.
     *
     * @param sectionIndex index of the section
     * @return true if there is at least one item in the content of the section
     */
    protected boolean sectionHasContent(int sectionIndex)
    {
        return sections.get(sectionIndex).getSectionContent() != null &&
                sections.get(sectionIndex).getSectionContent().size() > 0;
    }



    // endregion
    //================================================================================
}
