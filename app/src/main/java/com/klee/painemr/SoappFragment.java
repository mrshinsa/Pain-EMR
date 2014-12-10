package com.klee.painemr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import de.greenrobot.event.EventBus;

/**
 * A placeholder fragment containing a simple view.
 */
public class SoappFragment extends Fragment {
    private int soappIndex;
    private TextView description;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;

    public SoappFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_soapp, container, false);
        Bundle args = getArguments();
        soappIndex = args.getInt("INDEX");
        ((TextView) rootView.findViewById(R.id.descriptionId)).setText(
                (args.getString("DESCRIPTION")));
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
//        Injector.inject(this);
        description = (TextView) getView().findViewById(R.id.descriptionId);

        if (getArguments() != null && getArguments().containsKey("description")) {
            description.setText(getArguments().getString("description"));
        }
        addListenerOnButton();
    }

    public void addListenerOnButton() {
        radioSexGroup = (RadioGroup) getView().findViewById(R.id.radio_button_selction_id);
        radioSexGroup.setOnCheckedChangeListener((new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // get selected radio button from radioGroup
                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                radioSexButton = (RadioButton) getView().findViewById(selectedId);
                Toast.makeText(getActivity(),
                        radioSexButton.getText(), Toast.LENGTH_SHORT).show();
                ((ImageView) getView().findViewById(R.id.next_item_id)).setVisibility(View.VISIBLE);
                EventBus.getDefault().postSticky( new PollSelectionEvent(soappIndex, Integer.parseInt(radioSexButton.getText().toString())));
            }
        }));
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

