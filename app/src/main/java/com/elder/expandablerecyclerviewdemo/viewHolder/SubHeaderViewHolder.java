package com.elder.expandablerecyclerviewdemo.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.elder.expandablerecyclerviewdemo.R;

/**
 * Created by chris on 2017-06-13.
 */

public class SubHeaderViewHolder extends RecyclerView.ViewHolder
{
    //================================================================================
    // region VARIABLES

    TextView subHeaderTextView;

    // endregion
    //================================================================================

    //================================================================================
    // region CONSTRUCTOR
    public SubHeaderViewHolder(View view)
    {
        super(view);
        subHeaderTextView = (TextView) view.findViewById(R.id.sub_header_text_view);
    }
    // endregion
    //================================================================================


    //================================================================================
    // region BIND VIEWS

    public void setSubHeaderText(String text)
    {
        subHeaderTextView.setText(text);
    }

    // endregion
    //================================================================================
}
