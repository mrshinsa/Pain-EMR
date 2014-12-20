/*
 * XmlGui application.
 * Written by Frank Ableson for IBM Developerworks
 * June 2010
 * Use the code as you wish -- no warranty of fitness, etc, etc.
 */
package com.klee.painemr.formsengine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import android.app.AlertDialog.*;

import java.util.ListIterator;
import java.lang.Thread;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.klee.painemr.R;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;


public class RunForm extends Activity {
    /**
     * Called when the activity is first created.
     */
    String tag = RunForm.class.getName();
    XmlGuiForm theForm;
    ProgressDialog progressDialog;
    Handler progressHandler;
    ScrollView scrollView;
    LinearLayout linearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        String formNumber = "";
        String fileName = "";
        Intent startingIntent = getIntent();
        if (startingIntent == null) {
            Log.e(tag, "No Intent?  We're not supposed to be here...");
            finish();
            return;
        }
//    	formNumber = startingIntent.getStringExtra("formNumber");
//    	Log.i(tag,"Running Form [" + formNumber + "]");
//    	if (pullOutFormDataFromFile(formNumber)) {
//    		DisplayForm();
//    	}

        fileName = startingIntent.getStringExtra("FILE_NAME");
        Log.i(tag, "Running Form [" + fileName + "]");
        createView();
        if (parseFormData(fileName)) {
            DisplayForm();
        } else {
            Log.e(tag, "Couldn't parse the Form.");
            Builder bd = new Builder(this);
            AlertDialog ad = bd.create();
            ad.setTitle("Error");
            ad.setMessage("Could not parse the Form data");
            ad.show();
        }
    }

    private void createView() {
        Log.d(tag, "createView() : ");
        scrollView = new ScrollView(this);
        scrollView.setPadding((int) getResources().getDimension(R.dimen.forms_horizontal_margin),
                (int) getResources().getDimension(R.dimen.forms_vertical_margin),
                (int) getResources().getDimension(R.dimen.forms_horizontal_margin),
                (int) getResources().getDimension(R.dimen.forms_vertical_margin));
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(linearLayout);
        setContentView(scrollView);
    }

    private boolean DisplayForm() {
        Log.d(tag, "DisplayForm() : ");

        try {
            addFormsQuestionToLayout(linearLayout);
            // walk thru our form elements and dynamically create them, leveraging our mini library of tools.
            int i;
            for (i = 0; i < theForm.fields.size(); i++) {
                if (theForm.fields.elementAt(i).getType().equals("text")) {
                    theForm.fields.elementAt(i).obj = new XmlGuiEditBox(this, (theForm.fields.elementAt(i).isRequired() ? "*" : "") + theForm.fields.elementAt(i).getLabel(), "");
                    linearLayout.addView((View) theForm.fields.elementAt(i).obj);
                }
                if (theForm.fields.elementAt(i).getType().equals("numeric")) {
                    theForm.fields.elementAt(i).obj = new XmlGuiEditBox(this, (theForm.fields.elementAt(i).isRequired() ? "*" : "") + theForm.fields.elementAt(i).getLabel(), "");
                    ((XmlGuiEditBox) theForm.fields.elementAt(i).obj).makeNumeric();
                    linearLayout.addView((View) theForm.fields.elementAt(i).obj);
                }
                if (theForm.fields.elementAt(i).getType().equals("choice")) {
                    theForm.fields.elementAt(i).obj = new XmlGuiPickOne(this, (theForm.fields.elementAt(i).isRequired() ? "*" : "") + theForm.fields.elementAt(i).getLabel(), theForm.fields.elementAt(i).getOptions());
                    linearLayout.addView((View) theForm.fields.elementAt(i).obj);
                }
                if (theForm.fields.elementAt(i).getType().equals("checkbox")) {
                    theForm.fields.elementAt(i).obj = new XmlGuiCheckBox(this, (theForm.fields.elementAt(i).isRequired() ? "*" : "") + theForm.fields.elementAt(i).getLabel(), theForm.fields.elementAt(i).getOptions());
                    linearLayout.addView((View) theForm.fields.elementAt(i).obj);
                }
            }

            Button btn = new Button(this);
            btn.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            linearLayout.addView(btn);

            btn.setText("Next");
            btn.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    // check if this form is Valid
                    if (!CheckForm()) {
                        Builder bd = new Builder(linearLayout.getContext());
                        AlertDialog ad = bd.create();
                        ad.setTitle("Error");
                        ad.setMessage("Please enter all required (*) fields");
                        ad.show();
                        return;

                    }
                    if (theForm.getSubmitTo().equals("loopback")) {
                        // just display the results to the screen
                        String formResults = theForm.getFormattedResults();
//                        Log.i(tag, formResults);
                        Builder bd = new Builder(linearLayout.getContext());
                        AlertDialog ad = bd.create();
                        ad.setTitle("Results");
                        ad.setMessage(formResults);
                        ad.show();
                        return;

                    } else {
                        if (!SubmitForm()) {
                            Builder bd = new Builder(linearLayout.getContext());
                            AlertDialog ad = bd.create();
                            ad.setTitle("Error");
                            ad.setMessage("Error submitting form");
                            ad.show();
                            return;
                        }
                    }
                }
            });

            setTitle(theForm.getFormName());
            return true;
        } catch (Exception e) {
            Log.e(tag, "Error Displaying Form");
            return false;
        }
    }

    private void addFormsQuestionToLayout(LinearLayout ll) {
        TextView tv = new TextView(this);
        tv.setText(theForm.getFormQuestion());
        tv.setTextSize(16);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 16, 0, 16);
        ll.addView(tv, layoutParams);
    }


    private boolean SubmitForm() {
        try {
            boolean ok = true;
            this.progressDialog = ProgressDialog.show(this, theForm.getFormName(), "Saving Form Data", true, false);
            this.progressHandler = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    // process incoming messages here
                    switch (msg.what) {
                        case 0:
                            // update progress bar
                            progressDialog.setMessage("" + (String) msg.obj);
                            break;
                        case 1:
                            progressDialog.cancel();
                            finish();
                            break;
                        case 2:
                            progressDialog.cancel();
                            break;
                    }
                    super.handleMessage(msg);
                }

            };

            Thread workthread = new Thread(new TransmitFormData(theForm));

            workthread.start();

            return ok;
        } catch (Exception e) {
            Log.e(tag, "Error in SubmitForm()::" + e.getMessage());
            e.printStackTrace();
            // tell user we failed....
            Message msg = new Message();
            msg.what = 1;
            this.progressHandler.sendMessage(msg);

            return false;
        }

    }

    private boolean CheckForm() {
        try {
            int i;
            boolean good = true;
            for (i = 0; i < theForm.fields.size(); i++) {
                String fieldValue = (String) theForm.fields.elementAt(i).getData();
                Log.i(tag, theForm.fields.elementAt(i).getName() + " is [" + fieldValue + "]");
                if (theForm.fields.elementAt(i).isRequired()) {
                    if (fieldValue == null) {
                        good = false;
                    } else {
                        if (fieldValue.trim().length() == 0) {
                            good = false;
                        }
                    }

                }
            }
            return good;
        } catch (Exception e) {
            Log.e(tag, "Error in CheckForm()::" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void addFormPagetoLayout(Node formPage) {
        Log.d(tag, "id:" + formPage.getAttributes().getNamedItem("id").getNodeValue() + ", question:" + formPage.getAttributes().getNamedItem("question").getNodeValue());

        NodeList fieldsList = formPage.getChildNodes();


        Log.d(tag, "getLength:" + fieldsList.getLength());


        for (int i = 0; i < fieldsList.getLength(); i++) {
            Node fieldNode = fieldsList.item(i);
            NamedNodeMap attr = fieldNode.getAttributes();
            XmlGuiFormField tempField = new XmlGuiFormField();
            tempField.setName(attr.getNamedItem("name").getNodeValue());
            tempField.setLabel(attr.getNamedItem("label").getNodeValue());
            tempField.setType(attr.getNamedItem("type").getNodeValue());
            if (attr.getNamedItem("required").getNodeValue().equals("Y"))
                tempField.setRequired(true);
            else
                tempField.setRequired(false);
            tempField.setOptions(attr.getNamedItem("options").getNodeValue());

            theForm.getFields().add(tempField);
        }
    }

    private boolean parseFormData(String fileName) {
        try {
            Log.d(tag, "ProcessForm");
            Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( getAssets().open(fileName));


            Element root = dom.getDocumentElement();
            NodeList formsList = root.getElementsByTagName("form");
            if (formsList.getLength() < 1) {
                // nothing here??
                Log.e(tag, "No form, let's bail");
                return false;
            }
            Node form = formsList.item(0);

//            for (int i = 0; i < formsList.getLength(); i++) {
//                Node formPageNode = formsList.item(i);
//                addFormPagetoLayout(formPageNode);
//
//            }

            theForm = new XmlGuiForm();
            // process form level
            NamedNodeMap map = form.getAttributes();
            theForm.setFormNumber(map.getNamedItem("id").getNodeValue());
            theForm.setFormName(root.getAttribute("title"));
            theForm.setFormQuestion(map.getNamedItem("question").getNodeValue());
            if (root.getAttribute("submitTo") != null)
                theForm.setSubmitTo(root.getAttribute("submitTo"));
            else
                theForm.setSubmitTo("loopback");


            // now process the fields
            NodeList fieldsList = root.getElementsByTagName("field");
            for (int i = 0; i < fieldsList.getLength(); i++) {
                Node fieldNode = fieldsList.item(i);
                NamedNodeMap attr = fieldNode.getAttributes();
                XmlGuiFormField tempField = new XmlGuiFormField();
                tempField.setName(attr.getNamedItem("name").getNodeValue());
                tempField.setLabel(attr.getNamedItem("label").getNodeValue());
                tempField.setType(attr.getNamedItem("type").getNodeValue());
                if (attr.getNamedItem("required").getNodeValue().equals("Y"))
                    tempField.setRequired(true);
                else
                    tempField.setRequired(false);
                tempField.setOptions(attr.getNamedItem("options").getNodeValue());

                theForm.getFields().add(tempField);
            }

//            Log.i(tag, theForm.toString());
            return true;
        } catch (Exception e) {
            Log.e(tag, "Error occurred in ProcessForm:" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    private boolean GetFormData(String formNumber) {
        try {
            Log.i(tag, "ProcessForm");
            URL url = new URL("http://servername/xmlgui" + formNumber + ".xml");
            Log.i(tag, url.toString());
            InputStream is = url.openConnection().getInputStream();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            Document dom = db.parse(is);
            Element root = dom.getDocumentElement();
            NodeList forms = root.getElementsByTagName("form");
            if (forms.getLength() < 1) {
                // nothing here??
                Log.e(tag, "No form, let's bail");
                return false;
            }
            Node form = forms.item(0);
            theForm = new XmlGuiForm();

            // process form level
            NamedNodeMap map = form.getAttributes();
            theForm.setFormNumber(map.getNamedItem("id").getNodeValue());
            theForm.setFormName(map.getNamedItem("name").getNodeValue());
            if (map.getNamedItem("submitTo") != null)
                theForm.setSubmitTo(map.getNamedItem("submitTo").getNodeValue());
            else
                theForm.setSubmitTo("loopback");

            // now process the fields
            NodeList fields = root.getElementsByTagName("field");
            for (int i = 0; i < fields.getLength(); i++) {
                Node fieldNode = fields.item(i);
                NamedNodeMap attr = fieldNode.getAttributes();
                XmlGuiFormField tempField = new XmlGuiFormField();
                tempField.setName(attr.getNamedItem("name").getNodeValue());
                tempField.setLabel(attr.getNamedItem("label").getNodeValue());
                tempField.setType(attr.getNamedItem("type").getNodeValue());
                if (attr.getNamedItem("required").getNodeValue().equals("Y"))
                    tempField.setRequired(true);
                else
                    tempField.setRequired(false);
                tempField.setOptions(attr.getNamedItem("options").getNodeValue());
                theForm.getFields().add(tempField);
            }

            Log.i(tag, theForm.toString());
            return true;
        } catch (Exception e) {
            Log.e(tag, "Error occurred in ProcessForm:" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private class TransmitFormData implements Runnable {
        XmlGuiForm _form;
        Message msg;

        TransmitFormData(XmlGuiForm form) {
            this._form = form;
        }

        public void run() {

            try {
                msg = new Message();
                msg.what = 0;
                msg.obj = ("Connecting to Server");
                progressHandler.sendMessage(msg);

                URL url = new URL(_form.getSubmitTo());
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                BufferedOutputStream wr = new BufferedOutputStream(conn.getOutputStream());
                String data = _form.getFormEncodedData();
                wr.write(data.getBytes());
                wr.flush();
                wr.close();

                msg = new Message();
                msg.what = 0;
                msg.obj = ("Data Sent");
                progressHandler.sendMessage(msg);

                // Get the response
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = "";
                Boolean bSuccess = false;
                while ((line = rd.readLine()) != null) {
                    if (line.indexOf("SUCCESS") != -1) {
                        bSuccess = true;
                    }
                    // Process line...
//                    Log.v(tag, line);
                }
                wr.close();
                rd.close();

                if (bSuccess) {
                    msg = new Message();
                    msg.what = 0;
                    msg.obj = ("Form Submitted Successfully");
                    progressHandler.sendMessage(msg);

                    msg = new Message();
                    msg.what = 1;
                    progressHandler.sendMessage(msg);
                    return;

                }
            } catch (Exception e) {
                Log.d(tag, "Failed to send form data: " + e.getMessage());
                msg = new Message();
                msg.what = 0;
                msg.obj = ("Error Sending Form Data");
                progressHandler.sendMessage(msg);
            }
            msg = new Message();
            msg.what = 2;
            progressHandler.sendMessage(msg);
        }
    }
} 