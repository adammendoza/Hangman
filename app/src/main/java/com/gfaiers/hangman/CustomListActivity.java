package com.gfaiers.hangman;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class CustomListActivity extends AppCompatActivity {

    final static String PREFERENCE_SETTINGS = "settingWordLength";
    static String strWordsLists, strCustom, strFileName;
    static ArrayList<String> strWords = new ArrayList<>();
    static boolean booEditPressed = false, booOK = false;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        MobileAds.initialize(this, getResources().getString(R.string.banner_ad_unit_id_java));
        mAdView = (AdView) findViewById(R.id.adViewBanner);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        final EditText editTextListName = (EditText) findViewById(R.id.editTextListName);
        final EditText editTextAddWord = (EditText) findViewById(R.id.editTextAddWord);
        final Button buttonAdd = (Button) findViewById(R.id.buttonAdd);
        final Button buttonSave = (Button) findViewById(R.id.buttonSave);
        final Button buttonBack = (Button) findViewById(R.id.buttonBack);
        final Button buttonDelete = (Button) findViewById(R.id.buttonDelete);
        final ListView listViewList = (ListView) findViewById(R.id.listViewList);

        // Bring the variables to this activity
        Intent intentCustomList = getIntent();
        booEditPressed = intentCustomList.getExtras().getBoolean("booEdit");
        if (booEditPressed) {
            // If the user has pressed EDIT
            // Set the ListView to display the current list
            // Set the EditText to display the current file name
            // Display the Delete button
            strFileName = intentCustomList.getExtras().getString("strFileName");
            strWords = intentCustomList.getStringArrayListExtra("strLines");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(CustomListActivity.this, R.layout.spinner_list, strWords);
            assert listViewList != null;
            listViewList.setAdapter(adapter);
            assert  editTextListName != null;
            editTextListName.setText(strFileName);
            assert  buttonDelete != null;
            buttonDelete.setVisibility(View.VISIBLE);
        } else {
            // If the user has pressed NEW LIST
            strWordsLists = intentCustomList.getExtras().getString("strWordsLists");
            strCustom = intentCustomList.getExtras().getString("strCustom");
            assert  buttonBack != null;
            buttonBack.setVisibility(View.VISIBLE);
        }


        // Make the app remove an entry if the user "LONG CLICKS" the entry
        if (listViewList != null) {
            listViewList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    String strTemp = listViewList.getItemAtPosition(position).toString();
                    Toast.makeText(CustomListActivity.this, getResources().getString(R.string.removed) + " " + strTemp, Toast.LENGTH_SHORT).show();
                    assert editTextAddWord != null;
                    editTextAddWord.setText(strTemp);
                    strWords.remove(position);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CustomListActivity.this, R.layout.spinner_list, strWords);
                    listViewList.setAdapter(adapter);
                    if (strWords.isEmpty()){
                        booOK = false;
                    }
                    return false;
                }
            });
        }

        if (buttonAdd != null) {
            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Make sure that the content of the Edit Text for adding a word has something in it
                    // Make sure that the value of what's in the word is a string, with only a-z and spaces present
                    // Make sure that there is no extra white space
                    // Make sure that the word entered isn't already in the ListView of words
                    assert buttonSave != null;
                    buttonSave.setVisibility(View.VISIBLE);
                    assert editTextAddWord != null;
                    String strAdd = editTextAddWord.getText().toString();
                    if (!strAdd.equals("")){
                        if (booIsAZ(strAdd)){
                            if (!strWords.contains(strAdd)) {
                                if (!strAdd.endsWith(" ")) {
                                    strAdd = strAdd.replaceAll("\\s+", " ");
                                    // The words that's been entered is okay to use, now we need to add it to an array
                                    // and add it to the ListView
                                    strWords.add(strAdd);
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CustomListActivity.this, R.layout.spinner_list, strWords);
                                    assert listViewList != null;
                                    listViewList.setAdapter(adapter);
                                    editTextAddWord.setText("");
                                } else {
                                    strAdd = strAdd.replaceAll("\\s+", " ");
                                    // Remove the final space
                                    strAdd = strAdd.substring(0, strAdd.length()-1);
                                    // The words that's been entered is okay to use, now we need to add it to an array
                                    // and add it to the ListView
                                    strWords.add(strAdd);
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CustomListActivity.this, R.layout.spinner_list, strWords);
                                    assert listViewList != null;
                                    listViewList.setAdapter(adapter);
                                    editTextAddWord.setText("");
                                }
                                booOK = true;
                            } else {
                                Toast.makeText(CustomListActivity.this, getResources().getString(R.string.already_entered) + "\n" +
                                        strAdd, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CustomListActivity.this, getResources().getString(R.string.only_letters) + "\n" +
                                    getResources().getString(R.string.and_spaces), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CustomListActivity.this, getResources().getString(R.string.cant_enter_nothing), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (buttonBack != null) {
            buttonBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    strWords.clear();
                    finish();
                }
            });
        }

        if (buttonDelete != null) {
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    strWords.clear();
                    finish();
                }
            });
        }

        if (buttonSave != null) {
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Make sure the file name hasn't already been taken
                    // Make sure the file name only includes a - z and spaces
                    // Make sure the file name doesn't include extra white space
                    // Check that there are actually words saved in the word list
                    assert editTextListName != null;
                    String strListName = editTextListName.getText().toString();
                    if (!booFileExists(strListName)){
                        if (booIsAZ(strListName)){
                            if (!strWords.isEmpty()){
                                strListName = strListName.replaceAll("\\s+", " ");
                                // Save the file as an object
                                try {
                                    FileOutputStream fos = CustomListActivity.this.openFileOutput(strListName, MODE_PRIVATE);
                                    ObjectOutputStream out = new ObjectOutputStream(fos);
                                    out.writeObject(strWords);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                                strWordsLists = getResources().getString(R.string.custom_list);
                                strCustom = strListName;
                                saveSettings();
                                strWords.clear();
                                finish();

                            } else {
                                Toast.makeText(CustomListActivity.this, getResources().getString(R.string.to_add_word_list), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(CustomListActivity.this, getResources().getString(R.string.only_letters) + "\n" +
                                    getResources().getString(R.string.and_spaces_file_name), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CustomListActivity.this, getResources().getString(R.string.list_name_invalid), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        if (mAdView != null){
            mAdView.resume();
        }
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onPause(){

        if (mAdView != null){
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
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

        if (booOK) {
            backMessage(getResources().getString(R.string.not_saved), getResources().getString(R.string.ok), getResources().getString(R.string.cancel), "");
        } else {
            super.onBackPressed();
        }
    }
    private boolean booIsAZ(String testSubject){
        // Checks that the string carried is only letters or spaces
        char[] chars = testSubject.toCharArray();
        for (char c : chars) {
            if ((!Character.isLetter(c)) && (!Character.isSpaceChar(c))){
                return false;
            }
        }
        return true;
    }

    public boolean booFileExists(String fileName){
        File file = getBaseContext().getFileStreamPath(fileName);
        return file.exists();
    }

    private void saveSettings(){
        SharedPreferences settings = getSharedPreferences(PREFERENCE_SETTINGS,MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        //Don't save the settings if they've not changed
        editor.putString("settingWordsLists", strWordsLists);
        editor.putString("settingCustom", strCustom);
        editor.apply();
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
                strWords.clear();
                finish();
                dialog.dismiss();
            }
        });

        Button buttonNegative = (Button) dialog.findViewById(R.id.buttonNegative);
        buttonNegative.setText(strNegative);
        buttonNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
