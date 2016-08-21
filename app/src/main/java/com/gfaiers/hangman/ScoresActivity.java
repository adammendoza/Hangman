package com.gfaiers.hangman;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;


public class ScoresActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    final static String PREFERENCE_SETTINGS = "settingWordLength";

    private AdView mAdView;
    private GoogleApiClient mGoogleApiClient;
    private static int RC_SIGN_IN = 9001;
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        MobileAds.initialize(this, getResources().getString(R.string.banner_ad_unit_id_java));
        mAdView = (AdView) findViewById(R.id.adViewBanner);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        // Create the Google Api Client with access to the Play Games services
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                // add other APIs and scopes here as needed
                .build();

        long intBestTime1, intBestTime2, intBestTime3, intBestTime4, intBestTime5, intBestTime6, intBestTime7, intBestTime8, intBestTime9, intBestTime10;
        String strBestTime1, strBestTime2, strBestTime3, strBestTime4, strBestTime5, strBestTime6,strBestTime7, strBestTime8, strBestTime9, strBestTime10;
        TextView textView1Score = (TextView)findViewById(R.id.textView1Score);
        TextView textView2Score = (TextView)findViewById(R.id.textView2Score);
        TextView textView3Score = (TextView)findViewById(R.id.textView3Score);
        TextView textView4Score = (TextView)findViewById(R.id.textView4Score);
        TextView textView5Score = (TextView)findViewById(R.id.textView5Score);
        TextView textView6Score = (TextView)findViewById(R.id.textView6Score);
        TextView textView7Score = (TextView)findViewById(R.id.textView7Score);
        TextView textView8Score = (TextView)findViewById(R.id.textView8Score);
        TextView textView9Score = (TextView)findViewById(R.id.textView9Score);
        TextView textView10Score = (TextView)findViewById(R.id.textView10Score);
        TextView textView1Name = (TextView)findViewById(R.id.textView1PN);
        TextView textView2Name = (TextView)findViewById(R.id.textView2PN);
        TextView textView3Name = (TextView)findViewById(R.id.textView3PN);
        TextView textView4Name = (TextView)findViewById(R.id.textView4PN);
        TextView textView5Name = (TextView)findViewById(R.id.textView5PN);
        TextView textView6Name = (TextView)findViewById(R.id.textView6PN);
        TextView textView7Name = (TextView)findViewById(R.id.textView7PN);
        TextView textView8Name = (TextView)findViewById(R.id.textView8PN);
        TextView textView9Name = (TextView)findViewById(R.id.textView9PN);
        TextView textView10Name = (TextView)findViewById(R.id.textView10PN);
        LinearLayout layout1 = (LinearLayout)findViewById(R.id.layout1);
        LinearLayout layout2 = (LinearLayout)findViewById(R.id.layout2);
        LinearLayout layout3 = (LinearLayout)findViewById(R.id.layout3);
        LinearLayout layout4 = (LinearLayout)findViewById(R.id.layout4);
        LinearLayout layout5 = (LinearLayout)findViewById(R.id.layout5);
        LinearLayout layout6 = (LinearLayout)findViewById(R.id.layout6);
        LinearLayout layout7 = (LinearLayout)findViewById(R.id.layout7);
        LinearLayout layout8 = (LinearLayout)findViewById(R.id.layout8);
        LinearLayout layout9 = (LinearLayout)findViewById(R.id.layout9);
        LinearLayout layout10 = (LinearLayout)findViewById(R.id.layout10);
        Button buttonClearTimes = (Button) findViewById(R.id.buttonClearTimes);
        Button buttonAchievements = (Button) findViewById(R.id.buttonAchievements);

        SharedPreferences settings = getSharedPreferences(PREFERENCE_SETTINGS, MODE_PRIVATE);
        intBestTime1 = settings.getLong("settingBestTime1", 9223372036854775807L);
        intBestTime2 = settings.getLong("settingBestTime2", 9223372036854775807L);
        intBestTime3 = settings.getLong("settingBestTime3", 9223372036854775807L);
        intBestTime4 = settings.getLong("settingBestTime4", 9223372036854775807L);
        intBestTime5 = settings.getLong("settingBestTime5", 9223372036854775807L);
        intBestTime6 = settings.getLong("settingBestTime6", 9223372036854775807L);
        intBestTime7 = settings.getLong("settingBestTime7", 9223372036854775807L);
        intBestTime8 = settings.getLong("settingBestTime8", 9223372036854775807L);
        intBestTime9 = settings.getLong("settingBestTime9", 9223372036854775807L);
        intBestTime10 = settings.getLong("settingBestTime10", 9223372036854775807L);
        strBestTime1 = settings.getString("settingBestTime1Name", "");
        strBestTime2 = settings.getString("settingBestTime2Name", "");
        strBestTime3 = settings.getString("settingBestTime3Name", "");
        strBestTime4 = settings.getString("settingBestTime4Name", "");
        strBestTime5 = settings.getString("settingBestTime5Name", "");
        strBestTime6 = settings.getString("settingBestTime6Name", "");
        strBestTime7 = settings.getString("settingBestTime7Name", "");
        strBestTime8 = settings.getString("settingBestTime8Name", "");
        strBestTime9 = settings.getString("settingBestTime9Name", "");
        strBestTime10 = settings.getString("settingBestTime10Name", "");
        assert textView1Name != null;
        assert textView1Score != null;
        assert textView2Name != null;
        assert textView2Score != null;
        assert textView3Name != null;
        assert textView3Score != null;
        assert textView4Name != null;
        assert textView4Score != null;
        assert textView5Name != null;
        assert textView5Score != null;
        assert textView6Name != null;
        assert textView6Score != null;
        assert textView7Name != null;
        assert textView7Score != null;
        assert textView8Name != null;
        assert textView8Score != null;
        assert textView9Name != null;
        assert textView9Score != null;
        assert textView10Name != null;
        assert textView10Score != null;
        assert layout1 != null;
        assert layout2 != null;
        assert layout3 != null;
        assert layout4 != null;
        assert layout5 != null;
        assert layout6 != null;
        assert layout7 != null;
        assert layout8 != null;
        assert layout9 != null;
        assert layout10 != null;
        assert buttonClearTimes != null;
        assert buttonAchievements != null;

        String strTemp;
        if (intBestTime1 == 9223372036854775807L){
            buttonClearTimes.setVisibility(View.GONE);
            layout1.setVisibility(View.GONE);
        } else {
            strTemp = "" + intBestTime1;
            textView1Score.setText(strTemp);
            textView1Name.setText(strBestTime1);
        }
        if (intBestTime2 == 9223372036854775807L){
            layout2.setVisibility(View.GONE);
        } else {
            strTemp = "" + intBestTime2;
            textView2Score.setText(strTemp);
            textView2Name.setText(strBestTime2);
        }
        if (intBestTime3 == 9223372036854775807L) {
            layout3.setVisibility(View.GONE);
        } else {
            strTemp = "" + intBestTime3;
            textView3Score.setText(strTemp);
            textView3Name.setText(strBestTime3);
        }
        if (intBestTime4 == 9223372036854775807L) {
            layout4.setVisibility(View.GONE);
        } else {
            strTemp = "" + intBestTime4;
            textView4Score.setText(strTemp);
            textView4Name.setText(strBestTime4);
        }
        if (intBestTime5 == 9223372036854775807L) {
            layout5.setVisibility(View.GONE);
        } else {
            strTemp = "" + intBestTime5;
            textView5Score.setText(strTemp);
            textView5Name.setText(strBestTime5);
        }
        if (intBestTime6 == 9223372036854775807L) {
            layout6.setVisibility(View.GONE);
        } else {
            strTemp = "" + intBestTime6;
            textView6Score.setText(strTemp);
            textView6Name.setText(strBestTime6);
        }
        if (intBestTime7 == 9223372036854775807L) {
            layout7.setVisibility(View.GONE);
        } else {
            strTemp = "" + intBestTime7;
            textView7Score.setText(strTemp);
            textView7Name.setText(strBestTime7);
        }
        if (intBestTime8 == 9223372036854775807L) {
            layout8.setVisibility(View.GONE);
        } else {
            strTemp = "" + intBestTime8;
            textView8Score.setText(strTemp);
            textView8Name.setText(strBestTime8);
        }
        if (intBestTime9 == 9223372036854775807L) {
            layout9.setVisibility(View.GONE);
        } else {
            strTemp = "" + intBestTime9;
            textView9Score.setText(strTemp);
            textView9Name.setText(strBestTime9);
        }
        if (intBestTime10 == 9223372036854775807L) {
            layout10.setVisibility(View.GONE);
        } else {
            strTemp = "" + intBestTime10;
            textView10Score.setText(strTemp);
            textView10Name.setText(strBestTime10);
        }

        buttonAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), 1);
            }
        });

        buttonClearTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When the button is pressed the user has to be prompted with an "Are you sure?"
                // Window, if they select Yes, then it'll clear the times
                final Dialog dialog = new Dialog(ScoresActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.fragment_dialog);

                EditText editTextPlayerName = (EditText) dialog.findViewById(R.id.editTextPlayerName);
                editTextPlayerName.setVisibility(View.GONE);
                TextView textViewFragTime = (TextView) dialog.findViewById(R.id.textViewFragTime);
                textViewFragTime.setVisibility(View.GONE);
                ImageView imageResult = (ImageView) dialog.findViewById(R.id.imageResult);
                imageResult.setVisibility(View.GONE);
                String strTemp = getResources().getString(R.string.no_reverse) + "\n" + getResources().getString(R.string.are_you_sure);
                TextView text = (TextView) dialog.findViewById(R.id.textViewCaption);
                text.setText(strTemp);

                Button buttonPositive = (Button) dialog.findViewById(R.id.buttonPositive);
                buttonPositive.setText(getResources().getString(R.string.yes));
                buttonPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Clear the times
                        SharedPreferences settings = getSharedPreferences(PREFERENCE_SETTINGS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putLong("settingBestTime1", 9223372036854775807L);
                        editor.putString("settingBestTime1Name", "");
                        editor.putLong("settingBestTime2", 9223372036854775807L);
                        editor.putString("settingBestTime2Name", "");
                        editor.putLong("settingBestTime3", 9223372036854775807L);
                        editor.putString("settingBestTime3Name", "");
                        editor.putLong("settingBestTime4", 9223372036854775807L);
                        editor.putString("settingBestTime4Name", "");
                        editor.putLong("settingBestTime5", 9223372036854775807L);
                        editor.putString("settingBestTime5Name", "");
                        editor.putLong("settingBestTime6", 9223372036854775807L);
                        editor.putString("settingBestTime6Name", "");
                        editor.putLong("settingBestTime7", 9223372036854775807L);
                        editor.putString("settingBestTime7Name", "");
                        editor.putLong("settingBestTime8", 9223372036854775807L);
                        editor.putString("settingBestTime8Name", "");
                        editor.putLong("settingBestTime9", 9223372036854775807L);
                        editor.putString("settingBestTime9Name", "");
                        editor.putLong("settingBestTime10", 9223372036854775807L);
                        editor.putString("settingBestTime10Name", "");
                        editor.apply();
                        dialog.dismiss();
                        finish();
                    }
                });

                Button buttonNegative = (Button) dialog.findViewById(R.id.buttonNegative);
                buttonNegative.setText(getResources().getString(R.string.no));
                buttonNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop(){
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onPause(){
        if (mAdView != null){
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (mAdView != null){
            mAdView.resume();
        }
    }

    @Override
    protected void onDestroy(){
        if (mAdView != null){
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // The player is signed in. Hide the sign-in button and allow the
        // player to proceed.
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            // already resolving
            return;
        }

        // if the sign-in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.sign_in_other_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign-in, please try again later."
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, getResources().getString(R.string.sign_in_other_error))) {
                mResolvingConnectionFailure = false;
            }
        }

        // Put code here to display the sign-in button
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Attempt to reconnect
        mGoogleApiClient.connect();
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                // Bring up an error dialog to alert the user that sign-in
                // failed. The R.string.sign_in_failure should reference an error
                // string in your strings.xml file that tells the user they
                // could not be signed in, such as "Unable to sign in."
                BaseGameUtils.showActivityResultError(this,
                        requestCode, resultCode, R.string.sign_in_failure);
            }
        }
    }

    // Call when the sign-in button is clicked
    private void signInClicked() {
        mSignInClicked = true;
        mGoogleApiClient.connect();
    }

    // Call when the sign-out button is clicked
    private void signOutClicked() {
        mSignInClicked = false;
        Games.signOut(mGoogleApiClient);
    }
}
