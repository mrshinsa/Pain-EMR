package com.klee.painemr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import de.greenrobot.event.EventBus;

/**
 * Created by klee on 11/12/2014.
 */
public class CommWizard extends FragmentActivity {
    private static final String CURRENT_STEP = "current_step";
    private static final String TAG = "CommWizard";

    static private int currentStep;
    static final int NUM_ITEMS = 17;
    static private int[] pollResults = new int[NUM_ITEMS];
    static private String[] descriptionArray = {
            "1/17\nHow often have you had trouble with thinking clearly or had memory problems?",
            "2/17\nHow often do people complain that you are not completing necessary tasks? (i.e., doing things that need to be done, such as going to class, work, or appointments) ",
            "3/17\nHow often have you had to go to someone other than your prescribing physician to get sufficient pain relief from your medications? (i.e/17\nanother doctor, the Emergency Room) ",
            "4/17\nHow often have you taken your medications differently from how they are prescribed?",
            "5/17\nHow often have you seriously thought about hurting yourself?",
            "6/17\nHow much of your time was spent thinking about op oid medications? (having enough, taking them, dosing schedule, etc.)",
            "7/17\nHow often have you been in an argument?",
            "8/17\nHow often have you had trouble controlling your anger? (e.g., road rage, screaming, etc)",
            "9/17\nHow often have you needed to take pain medications belonging to someone else?",
            "10/17\nHow often have you been worried about how you're handling your medications?",
            "11/17\nHow often have others been worried about how you're handling your medications?",
            "12/17\nHow often have you had to make an emergency phone call or show up at the clinic without an appointment?",
            "13/17\nHow often have you gotten angry with people?",
            "14/17\nHow often have you had to take more of your medication than prescribed?",
            "15/17\nHow often have you borrowed pain medication from someone else?",
            "16/17\nHow often have you used your pain medicine for symptoms other than for pain? (e.g/17\nto help you sleep, improve your mood, or relieve stress)",
            "17/17\nHow often have you had to visit the Emergency Room?",
    };

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_soap);

        // Create the detail fragment and add it to the activity
        // using a fragment transaction.
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
//                    actionBar.setSelectedNavigationItem(position);
            }
        });

        if (savedInstanceState != null) {
            currentStep = savedInstanceState.getInt(CURRENT_STEP);
            mViewPager.setCurrentItem(savedInstanceState.getInt(CURRENT_STEP));
        } else {
            currentStep = 0;
        }
        EventBus.getDefault().registerSticky(this);
    }

    public void onEvent(PollSelectionEvent pollSelectionEvent) {
        if (null == pollSelectionEvent)
            return;
        pollResults[pollSelectionEvent.getIndex()] = pollSelectionEvent.getSelection();


        Log.d(TAG, "currentStep: " + pollSelectionEvent.getIndex() + ", getSelection: " + pollSelectionEvent.getSelection());
    }

    public void onEvent(SeeResultEvent seeResultEvent) {
        if (null == seeResultEvent)
            return;

        Log.d(TAG, "seeResultEvent: ");
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("RESULT_ARRAY", pollResults);

        startActivity(intent);

        return;

    }


    @Override
    protected void onResume() {
        super.onResume();
//        bus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        bus.unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.registration_wizard, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_STEP, currentStep);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
//
//    @Subscribe
//    public void onNextStepEvent(NextStepEvent event) {
//
//        switch (currentStep) {
//            case 0:
//                this.firstName = event.getFirstNameText();
//                this.lastName = event.getLastNameText();
//                break;
//            case 1:
//                this.address = event.getAddress();
//                break;
//        }
//
//        currentStep++;
//        loadStep();
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        currentStep--;
//        loadStep();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case NUM_ITEMS:
                    Log.d(TAG, "NUM_ITEMS: " + NUM_ITEMS);
                    // The other sections of the app are dummy placeholders.
                    SoappDoneFragment fragment = new SoappDoneFragment();
                    Bundle args = new Bundle();
                    args.putIntArray("RESULT_ARRAY", pollResults);
                    fragment.setArguments(args);
                    return fragment;

                default:
                    Log.d(TAG, "i: " + i);
                    // The other sections of the app are dummy placeholders.
                    SoappFragment soappFragment = new SoappFragment();
                    Bundle soappArgs = new Bundle();

                    soappArgs.putInt("INDEX", i);
                    soappArgs.putString("DESCRIPTION", descriptionArray[i]);
                    soappFragment.setArguments(soappArgs);

                    currentStep = i;
                    return soappFragment;
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS + 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Question " + (position + 1);
        }
    }
}
