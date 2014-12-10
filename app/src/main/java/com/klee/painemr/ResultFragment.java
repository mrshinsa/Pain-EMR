package com.klee.painemr;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by klee on 11/5/2014.
 */
public class ResultFragment extends Fragment {
    private TextView description;

    public ResultFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);
        Bundle args = getArguments();
        TextView resultText =((TextView) rootView.findViewById(R.id.result_text_id));
        resultText.setText(getFormattedResultString(args.getIntArray("RESULT_ARRAY")));
        return rootView;
    }

    private String getFormattedResultString(int[] result_arrays) {
        String formattedResultString = "";
        for (int i=0; i< result_arrays.length; i++) {
            formattedResultString += i + ") " + result_arrays[i] + "\n" ;
        }
        return formattedResultString;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
//        Injector.inject(this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.next, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.:
////                final NextStepEvent next = new NextStepEvent(addressOne.getText());
////                bus.post(next);
////                break;
//            default:
//                return super.onOptionsItemSelected(item);
//        }

        return true;

    }
}

