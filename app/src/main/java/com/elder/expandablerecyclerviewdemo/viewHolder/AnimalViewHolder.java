package com.elder.expandablerecyclerviewdemo.viewHolder;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.elder.expandablerecyclerviewdemo.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by chris on 2017-06-13.
 */

public class AnimalViewHolder extends RecyclerView.ViewHolder
{
    //================================================================================
    // region VARIABLES


    CircleImageView animalImageView;
    TextView nameTextView;
    TextView fluffFactorTextView;
    TextView speedTextView;

    // endregion
    //================================================================================

    //================================================================================
    // region CONSTRUCTOR

    public  AnimalViewHolder(View view)
    {
        super(view);
        animalImageView = (CircleImageView) view.findViewById(R.id.animal_circle_image_view);
        nameTextView = (TextView) view.findViewById(R.id.name_text_view);
        fluffFactorTextView = (TextView) view.findViewById(R.id.fluff_factor_text_view);
        speedTextView = (TextView) view.findViewById(R.id.speed_text_view);
    }

    // endregion
    //================================================================================

    //================================================================================
    // region BIND VIEWS

    public void setAnimalImage(Drawable drawable)
    {
        animalImageView.setImageDrawable(drawable);
    }

    public void setName(String name)
    {
        this.nameTextView.setText(name);
    }

    public void setFluffFactorText(String text)
    {
        fluffFactorTextView.setText(text);
    }

    public void setSpeedTextView(String text)
    {
        speedTextView.setText(text);
    }

    // endregion
    //================================================================================

}
