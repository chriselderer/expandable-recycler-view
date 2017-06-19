package com.elder.expandablerecyclerviewdemo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.elder.expandablerecyclerviewdemo.models.Animal;

public class ExampleOneActivity extends AppCompatActivity
{
    //================================================================================
    // region VARIABLES

    Toolbar toolbar;
    RecyclerView recyclerView;
    ExpandableRecyclerViewAdapterImplementationOne adapter;

    // endregion
    //================================================================================

    //================================================================================
    // region LIFECYCLE

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        // Get views
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Set up toolbar
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Set up adapter
        adapter = new ExpandableRecyclerViewAdapterImplementationOne(this);

        // Pre load some of the data
        loadCats();

        // recycler view
        setUpRecyclerView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        adapter.onRestoreInstanceState(savedInstanceState);
    }

    // endregion
    //================================================================================

    //================================================================================
    // region RECYCLER VIEW

    private void setUpRecyclerView()
    {

        // Set up decorations
        ExpandableDecorationImplementationOne decoration = new ExpandableDecorationImplementationOne(this);

        // Layout manager
        LinearLayoutManager manager = new LinearLayoutManager(this);

        // Add to recycler view
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);
    }

    // endregion
    //================================================================================

    //================================================================================
    // region LOAD DATA

    public void loadDogs()
    {
        // Mock networking operation

        // Create dogs
        Drawable image1 = ContextCompat.getDrawable(this, R.drawable.dog1);
        int fluffFactor1 = 25;
        int speed1 = 30;
        Animal dog1 = new Animal(image1, "Kona", fluffFactor1, speed1);

        Drawable image2 = ContextCompat.getDrawable(this, R.drawable.dog2);
        int fluffFactor2 = 9;
        int speed2 = 50;
        Animal dog2 = new Animal(image2, "Flash", fluffFactor2, speed2);

        Drawable image3 = ContextCompat.getDrawable(this, R.drawable.dog3);
        int fluffFactor3 = 10;
        int speed3 = 15;
        Animal dog3 = new Animal(image3, "Buffy", fluffFactor3, speed3);

        // add dogs to data source
        adapter.dogs.add(dog1);
        adapter.dogs.add(dog2);
        adapter.dogs.add(dog3);

        // Simulate network delay, then update expansion state
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.setExpansionStateExpanded(0);
            }
        }, 1800);
    }

    public void loadCats()
    {
        // Mock networking operation
        // Create list of cats

        Drawable image1 = ContextCompat.getDrawable(this, R.drawable.cat1);
        int fluffFactor1 = 8;
        int speed1 = 15;
        Animal cat1 = new Animal(image1, "Smokey", fluffFactor1, speed1);

        Drawable image2 = ContextCompat.getDrawable(this, R.drawable.cat2);
        int fluffFactor2 = 9;
        int speed2 = 40;
        Animal cat2 = new Animal(image2, "Tiger", fluffFactor2, speed2);

        Drawable image3 = ContextCompat.getDrawable(this, R.drawable.cat3);
        int fluffFactor3 = 5;
        int speed3 = -5;
        Animal cat3 = new Animal(image3, "Grumps", fluffFactor3, speed3);

        // add cats to data source
        adapter.cats.add(cat1);
        adapter.cats.add(cat2);
        adapter.cats.add(cat3);
    }

    public void loadGoats() {
        // Mock networking operation
        // Create list of goats

        Drawable image1 = ContextCompat.getDrawable(this, R.drawable.goat1);
        int fluffFactor1 = 8;
        int speed1 = 18;
        Animal goat1 = new Animal(image1, "Charles", fluffFactor1, speed1);

        Drawable image2 = ContextCompat.getDrawable(this, R.drawable.goat2);
        int fluffFactor2 = 6;
        int speed2 = 20;
        Animal goat2 = new Animal(image2, "Angus", fluffFactor2, speed2);

        Drawable image3 = ContextCompat.getDrawable(this, R.drawable.goat3);
        int fluffFactor3 = 7;
        int speed3 = 11;
        Animal goat3 = new Animal(image3, "Sky", fluffFactor3, speed3);

        // add goats to data source
        adapter.goats.add(goat1);
        adapter.goats.add(goat2);
        adapter.goats.add(goat3);

        // Simulate network delay, then update expansion state
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.setExpansionStateExpanded(2);
            }
        }, 1800);
    }

    // endregion
    //================================================================================

}
