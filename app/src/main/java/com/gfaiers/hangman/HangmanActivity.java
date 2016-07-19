package com.gfaiers.hangman;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//public class HangmanActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
public class HangmanActivity extends AppCompatActivity {

    // Declare the imageButton ID's in the keyboard layout
    private static final int[] BUTTON_IDS = {
            R.id.imageButtonA, R.id.imageButtonB, R.id.imageButtonC, R.id.imageButtonD, R.id.imageButtonE,
            R.id.imageButtonF, R.id.imageButtonG, R.id.imageButtonH, R.id.imageButtonI, R.id.imageButtonJ,
            R.id.imageButtonK, R.id.imageButtonL, R.id.imageButtonM, R.id.imageButtonN, R.id.imageButtonO,
            R.id.imageButtonP, R.id.imageButtonQ, R.id.imageButtonR, R.id.imageButtonS, R.id.imageButtonT,
            R.id.imageButtonU, R.id.imageButtonV, R.id.imageButtonW, R.id.imageButtonX, R.id.imageButtonY, R.id.imageButtonZ
    };

    // Declare the imageView ID's in the correct guesses layout
    private static final int[] LETTER_IDS = {
            R.id.imageView0, R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5,
            R.id.imageView6, R.id.imageView7, R.id.imageView8, R.id.imageView9, R.id.imageView10, R.id.imageView11,
            R.id.imageView12, R.id.imageView13, R.id.imageView14, R.id.imageView15, R.id.imageView16, R.id.imageView17,
            R.id.imageView18, R.id.imageView19, R.id.imageView20, R.id.imageView21, R.id.imageView22, R.id.imageView23,
            R.id.imageView24, R.id.imageView25, R.id.imageView26, R.id.imageView27, R.id.imageView28, R.id.imageView29
    };

    // Declare the <3 and </3 ID's in the lives layout
    private static final int[] LIFE_IDS = {
            R.id.imageLife0, R.id.imageLife1, R.id.imageLife2, R.id.imageLife3, R.id.imageLife4,
            R.id.imageLife5, R.id.imageLife6, R.id.imageLife7, R.id.imageLife8, R.id.imageLife9,
            R.id.imageLife10, R.id.imageLife11, R.id.imageLife12, R.id.imageLife13, R.id.imageLife14,
    };

