package com.example.sddservertest;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 80066158 on 2017-04-01.
 */

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    private static MyApplication instance = null;
    private List<Activity> activityList = null;

    public synchronized static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }

        return instance;
    }

    public void addActivity(Activity activity) {
        if (null == activity) {
            return;
        }

        if (null == activityList) {
            activityList = new ArrayList<>();
        }

        if (isActivityExist(activity)) {
            return;
        }
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (null == activity ||
                null == activityList) {
            return;
        }

        activityList.remove(activity);
    }

    public void exit() {
        Log.i(TAG, "<exit> start");

        if (null != activityList) {
            for (Activity activity : activityList) {
                activity.finish();
            }
        }

        System.exit(0);
    }

    private boolean isActivityExist(Activity activity) {
        if (null == activityList) {
            return false;
        }

        for (Activity temp : activityList) {
            if (temp == activity) {
                return true;
            }
        }

        return false;
    }
}
