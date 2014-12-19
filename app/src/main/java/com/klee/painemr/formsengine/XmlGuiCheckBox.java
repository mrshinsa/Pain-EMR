package com.klee.painemr.formsengine;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by klee on 12/11/2014.
 */
public class XmlGuiCheckBox extends LinearLayout {
    String tag = XmlGuiPickOne.class.getName();
    TextView label;
    ArrayAdapter<String> aa;
    CheckBox checkBox;

    public XmlGuiCheckBox(Context context, String labelText, String options) {
        super(context);
        label = new TextView(context);
        label.setText(labelText);
        String[] opts = options.split("\\|");
        aa = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_checked, opts);

        for (int i = 0; i < opts.length; i++) {
            CheckBox checkedTextView = new CheckBox(context);
            checkedTextView.setText(opts[i]);
            checkedTextView.setId(i);

            this.addView(checkedTextView);
        }
        this.addView(label);
    }

    public XmlGuiCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }


    public String getValue() {
        return "";
    }

}
