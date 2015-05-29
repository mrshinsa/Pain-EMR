package com.klee.painemr;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

/**
 * Created by Kevin on 2015-01-05.
 */
public class PainApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
//        Parse.enableLocalDatastore(this);
//
//        Parse.initialize(this, "EPIXsBUiHHbnh4sZYPkwHzEZ97zNvBfSq2ZSs8SI", "OQyvVmwW9OJNErpsQ7YPr4EZLxEl8h7QvaZij1Uh");
    }
}
