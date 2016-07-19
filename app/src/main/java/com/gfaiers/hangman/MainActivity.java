package com.gfaiers.hangman;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameUtils;



public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // Declare the imageView ID's in the correct guesses layout
    private static final int[] LETTER_IDS = {
            R.id.imageViewMain0, R.id.imageViewMain1, R.id.imageViewMain2, R.id.imageViewMain3,
            R.id.imageViewMain4, R.id.imageViewMain5, R.id.imageViewMain6, R.id.imageViewMain7,
            R.id.imageViewMain8, R.id.imageViewMain9, R.id.imageViewMain10,
    };

    final static String PREFERENCE_SETTINGS = "settingWordLength";
    static int intShortestWord, intLongestWord, intLives, intRand, intCount, intDemoPlay;
    public static int intLivesRem;
    static boolean booFirstPlay, booNoRate, booGuessInTitle, booTitleIncomplete;
    static String strWordsLists, strCustom, strLetters, strTitle;
    public static String strRandomGuess;
    static char chrRandomGuess;
    static long intTimesRan;
    static CountDownTimer cdTimer;
    private AdView mAdView;
    private GoogleApiClient mGoogleApiClient;
    private static int RC_SIGN_IN = 9001;
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);

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

        // Initialize the components
        Button buttonNewGame = (Button) findViewById(R.id.buttonNewGame);
        Button buttonHighScores = (Button) findViewById(R.id.buttonHighScores);
        Button buttonSettings = (Button) findViewById(R.id.buttonSettings);

        SharedPreferences settings = getSharedPreferences(PREFERENCE_SETTINGS, MODE_PRIVATE);
        intLongestWord = settings.getInt("settingLongestWord", 28) + 2;
        intShortestWord = settings.getInt("settingShortestWord", 1) + 1;
        intLives = settings.getInt("settingLives", 5) + 5;
        strWordsLists = settings.getString("settingWordsLists", getResources().getString(R.string.default_list));
        strCustom = settings.getString("settingCustom", null);
        booFirstPlay = settings.getBoolean("settingFirstPlay", true);
        intTimesRan = settings.getLong("settingTimesRan", 0);
        booNoRate = settings.getBoolean("settingNoRate", false);

        // Iterate the times the app has been loaded
        intTimesRan ++;
        // Then save this new figure
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("settingTimesRan", intTimesRan);
        editor.apply();
        // If the user hasn't said NEVER, and it's been loaded a multiple of 10 times then ask to rate
        if ((!booNoRate) && (intTimesRan % 10 == 0)){
            RateMe(getResources().getString(R.string.enjoyed), getResources().getString(R.string.ok),
                    getResources().getString(R.string.not_yet), getResources().getString(R.string.never));
        }
        long intBestTime1 = settings.getLong("settingBestTime1", 9223372036854775807L);

        assert  buttonNewGame != null;
        buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signInClicked();
                Intent intentGame = new Intent(MainActivity.this, HangmanActivity.class);
                final int result = 1;
                intentGame.putExtra("intShortestWord", intShortestWord);
                intentGame.putExtra("intLongestWord", intLongestWord);
                intentGame.putExtra("intLives", intLives);
                intentGame.putExtra("strWordsLists", strWordsLists);
                intentGame.putExtra("strCustom", strCustom);
                startActivityForResult(intentGame, result);
            }
        });

        assert buttonHighScores != null;
        if (intBestTime1 == 9223372036854775807L) {
            buttonHighScores.setVisibility(View.GONE);
        } else {
            buttonHighScores.setVisibility(View.VISIBLE);
        }
        buttonHighScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentScores = new Intent(MainActivity.this, ScoresActivity.class);
                final int result = 1;
                intentScores.putExtra("intShortestWord", intShortestWord);
                intentScores.putExtra("intLongestWord", intLongestWord);
                intentScores.putExtra("intLives", intLives);
                startActivityForResult(intentScores, result);
            }
        });

        assert buttonSettings != null;
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentSettings = new Intent(MainActivity.this, SettingsActivity.class);
                final int result = 1;
                intentSettings.putExtra("intShortestWord", intShortestWord);
                intentSettings.putExtra("intLongestWord", intLongestWord);
                intentSettings.putExtra("intLives", intLives);
                intentSettings.putExtra("strWordsLists", strWordsLists);
                intentSettings.putExtra("strCustom", strCustom);
                startActivityForResult(intentSettings, result);
            }
        });
    }

    @Override
    protected void onPause(){
        if (mAdView != null){
            mAdView.pause();
        }
        if (cdTimer != null){
            cdTimer.cancel();
        }
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (mAdView != null){
            mAdView.resume();
        }
        StartDemo();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        SharedPreferences settings = getSharedPreferences(PREFERENCE_SETTINGS, MODE_PRIVATE);
        intLongestWord = settings.getInt("settingLongestWord", 28) + 2;
        intShortestWord = settings.getInt("settingShortestWord", 1) + 1;
        intLives = settings.getInt("settingLives", 5) + 5;
        strWordsLists = settings.getString("settingWordsLists", getResources().getString(R.string.default_list));
        strCustom = settings.getString("settingCustom", null);
        booFirstPlay = settings.getBoolean("settingFirstPlay", true);
        long intBestTime1 = settings.getLong("settingBestTime1", 9223372036854775807L);
        Button buttonHighScores = (Button) findViewById(R.id.buttonHighScores);
        assert buttonHighScores != null;
        if (intBestTime1 == 9223372036854775807L) {
            buttonHighScores.setVisibility(View.GONE);
        } else {
            buttonHighScores.setVisibility(View.VISIBLE);
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
    protected void onStart(){
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop(){
        super.onStop();
        // signOutClicked();
        mGoogleApiClient.disconnect();
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

    // FOR HANGMAN TITLE BAR
    public void StartDemo(){

        // Reset variables
        strLetters = getResources().getString(R.string.alphabet);
        strTitle = getResources().getString(R.string.APP_NAME);
        booTitleIncomplete = false;
        intLivesRem = 15;
        intCount = 0;

        int intLetterCount = 0;
        for (int id : LETTER_IDS){
            final ImageView imageLettersUsage = (ImageView) findViewById(id);
            if (imageLettersUsage != null) {
                // Reset the array for new game
                imageLettersUsage.setImageResource(R.drawable.us);
                imageLettersUsage.setVisibility(View.VISIBLE);
                // Make all spaces appear empty (invisible)
                if (!(intLetterCount >= strTitle.length())) {
                    if (strTitle.charAt(intLetterCount) == ' ') imageLettersUsage.setVisibility(View.INVISIBLE);
                }
                // If past word length then make it gone
                if (intLetterCount >= strTitle.length()){
                    imageLettersUsage.setVisibility(View.GONE);
                }
            }
            intLetterCount ++;
        }
        cdTimer = new CountDownTimer(30000, 1000){

            @Override
            public void onTick(long millisUntilFinished) {

                if (!(intLivesRem == 0)) {
                    booTitleIncomplete = false;
                    char chrLetters[] = strTitle.toCharArray();
                    for (char c : chrLetters) {
                        String strC = String.valueOf(c);
                        if (strLetters.contains(strC)) {
                            booTitleIncomplete = true;
                        }
                    }
                    if (booTitleIncomplete) {
                        GenerateLetter();
                        UseTheLetter();
                    }
                } else {
                    intCount ++;
                }
                if (intCount >= 2){
                    //DISPLAY THE FULL TITLE
                    strLetters = getResources().getString(R.string.alphabet);
                    char chrAlphabet[] = strLetters.toCharArray();
                    for (char c : chrAlphabet) {
                        for (int position = 0; position < strTitle.length(); position++) {
                            if (strTitle.charAt(position) == c) {
                                //Place the guess in the correct location(s)
                                int intLetterCount = 0;
                                // Set the found place to the correct image
                                for (int id : LETTER_IDS) {
                                    final ImageView imageLettersUsage = (ImageView) findViewById(id);
                                    if (imageLettersUsage != null) {
                                        if (intLetterCount == position) {
                                            if (c == 'A') imageLettersUsage.setImageResource(R.drawable.a);
                                            if (c == 'B') imageLettersUsage.setImageResource(R.drawable.b);
                                            if (c == 'C') imageLettersUsage.setImageResource(R.drawable.c);
                                            if (c == 'D') imageLettersUsage.setImageResource(R.drawable.d);
                                            if (c == 'E') imageLettersUsage.setImageResource(R.drawable.e);
                                            if (c == 'F') imageLettersUsage.setImageResource(R.drawable.f);
                                            if (c == 'G') imageLettersUsage.setImageResource(R.drawable.g);
                                            if (c == 'H') imageLettersUsage.setImageResource(R.drawable.h);
                                            if (c == 'I') imageLettersUsage.setImageResource(R.drawable.i);
                                            if (c == 'J') imageLettersUsage.setImageResource(R.drawable.j);
                                            if (c == 'K') imageLettersUsage.setImageResource(R.drawable.k);
                                            if (c == 'L') imageLettersUsage.setImageResource(R.drawable.l);
                                            if (c == 'M') imageLettersUsage.setImageResource(R.drawable.m);
                                            if (c == 'N') imageLettersUsage.setImageResource(R.drawable.n);
                                            if (c == 'O') imageLettersUsage.setImageResource(R.drawable.o);
                                            if (c == 'P') imageLettersUsage.setImageResource(R.drawable.p);
                                            if (c == 'Q') imageLettersUsage.setImageResource(R.drawable.q);
                                            if (c == 'R') imageLettersUsage.setImageResource(R.drawable.r);
                                            if (c == 'S') imageLettersUsage.setImageResource(R.drawable.s);
                                            if (c == 'T') imageLettersUsage.setImageResource(R.drawable.t);
                                            if (c == 'U') imageLettersUsage.setImageResource(R.drawable.u);
                                            if (c == 'V') imageLettersUsage.setImageResource(R.drawable.v);
                                            if (c == 'W') imageLettersUsage.setImageResource(R.drawable.w);
                                            if (c == 'X') imageLettersUsage.setImageResource(R.drawable.x);
                                            if (c == 'Y') imageLettersUsage.setImageResource(R.drawable.y);
                                            if (c == 'Z') imageLettersUsage.setImageResource(R.drawable.z);
                                        }
                                        intLetterCount++;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFinish() {
                // Timer is finished, reset the title bar and restart animation
                StartDemo();
                intDemoPlay ++;
                // If the demo has been watched 5 times in a row
                if (intDemoPlay == 5){
                    if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                        // Unlock the secret achievement
                        Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_the_secret));
                    }
                }
            }
        }.start();
    }

    public void GenerateLetter(){
        if (!(strLetters.equals(""))) {
            intRand = (int) (Math.random() * strLetters.length());
            chrRandomGuess = strLetters.charAt(intRand);
            strRandomGuess = String.valueOf(chrRandomGuess);
            strLetters = strLetters.replace(strRandomGuess, "");
        }
    }

    public void UseTheLetter(){

        booGuessInTitle = (strTitle.indexOf(chrRandomGuess)) != -1;
        if (!booGuessInTitle) {
            intLivesRem --;
        } else {
            // The guess is in the title so add it to display
            for (int position = 0; position < strTitle.length(); position++) {
                if (strTitle.charAt(position) == chrRandomGuess) {
                    //Place the guess in the correct location(s)
                    int intLetterCount = 0;
                    // Set the found place to the correct image
                    for (int id : LETTER_IDS) {
                        final ImageView imageLettersUsage = (ImageView) findViewById(id);
                        if (imageLettersUsage != null) {
                            if (intLetterCount == position) {
                                if (strRandomGuess.equals("A"))
                                    imageLettersUsage.setImageResource(R.drawable.a);
                                if (strRandomGuess.equals("B"))
                                    imageLettersUsage.setImageResource(R.drawable.b);
                                if (strRandomGuess.equals("C"))
                                    imageLettersUsage.setImageResource(R.drawable.c);
                                if (strRandomGuess.equals("D"))
                                    imageLettersUsage.setImageResource(R.drawable.d);
                                if (strRandomGuess.equals("E"))
                                    imageLettersUsage.setImageResource(R.drawable.e);
                                if (strRandomGuess.equals("F"))
                                    imageLettersUsage.setImageResource(R.drawable.f);
                                if (strRandomGuess.equals("G"))
                                    imageLettersUsage.setImageResource(R.drawable.g);
                                if (strRandomGuess.equals("H"))
                                    imageLettersUsage.setImageResource(R.drawable.h);
                                if (strRandomGuess.equals("I"))
                                    imageLettersUsage.setImageResource(R.drawable.i);
                                if (strRandomGuess.equals("J"))
                                    imageLettersUsage.setImageResource(R.drawable.j);
                                if (strRandomGuess.equals("K"))
                                    imageLettersUsage.setImageResource(R.drawable.k);
                                if (strRandomGuess.equals("L"))
                                    imageLettersUsage.setImageResource(R.drawable.l);
                                if (strRandomGuess.equals("M"))
                                    imageLettersUsage.setImageResource(R.drawable.m);
                                if (strRandomGuess.equals("N"))
                                    imageLettersUsage.setImageResource(R.drawable.n);
                                if (strRandomGuess.equals("O"))
                                    imageLettersUsage.setImageResource(R.drawable.o);
                                if (strRandomGuess.equals("P"))
                                    imageLettersUsage.setImageResource(R.drawable.p);
                                if (strRandomGuess.equals("Q"))
                                    imageLettersUsage.setImageResource(R.drawable.q);
                                if (strRandomGuess.equals("R"))
                                    imageLettersUsage.setImageResource(R.drawable.r);
                                if (strRandomGuess.equals("S"))
                                    imageLettersUsage.setImageResource(R.drawable.s);
                                if (strRandomGuess.equals("T"))
                                    imageLettersUsage.setImageResource(R.drawable.t);
                                if (strRandomGuess.equals("U"))
                                    imageLettersUsage.setImageResource(R.drawable.u);
                                if (strRandomGuess.equals("V"))
                                    imageLettersUsage.setImageResource(R.drawable.v);
                                if (strRandomGuess.equals("W"))
                                    imageLettersUsage.setImageResource(R.drawable.w);
                                if (strRandomGuess.equals("X"))
                                    imageLettersUsage.setImageResource(R.drawable.x);
                                if (strRandomGuess.equals("Y"))
                                    imageLettersUsage.setImageResource(R.drawable.y);
                                if (strRandomGuess.equals("Z"))
                                    imageLettersUsage.setImageResource(R.drawable.z);
                            }
                            intLetterCount++;
                        }
                    }
                }
            }
        }
    }

    // BOTH FOR RATING THE APP POP UP
    public void RateMe(String strMessage, String strPositive, String strNeutral, String strNegative){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.fragment_dialog);

        // Set everything View.GONE that we don't need
        EditText editTextPlayerName = (EditText) dialog.findViewById(R.id.editTextPlayerName);
        editTextPlayerName.setVisibility(View.GONE);
        ImageView imageResult = (ImageView) dialog.findViewById(R.id.imageResult);
        imageResult.setVisibility(View.GONE);
        TextView textViewFragTime = (TextView) dialog.findViewById(R.id.textViewFragTime);
        textViewFragTime.setVisibility(View.GONE);

        TextView text = (TextView) dialog.findViewById(R.id.textViewCaption);
        text.setText(strMessage);

        Button buttonPositive = (Button) dialog.findViewById(R.id.buttonPositive);
        buttonPositive.setText(strPositive);
        buttonPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User pressed "Yes"
                // Take to the Play Store and load the app on there
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //Try Google play
                //TODO Ensure Google Play has the right package for my app
                intent.setData(Uri.parse(getResources().getString(R.string.google_play)));
                if (!MyStartActivity(intent)) {
                    //Market (Google play) app seems not installed, let's try to open a web browser
                    intent.setData(Uri.parse(getResources().getString(R.string.google_play_website)));
                    if (!MyStartActivity(intent)) {
                        //Well if this also fails, we have run out of options, inform the user.
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.failed_to_rate), Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.dismiss();
            }
        });

        Button buttonNeutral = (Button) dialog.findViewById(R.id.buttonNeutral);
        buttonNeutral.setVisibility(View.VISIBLE);
        buttonNeutral.setText(strNeutral);
        buttonNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User pressed "Not yet"
                dialog.dismiss();
            }
        });

        Button buttonNegative = (Button) dialog.findViewById(R.id.buttonNegative);
        buttonNegative.setText(strNegative);
        buttonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User pressed "NO"
                booNoRate = true;
                SharedPreferences settings = getSharedPreferences(PREFERENCE_SETTINGS, MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("settingNoRate", true);
                editor.apply();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private boolean MyStartActivity(Intent aIntent) {
        try
        {
            startActivity(aIntent);
            return true;
        }
        catch (ActivityNotFoundException e)
        {
            return false;
        }
    }
}