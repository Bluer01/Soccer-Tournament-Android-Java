package com.example.interfaces.interfacesapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

public class Home extends AppCompatActivity {

    private int teamNum;
    private Tournament tournament;
    private String name, type;
    private Bundle tourBun;
    final int REQUEST_CODE =0;

    final Context context = this;

    private Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private static File saveBitmap(Bitmap bm, String fileName){
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(path);
        if(!dir.exists())
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        File file = new File(dir, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_layout);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tourBun = getIntent().getExtras();
        tournament = new Tournament(context, tourBun);

        // Add tabs to TabLayout on screen
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Overview"));
        tabLayout.addTab(tabLayout.newTab().setText("Schedule"));
        tabLayout.addTab(tabLayout.newTab().setText("Rankings"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Create view pager for tabs + tab listeners
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final Home_PagerAdapter adapter = new Home_PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            // OnClick event for tabs: change screens when a tab is clicked
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });

        // If the user was previously on a "Round" view, return them back to that tab & round #
        Intent extras = getIntent();
        tourBun = extras.getExtras();

        int goToRound = extras.getIntExtra("goToRound", 0);

        if (goToRound != 0) {
            TabLayout tabs = (TabLayout) findViewById(R.id.tab_layout);
            TabLayout.Tab tab = tabs.getTabAt(1);
            assert tab != null;
            tab.select();
        }

    }

    // Inflater for menu object
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            Intent data = new Intent(getApplicationContext(), Create_InputTeams.class);
            data.putExtra("customize","ATTEMPTING TO SEND CUSTOMIZE");
            setResult(RESULT_OK, data);
            super.finish();


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

            alertDialogBuilder
                    .setTitle("Warning!")
                    .setMessage("Doing this will clear all tournament data from your device. Are you sure you want to continue?")
                    .setCancelable(false)
                    .setPositiveButton("Yes, I Understand", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
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
        } /*else if (id == R.id.action_share) {
            Bitmap bm = screenShot(getWindow().getDecorView().getRootView());
            File file = saveBitmap(bm, "mantis_image.png");
            Log.i("chase", "filepath: " + file.getAbsolutePath());
            Uri uri = Uri.fromFile(new File(file.getAbsolutePath()));
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out my app.");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setType("image/*");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "share via"));


                // User chose the "Share" action, mark the current item
                // as a favorite...
                return true;
        }*/ else if (id == R.id.action_howtouse) {
            Intent intent = new Intent(getApplicationContext(), HowToUse.class);
            startActivityForResult(intent, 0);
            return true;
        } else if (id == R.id.action_customize) {
            Intent intentCustom = new Intent(getApplicationContext(), Create_InputTeams.class);
            customize();

            intentCustom.putExtra("tourName", name);
            intentCustom.putExtra("tourType", type);
            intentCustom.putExtra("tourTeams", teamNum);

            startActivityForResult(intentCustom, 0);
            return true;
        }



        return super.onOptionsItemSelected(item);

    }

    public void customize() {
        Tournament tournament = new Tournament(this, tourBun);
        int tempNum = tournament.getNumTeams();
        String tempType = tournament.getType();
        String tempName = tournament.getName();

        teamNum = tempNum;
        type = tempType;
        name = tempName;
    }

}
