package com.example.interfaces.interfacesapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.GridLayout.LayoutParams;
import android.util.TypedValue;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Team input list for creating a new tournament.
 *
 * @author Anatolie Diordita (Code + UI)
 * @author Willem Kowal (Code)
 * @author Willie Ausrotas (UI)
 */
public class Create_InputTeams extends AppCompatActivity {

    private String name, type;
    private int amountOfTeams;

    final int REQUEST_CODE=0;

    //tag is the full string for the id of the jersey passed by the Jersey Selection
    private String tag;
    //Jersey is the int value of the jersey, as associiaitied with somrtihng
    private int jersey;

    //Int that passes through to Jersey Selection that keeps track of something
    private int ID; //ID OF JERSEY CLICKED THAT CAN BE USED LATER

    //SUMTING
    private int[] jerseyArray;

    private HashMap<Integer, Integer> testMap = new HashMap<>(); //relating i to jersey
    private HashMap<Integer, ImageButton> jerseyMap = new HashMap<>(); //relating i to imgButton
    private HashMap<ImageButton, Integer> imageJersey = new HashMap<>();//relating imgButton to jersey



    final Context context = this;
    //final Drawable errorIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState){

        System.out.println("TESTING FILE OUTPUT");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_teams);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        jerseyArray = new int[amountOfTeams];



        Intent extras = getIntent();

        name = extras.getStringExtra("tourName");
        type = extras.getStringExtra("tourType");
        amountOfTeams = extras.getIntExtra("tourTeams", 0);
        tag = extras.getStringExtra("tag");

        Log.i("ON_CREATE", tag + " is the tag value");



        // Get the container for team name EditText objects
        LinearLayout TeamInputList = (LinearLayout) findViewById(R.id.teamListContainer);

        // Object containing visual settings for EditText inputs to be generated below
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(750, LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);



        // Generate x amount of EditText inputs on the team name entry page
        for (int i = 0; i < amountOfTeams; i++) {

            EditText temp = new EditText(this);

            temp.setHint("Team " + (i + 1));
            temp.setId(i);
            temp.setLayoutParams(layoutParams);
            temp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);



            //Creates x Image Buttons to customize Jersey Types
            final ImageButton tempImg = new ImageButton(this);
            tempImg.setImageResource(R.drawable.shirtclick);
            tempImg.setBackgroundDrawable(null);
            tempImg.setId(i);

            //Puts ImageButton with ID i into HashMap
            jerseyMap.put(i, tempImg);//get(i) returns ImageButton
            imageJersey.put(tempImg, jersey);//get tempImg, returns jersey




            //Listens for onClick of ImageButton, takes user to Jersey Selection screen
            tempImg.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), JerseySelection.class);
                    intent.putExtra("IDval", tempImg.getId());

                    startActivityForResult(intent, REQUEST_CODE);
                }
            });

            imageParams.setMargins((750 - (int) tempImg.getX()), 0, 0, -125);
            tempImg.setLayoutParams(imageParams);


            // Push the EditText object into the list
            TeamInputList.addView(tempImg);
            TeamInputList.addView(temp);

            String str= temp.getText().toString();

            if(str.equalsIgnoreCase("") ){
                Drawable dr = getResources().getDrawable(R.drawable.chevron_right);
                //add an error icon to your drawable files
                dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
                temp.setCompoundDrawables(null, null, dr, null);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if ((data.hasExtra("IDval"))
                    &&(data.hasExtra("tag"))) {

                tag = data.getStringExtra("tag"); //tag is the placeholder string for the integer jersey. Since ImageButtons can use tags, I set the tags for all objects
                jersey = Integer.parseInt(data.getStringExtra("tag"));
                ID = data.getIntExtra("IDval", 0);
                ImageButton test = jerseyMap.get(ID);
                jersey = customizeTeams(jersey);
                test.setImageResource(jersey);
                testMap.put(ID, jersey);
            }
            else if((data.hasExtra("customize"))) {

            }
        }


    }




    /**
     * Listener for every time user submits the create form.
     *
     * @param view The window's view
     */
    public void onViewTournament(View view) {

        // Initialize view variables
        String[] teamNames = new String[amountOfTeams];
        LinearLayout container = (LinearLayout) findViewById(R.id.teamListContainer);

        // Loop through and gather values of all team names from EditText elements
        for (int i = 0; i < container.getChildCount(); i++) {

            View theView = container.getChildAt(i);

            if (theView instanceof EditText) {
                EditText editText = (EditText) theView;

                String value = editText.getText().toString();

                int id = editText.getId();

                // If no team name entered, default to hint value on input
                if (value.matches("")) {
                    editText.setError("Please enter a team name.");
                    return;
                }
                //value = editText.getHint().toString();

                // Verify team name is in bounds for addition to array
                if (id < teamNames.length)
                    teamNames[id] = value;

            }

        }

        // Create and save the new tournament
        Tournament createdTournament = createTournament(name, teamNames, type);
        createdTournament.saveTournament();

        // Move on to home screen
        Intent intent = new Intent(getApplicationContext(), Home.class);
        intent.putExtras(createdTournament.getTourBun());
        startActivityForResult(intent, 0);

    }

    private int customizeTeams(int jerseyInt) {
        if (jerseyInt != 0) {
            switch (jerseyInt) {
                case 1:
                    jerseyInt= R.drawable.shirt1;
                    break;
                case 2:
                    jerseyInt= R.drawable.shirt2;
                    break;
                case 3:
                    jerseyInt= R.drawable.shirt3;
                    break;
                case 4:
                    jerseyInt= R.drawable.shirt4;
                    break;
                case 5:
                    jerseyInt= R.drawable.shirt5;
                    break;
                case 6:
                    jerseyInt= R.drawable.shirt6;
                    break;
                case 7:
                    jerseyInt= R.drawable.shirt7;
                    break;
                case 8:
                    jerseyInt= R.drawable.shirt8;
                    break;
                case 9:
                    jerseyInt= R.drawable.shirt9;
                    break;
                case 10:
                    jerseyInt= R.drawable.shirt10;
                    break;
                case 11:
                    jerseyInt= R.drawable.shirt11;
                    break;
                case 12:
                    jerseyInt= R.drawable.shirt12;
                    break;
                case 13:
                    jerseyInt= R.drawable.shirt13;
                    break;
                case 14:
                    jerseyInt= R.drawable.shirt14;
                    break;
                case 15:
                    jerseyInt= R.drawable.shirt15;
                    break;
                case 16:
                    jerseyInt= R.drawable.shirt16;
                    break;
                case 17:
                    jerseyInt= R.drawable.shirt17;
                    break;
                case 18:
                    jerseyInt= R.drawable.shirt18;
                    break;
                case 19:
                    jerseyInt= R.drawable.shirt19;
                    break;
                case 20:
                    jerseyInt= R.drawable.shirt20;
                    break;
            }
        }
        return jerseyInt;
    }

    /**
     * Returns a new Tournament with a knockout layout.
     *
     * @param name
     * @param teamNames
     * @return
     */

    private Tournament createKnockout(String name, String[] teamNames) {
        int numRounds = (int) Math.ceil(Math.log10((double) teamNames.length) / Math.log10(2)), numMatches = teamNames.length / 2;

        Log.i("CREATE_KNOCK", "Start creation of knockout (rounds: " + numRounds + " ~ matches: " + numMatches);

        if (teamNames.length % 2 == 1 || teamNames.length % 4 == 1) {
            numRounds++;
            Log.i("CREATE_KNOCK", "Odd number of teams, adding an extra round");
        }

        Tournament tournament = new Tournament(name, "knockout", teamNames.length, numRounds);

        // Add teams to tournaments from inputs
        for (int i = 0; i < teamNames.length; i++) {
            try {
                tournament.addTeam(new Team(teamNames[i], testMap.get(i)));
            }catch(NullPointerException ne) {
                tournament.addTeam(new Team(teamNames[i]));
            }
            Log.i("CREATE_KNOCK", "Added team " + teamNames[i] + " to position " + i + " with jersey " + testMap.get(i));
        }

        do {//Create empty rounds of play
            tournament.addRound(numMatches);
            numMatches = numMatches / 2;
            Log.i("CREATE_KNOCK", "Added round " + numMatches);
        } while (numMatches > 0);

        if (teamNames.length % 2 != 0 || teamNames.length % 4 != 0) {
            Log.i("CREATE_KNOCK", "Odd number of teams, adding extra round to end");
            tournament.addRound(1);
        }

        Log.i("CREATE_KNOCK", "Randomizing tournament...");
        tournament.randomStart();

        return tournament;
    }

    /**
     * Returns a new Tournament with a round robbin layout.
     *
     * @param name
     * @param teamNames
     * @return
     */
    private Tournament createRoundRobin(String name, String[] teamNames) {

        Log.i("CREATE_ROBIN", "Start creation of knockout (rounds: " + teamNames.length + ")");

        Tournament tournament = new Tournament(name, "roundrobin", teamNames.length, teamNames.length - 1);
// Add teams to tournaments from inputs
        for (int i = 0; i < teamNames.length; i++) {
            try {
                tournament.addTeam(new Team(teamNames[i], testMap.get(i)));
            }catch(NullPointerException ne) {
                tournament.addTeam(new Team(teamNames[i]));
            }
            Log.i("CREATE_ROBIN", "Added team " + teamNames[i] + " to position " + i + " with jersey ");
        }
//Create empty rounds
        for (int i = 0; i < teamNames.length - 1; i++) {
            tournament.addRound(teamNames.length - i - 1);
            Log.i("CREATE_ROBIN", "Added team " + teamNames[i] + " to position " + i + " with jersey " + testMap.get(i));
        }
//Fill each round with the required teams
        for (int i = 0; i < teamNames.length - 1; i++) {//round loop

            Log.i("CREATE_ROBIN", "Added round " + i + " with " + tournament.getRound(i).getNumMatches() + " matches");

            for (int j = i + 1; j < teamNames.length; j++) {//match loop
                Log.i("CREATE_ROBIN", "Added match at round " + i + " between " + tournament.getTeam(i).getName() + " and " + tournament.getTeam(i + 1).getName());
                tournament.addMatch(i, tournament.getTeam(i), tournament.getTeam(j));
            }

        }
//Going to try and print out the string


        return tournament;
    }

    /**
     * Returns a new Tournament with a combo layout.
     * @param name
     * @param teamNames
     * @return
     */
    private Tournament createCombo(String name, String[] teamNames) {

        Log.i("CREATE_COMBO", "Start creation of combo (rounds: " + teamNames.length + ")");

        //Determine number of rounds
        int numRounds = teamNames.length - 1;
        int numKnockoutRounds = 0;
        numKnockoutRounds = ((int) Math.floor(Math.log(numRounds) / Math.log(2)));
        numRounds += numKnockoutRounds;

        //Load teams
        Tournament tournament = new Tournament(name, "combo", teamNames.length, numRounds);
        for (int i = 0; i < teamNames.length; i++) {
            try {
                tournament.addTeam(new Team(teamNames[i], testMap.get(i)));
            }catch(NullPointerException ne) {
                tournament.addTeam(new Team(teamNames[i]));
            }
            Log.i("CREATE_COMBO", "Add team: " + teamNames[i] + "  to position: " + i + " with jersey " + testMap.get(i) + "\n");
        }

        //Create the round robin rounds
        for (int i = 0; i < teamNames.length - 1; i++) {
            tournament.addRound(teamNames.length - i - 1);
            Log.i("CREATE_COMBO", "Added team " + teamNames[i] + " to position " + i);
        }

        //Assign round robin rounds
        for (int i = 0; i < teamNames.length - 1; i++) {// round loop
            Log.i("CREATE_COMBO", "Creating round " + i + " with " + tournament.getRound(i).getNumMatches() + " matches");

            for (int j = i + 1; j < teamNames.length; j++) {// match loop
                Log.i("CREATE_COMBO", "Added match at round " + i + " between " + tournament.getTeam(i).getName() + " and " + tournament.getTeam(i + 1).getName());
                tournament.addMatch(i, tournament.getTeam(i), tournament.getTeam(j));
            }

        }

        for (int i = numKnockoutRounds - 1; i >= 0; i--) {
            tournament.addRound((int) Math.pow(2, i));
        }

        Log.i("CREATE_COMBO", "Combo tournament has " + numRounds + " rounds");
        return tournament;

    }

    /**
     * A factory to create the required type of tournament.
     * @param name
     * @param teamNames
     * @param type
     * @return
     */
    private Tournament createTournament(String name, String[] teamNames, String type) {

        int numTeams = teamNames.length;
        Team[] teamList = new Team[numTeams];

        Tournament tournament;

        // Defaults to combo
        if (type.equals("Round Robin"))
            tournament = createRoundRobin(name, teamNames);
        else if (type.equals("Knockout"))
            tournament = createKnockout(name, teamNames);
        else
            tournament = createCombo(name, teamNames);



//TESTING STRING
        for (int i = 0; i < teamNames.length; i++) {
            //Log.i("TEST_STRING", String.valueOf(testMap.get(i)));
            tournament.getTeam(i).setJersey(testMap.get(i));
            Log.i("TEST_STRING", tournament.getTeam(i).getName() + "'s Jersey value: " + String.valueOf(tournament.getTeam(i).getJersey()));
        }

        return tournament;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

            alertDialogBuilder
                    .setTitle("Warning!")
                    .setMessage("Doing this will clear all tournament data from your device. Are you sure you want to continue?")
                    .setCancelable(false)
                    .setPositiveButton("Yes, I Understand",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivityForResult(intent, 0);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            return true;
        }

        else if (id == R.id.action_howtouse) {
            Intent intent = new Intent(getApplicationContext(), HowToUse.class);
            startActivityForResult(intent, 0);
            return true;
        }

        return super.onOptionsItemSelected(item);

    }


}
