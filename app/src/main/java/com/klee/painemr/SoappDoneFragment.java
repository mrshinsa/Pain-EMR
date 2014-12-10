package com.klee.painemr;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.greenrobot.event.EventBus;

/**
 * A fragment with a Google +1 button.
 */
public class SoappDoneFragment extends Fragment {

    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://developer.android.com";

    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;

    private Button resultButton;


    public SoappDoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_soapp_done, container, false);

        //Find the +1 button
        resultButton = (Button) view.findViewById(R.id.see_result_button_id);
        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.d("SoappDoneFragment", "SoappDoneFragment onClick: ");
                EventBus.getDefault().postSticky( new SeeResultEvent( ));
            }
        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}
