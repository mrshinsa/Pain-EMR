/*
 * XmlGui application.
 * Written by Frank Ableson for IBM Developerworks
 * June 2010
 * Use the code as you wish -- no warranty of fitness, etc, etc.
 */
package com.klee.painemr.formsengine;


import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.util.Log;

import com.klee.painemr.R;

import java.io.File;

public class XmlGui extends Activity implements View.OnClickListener {
    final String tag = XmlGui.class.getName();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms_engine);


        this.findViewById(R.id.btnRunForm1).setOnClickListener(this);
        this.findViewById(R.id.btnRunForm2).setOnClickListener(this);
        this.findViewById(R.id.aboutButtonId).setOnClickListener(this);
        this.findViewById(R.id.historyButtonId).setOnClickListener(this);
        this.findViewById(R.id.painButtonId).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String PACKAGE_NAME = getApplicationContext().getPackageName();
        File ChildFolder = new File(Environment.getExternalStorageDirectory() + "/" + PACKAGE_NAME);
        if (!ChildFolder.mkdirs()) {
            Log.e("FormGeneratorExample", "Directory not created");
        }


        EditText formNumber = (EditText) findViewById(R.id.formNumberId);
        Log.i(tag,"Attempting to process Form # [" + formNumber.getText().toString() + "]");


        switch (v.getId()) {
            case R.id.btnRunForm1:
                String fileName = Environment.getExternalStorageDirectory() + "/" + PACKAGE_NAME + "/" + "xmlgui1.xml";
                Intent newFormInfo = new Intent(XmlGui.this, RunForm.class);
                newFormInfo.putExtra("FILE_NAME", fileName);
                startActivity(newFormInfo);
                break;

            case R.id.btnRunForm2:
                fileName = Environment.getExternalStorageDirectory() + "/" + PACKAGE_NAME + "/" + "xmlgui2.xml";
                newFormInfo = new Intent(XmlGui.this, RunForm.class);
                newFormInfo.putExtra("FILE_NAME", fileName);
                startActivity(newFormInfo);
                break;

            case R.id.aboutButtonId:
                fileName = Environment.getExternalStorageDirectory() + "/" + PACKAGE_NAME + "/" + "about_" + formNumber.getText().toString() +".xml";
                newFormInfo = new Intent(XmlGui.this, RunForm.class);
                newFormInfo.putExtra("FILE_NAME", fileName);
                startActivity(newFormInfo);
                break;

            case R.id.historyButtonId:
                fileName = Environment.getExternalStorageDirectory() + "/" + PACKAGE_NAME + "/" + "history_" + formNumber.getText().toString() +".xml";
                newFormInfo = new Intent(XmlGui.this, RunForm.class);
                newFormInfo.putExtra("FILE_NAME", fileName);
                startActivity(newFormInfo);
                break;

            case R.id.painButtonId:
                fileName = Environment.getExternalStorageDirectory() + "/" + PACKAGE_NAME + "/" + "pain_" + formNumber.getText().toString() +".xml";
                newFormInfo = new Intent(XmlGui.this, RunForm.class);
                newFormInfo.putExtra("FILE_NAME", fileName);
                startActivity(newFormInfo);
                break;
        }
    }
}