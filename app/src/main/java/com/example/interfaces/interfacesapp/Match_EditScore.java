package com.example.interfaces.interfacesapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Match_EditScore extends AppCompatActivity {

    private Tournament tournament;

    int round, match;
    private Bundle tourBun;
    private EditText score1, score2;
    private TextView team1, team2, roundDisplay, matchDisplay;
    private String score1Val, score2Val;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_score);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tourBun = getIntent().getExtras();

        System.out.println("tourBun details at editscores: " +
                tourBun.getString("name") + "\n" +
                tourBun.getString("type") + "\n" +
                tourBun.getInt("numTeams") + "\n" +
                tourBun.getInt("numRounds"));

        tournament = new Tournament(context, tourBun);

        System.out.println("tournament at editscore is: ");
        System.out.println(tournament.toString());

        roundDisplay = (TextView) findViewById(R.id.round);
        matchDisplay = (TextView) findViewById(R.id.match);

        score1 = (EditText) findViewById(R.id.score1);
        score2 = (EditText) findViewById(R.id.score2);
        team1 = (TextView) findViewById(R.id.team1);
        team2 = (TextView) findViewById(R.id.team2);

        Intent extras = getIntent();

        round = extras.getIntExtra("round", 0);
        match = extras.getIntExtra("match", 0);

        System.out.println("round received in editscore is: ");
        System.out.println(round);
        System.out.println("match received in editscore is: ");
        System.out.println(match);
        initializeScreen();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeScreen() {

        Match game = tournament.getRound(round).getMatch(match);

        team1.setText(game.getTeam1().getName());
        team2.setText(game.getTeam2().getName());

        roundDisplay.setText("Round " + (round + 1));
        matchDisplay.setText("Match " + (match + 1));

        if ( game.getTeam1Goals() != -1 ) {
            score1.setText(String.valueOf(tournament.getRound(round).getMatch(match).getTeam1Goals()));
            score2.setText(String.valueOf(tournament.getRound(round).getMatch(match).getTeam2Goals()));
        }

    }

    public void onSaveScores(View view){

        boolean validated = true;

        String score1Val = score1.getText().toString();
        String score2Val = score2.getText().toString();

        if ( score1Val.matches("") ) {
            score1.setError("Please enter goals for team 1");
            validated = false;
        }

        if ( score2Val.matches("") ) {
            score2.setError("Please enter goals for team 2");
            validated = false;
        }

        // Not allowing ties to be input
        if ( score1Val.matches(score2Val) ) {

            new AlertDialog.Builder(context)
                    .setTitle("Oops!")
                    .setMessage("There must be a winner out of the two teams to continue.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) { }
                    })
                    .show();

            validated = false;
        }

        if ( validated ) {
            saveScores(Integer.parseInt(score1Val), Integer.parseInt(score2Val));

            Intent intent = new Intent(getApplicationContext(), Home.class);
            intent.putExtra("goToRound", (round + 1));
            intent.putExtras(tournament.getTourBun());
            startActivityForResult(intent, 0);
        }
    }

    private void saveScores(int score1, int score2) {
        tournament.enterScore(round, match, score1, score2);
        tournament.saveTournament();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);

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

            android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            return true;
        }

        else if ( id == R.id.action_howtouse) {
            Intent intent = new Intent(getApplicationContext(), HowToUse.class);
            startActivityForResult(intent, 0);
            return true;
        }

        else if ( id == android.R.id.home ) {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            intent.putExtra("goToRound", (round + 1));
            intent.putExtras(tournament.getTourBun());
            startActivityForResult(intent, 0);
        }

        return super.onOptionsItemSelected(item);

    }

}
