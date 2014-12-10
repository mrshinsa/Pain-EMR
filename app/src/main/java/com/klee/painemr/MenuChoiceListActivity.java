package com.klee.painemr;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;


/**
 * An activity representing a list of Menu Choices. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MenuChoiceDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link MenuChoiceListFragment} and the item details
 * (if present) is a {@link MenuChoiceDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link MenuChoiceListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class MenuChoiceListActivity extends Activity
        implements MenuChoiceListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuchoice_list);

        if (findViewById(R.id.menuchoice_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((MenuChoiceListFragment) getFragmentManager()
                    .findFragmentById(R.id.menuchoice_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link MenuChoiceListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(MenuChoiceDetailFragment.ARG_ITEM_ID, id);
            MenuChoiceDetailFragment fragment = new MenuChoiceDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.menuchoice_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Log.d("MenuChoiceListActivity", "onItemSelected(): " + id);
            if (id.equals("5")) {
                Intent settingsIntent = new Intent(this, SoappWizard.class);
                startActivity(settingsIntent);
            } else if (id.equals("6")) {
                Intent settingsIntent = new Intent(this, CommWizard.class);
                startActivity(settingsIntent);
            } else if (id.equals("7")) {
                Intent settingsIntent = new Intent(this, FormGeneratorExample.class);
                startActivity(settingsIntent);
            } else if (id.equals("10")) {
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
            } else {
                Intent detailIntent = new Intent(this, MenuChoiceDetailActivity.class);
                detailIntent.putExtra(MenuChoiceDetailFragment.ARG_ITEM_ID, id);
                startActivity(detailIntent);
            }
        }
    }
}