    // Declare variables to use within the application
    private static final String TAG = "Hangman";
    private static final int RC_SIGN_IN = 9001; // Request code used to invoke sign in user interactions.
    private GoogleApiClient mGoogleApiClient; // Client user to interact with Google APIs.
    private boolean mResolvingConnectionFailure = false; // Are we currently resolving a connection failure?
    private boolean mSignInClicked = false; // Has the user clicked the sign in  button?
    private boolean mAutoStartSignInFlow = true; // Set to true to automatically start the sign in flow when the Activity starts.  If false user has to click to sign in
    final static String PREFERENCE_SETTINGS = "settingWordLength";
    static boolean booGuessInWord, booFirstPlay = false, booOK = false, booCancelHandler = false, booNewGame = false, booHasBeenPaused = false;
    public static int intLivesRemaining, intLives;
    static char chrGuessed;
    static int intLettersRemaining, intLivesLost, intShortestWord, intLongestWord, intWinStreak, intLossStreak;
    static long  intTime, intBestTime1, intBestTime2, intBestTime3, intBestTime4, intBestTime5, intBestTime6, intBestTime7, intBestTime8, intBestTime9, intBestTime10;
    static String strBestTime1, strBestTime2, strBestTime3, strBestTime4, strBestTime5, strBestTime6,strBestTime7, strBestTime8, strBestTime9, strBestTime10;
    static String strWord, strGuessed, strWordsLists, strCustom;
    static String[] strCorrectLetters = null;
    static List<String> aryStars = new ArrayList<>();
    static List<String> strAllGuessed = new ArrayList<>();
    static final Handler handler = new Handler();
    Timer timer;
    MyTimerTask myTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangman);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // Get settings from internal storage for use within the activity
        SharedPreferences settings = getSharedPreferences(PREFERENCE_SETTINGS, MODE_PRIVATE);
        intLongestWord = settings.getInt("settingLongestWord", 28) + 2;
        intShortestWord = settings.getInt("settingShortestWord", 1) + 1;
        intLives = settings.getInt("settingLives", 5) + 5;
        booFirstPlay = settings.getBoolean("settingFirstPlay", true);
        strWordsLists = settings.getString("settingWordsLists", getResources().getString(R.string.default_list));
        strCustom = settings.getString("settingCustom", getResources().getString(R.string.default_custom));
        intWinStreak = settings.getInt("settingWinStreak", 0);
        intLossStreak = settings.getInt("settingLossStreak", 0);
        newGame();

        // Make the hearts appear for how many lives the player has, and set their image to the heart icon
        int intLivesCount = 0;
        for (int id: LIFE_IDS) {
            intLivesCount ++;
            final ImageView imageLivesUsage = (ImageView)findViewById(id);
            if (imageLivesUsage != null) {
                imageLivesUsage.setImageResource(R.drawable.lifeimage);
                if (intLivesCount > intLives) {
                    imageLivesUsage.setVisibility(View.GONE);
                }
            }
        }
        for (int id: BUTTON_IDS) {
            final ImageButton buttonLettersUsage = (ImageButton)findViewById(id);
            if (buttonLettersUsage != null) {
                buttonLettersUsage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (buttonLettersUsage.getId()){
                            case R.id.imageButtonQ:
                                strGuessed = "Q";
                                break;
                            case R.id.imageButtonW:
                                strGuessed = "W";
                                break;
                            case R.id.imageButtonE:
                                strGuessed = "E";
                                break;
                            case R.id.imageButtonR:
                                strGuessed = "R";
                                break;
                            case R.id.imageButtonT:
                                strGuessed = "T";
                                break;
                            case R.id.imageButtonY:
                                strGuessed = "Y";
                                break;
                            case R.id.imageButtonU:
                                strGuessed = "U";
                                break;
                            case R.id.imageButtonI:
                                strGuessed = "I";
                                break;
                            case R.id.imageButtonO:
                                strGuessed = "O";
                                break;
                            case R.id.imageButtonP:
                                strGuessed = "P";
                                break;
                            case R.id.imageButtonA:
                                strGuessed = "A";
                                break;
                            case R.id.imageButtonS:
                                strGuessed = "S";
                                break;
                            case R.id.imageButtonD:
                                strGuessed = "D";
                                break;
                            case R.id.imageButtonF:
                                strGuessed = "F";
                                break;
                            case R.id.imageButtonG:
                                strGuessed = "G";
                                break;
                            case R.id.imageButtonH:
                                strGuessed = "H";
                                break;
                            case R.id.imageButtonJ:
                                strGuessed = "J";
                                break;
                            case R.id.imageButtonK:
                                strGuessed = "K";
                                break;
                            case R.id.imageButtonL:
                                strGuessed = "L";
                                break;
                            case R.id.imageButtonZ:
                                strGuessed = "Z";
                                break;
                            case R.id.imageButtonX:
                                strGuessed = "X";
                                break;
                            case R.id.imageButtonC:
                                strGuessed = "C";
                                break;
                            case R.id.imageButtonV:
                                strGuessed = "V";
                                break;
                            case R.id.imageButtonB:
                                strGuessed = "B";
                                break;
                            case R.id.imageButtonN:
                                strGuessed = "N";
                                break;
                            case R.id.imageButtonM:
                                strGuessed = "M";
                                break;
                            default:
                                break;
                        }
                        btnGuessClicked();
                        buttonLettersUsage.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }
    }

    @Override
    protected void onPause(){

        if (timer != null){
            timer.cancel();
        }
        booHasBeenPaused = true;
        super.onPause();

    }

    @Override
    protected void onStop(){
        // This controls the images and makes sure they're not using memory once the activity is closed
        booHasBeenPaused = false;
        super.onStop();

        SharedPreferences settings = getSharedPreferences(PREFERENCE_SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("settingWinStreak", intWinStreak);
        editor.putInt("settingLossStreak", intLossStreak);
        editor.apply();

        // The hearts
        for (int id: LIFE_IDS) {
            final ImageView imageLivesUsage = (ImageView)findViewById(id);
            if (imageLivesUsage != null) {
                Drawable d = imageLivesUsage.getDrawable();
                if (d != null) d.setCallback(null);
                imageLivesUsage.setImageDrawable(null);
            }
        }
        // The word
        for (int id : LETTER_IDS) {
            final ImageView imageLettersUsage = (ImageView) findViewById(id);
            if (imageLettersUsage != null) {
                Drawable d = imageLettersUsage.getDrawable();
                if (d != null) d.setCallback(null);
                imageLettersUsage.setImageDrawable(null);
            }
        }
        // The buttons
        for (int id: BUTTON_IDS) {
            final ImageButton buttonLettersUsage = (ImageButton)findViewById(id);
            if (buttonLettersUsage != null) {
                Drawable d = buttonLettersUsage.getDrawable();
                if (d != null) d.setCallback(null);
                buttonLettersUsage.setImageDrawable(null);
            }
        }

    }

    @Override
    protected void onResume(){

        if (booHasBeenPaused){
            timer = new Timer();
            myTimerTask = new MyTimerTask();
            timer.schedule(myTimerTask, 0, 100);
            booHasBeenPaused = false;
        }
        super.onResume();

        // The hearts
        int intCount = 0;
        for (int id: LIFE_IDS) {
            final ImageView imageLivesUsage = (ImageView)findViewById(id);
            if (imageLivesUsage != null) {
                if (intCount < intLivesRemaining) {
                    imageLivesUsage.setImageResource(R.drawable.lifeimage);
                } else {
                    imageLivesUsage.setImageResource(R.drawable.lifelostimage);
                }
            }
            intCount ++;
        }
        // The word
        intCount = 0;
        for (int id : LETTER_IDS) {
            final ImageView imageLettersUsage = (ImageView) findViewById(id);
            if (imageLettersUsage != null) {
                // Set the image to an underscore
                imageLettersUsage.setImageResource(R.drawable.us);
                // If the letter is present and in the position being set, set it to that image
                if (!(intCount >= strWord.length())) {
                    if (strCorrectLetters[intCount].equals("Q")) imageLettersUsage.setImageResource(R.drawable.qxml);
                    if (strCorrectLetters[intCount].equals("W")) imageLettersUsage.setImageResource(R.drawable.wxml);
                    if (strCorrectLetters[intCount].equals("E")) imageLettersUsage.setImageResource(R.drawable.exml);
                    if (strCorrectLetters[intCount].equals("R")) imageLettersUsage.setImageResource(R.drawable.rxml);
                    if (strCorrectLetters[intCount].equals("T")) imageLettersUsage.setImageResource(R.drawable.txml);
                    if (strCorrectLetters[intCount].equals("Y")) imageLettersUsage.setImageResource(R.drawable.yxml);
                    if (strCorrectLetters[intCount].equals("U")) imageLettersUsage.setImageResource(R.drawable.uxml);
                    if (strCorrectLetters[intCount].equals("I")) imageLettersUsage.setImageResource(R.drawable.ixml);
                    if (strCorrectLetters[intCount].equals("O")) imageLettersUsage.setImageResource(R.drawable.oxml);
                    if (strCorrectLetters[intCount].equals("P")) imageLettersUsage.setImageResource(R.drawable.pxml);
                    if (strCorrectLetters[intCount].equals("A")) imageLettersUsage.setImageResource(R.drawable.axml);
                    if (strCorrectLetters[intCount].equals("S")) imageLettersUsage.setImageResource(R.drawable.sxml);
                    if (strCorrectLetters[intCount].equals("D")) imageLettersUsage.setImageResource(R.drawable.dxml);
                    if (strCorrectLetters[intCount].equals("F")) imageLettersUsage.setImageResource(R.drawable.fxml);
                    if (strCorrectLetters[intCount].equals("G")) imageLettersUsage.setImageResource(R.drawable.gxml);
                    if (strCorrectLetters[intCount].equals("H")) imageLettersUsage.setImageResource(R.drawable.hxml);
                    if (strCorrectLetters[intCount].equals("J")) imageLettersUsage.setImageResource(R.drawable.jxml);
                    if (strCorrectLetters[intCount].equals("K")) imageLettersUsage.setImageResource(R.drawable.kxml);
                    if (strCorrectLetters[intCount].equals("L")) imageLettersUsage.setImageResource(R.drawable.lxml);
                    if (strCorrectLetters[intCount].equals("Z")) imageLettersUsage.setImageResource(R.drawable.zxml);
                    if (strCorrectLetters[intCount].equals("X")) imageLettersUsage.setImageResource(R.drawable.xxml);
                    if (strCorrectLetters[intCount].equals("C")) imageLettersUsage.setImageResource(R.drawable.cxml);
                    if (strCorrectLetters[intCount].equals("V")) imageLettersUsage.setImageResource(R.drawable.vxml);
                    if (strCorrectLetters[intCount].equals("B")) imageLettersUsage.setImageResource(R.drawable.bxml);
                    if (strCorrectLetters[intCount].equals("N")) imageLettersUsage.setImageResource(R.drawable.nxml);
                    if (strCorrectLetters[intCount].equals("M")) imageLettersUsage.setImageResource(R.drawable.mxml);
                }
            }
            intCount ++;
        }
        // The buttons
        for (int id: BUTTON_IDS) {
            final ImageButton buttonLettersUsage = (ImageButton) findViewById(id);
            if (buttonLettersUsage != null) {
                if (id == R.id.imageButtonQ) buttonLettersUsage.setImageResource(R.drawable.qxml);
                if (id == R.id.imageButtonW) buttonLettersUsage.setImageResource(R.drawable.wxml);
                if (id == R.id.imageButtonE) buttonLettersUsage.setImageResource(R.drawable.exml);
                if (id == R.id.imageButtonR) buttonLettersUsage.setImageResource(R.drawable.rxml);
                if (id == R.id.imageButtonT) buttonLettersUsage.setImageResource(R.drawable.txml);
                if (id == R.id.imageButtonY) buttonLettersUsage.setImageResource(R.drawable.yxml);
                if (id == R.id.imageButtonU) buttonLettersUsage.setImageResource(R.drawable.uxml);
                if (id == R.id.imageButtonI) buttonLettersUsage.setImageResource(R.drawable.ixml);
                if (id == R.id.imageButtonO) buttonLettersUsage.setImageResource(R.drawable.oxml);
                if (id == R.id.imageButtonP) buttonLettersUsage.setImageResource(R.drawable.pxml);
                if (id == R.id.imageButtonA) buttonLettersUsage.setImageResource(R.drawable.axml);
                if (id == R.id.imageButtonS) buttonLettersUsage.setImageResource(R.drawable.sxml);
                if (id == R.id.imageButtonD) buttonLettersUsage.setImageResource(R.drawable.dxml);
                if (id == R.id.imageButtonF) buttonLettersUsage.setImageResource(R.drawable.fxml);
                if (id == R.id.imageButtonG) buttonLettersUsage.setImageResource(R.drawable.gxml);
                if (id == R.id.imageButtonH) buttonLettersUsage.setImageResource(R.drawable.hxml);
                if (id == R.id.imageButtonJ) buttonLettersUsage.setImageResource(R.drawable.jxml);
                if (id == R.id.imageButtonK) buttonLettersUsage.setImageResource(R.drawable.kxml);
                if (id == R.id.imageButtonL) buttonLettersUsage.setImageResource(R.drawable.lxml);
                if (id == R.id.imageButtonZ) buttonLettersUsage.setImageResource(R.drawable.zxml);
                if (id == R.id.imageButtonX) buttonLettersUsage.setImageResource(R.drawable.xxml);
                if (id == R.id.imageButtonC) buttonLettersUsage.setImageResource(R.drawable.cxml);
                if (id == R.id.imageButtonV) buttonLettersUsage.setImageResource(R.drawable.vxml);
                if (id == R.id.imageButtonB) buttonLettersUsage.setImageResource(R.drawable.bxml);
                if (id == R.id.imageButtonN) buttonLettersUsage.setImageResource(R.drawable.nxml);
                if (id == R.id.imageButtonM) buttonLettersUsage.setImageResource(R.drawable.mxml);
            }
        }

    }

    @Override
    public void onBackPressed(){
        // This happens when back is pressed before onPause / onStop is called
        if (timer != null){
            timer.cancel();
        }
        booOK = false;
        for (int id: BUTTON_IDS) {
            final ImageButton buttonLettersUsage = (ImageButton) findViewById(id);
            if (buttonLettersUsage != null) {
                if (buttonLettersUsage.getVisibility() == View.INVISIBLE){
                    // Game is in progress
                    booOK = true;
                    break;
                }
            }
        }
        // This is only asked if the game is in play, if it hasn't started, then don't ask
        if (booOK) {
            backMessage(getResources().getString(R.string.game_lost), getResources().getString(R.string.ok), getResources().getString(R.string.cancel), "");
        } else {
            booCancelHandler = true;
            super.onBackPressed();
        }
    }

    @SuppressWarnings("unchecked")
    public String getTheWord() {

        TextView textViewCategory = (TextView) findViewById(R.id.textViewCategory);
        String strTemp;
        if (strWordsLists.equals(getResources().getString(R.string.custom_list))){
            strTemp = getResources().getString(R.string.category) + " " + strCustom;
            assert textViewCategory != null;
            textViewCategory.setText(strTemp);
            ArrayList<String> lines = new ArrayList<>();
            try {
                FileInputStream fis = openFileInput(strCustom);
                ObjectInputStream in = new ObjectInputStream(fis);
                lines = (ArrayList<String>) in.readObject();
                in.close();
            } catch (FileNotFoundException e) {
                alertMessage(this, getResources().getString(R.string.file_not_found), getResources().getString(R.string.ok));
            } catch (StreamCorruptedException e) {
                alertMessage(this, getResources().getString(R.string.stream_corrupted), getResources().getString(R.string.ok));
            } catch (IOException e) {
                alertMessage(this, getResources().getString(R.string.ioexception), getResources().getString(R.string.ok));
            } catch (ClassNotFoundException e) {
                alertMessage(this, getResources().getString(R.string.class_not_found), getResources().getString(R.string.ok));
            }
            // Put the ArrayList from the file into the new strWords array, then clear what has been read
            String[] strWords = lines.toArray(new String[lines.size()]);
            lines.clear();
            // Refill the ArrayList with only values that are within the correct length boundaries
            for (String s : strWords){
                if ((s.length() >= intShortestWord) && (s.length() <= intLongestWord))
                    lines.add(s);
            }
            int intLen;
            String[] strWords2 = lines.toArray(new String[lines.size()]);
            intLen = strWords2.length;
            int intRand = (int) (Math.random() * intLen);
            if (strWords2[intRand].charAt(strWords2[intRand].length()-1) == ' '){
                strWords2[intRand] = strWords2[intRand].substring(0, strWords2[intRand].length()-1);
            }
            return strWords2[intRand];
        } else {
            // Read from a file in ASSETS (For the default files)
            // This method reads the file line by line, checking each line if it's long enough / short enough to be used
            // Then it picks a random word from the remaining stack
            strTemp = getResources().getString(R.string.category) + " " + strWordsLists;
            assert textViewCategory != null;
            textViewCategory.setText(strTemp);
            List<String> lines = new ArrayList<>();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(getAssets().open(strWordsLists)));

                // do reading, usually loop until end of file reading
                String mLine;
                while ((mLine = reader.readLine()) != null) {
                    //  First time playing
                    if (booFirstPlay) {
                        // Set the variables for shortest and longest words
                        if (mLine.length() <= intShortestWord) intShortestWord = mLine.length();
                        if (mLine.length() >= intLongestWord) intLongestWord = mLine.length();
                        // Add all words as it's the first time game is played
                        lines.add(mLine);
                    } else {
                        // It's not the first time the game is played, add only the words that are less than longest word and
                        // longer than shortest word
                        if ((mLine.length() >= intShortestWord) && (mLine.length() <= intLongestWord))
                            lines.add(mLine);
                    }
                }
                int intLen;
                String[] strWords = lines.toArray(new String[lines.size()]);
                intLen = strWords.length;
                int intRand = (int) (Math.random() * intLen);
                if (strWords[intRand].charAt(strWords[intRand].length()-1) == ' '){
                    strWords[intRand] = strWords[intRand].substring(0, strWords[intRand].length()-1);
                }
                return strWords[intRand];

            } catch (IOException e) {
                // Log the exception
                alertMessage(this,getResources().getString(R.string.file_not_found), getResources().getString(R.string.ok));
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        // Log the exception
                        alertMessage(this, getResources().getString(R.string.unable_to_close), getResources().getString(R.string.ok));
                    }
                }
            }
        }
        return null;

    }

    public void newGame() {

        // Pick a new word to play with
        strWord = getTheWord().toUpperCase();

        // Reset all the variables, ready for a new game
        ResetVariables();
        booCancelHandler = false;

        TextView textViewTime = (TextView) findViewById(R.id.textViewTime);
        String strTemp = getResources().getString(R.string.time) + ": " + intTime;
        assert textViewTime != null;
        textViewTime.setText(strTemp);
        // Display only the correct number of ImageViews for the length of the word
        int intLetterCount = 0;
        for (int id : LETTER_IDS){
            final ImageView imageLettersUsage = (ImageView) findViewById(id);
            if (imageLettersUsage != null) {
                // Reset the array for new game
                imageLettersUsage.setImageResource(R.drawable.us);
                imageLettersUsage.setVisibility(View.VISIBLE);
                // Make all spaces appear empty (invisible)
                if (!(intLetterCount >= strWord.length())) {
                    if (strWord.charAt(intLetterCount) == ' ') imageLettersUsage.setVisibility(View.INVISIBLE);
                }
                // If past word length then make it gone
                if (intLetterCount >= strWord.length()){
                    imageLettersUsage.setVisibility(View.GONE);
                }
            }
            intLetterCount ++;
        }

        // Reset the labels displaying the **'s and the incorrect guesses
        for (int count = 0; count < strWord.length(); count++) {
            aryStars.add("*");
        }
        strCorrectLetters = aryStars.toArray(new String[aryStars.size()]);

        // Make spaces appear as a gap in the array too
        for (int count = 0; count < strWord.length(); count ++){
            if (strWord.charAt(count) == ' '){
                strCorrectLetters[count] = " ";
            }
        }

        // Make the array of buttons visible ready for a new game
        int intLivesCount = 0;
        for (int id: LIFE_IDS) {
            intLivesCount ++;
            final ImageView imageLivesUsage = (ImageView)findViewById(id);
            if (imageLivesUsage != null) {
                imageLivesUsage.setImageResource(R.drawable.lifeimage);
                if (intLivesCount > intLives) {
                    imageLivesUsage.setVisibility(View.GONE);
                }
            }
        }

        for (int id: BUTTON_IDS) {
            final ImageButton buttonLettersUsage = (ImageButton) findViewById(id);
            if (buttonLettersUsage != null) {
                buttonLettersUsage.setVisibility(View.VISIBLE);
            }
        }

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
        // Toast.makeText(HangmanActivity.this, strWord + " " + intLivesRemaining, Toast.LENGTH_SHORT).show();
    }

    public void btnGuessClicked() {

        if (booNewGame) {
            // Reset the timer
            if (timer != null){
                timer.cancel();
            }
            timer = new Timer();
            myTimerTask = new MyTimerTask();
            timer.schedule(myTimerTask, 0, 100);
            booNewGame = false;
        }
        // Set the guess
        chrGuessed = Character.toUpperCase(strGuessed.charAt(0));
        booGuessInWord = (strWord.indexOf(chrGuessed)) != -1;
        strAllGuessed.add(strGuessed);
        if (!booGuessInWord) {
            // INCORRECT GUESS
            // Remove a life and add to incorrect list
            LifeLost();
        } else {
            // CORRECT GUESS
            boolean booNewGuess = true;
            for (int position = 0; position < strWord.length(); position++) {
                if (strCorrectLetters[position].equals(strGuessed)) {
                    booNewGuess = false;
                }
            }
            if (booNewGuess) {
                // Find the location of the guess
                for (int position = 0; position < strWord.length(); position++) {
                    if (strWord.charAt(position) == chrGuessed) {
                        //Place the guess in the correct location(s)

                        strCorrectLetters[position] = strGuessed;
                        intLettersRemaining--;

                        int intLetterCount = 0;
                        // Set the found place to the correct image
                        for (int id : LETTER_IDS){
                            final ImageView imageLettersUsage = (ImageView) findViewById(id);
                            if (imageLettersUsage != null) {
                                if (intLetterCount == position){
                                    if (strGuessed.equals("A")) imageLettersUsage.setImageResource(R.drawable.a);
                                    if (strGuessed.equals("B")) imageLettersUsage.setImageResource(R.drawable.b);
                                    if (strGuessed.equals("C")) imageLettersUsage.setImageResource(R.drawable.c);
                                    if (strGuessed.equals("D")) imageLettersUsage.setImageResource(R.drawable.d);
                                    if (strGuessed.equals("E")) imageLettersUsage.setImageResource(R.drawable.e);
                                    if (strGuessed.equals("F")) imageLettersUsage.setImageResource(R.drawable.f);
                                    if (strGuessed.equals("G")) imageLettersUsage.setImageResource(R.drawable.g);
                                    if (strGuessed.equals("H")) imageLettersUsage.setImageResource(R.drawable.h);
                                    if (strGuessed.equals("I")) imageLettersUsage.setImageResource(R.drawable.i);
                                    if (strGuessed.equals("J")) imageLettersUsage.setImageResource(R.drawable.j);
                                    if (strGuessed.equals("K")) imageLettersUsage.setImageResource(R.drawable.k);
                                    if (strGuessed.equals("L")) imageLettersUsage.setImageResource(R.drawable.l);
                                    if (strGuessed.equals("M")) imageLettersUsage.setImageResource(R.drawable.m);
                                    if (strGuessed.equals("N")) imageLettersUsage.setImageResource(R.drawable.n);
                                    if (strGuessed.equals("O")) imageLettersUsage.setImageResource(R.drawable.o);
                                    if (strGuessed.equals("P")) imageLettersUsage.setImageResource(R.drawable.p);
                                    if (strGuessed.equals("Q")) imageLettersUsage.setImageResource(R.drawable.q);
                                    if (strGuessed.equals("R")) imageLettersUsage.setImageResource(R.drawable.r);
                                    if (strGuessed.equals("S")) imageLettersUsage.setImageResource(R.drawable.s);
                                    if (strGuessed.equals("T")) imageLettersUsage.setImageResource(R.drawable.t);
                                    if (strGuessed.equals("U")) imageLettersUsage.setImageResource(R.drawable.u);
                                    if (strGuessed.equals("V")) imageLettersUsage.setImageResource(R.drawable.v);
                                    if (strGuessed.equals("W")) imageLettersUsage.setImageResource(R.drawable.w);
                                    if (strGuessed.equals("X")) imageLettersUsage.setImageResource(R.drawable.x);
                                    if (strGuessed.equals("Y")) imageLettersUsage.setImageResource(R.drawable.y);
                                    if (strGuessed.equals("Z")) imageLettersUsage.setImageResource(R.drawable.z);
                                }
                                intLetterCount ++;
                            }
                        }
                    }
                }
            }
        }

        boolean booWordComplete = true;
        for (int position = 0; position < strWord.length(); position++) {
            if (strCorrectLetters[position].equals("*")) {
                booWordComplete = false;
            }
        }
        if (booWordComplete) {
            // The game has been won
            if (timer != null) {
                timer.cancel();
            }
            intWinStreak ++;
            intLossStreak = 0;
            for (int id: BUTTON_IDS) {
                final ImageButton buttonLettersUsage = (ImageButton) findViewById(id);
                if (buttonLettersUsage != null) {
                    buttonLettersUsage.setVisibility(View.INVISIBLE);
                }
            }

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!booCancelHandler) {
                        endGameQuestion(getResources().getString(R.string.well_done) + "\n" +
                                getResources().getString(R.string.play_again), getResources().getString(R.string.ok), getResources().getString(R.string.not_yet), getResources().getString(R.string.win));
                    }
                }
            }, 3500);
        }
    }

    public void LifeLost() {

        int intLivesCount = 0;
        for (int id: LIFE_IDS) {
            intLivesCount ++;
            final ImageView imageLivesUsage = (ImageView)findViewById(id);
            if (imageLivesUsage != null) {
                if (intLivesCount == intLivesRemaining) {
                    imageLivesUsage.setImageResource(R.drawable.lifelostimage);
                }
            }
        }
        intLivesLost++;
        intLivesRemaining--;

        // Player is dead
        if (intLivesRemaining == 0) {
            intWinStreak = 0;
            intLossStreak ++;
            if (timer != null) {
                timer.cancel();
            }
            for (int id: BUTTON_IDS) {
                final ImageButton buttonLettersUsage = (ImageButton) findViewById(id);
                if (buttonLettersUsage != null) {
                    buttonLettersUsage.setVisibility(View.INVISIBLE);
                }
            }

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Start a new game after 1500ms
                    // The game has been lost
                    if (!booCancelHandler) {
                        endGameQuestion(getResources().getString(R.string.you_have_lost) + "\n" +
                                strWord + "\n" +
                                getResources().getString(R.string.play_again), getResources().getString(R.string.ok), getResources().getString(R.string.not_yet), getResources().getString(R.string.loss));
                    }
                }
            }, 2000);
        }
    }

    public void ResetVariables(){
        strCorrectLetters = null;
        intLettersRemaining = strWord.length();
        intLivesRemaining = intLives;
        intLivesLost = 0;
        booNewGame = true;
        intTime = 0;
        strAllGuessed.clear();
    }

    public void alertMessage(Activity a, String msgText, String buttonText) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(a);
        builder.setTitle(getResources().getString(R.string.alert_dialog));
        builder.setMessage(msgText);
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setNeutralButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Pressed button
                System.runFinalization();
            }
        });
        builder.create().show();
    }

    public void backMessage(String strMessage, String strPositive, String strNegative, String strResult){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.fragment_dialog);

        EditText editTextPlayerName = (EditText) dialog.findViewById(R.id.editTextPlayerName);
        editTextPlayerName.setVisibility(View.GONE);
        if (strResult.equals("")){
            ImageView imageResult = (ImageView) dialog.findViewById(R.id.imageResult);
            imageResult.setVisibility(View.GONE);
            TextView textViewFragTime = (TextView) dialog.findViewById(R.id.textViewFragTime);
            textViewFragTime.setVisibility(View.GONE);
        }
        TextView text = (TextView) dialog.findViewById(R.id.textViewCaption);
        text.setText(strMessage);

        Button buttonPositive = (Button) dialog.findViewById(R.id.buttonPositive);
        buttonPositive.setText(strPositive);
        buttonPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                booCancelHandler = true;
                ResetVariables();
                finish();
                dialog.dismiss();
            }
        });

        Button buttonNegative = (Button) dialog.findViewById(R.id.buttonNegative);
        buttonNegative.setText(strNegative);
        buttonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Restart the timer, they're continuing the game
                timer = new Timer();
                myTimerTask = new MyTimerTask();
                timer.schedule(myTimerTask, 0, 100);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void endGameQuestion(String strMessage, String strPositive, String strNegative, final String strResult) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.fragment_dialog);
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            // Unlock the achievement for playing first game
            Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_first_game));

            if (!(strCustom.equals(getResources().getString(R.string.default_custom)))){
                // Increment achievements for using custom words lists
                Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_use_your_words));
                Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_use_your_words_5),1);
                Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_use_your_words_10),1);
                Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_use_your_words_20),1);
                Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_use_your_words_100),1);
                Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_use_your_words_200),1);
                Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_use_your_words_500),1);
                Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_use_your_words_1000),1);
            }
        }
        final EditText editTextPlayerName = (EditText) dialog.findViewById(R.id.editTextPlayerName);
        editTextPlayerName.setVisibility(View.GONE);
        TextView textViewFragTime = (TextView) dialog.findViewById(R.id.textViewFragTime);
        textViewFragTime.setVisibility(View.GONE);
        if (strResult.equals(getResources().getString(R.string.win))) {
            // Won game
            ImageView imageResult = (ImageView) dialog.findViewById(R.id.imageResult);
            imageResult.setImageResource(R.drawable.lifeimage);
            if ((intTime < intBestTime10) && (strCustom.equals(getResources().getString(R.string.default_custom)))) {
                // New best time
                textViewFragTime.setVisibility(View.VISIBLE);
                editTextPlayerName.setVisibility(View.VISIBLE);

                // Achievements for winning a game
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    // Unlock the achievement for playing first game
                    Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_first_game));

                    // Increment achievements for multiple plays
                    Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_win_5_games),1);
                    Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_win_10_games),1);
                    Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_win_20_games),1);
                    Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_win_100_games),1);
                    Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_win_200_games),1);
                    Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_win_500_games),1);
                    Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_win_1000_games),1);

                    switch (intWinStreak) {
                        case 5:
                            // Unlock achievement winning 5 games
                            Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_5_in_a_row));
                            break;
                        case 10:
                            // Unlock achievement winning 10 games
                            Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_10_in_a_row));
                            break;
                        default:
                            break;
                    }
                    // Unlock the achievements for winning in under X time
                    if (intTime <= 500){
                        Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_under_500));
                    }
                    if (intTime <= 250){
                        Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_under_250));
                    }
                    if (intTime <= 100){
                        Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_under_100));
                    }
                    if (intTime <= 50){
                        Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_under_50));
                    }
                    if (intTime <= 25){
                        Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_under_25));
                    }
                    if (intTime <= 10){
                        Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_under_10));
                    }
                    // Unlock the achievements for winning with X lives
                    // and for not losing any lives
                    switch (intLives){
                        case 5:
                            Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_5_lives));
                            if (intLivesLost == 0) Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_none_lost_5));
                            break;
                        case 6:
                            Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_6_lives));
                            break;
                        case 7:
                            Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_7_lives));
                            break;
                        case 8:
                            Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_8_lives));
                            break;
                        case 9:
                            Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_9_lives));
                            break;
                        case 10:
                            Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_10_lives));
                            if (intLivesLost == 0) Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_none_lost_10));
                            break;
                        case 11:
                            Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_11_lives));
                            break;
                        case 12:
                            Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_12_lives));
                            break;
                        case 13:
                            Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_13_lives));
                            break;
                        case 14:
                            Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_14_lives));
                            break;
                        case 15:
                            Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_15_lives));
                            if (intLivesLost == 0) Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_none_lost_15));
                            break;
                        default:
                            break;
                    }

                } else {
                    // Alternative implementation (or warn user that they must
                    // sign in to use this feature)
                    Toast.makeText(HangmanActivity.this, getResources().getString(R.string.achievement_message), Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (strResult.equals(getResources().getString(R.string.loss))) {
            // Lost game
            ImageView imageResult = (ImageView) dialog.findViewById(R.id.imageResult);
            imageResult.setImageResource(R.drawable.lifelostimage);

            // Achievements for losing a game
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                // Unlock the achievement for playing first game
                Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_loser));

                // Unlock the achievements for losing many in a row
                switch (intLossStreak) {
                    case 5:
                        Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_losing_streak));
                        break;
                    case 10:
                        Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_big_loser));
                        break;
                    default:
                        break;
                }

            } else {
                // Alternative implementation (or warn user that they must
                // sign in to use this feature)
                Toast.makeText(HangmanActivity.this, getResources().getString(R.string.achievement_message), Toast.LENGTH_SHORT).show();
            }
        }
        TextView textViewCaption = (TextView) dialog.findViewById(R.id.textViewCaption);
        textViewCaption.setText(strMessage);

        Button buttonPositive = (Button) dialog.findViewById(R.id.buttonPositive);
        buttonPositive.setText(strPositive);
        buttonPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String strName;
                if (editTextPlayerName.getText().toString().equals("")) {
                    strName = getResources().getString(R.string.unnamed);
                } else {
                    strName = editTextPlayerName.getText().toString();
                }
                SaveTopScore(strName, strResult);
                newGame();
            }
        });

        Button buttonNegative = (Button) dialog.findViewById(R.id.buttonNegative);
        buttonNegative.setText(strNegative);
        buttonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String strName;
                if (editTextPlayerName.getText().toString().equals("")) {
                    strName = getResources().getString(R.string.unnamed);
                } else {
                    strName = editTextPlayerName.getText().toString();
                }
                SaveTopScore(strName, strResult);
                finish();
            }
        });
        dialog.show();
    }

    public void SaveTopScore(String strName, String strResult){
        SharedPreferences settings = getSharedPreferences(PREFERENCE_SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        if (strCustom.equals(getResources().getString(R.string.default_custom))) {
            if (strResult.equals(getResources().getString(R.string.win))) {
                if (intTime < intBestTime10) {
                    if (intTime < intBestTime9) {
                        if (intTime < intBestTime8) {
                            if (intTime < intBestTime7) {
                                if (intTime < intBestTime6) {
                                    if (intTime < intBestTime5) {
                                        if (intTime < intBestTime4) {
                                            if (intTime < intBestTime3) {
                                                if (intTime < intBestTime2) {
                                                    if (intTime < intBestTime1) {
                                                        // New Best Time #1
                                                        editor.putLong("settingBestTime10", intBestTime9);
                                                        editor.putLong("settingBestTime9", intBestTime8);
                                                        editor.putLong("settingBestTime8", intBestTime7);
                                                        editor.putLong("settingBestTime7", intBestTime6);
                                                        editor.putLong("settingBestTime6", intBestTime5);
                                                        editor.putLong("settingBestTime5", intBestTime4);
                                                        editor.putLong("settingBestTime4", intBestTime3);
                                                        editor.putLong("settingBestTime3", intBestTime2);
                                                        editor.putLong("settingBestTime2", intBestTime1);
                                                        editor.putLong("settingBestTime1", intTime);
                                                        editor.putString("settingBestTime10Name", strBestTime9);
                                                        editor.putString("settingBestTime9Name", strBestTime8);
                                                        editor.putString("settingBestTime8Name", strBestTime7);
                                                        editor.putString("settingBestTime7Name", strBestTime6);
                                                        editor.putString("settingBestTime6Name", strBestTime5);
                                                        editor.putString("settingBestTime5Name", strBestTime4);
                                                        editor.putString("settingBestTime4Name", strBestTime3);
                                                        editor.putString("settingBestTime3Name", strBestTime2);
                                                        editor.putString("settingBestTime2Name", strBestTime1);
                                                        editor.putString("settingBestTime1Name", strName);
                                                    } else {
                                                        // New Best Time #2
                                                        editor.putLong("settingBestTime10", intBestTime9);
                                                        editor.putLong("settingBestTime9", intBestTime8);
                                                        editor.putLong("settingBestTime8", intBestTime7);
                                                        editor.putLong("settingBestTime7", intBestTime6);
                                                        editor.putLong("settingBestTime6", intBestTime5);
                                                        editor.putLong("settingBestTime5", intBestTime4);
                                                        editor.putLong("settingBestTime4", intBestTime3);
                                                        editor.putLong("settingBestTime3", intBestTime2);
                                                        editor.putLong("settingBestTime2", intTime);
                                                        editor.putString("settingBestTime10Name", strBestTime9);
                                                        editor.putString("settingBestTime9Name", strBestTime8);
                                                        editor.putString("settingBestTime8Name", strBestTime7);
                                                        editor.putString("settingBestTime7Name", strBestTime6);
                                                        editor.putString("settingBestTime6Name", strBestTime5);
                                                        editor.putString("settingBestTime5Name", strBestTime4);
                                                        editor.putString("settingBestTime4Name", strBestTime3);
                                                        editor.putString("settingBestTime3Name", strBestTime2);
                                                        editor.putString("settingBestTime2Name", strName);
                                                    }
                                                } else {
                                                    // New Best Time #3
                                                    editor.putLong("settingBestTime10", intBestTime9);
                                                    editor.putLong("settingBestTime9", intBestTime8);
                                                    editor.putLong("settingBestTime8", intBestTime7);
                                                    editor.putLong("settingBestTime7", intBestTime6);
                                                    editor.putLong("settingBestTime6", intBestTime5);
                                                    editor.putLong("settingBestTime5", intBestTime4);
                                                    editor.putLong("settingBestTime4", intBestTime3);
                                                    editor.putLong("settingBestTime3", intTime);
                                                    editor.putString("settingBestTime10Name", strBestTime9);
                                                    editor.putString("settingBestTime9Name", strBestTime8);
                                                    editor.putString("settingBestTime8Name", strBestTime7);
                                                    editor.putString("settingBestTime7Name", strBestTime6);
                                                    editor.putString("settingBestTime6Name", strBestTime5);
                                                    editor.putString("settingBestTime5Name", strBestTime4);
                                                    editor.putString("settingBestTime4Name", strBestTime3);
                                                    editor.putString("settingBestTime3Name", strName);
                                                }
                                            } else {
                                                // New Best Time #4
                                                editor.putLong("settingBestTime10", intBestTime9);
                                                editor.putLong("settingBestTime9", intBestTime8);
                                                editor.putLong("settingBestTime8", intBestTime7);
                                                editor.putLong("settingBestTime7", intBestTime6);
                                                editor.putLong("settingBestTime6", intBestTime5);
                                                editor.putLong("settingBestTime5", intBestTime4);
                                                editor.putLong("settingBestTime4", intTime);
                                                editor.putString("settingBestTime10Name", strBestTime9);
                                                editor.putString("settingBestTime9Name", strBestTime8);
                                                editor.putString("settingBestTime8Name", strBestTime7);
                                                editor.putString("settingBestTime7Name", strBestTime6);
                                                editor.putString("settingBestTime6Name", strBestTime5);
                                                editor.putString("settingBestTime5Name", strBestTime4);
                                                editor.putString("settingBestTime4Name", strName);
                                            }
                                        } else {
                                            // New Best Time #5
                                            editor.putLong("settingBestTime10", intBestTime9);
                                            editor.putLong("settingBestTime9", intBestTime8);
                                            editor.putLong("settingBestTime8", intBestTime7);
                                            editor.putLong("settingBestTime7", intBestTime6);
                                            editor.putLong("settingBestTime6", intBestTime5);
                                            editor.putLong("settingBestTime5", intTime);
                                            editor.putString("settingBestTime10Name", strBestTime9);
                                            editor.putString("settingBestTime9Name", strBestTime8);
                                            editor.putString("settingBestTime8Name", strBestTime7);
                                            editor.putString("settingBestTime7Name", strBestTime6);
                                            editor.putString("settingBestTime6Name", strBestTime5);
                                            editor.putString("settingBestTime5Name", strName);
                                        }
                                    } else {
                                        // New Best Time #6
                                        editor.putLong("settingBestTime10", intBestTime9);
                                        editor.putLong("settingBestTime9", intBestTime8);
                                        editor.putLong("settingBestTime8", intBestTime7);
                                        editor.putLong("settingBestTime7", intBestTime6);
                                        editor.putLong("settingBestTime6", intTime);
                                        editor.putString("settingBestTime10Name", strBestTime9);
                                        editor.putString("settingBestTime9Name", strBestTime8);
                                        editor.putString("settingBestTime8Name", strBestTime7);
                                        editor.putString("settingBestTime7Name", strBestTime6);
                                        editor.putString("settingBestTime6Name", strName);
                                    }
                                } else {
                                    // New Best Time #7
                                    editor.putLong("settingBestTime10", intBestTime9);
                                    editor.putLong("settingBestTime9", intBestTime8);
                                    editor.putLong("settingBestTime8", intBestTime7);
                                    editor.putLong("settingBestTime7", intTime);
                                    editor.putString("settingBestTime10Name", strBestTime9);
                                    editor.putString("settingBestTime9Name", strBestTime8);
                                    editor.putString("settingBestTime8Name", strBestTime7);
                                    editor.putString("settingBestTime7Name", strName);
                                }
                            } else {
                                // New Best Time #8
                                editor.putLong("settingBestTime10", intBestTime9);
                                editor.putLong("settingBestTime9", intBestTime8);
                                editor.putLong("settingBestTime8", intTime);
                                editor.putString("settingBestTime10Name", strBestTime9);
                                editor.putString("settingBestTime9Name", strBestTime8);
                                editor.putString("settingBestTime8Name", strName);
                            }
                        } else {
                            // New Best Time #9
                            editor.putLong("settingBestTime10", intBestTime9);
                            editor.putLong("settingBestTime9", intTime);
                            editor.putString("settingBestTime10Name", strBestTime9);
                            editor.putString("settingBestTime9Name", strName);
                        }
                    } else {
                        // New Best Time #10
                        editor.putLong("settingBestTime10", intBestTime10);
                        editor.putString("settingBestTime10Name", strBestTime10);
                    }
                }
                editor.apply();
            }
        }
    }

    class MyTimerTask extends TimerTask{

        @Override
        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    intTime ++;
                    String strTemp = getResources().getString(R.string.time) + ": " + intTime;
                    TextView textViewTime = (TextView) findViewById(R.id.textViewTime);
                    assert textViewTime != null;
                    textViewTime.setText(strTemp);
                }
            });
        }
    }
}