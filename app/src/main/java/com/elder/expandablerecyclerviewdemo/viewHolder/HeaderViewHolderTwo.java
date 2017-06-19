package com.elder.expandablerecyclerviewdemo.viewHolder;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.elder.expandablerecyclerviewdemo.R;

/**
 * Created by chris on 2017-06-13.
 */

public class HeaderViewHolderTwo extends RecyclerView.ViewHolder
{
    //================================================================================
    // region VARIABLES

    TextView headerTextView;
    public ImageButton expandButton;

    // endregion
    //================================================================================

    //================================================================================
    // region CONSTRUCTOR

    public HeaderViewHolderTwo(View view)
    {
        super(view);
        headerTextView = (TextView) view.findViewById(R.id.header_text_view);
        expandButton = (ImageButton) view.findViewById(R.id.expand_button);
    }

    // endregion
    //================================================================================

    //================================================================================
    // region BIND VIEWS

    public void setHeaderText(String text)
    {
        headerTextView.setText(text);
    }


    public void setExpandButtonImage(Drawable drawable)
    {
        expandButton.setImageDrawable(drawable);
    }

    // endregion
    //================================================================================
}
