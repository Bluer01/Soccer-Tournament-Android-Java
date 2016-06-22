package com.example.interfaces.interfacesapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class JerseySelection extends AppCompatActivity {

    private String name, type;
    private int amountOfTeams, IDval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jersey_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent extras = getIntent();

        IDval = extras.getIntExtra("IDval", 0);
    }

    public void updateJersey(View view) {
        String tag = view.getTag().toString();
        Intent data = new Intent();
        data.putExtra("tag", tag); //WHAT IS THIS TAG AGAIN?
        data.putExtra("IDval", IDval);
        setResult(RESULT_OK, data);
        super.finish();
    }

}
