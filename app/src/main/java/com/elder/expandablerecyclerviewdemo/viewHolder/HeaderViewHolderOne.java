package com.elder.expandablerecyclerviewdemo.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.elder.expandablerecyclerviewdemo.R;

/**
 * Created by chris on 2017-06-13.
 */

public class HeaderViewHolderOne extends RecyclerView.ViewHolder
{
    //================================================================================
    // region VARIABLES


    TextView headerTextView;


    // endregion
    //================================================================================

    //================================================================================
    // region CONSTRUCTOR

    public HeaderViewHolderOne(View view)
    {
        super(view);
        headerTextView = (TextView) view.findViewById(R.id.header_text_view);
    }

    // endregion
    //================================================================================

    //================================================================================
    // region BIND VIEWS

    public void setHeaderText(String text)
    {
        headerTextView.setText(text);
    }

    // endregion
    //================================================================================
}
