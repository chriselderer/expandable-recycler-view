package com.elder.expandablerecyclerviewdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elder.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.elder.expandablerecyclerview.ExpandableRowOnClickListener;
import com.elder.expandablerecyclerviewdemo.models.Animal;
import com.elder.expandablerecyclerviewdemo.models.SectionHeader;
import com.elder.expandablerecyclerviewdemo.models.SectionSubHeader;
import com.elder.expandablerecyclerviewdemo.viewHolder.AnimalViewHolder;
import com.elder.expandablerecyclerviewdemo.viewHolder.HeaderViewHolderOne;
import com.elder.expandablerecyclerviewdemo.viewHolder.SubHeaderViewHolder;

import java.util.ArrayList;

/**
 * Created by chris on 2016-12-05.
 */
public class ExpandableRecyclerViewAdapterImplementationOne
        extends ExpandableRecyclerViewAdapter<SectionHeader, SectionSubHeader, Animal, HeaderViewHolderOne, SubHeaderViewHolder, AnimalViewHolder>
        implements ExpandableRowOnClickListener<SectionHeader, SectionSubHeader, Animal>
{
    //================================================================================
    // region VARIABLES

    private Context context;

    //----------------- data source for section content -----------------\\
    // Separate data source for the content of each section.
    // Make changes to the data source as you normally would in a RecyclerView.
    // After changes --> update????
    public ArrayList<Animal> dogs = new ArrayList<>();
    public ArrayList<Animal> cats = new ArrayList<>();
    public ArrayList<Animal> goats = new ArrayList<>();

    // endregion
    //================================================================================

    //================================================================================
    // region CONSTRUCTOR

    public ExpandableRecyclerViewAdapterImplementationOne(Context context)
    {
        super(HeaderViewHolderOne.class, SubHeaderViewHolder.class, AnimalViewHolder.class);
        this.context = context;

        this.expandableRowOnClickListener = this;
    }

    // endregion
    //================================================================================

    //================================================================================
    // region EXPANDABLE: CREATING SECTIONS

    @Override
    public int getNumberOfSections()
    {
        // Return the number of sections we want in the recycler view
        return 3;
    }

    @Override
    public SectionHeader getHeaderForSection(int sectionIndex)
    {
        // Create each header section

        switch (sectionIndex)
        {
            case 0:
                return new SectionHeader("Dogs");
            case 1:
                return new SectionHeader("Cats");
            default:
                return new SectionHeader("Goats");
        }
    }

    @Override
    public SectionSubHeader getSubHeaderForSection(int sectionIndex)
    {
        // Create each sub header section
        // using the same section sub header for each section
        // you can easily change this according to the section index
        return new SectionSubHeader("Expand", "Minimize", "Loading", "Error Loading - retry");
    }

    @Override
    public ArrayList<Animal> getContentForSection(int sectionIndex)
    {
        // Pass the Lists representing the content of each section
        switch (sectionIndex)
        {
            case 0:
                return dogs;
            case 1:
                return cats;
            default:
                return goats;
        }
    }

    // endregion
    //================================================================================

    //================================================================================
    // region EXPANDABLE: CREATING VIEW HOLDERS

    @Override
    public HeaderViewHolderOne createSectionHeaderViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_row_one, parent, false);
        return new HeaderViewHolderOne(view);
    }

    @Override
    public SubHeaderViewHolder createSectionSubHeaderViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subheader_row, parent, false);
        return new SubHeaderViewHolder(view);
    }

    @Override
    public AnimalViewHolder createSectionContentViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_row, parent, false);
        return new AnimalViewHolder(view);
    }

    // endregion
    //================================================================================

    //================================================================================
    // region EXPANDABLE: BINDING VIEW HOLDER VIEWS

    @Override
    public void bindSectionHeaderViewHolder(HeaderViewHolderOne holder, SectionHeader header, int sectionIndex, int expansionState) {
        holder.setHeaderText(header.getTitle());
    }

    @Override
    public void bindSectionSubHeaderViewHolder(SubHeaderViewHolder holder, SectionSubHeader subHeader, int sectionIndex, int expansionState) {
        switch (expansionState)
        {
            case ExpandableRecyclerViewAdapter.EXPANSION_STATE_MINIMIZED:
                holder.setSubHeaderText(subHeader.getExpandString());
                break;
            case ExpandableRecyclerViewAdapter.EXPANSION_STATE_EXPANDED:
                holder.setSubHeaderText(subHeader.getMinimizeString());
                break;
            case ExpandableRecyclerViewAdapter.EXPANSION_STATE_LOADING_CONTENT:
                holder.setSubHeaderText(subHeader.getLoadingString());
                break;
            case ExpandableRecyclerViewAdapter.EXPANSION_STATE_ERROR_LOADING:
                holder.setSubHeaderText(subHeader.getErrorString());
                break;
        }
    }

    @Override
    public void bindSectionContentViewHolder(AnimalViewHolder holder, Animal content, int sectionIndex, int expansionState)
    {
        holder.setAnimalImage(content.getPicture());

        String fluff = "Fluff Factor: " + String.valueOf(content.getFluffFactor()) + " out of 10";
        String speed = "Speed: " + String.valueOf(content.getSpeed()) + " m/s";

        holder.setName(content.getName());
        holder.setFluffFactorText(fluff);
        holder.setSpeedTextView(speed);
    }

    // endregion
    //================================================================================

    //================================================================================
    // region EXPANDABLE: OTHER STATE METHODS

    @Override
    public int getDefaultExpansionStateForSection(int sectionIndex)
    {
        // Provide the default state of each section.
        return EXPANSION_STATE_MINIMIZED;
    }

    @Override
    public boolean shouldShowSectionSubHeader(int sectionIndex, int expansionState)
    {
        return true;
    }

    @Override
    public int getSavedStateForSection(int sectionIndex, int expansionState)
    {
        if(expansionState == EXPANSION_STATE_EXPANDED)
            return expansionState;
        else
            return EXPANSION_STATE_MINIMIZED;
    }

    // endregion
    //================================================================================

    //================================================================================
    // region EXPANDABLE ROW ON CLICK LISTENER

    @Override
    public void sectionHeaderClicked(SectionHeader header, int sectionIndex, int expansionState) {
        // Logic for when a section header is clicked
        // This could also be used to handle expansion logic
    }

    @Override
    public void sectionSubHeaderClicked(SectionSubHeader subHeader, int sectionIndex, int expansionState)
    {
        // Logic for when a sub header is clicked
        // In this implementation, clicking the sub header row is what handles our expanding and minimizing

        switch (expansionState)
        {
            case ExpandableRecyclerViewAdapter.EXPANSION_STATE_EXPANDED:
            {
                // section is currently expanded - minimize it
                setExpansionStateMinimized(sectionIndex);
                break;
            }
            case ExpandableRecyclerViewAdapter.EXPANSION_STATE_MINIMIZED:
            {
                // section is currently minimized

                // Check if there is content already loaded
                if(sectionHasContent(sectionIndex))
                {
                    // We can show the section
                    setExpansionStateExpanded(sectionIndex);

                    // At this point you may want to call the activity and refresh the data source as well.
                }
                else
                {
                    // Section is empty - set the expansion state and try to load the section
                    setExpansionStateLoading(sectionIndex);
                    ExampleOneActivity activity = (ExampleOneActivity) context;
                    if(sectionIndex == 0)
                    {
                        activity.loadDogs();
                    }
                    else if(sectionIndex == 1)
                    {
                        activity.loadCats();
                    }
                    else
                    {
                        activity.loadGoats();
                    }
                }

                break;
            }

            case ExpandableRecyclerViewAdapter.EXPANSION_STATE_ERROR_LOADING:
            {
                // Error occurred when loading - try to reload
                setExpansionStateLoading(sectionIndex);
                ExampleOneActivity activity = (ExampleOneActivity) context;
                if(sectionIndex == 0)
                {
                    activity.loadDogs();
                }
                else if(sectionIndex == 1)
                {
                    activity.loadDogs();
                }
                else
                {
                    activity.loadGoats();
                }

                break;
            }
        }
    }

    @Override
    public void sectionContentClicked(Animal content, int sectionIndex, int sectionContentIndex)
    {
        // A content row was clicked
        // Here we would likely drill down to our next view from the list and show a detailed view.
    }

    // endregion
    //================================================================================
}
