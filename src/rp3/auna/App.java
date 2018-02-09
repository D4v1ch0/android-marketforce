package rp3.auna;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;


import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jesus Villa on 21/10/2017.
 */

public class App extends Application {
    private static final String TAG = App.class.getSimpleName();
    public boolean wasInBackground = true;
    private Timer mActivityTransitionTimer;
    private TimerTask mActivityTransitionTimerTask;
    private final long MAX_ACTIVITY_TRANSITION_TIME_MS = 2000;  // Time allowed for transitions

    public App() {
        super();
        Log.d(TAG,"App...");
    }

    Application.ActivityLifecycleCallbacks activityCallbacks = new Application.ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.d(TAG,"App...");
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.d(TAG,"App...");
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Log.d(TAG,"onActivityResumed...");
            if (wasInBackground) {
                //Do app-wide came-here-from-background code
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Log.d(TAG,"onActivityPaused...");
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.d(TAG,"onActivityStopped...");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Log.d(TAG,"onActivitySaveInstanceState...");
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.d(TAG,"onActivityDestroyed...");
        }


    };

    @Override
    public void onCreate() {
        super.onCreate();
        //SugarContext.init(this);
        Log.d(TAG,"onCreate...");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //SugarContext.terminate();
        Log.d(TAG,"onTerminate...");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG,"onConfigurationChanged...");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG,"onLowMemory...");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.d(TAG,"onTrimMemory...");
        Log.d(TAG,"Level:"+level);
    }

    @Override
    public void registerComponentCallbacks(ComponentCallbacks callback) {
        super.registerComponentCallbacks(callback);
        Log.d(TAG,"registerComponentCallbacks...");
    }

    @Override
    public void unregisterComponentCallbacks(ComponentCallbacks callback) {
        super.unregisterComponentCallbacks(callback);
        Log.d(TAG,"unregisterComponentCallbacks...");
    }

    @Override
    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        super.registerActivityLifecycleCallbacks(activityCallbacks);
        Log.d(TAG,"registerActivityLifecycleCallbacks...");
    }

    @Override
    public void unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        super.unregisterActivityLifecycleCallbacks(callback);
        Log.d(TAG,"unregisterActivityLifecycleCallbacks...");
    }

    @Override
    public void registerOnProvideAssistDataListener(OnProvideAssistDataListener callback) {
        super.registerOnProvideAssistDataListener(callback);
        Log.d(TAG,"registerOnProvideAssistDataListener...");
    }

    @Override
    public void unregisterOnProvideAssistDataListener(OnProvideAssistDataListener callback) {
        super.unregisterOnProvideAssistDataListener(callback);
        Log.d(TAG,"unregisterOnProvideAssistDataListener...");
    }

}
