package com.elder.expandablerecyclerviewdemo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elder.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.elder.expandablerecyclerview.ExpandableRowOnClickListener;
import com.elder.expandablerecyclerview.ExpandableRowSubViewOnClickListener;
import com.elder.expandablerecyclerviewdemo.models.Animal;
import com.elder.expandablerecyclerviewdemo.models.SectionHeader;
import com.elder.expandablerecyclerviewdemo.models.SectionSubHeader;
import com.elder.expandablerecyclerviewdemo.viewHolder.AnimalViewHolder;
import com.elder.expandablerecyclerviewdemo.viewHolder.HeaderViewHolderTwo;
import com.elder.expandablerecyclerviewdemo.viewHolder.SubHeaderViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 2016-12-05.
 */
public class ExpandableRecyclerViewAdapterImplementationTwo
        extends ExpandableRecyclerViewAdapter<SectionHeader, SectionSubHeader, Animal, HeaderViewHolderTwo, SubHeaderViewHolder, AnimalViewHolder>
        implements ExpandableRowSubViewOnClickListener<SectionHeader, SectionSubHeader, Animal>,
        ExpandableRowOnClickListener<SectionHeader, SectionSubHeader, Animal>
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

    public ExpandableRecyclerViewAdapterImplementationTwo(Context context)
    {
        // Provide the ExpandableRecyclerViewAdapter with class information for our view holders
        super(HeaderViewHolderTwo.class,
                SubHeaderViewHolder.class,
                AnimalViewHolder.class);

        // Set the reference to context
        this.context = context;

        // Set the on click listener for sub views of our rows.
        this.expandableRowSubViewOnClickListener = this;
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
    public List<Animal> getContentForSection(int sectionIndex)
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
    public HeaderViewHolderTwo createSectionHeaderViewHolder(ViewGroup parent)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_row_two, parent, false);
        return new HeaderViewHolderTwo(view);
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
    public void bindSectionHeaderViewHolder(HeaderViewHolderTwo holder,
                                            SectionHeader header,
                                            int sectionIndex,
                                            int expansionState)
    {
        // set header text
        holder.setHeaderText(header.getTitle());

        // set up expand button
        Drawable drawable;
        if(expansionState == EXPANSION_STATE_MINIMIZED)
        {
            drawable = ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_right_black_24dp);
        }
        else
        {
            drawable = ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_down_black_24dp);
        }

        holder.setExpandButtonImage(drawable);

        // explicitly register the image button for click listening
        // DO NOT implement OnClickListener in this subclass.
        // OnClickListener is implemented by ExpandableRecyclerViewAdapter and delegates to
        // ExpandableRowSubViewOnClickListener (this must be implemented here).
        holder.expandButton.setOnClickListener(this);
    }

    @Override
    public void bindSectionSubHeaderViewHolder(SubHeaderViewHolder holder,
                                               SectionSubHeader subHeader,
                                               int sectionIndex,
                                               int expansionState)
    {
        // Only need to handle loading and error, sub header is hidden for other states

        switch (expansionState)
        {
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
        // Only show the sub header is the section is loading or had an error
        return  expansionState == EXPANSION_STATE_LOADING_CONTENT ||
                expansionState == EXPANSION_STATE_ERROR_LOADING;
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
    public void sectionHeaderClicked(SectionHeader header, int sectionIndex, int expansionState)
    {
        Log.i(String.valueOf(sectionIndex), "section Header Clicked");
    }

    @Override
    public void sectionSubHeaderClicked(SectionSubHeader subHeader, int sectionIndex, int expansionState)
    {
        // If there is an error, reload the section
        if(expansionState == EXPANSION_STATE_ERROR_LOADING)
        {
            // Reload
            setExpansionStateLoading(sectionIndex);

            ExampleTwoActivity activity = (ExampleTwoActivity) context;

            switch (sectionIndex)
            {
                case 0:
                    activity.loadDogs();
                    break;
                case 1:
                    activity.loadCats();
                    break;
                case 2:
                    activity.loadGoats();
                    break;
            }
        }
    }

    @Override
    public void sectionContentClicked(Animal content, int sectionIndex, int sectionContentIndex)
    {
        Log.i(String.valueOf(sectionIndex), "section Content Clicked");
    }

    // endregion
    //================================================================================

    //================================================================================
    // region EXPANDABLE ROW SUB VIEW ON CLICK LISTENER

    @Override
    public void onSubViewClickedInSectionHeader(View view, SectionHeader header, int sectionIndex, int expansionState)
    {
        if(view.getId() == R.id.expand_button)
        {
            if(expansionState == EXPANSION_STATE_MINIMIZED)
            {
                if(sectionHasContent(sectionIndex))
                {
                    setExpansionStateExpanded(sectionIndex);
                }
                else
                {
                    setExpansionStateLoading(sectionIndex);

                    ExampleTwoActivity activity = (ExampleTwoActivity) context;

                    switch (sectionIndex)
                    {
                        case 0:
                            activity.loadDogs();
                            break;
                        case 1:
                            activity.loadCats();
                            break;
                        case 2:
                            activity.loadGoats();
                            break;
                    }
                }
            }
            else if(expansionState == EXPANSION_STATE_EXPANDED)
            {
                setExpansionStateMinimized(sectionIndex);
            }
        }
    }

    @Override
    public void onSubViewClickedInSectionSubHeader(View view, SectionSubHeader subHeader, int sectionIndex, int expansionState)
    {
        Log.i(String.valueOf(sectionIndex), "sub view clicked in sub header");
    }

    @Override
    public void onSubViewClickedInSectionContentRow(View view, Animal content, int sectionIndex, int sectionContentIndex)
    {
        Log.i(String.valueOf(sectionIndex), "sub view clicked in content row");
    }


    // endregion
    //================================================================================
}
