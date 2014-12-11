package com.klee.painemr;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.klee.painemr.formgenerator.FormActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FormGeneratorExample extends FormActivity {
    public static final int OPTION_SAVE = 0;
    public static final int OPTION_POPULATE = 1;
    public static final int OPTION_CANCEL = 2;
    String SD_CARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath().toString();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String PACKAGE_NAME = getApplicationContext().getPackageName();
        File ChildFolder = new File(Environment.getExternalStorageDirectory() + "/" + PACKAGE_NAME);
        if (!ChildFolder.mkdirs()) {
            Log.e("FormGeneratorExample", "Directory not created");
        }

        //Get the text file
        File file = new File(ChildFolder, "schemas.json");
        generateForm(readFileToString(file));
//        generateForm( FormActivity.parseFileToString( this, readFileToString(file) ) );
        save();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, OPTION_SAVE, 0, "Save");
        menu.add(0, OPTION_POPULATE, 0, "Populate");
        menu.add(0, OPTION_CANCEL, 0, "Cancel");
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int id, MenuItem item) {

        switch (item.getItemId()) {
            case OPTION_SAVE:
                save();
                break;

            case OPTION_POPULATE:
                populate(FormActivity.parseFileToString(this, "data.json"));
                break;

            case OPTION_CANCEL:

                break;
        }

        return super.onMenuItemSelected(id, item);
    }

    private String readFileToString(File filename) {
        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            return text.toString();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }
        return null;
    }
}