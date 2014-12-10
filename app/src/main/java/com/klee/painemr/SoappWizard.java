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


public class SoappWizard extends FragmentActivity {
    private static final String CURRENT_STEP = "current_step";
    private static final String TAG = "SoappWizard";

    static private int currentStep;
    static final int NUM_ITEMS = 24;
    static private int[] pollResults = new int[NUM_ITEMS];
    static private String[] descriptionArray = {
            "1/24\nHow often do you have mood swings?",
            "2/24\nHow often have you felt a need for higher doses of medication to treat your pain?",
            "3/24\nHow often have you felt impatient with your doctors?",
            "4/24\nHow often have you felt that things are just too overwhelming that you can't handle them?",
            "5/24\nHow often is there tension in the home?",
            "6/24\nHow often have you counted pain pills to see how many are remaining?",
            "7/24\nHow often have you been concerned that people will judge you for taking pain medication?",
            "8/24\nHow often do you feel bored?",
            "9/24\nHow often have you taken more pain medication than you were supposed to?",
            "10/24\nHow often have you worried about being left alone?",
            "11/24\nHow often have you felt a craving for medication? ",
            "12/24\nHow often have others expressed concern over your use of medication?",
            "13/24\nHow often have any of your close friends had a problem with alcohol or drugs?",
            "14/24\nHow often have others told you that you had a bad temper?",
            "15/24\nHow often have you felt consumed by the need to get pain medication?",
            "16/24\nHow often have you run out of pain medication early?",
            "17/24\nHow often have others kept you from getting what you deserve?",
            "18/24\nHow often, in your lifetime, have you had legal problems or been arrested? ",
            "19/24\nHow often have you attended an AA or NA meeting?",
            "20/24\nHow often have you been in an argument that was so out of control that someone got hurt?",
            "21/24\nHow often have you been sexually abused?",
            "22/24\nHow often have others suggested that you have a drug or alcohol problem? ",
            "23/24\nHow often have you had to borrow pain medications from your family or friends?",
            "24/24\nHow often have you been treated for an alcohol or drug problem?"};

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
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
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


//    private void loadStep() {
//        switch (currentStep) {
//            case 0:
//                goToNextStep();
//                break;
//            case 1:
//                goToNextStep();
//                break;
//            case 2:
//                goToNextStep();
//                break;
//            case 3:
//                goToNextStep();
//                break;
//        }
//    }
//
//
//    private void goToNextStep() {
//
//        final SoappFragment soappFragment = new SoappFragment();
//
//        final Bundle bundle = new Bundle();
//        bundle.putString("description", descriptionArray[currentStep]);
//        soappFragment.setArguments(bundle);
//
//        getFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, soappFragment)
//                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                .commit();
//    }

//    private void goToFourthStep() {
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container, new Step4Fragment(), Step4Fragment.class.getName())
//                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                .commit();
//    }
//
//    private void loadThirdStep() {
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container, new Step3Fragment(), Step3Fragment.class.getName())
//                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                .addToBackStack(null)
//                .commit();
//    }
//
//    private void loadSecondStep() {
//
//        final Step2Fragment step2 = new Step2Fragment();
//
//        if(address != null && address.length() > 0) {
//            final Bundle bundle = new Bundle();
//            bundle.putString(Constants.Extras.KEY_ADDRESS_ONE, address.toString());
//            step2.setArguments(bundle);
//        }
//
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container, step2, Step2Fragment.class.getName())
//                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                .addToBackStack(null)
//                .commit();
//    }
//
//    private void loadFirstStep() {
//
//        final Step1Fragment step1 = new Step1Fragment();
//
//        if(firstName != null && firstName.length() > 0 && lastName != null && lastName.length() > 0) {
//            final Bundle bundle = new Bundle();
//            bundle.putString(Constants.Extras.KEY_FIRST_NAME, firstName.toString());
//            bundle.putString(Constants.Extras.KEY_LAST_NAME, lastName.toString());
//            step1.setArguments(bundle);
//        }
//
//        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//
//        ft.replace(R.id.container, step1, Step1Fragment.class.getName())
//                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                .commit();
//    }

}
