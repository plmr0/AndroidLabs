package ru.mirea.lugovoy.practice3.lifecycleactivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity
{
    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG,"onCreate()");
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.i(TAG,"onStart()");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        Log.i(TAG,"onSaveInstanceState()");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG,"onRestoreInstanceState()");
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState)
    {
        super.onPostCreate(savedInstanceState, persistentState);
        Log.i(TAG,"onPostCreate()");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.i(TAG,"onResume()");
    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();
        Log.i(TAG,"onPostResume()");
    }

    @Override
    public void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        Log.i(TAG,"onAttachedToWindow()");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.i(TAG,"onPause()");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState)
    {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.i(TAG,"onSaveInstanceState()");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.i(TAG,"onStop()");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.i(TAG,"onDestroy()");
    }

    @Override
    public void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        Log.i(TAG,"onDetachedFromWindow()");
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        Log.i(TAG,"onRestart()");
    }
}