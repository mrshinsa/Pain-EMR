package com.klee.painemr.formsengine;

/**
 * Created by Kevin on 2014-12-19.
 */


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import com.klee.painemr.CommWizard;
import com.klee.painemr.MenuChoiceDetailActivity;
import com.klee.painemr.MenuChoiceDetailFragment;
import com.klee.painemr.R;
import com.klee.painemr.SettingsActivity;
import com.klee.painemr.SoappWizard;

import java.io.File;

public class XmlMainList extends ListActivity {

    static final String[] CATEGORY_STRING_ARRAY = new String[]{"About", "History", "Pain"};

    class MobileArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;

        public MobileArrayAdapter(Context context, String[] values) {
            super(context, R.layout.custom_list_entry, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.custom_list_entry, parent, false);
            TextView label = (TextView) rowView.findViewById(R.id.labelId);
            TextView description = (TextView) rowView.findViewById(R.id.descriptionId);
            label.setText(values[position]);
            description.setText("descriptions");
            // Change icon based on name
            String s = values[position];
            System.out.println(s);
            return rowView;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new MobileArrayAdapter(this, CATEGORY_STRING_ARRAY));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        String fileName = "";
        Intent newFormInfo;
        String PACKAGE_NAME = getApplicationContext().getPackageName();
        File ChildFolder = new File(Environment.getExternalStorageDirectory() + "/" + PACKAGE_NAME);
        if (!ChildFolder.mkdirs()) {
            Log.e("FormGeneratorExample", "Directory not created");
        }

        //get selected items
        String selectedValue = (String) getListAdapter().getItem(position);
        Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();

        if (selectedValue.equals("About")) {
            fileName = Environment.getExternalStorageDirectory() + "/" + PACKAGE_NAME + "/" + "about" + ".xml";
            newFormInfo = new Intent(this, RunForm.class);
            newFormInfo.putExtra("FILE_NAME", fileName);
            startActivity(newFormInfo);
        } else if (selectedValue.equals("History")) {
            fileName = Environment.getExternalStorageDirectory() + "/" + PACKAGE_NAME + "/" + "history" + ".xml";
            newFormInfo = new Intent(this, RunForm.class);
            newFormInfo.putExtra("FILE_NAME", fileName);
            startActivity(newFormInfo);
        } else if (selectedValue.equals("Pain")) {
            fileName = Environment.getExternalStorageDirectory() + "/" + PACKAGE_NAME + "/" + "pain_0" + ".xml";
            newFormInfo = new Intent(this, RunForm.class);
            newFormInfo.putExtra("FILE_NAME", fileName);
            startActivity(newFormInfo);
        }
    }

}