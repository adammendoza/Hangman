package com.gfaiers.hangman;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    final static String PREFERENCE_SETTINGS = "settingWordLength";

    static int intShortestWord, // This is equal to the shortest possible length of word
            intLongestWord, // This is equal to the longest possible length of word
            intLives; // This is equal to the total amount of lives the player has
    static boolean booLivesChanged = false, // Triggered when LIVES needs re-saving
            booMinChanged = false, // Triggered when SHORTEST WORD needs re-saving
            booMaxChanged = false, // Triggered when LONGEST WORD needs re-saving
            booFirstPlay = false, // Triggered when it's the first time the player has played
            booListChanged = false, // Triggered when the final word list has changed
            booCustomChanged = false, // Triggered when the custom word list has changed
            booEditPressed = false; // Triggered when you the user presses EDIT
    static String strWordsLists, // Holds the name of the selected Words List
            strCustom; // Holds the name of the selected custom list
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        MobileAds.initialize(this, getResources().getString(R.string.banner_ad_unit_id_java));
        mAdView = (AdView) findViewById(R.id.adViewBanner);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        // Set up the components
        final SeekBar seekBarMinWordLength = (SeekBar) findViewById(R.id.seekBarMinWordLength);
        final SeekBar seekBarMaxWordLength = (SeekBar) findViewById(R.id.seekBarMaxWordLength);
        final SeekBar seekBarLives = (SeekBar) findViewById(R.id.seekBarLives);
        final TextView textViewMinWordLen = (TextView) findViewById(R.id.textViewMinWordLen);
        final TextView textViewMaxWordLen = (TextView) findViewById(R.id.textViewMaxWordLen);
        final TextView textViewLives = (TextView) findViewById(R.id.textViewLives);
        final Spinner spinnerWordsLists = (Spinner) findViewById(R.id.spinnerWordsLists);
        final Spinner spinnerCustomList = (Spinner) findViewById(R.id.spinnerCustomList);
        final Button buttonNewList = (Button) findViewById(R.id.buttonNewList);
        final Button buttonEditList = (Button) findViewById(R.id.buttonEditList);

        // Pull up the variables that were passed to this class
        Intent intentSettings = getIntent();
        intLongestWord = intentSettings.getExtras().getInt("intLongestWord");
        intShortestWord = intentSettings.getExtras().getInt("intShortestWord");
        intLives = intentSettings.getExtras().getInt("intLives");
        strWordsLists = intentSettings.getExtras().getString("strWordsLists");
        strCustom = intentSettings.getExtras().getString("strCustom");

        // Set up the first seekBar for the shortest word
        assert seekBarMinWordLength != null;
        assert seekBarMaxWordLength != null;
        assert seekBarLives != null;
        assert textViewMinWordLen != null;
        assert textViewLives != null;
        assert textViewMaxWordLen != null;
        assert spinnerCustomList != null;
        assert spinnerWordsLists != null;
        assert buttonEditList != null;
        assert buttonNewList != null;

        seekBarMinWordLength.setMax(19);
        seekBarMinWordLength.setProgress(intShortestWord - 1);
        String strMinWordLen = Integer.toString(intShortestWord);
        textViewMinWordLen.setText(strMinWordLen);
        seekBarMinWordLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String strProgress = Integer.toString(progress + 1);
                textViewMinWordLen.setText(strProgress);
                intShortestWord = progress;
                // If the max word length is less than the min word length
                // then change the max word length to min word + 1
                if (seekBarMaxWordLength.getProgress() < progress){
                    seekBarMaxWordLength.setProgress(progress + 1);
                }
                booMinChanged = true;
                saveSettings();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (spinnerWordsLists.getSelectedItem().toString().equals(getResources().getString(R.string.custom_list))) {
                    WordLenOK(intLongestWord, intShortestWord, spinnerCustomList.getSelectedItem().toString());
                } else {
                    WordLenOK(intLongestWord, intShortestWord, spinnerWordsLists.getSelectedItem().toString());
                }
                saveSettings();
            }
        });
        //Set up the first seekBar for the longest word
        seekBarMaxWordLength.setMax(28);
        seekBarMaxWordLength.setProgress(intLongestWord - 2);
        String strMaxWordLen = Integer.toString(intLongestWord);
        textViewMaxWordLen.setText(strMaxWordLen);
        seekBarMaxWordLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String strProgress = Integer.toString(progress + 2);
                textViewMaxWordLen.setText(strProgress);
                intLongestWord = progress;
                // If the max word length is less than the min word length
                // then change the min word length to max word -1
                if (seekBarMinWordLength.getProgress() > progress){
                    seekBarMinWordLength.setProgress(progress + 1);
                }
                booMaxChanged = true;
                saveSettings();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (spinnerWordsLists.getSelectedItem().toString().equals(getResources().getString(R.string.custom_list))) {
                    WordLenOK(intLongestWord, intShortestWord, spinnerCustomList.getSelectedItem().toString());
                } else {
                    WordLenOK(intLongestWord, intShortestWord, spinnerWordsLists.getSelectedItem().toString());
                }
                saveSettings();
            }
        });

        //Set up the second seekBar for the number of lives
        seekBarLives.setMax(10);
        seekBarLives.setProgress(intLives - 5);
        String strTemp = Integer.toString(intLives);
        textViewLives.setText(strTemp);
        seekBarLives.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String strProgress = Integer.toString(progress + 5);
                textViewLives.setText(strProgress);
                intLives = progress;
                booLivesChanged = true;
                saveSettings();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,R.array.words_lists_array, R.layout.spinner_list);
        adapter.setDropDownViewResource(R.layout.spinner_list_dropdown);
        spinnerWordsLists.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Update score sheet depending on what time option was selected
                if (spinnerWordsLists.getSelectedItem().toString().equals(getResources().getString(R.string.custom_list))){
                    // This will make the editText visible and the save button visible
                    // It won't change the value of booListChanged until the SAVE button has been pressed
                    Button buttonNewList = (Button) findViewById(R.id.buttonNewList);
                    Spinner spinnerCustomList = (Spinner) findViewById(R.id.spinnerCustomList);
                    assert buttonNewList != null;
                    assert spinnerCustomList != null;
                    buttonNewList.setVisibility(View.VISIBLE);
                    spinnerCustomList.setVisibility(View.VISIBLE);
                } else {
                    // If the user is using a pre built word list, then don't let them edit one
                    strWordsLists = spinnerWordsLists.getSelectedItem().toString();
                    booListChanged = true;
                    Button buttonNewList = (Button) findViewById(R.id.buttonNewList);
                    Button buttonEditList = (Button) findViewById(R.id.buttonEditList);
                    Spinner spinnerCustomList = (Spinner) findViewById(R.id.spinnerCustomList);
                    assert buttonNewList != null;
                    assert buttonEditList != null;
                    assert spinnerCustomList != null;
                    buttonNewList.setVisibility(View.GONE);
                    buttonEditList.setVisibility(View.GONE);
                    spinnerCustomList.setVisibility(View.GONE);
                }
                if (spinnerWordsLists.getSelectedItem().toString().equals(getResources().getString(R.string.custom_list))) {
                    WordLenOK(intLongestWord, intShortestWord, spinnerCustomList.getSelectedItem().toString());
                } else {
                    WordLenOK(intLongestWord, intShortestWord, spinnerWordsLists.getSelectedItem().toString());
                }
                saveSettings();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerWordsLists.setAdapter(adapter);
        for (int intCount = 0; intCount < spinnerWordsLists.getCount(); intCount ++){
            if (spinnerWordsLists.getItemAtPosition(intCount).toString().equals(strWordsLists)){
                spinnerWordsLists.setSelection(intCount);
                break;
            }
        }

        String strSavedFile[];
        ArrayList<String> strSavedFiles = new ArrayList<>();
        strSavedFile = getApplicationContext().fileList();
        for (String s : strSavedFile){
            if ((!s.equals("instant-run")) && (!s.equals("")) && (!s.contains("."))) strSavedFiles.add(s);
        }
        if (strSavedFiles.isEmpty()){
            strSavedFiles.add(getResources().getString(R.string.default_custom));
        }
        String[] strSavedFile1 = strSavedFiles.toArray(new String[strSavedFiles.size()]);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(SettingsActivity.this, R.layout.spinner_list, strSavedFile1);
        adapter1.setDropDownViewResource(R.layout.spinner_list_dropdown);
        spinnerCustomList.setAdapter(adapter1);
        spinnerCustomList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // If the value of the spinner is "Please create a list", then don't use the value for the list
                // as a list doesn't exist with that name
                if (!spinnerCustomList.getItemAtPosition(position).toString().equals(getResources().getString(R.string.default_custom))) {
                    strCustom = spinnerCustomList.getItemAtPosition(position).toString();
                    strWordsLists = getResources().getString(R.string.custom_list);
                    booCustomChanged = true;
                    booListChanged = true;
                    WordLenOK(intLongestWord, intShortestWord, spinnerCustomList.getSelectedItem().toString());
                    buttonEditList.setVisibility(View.VISIBLE);
                } else {
                    buttonEditList.setVisibility(View.GONE);
                }
                saveSettings();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        for (int intCount = 0; intCount < spinnerCustomList.getCount(); intCount ++){
            if (spinnerCustomList.getItemAtPosition(intCount).toString().equals(strCustom)){
                spinnerCustomList.setSelection(intCount);
                break;
            }
        }
        buttonNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent customListActivity = new Intent(SettingsActivity.this, CustomListActivity.class);
                int result = 1;
                saveSettings();
                // Go to the Custom List activity
                customListActivity.putExtra("booEdit", booEditPressed);
                customListActivity.putExtra("intShortestWord", intShortestWord);
                customListActivity.putExtra("intLongestWord", intLongestWord);
                customListActivity.putExtra("intLives", intLives);
                customListActivity.putExtra("strWordsLists", strWordsLists);
                customListActivity.putExtra("strCustom", strCustom);
                startActivityForResult(customListActivity, result);
                saveSettings();
            }
        });

        buttonEditList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // For EDIT list Button, it needs to delete the list first
                // Then send the contents of the deleted list to the custom list activity
                // Then if the user presses "DELETE LIST" it just brings the user back as it currently does
                // If the user clicks save list, then it'll save it as a new list as the original is deleted

                String strTemp = spinnerCustomList.getSelectedItem().toString();
                if (!strTemp.equals(getResources().getString(R.string.default_custom))) {
                    booEditPressed = true;
                    // LOAD THE DATA FROM THE LIST
                    // Including the file name and the file contents
                    FileInputStream fis = null;
                    ObjectInputStream in = null;
                    ArrayList<String> lines = new ArrayList<>();
                    try {
                        fis = openFileInput(strTemp);
                        in = new ObjectInputStream(fis);
                        lines = (ArrayList<String>) in.readObject();
                        in.close();
                    } catch (FileNotFoundException e) {
                        alertMessage(SettingsActivity.this, getResources().getString(R.string.file_not_found), getResources().getString(R.string.ok));
                    } catch (StreamCorruptedException e) {
                        alertMessage(SettingsActivity.this, getResources().getString(R.string.stream_corrupted), getResources().getString(R.string.ok));
                    } catch (IOException e) {
                        alertMessage(SettingsActivity.this, getResources().getString(R.string.ioexception), getResources().getString(R.string.ok));
                    } catch (ClassNotFoundException e) {
                        alertMessage(SettingsActivity.this, getResources().getString(R.string.class_not_found), getResources().getString(R.string.ok));
                    }

                    // DELETE THE LIST
                    deleteFile(strTemp);
                    // OPEN THE NEW ACTIVITY
                    // Sending the file name and the file contents
                    // Populate the file name into the editTextListName
                    // Populate the file contents into the listViewList
                    Intent customListActivity = new Intent(SettingsActivity.this, CustomListActivity.class);
                    int result = 1;
                    customListActivity.putExtra("strFileName", strTemp);
                    customListActivity.putStringArrayListExtra("strLines", lines);
                    customListActivity.putExtra("booEdit", booEditPressed);
                    startActivityForResult(customListActivity, result);

                    // Continue prepping SettingsActivity while in the background
                    booCustomChanged = true;
                    String strSavedFile[];
                    ArrayList<String> strSavedFiles = new ArrayList<>();
                    strSavedFile = getApplicationContext().fileList();
                    for (String s : strSavedFile) {
                        if (!((s.equals("instant-run") || (s.equals("")) || (s.contains("."))))) {
                            strSavedFiles.add(s);
                        }
                    }
                    if (strSavedFiles.isEmpty()) {
                        strSavedFiles.add(getResources().getString(R.string.default_custom));
                        strWordsLists = getResources().getString(R.string.default_list);
                        strCustom = getResources().getString(R.string.default_custom);
                    }
                    String[] strSavedFilez = strSavedFiles.toArray(new String[strSavedFiles.size()]);
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<>(SettingsActivity.this, R.layout.spinner_list, strSavedFilez);
                    spinnerCustomList.setAdapter(adapter1);
                    if (spinnerWordsLists.getSelectedItem().toString().equals(getResources().getString(R.string.custom_list))) {
                        WordLenOK(intLongestWord, intShortestWord, spinnerCustomList.getSelectedItem().toString());
                    } else {
                        WordLenOK(intLongestWord, intShortestWord, spinnerWordsLists.getSelectedItem().toString());
                    }
                    saveSettings();
                }
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        if (mAdView != null){
            mAdView.resume();
        }
        final Spinner spinnerWordsLists = (Spinner) findViewById(R.id.spinnerWordsLists);
        final Spinner spinnerCustomList = (Spinner) findViewById(R.id.spinnerCustomList);
        final Button buttonNewList = (Button) findViewById(R.id.buttonNewList);
        final Button buttonEditList = (Button) findViewById(R.id.buttonEditList);

        SharedPreferences settings = getSharedPreferences(PREFERENCE_SETTINGS, MODE_PRIVATE);
        strWordsLists = settings.getString("settingWordsLists", getResources().getString(R.string.default_list));
        strCustom = settings.getString("settingCustom", null);
        booEditPressed = false;
        assert spinnerWordsLists != null;
        assert spinnerCustomList != null;
        assert buttonNewList != null;
        assert buttonEditList != null;
        if (spinnerWordsLists.getSelectedItem().toString().equals(getResources().getString(R.string.custom_list))) {
            buttonNewList.setVisibility(View.VISIBLE);
            buttonEditList.setVisibility(View.VISIBLE);
            spinnerCustomList.setVisibility(View.VISIBLE);
            WordLenOK(intLongestWord, intShortestWord, spinnerCustomList.getSelectedItem().toString());
        } else {
            buttonNewList.setVisibility(View.GONE);
            buttonEditList.setVisibility(View.GONE);
            spinnerCustomList.setVisibility(View.GONE);
            WordLenOK(intLongestWord, intShortestWord, spinnerWordsLists.getSelectedItem().toString());
        }
        for (int intCount = 0; intCount < spinnerWordsLists.getCount(); intCount ++){
            if (spinnerWordsLists.getItemAtPosition(intCount).toString().equals(strWordsLists)){
                spinnerWordsLists.setSelection(intCount);
                break;
            }
        }
        String strSavedFile[];
        ArrayList<String> strSavedFiles = new ArrayList<>();
        strSavedFile = getApplicationContext().fileList();
        for (String s : strSavedFile){
            if ((!s.equals("instant-run")) && (!s.equals("")) && (!s.contains("."))) strSavedFiles.add(s);
        }
        if (strSavedFiles.isEmpty()){
            strSavedFiles.add(getResources().getString(R.string.default_custom));
        }
        String[] strSavedFile1 = strSavedFiles.toArray(new String[strSavedFiles.size()]);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(SettingsActivity.this, R.layout.spinner_list, strSavedFile1);
        adapter1.setDropDownViewResource(R.layout.spinner_list_dropdown);
        spinnerCustomList.setAdapter(adapter1);
        for (int intCount = 0; intCount < spinnerCustomList.getCount(); intCount ++){
            if (spinnerCustomList.getItemAtPosition(intCount).toString().equals(strCustom)){
                spinnerCustomList.setSelection(intCount);
                break;
            }
        }
        saveSettings();
    }

    @Override
    protected void onPause(){

        if (mAdView != null){
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        if (mAdView != null){
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        final Spinner spinnerWordsLists = (Spinner) findViewById(R.id.spinnerWordsLists);
        final Spinner spinnerCustomList = (Spinner) findViewById(R.id.spinnerCustomList);
        final Button buttonNewList = (Button) findViewById(R.id.buttonNewList);
        final Button buttonEditList = (Button) findViewById(R.id.buttonEditList);
        assert spinnerWordsLists != null;
        assert spinnerCustomList != null;
        assert buttonNewList != null;
        assert buttonEditList != null;
        if (spinnerWordsLists.getSelectedItem().toString().equals(getResources().getString(R.string.custom_list)) && spinnerCustomList.getSelectedItem().toString().equals(getResources().getString(R.string.default_custom))){
            // If there is no word list selected then set as default
            booListChanged = true;
            booCustomChanged = true;
            strWordsLists = getResources().getString(R.string.default_list);
            strCustom = getResources().getString(R.string.default_custom);
            spinnerWordsLists.setSelection(1);
            spinnerCustomList.setVisibility(View.GONE);
            buttonNewList.setVisibility(View.GONE);
            buttonEditList.setVisibility(View.GONE);
        }
        saveSettings();
    }

    private void WordLenOK(int intLong, int intShort, String strFileName) {

        if (!strFileName.equals(getResources().getString(R.string.custom_list)) && !strFileName.equals(getResources().getString(R.string.default_custom))) {
            intShort--;
            intLong = intLong + 2;
            // This needs to react to the file depending on if it's in Assets or if it's a custom file
            Spinner spinnerCustomList = (Spinner) findViewById(R.id.spinnerCustomList);
            ArrayList<String> lines = new ArrayList<>();

            assert spinnerCustomList != null;
            if (spinnerCustomList.getVisibility() == View.GONE) {
                // It's using a file in Assets
                // Read from a file in ASSETS (For the default files)
                // This method reads the file line by line, checking each line if it's long enough / short enough to be used
                // Then it picks a random word from the remaining stack
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(getAssets().open(strFileName)));

                    // do reading, usually loop until end of file reading
                    String mLine;
                    while ((mLine = reader.readLine()) != null) {
                        lines.add(mLine);
                    }
                } catch (IOException e) {
                    // Log the exception
                    alertMessage(this, getResources().getString(R.string.file_not_found), getResources().getString(R.string.ok));
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
            } else {
                FileInputStream fis = null;
                ObjectInputStream in = null;
                try {
                    fis = openFileInput(strFileName);
                    in = new ObjectInputStream(fis);
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
            }

            if (!lines.isEmpty()) {
                // Put the ArrayList from the file into the new strWords array, then clear what has been read
                String[] strWords = lines.toArray(new String[lines.size()]);
                lines.clear();
                // Refill the ArrayList with only values that are within the correct length boundaries
                for (String s : strWords) {
                    if ((s.length() >= intShort) && (s.length() <= intLong))
                        lines.add(s);
                }

                if (lines.isEmpty()) {
                    // NOT OKAY
                    SeekBar seekBarMinWordLength = (SeekBar) findViewById(R.id.seekBarMinWordLength);
                    SeekBar seekBarMaxWordLength = (SeekBar) findViewById(R.id.seekBarMaxWordLength);
                    assert seekBarMaxWordLength != null;
                    assert seekBarMinWordLength != null;
                    seekBarMaxWordLength.setProgress(28);
                    seekBarMinWordLength.setProgress(0);
                }
            }
        }
    }


    private void saveSettings() {
        SharedPreferences settings = getSharedPreferences(PREFERENCE_SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        //Don't save the settings if they've not changed
        if (booMinChanged) editor.putInt("settingShortestWord", intShortestWord);
        booMinChanged = false;
        if (booMaxChanged) editor.putInt("settingLongestWord", intLongestWord);
        booMaxChanged = false;
        if (booLivesChanged) editor.putInt("settingLives", intLives);
        booLivesChanged = false;
        if (booListChanged) editor.putString("settingWordsLists", strWordsLists);
        booListChanged = false;
        if (booCustomChanged) editor.putString("settingCustom", strCustom);
        booCustomChanged = false;
        editor.putBoolean("settingFirstPlay", booFirstPlay);
        editor.apply();
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
}