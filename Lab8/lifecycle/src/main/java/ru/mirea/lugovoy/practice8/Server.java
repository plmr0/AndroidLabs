package ru.mirea.lugovoy.practice8;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class Server implements LifecycleObserver
{
    private String TAG = "LIFECYCLE";

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void connect()
    {
        Log.d(TAG,"connect to web-server");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void disconnect()
    {
        Log.d(TAG,"disconnect");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    public void onAny()
    {
        Log.d(TAG,"ON_ANY");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate()
    {
        Log.d(TAG,"ON_CREATE");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume()
    {
        Log.d(TAG,"ON_RESUME");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause()
    {
        Log.d(TAG,"ON_PAUSE");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy()
    {
        Log.d(TAG,"ON_DESTROY");
    }
}
