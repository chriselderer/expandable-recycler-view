package com.elder.expandablerecyclerviewdemo;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    //================================================================================
    // region VARIABLES

    Toolbar toolbar;
    Button exampleOneButton;
    Button exampleTwoButton;

    // endregion
    //================================================================================

    //================================================================================
    // region LIFECYCLE

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get views
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        exampleOneButton = (Button) findViewById(R.id.example_one_button);
        exampleTwoButton = (Button) findViewById(R.id.example_two_button);

        // On click
        exampleOneButton.setOnClickListener(this);
        exampleTwoButton.setOnClickListener(this);

        // Set up toolbar
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    // endregion
    //================================================================================

    //================================================================================
    // region ON CLICK

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.example_one_button:
                Intent intent1 = new Intent(this, ExampleOneActivity.class);
                startActivity(intent1);
                break;
            case R.id.example_two_button:
                Intent intent2 = new Intent(this, ExampleTwoActivity.class);
                startActivity(intent2);
                break;
        }
    }

    // endregion
    //================================================================================

}