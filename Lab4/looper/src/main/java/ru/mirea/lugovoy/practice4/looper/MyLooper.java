package ru.mirea.lugovoy.practice4.looper;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MyLooper extends Thread
{
    private int number = 0;
    Handler handler;

    @SuppressLint("HandlerLeak")
    @Override
    public void run()
    {
        Log.d("MyLooper", "run");
        Looper.prepare();

        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                Bundle bundle = msg.getData();

                Log.d("MyLooper",
                        String.format("[%d]: %s, %s, %s",
                        number++,
                        bundle.getString("KEY"),
                        bundle.getString("WORK"),
                        bundle.getInt("AGE")));
            }
        };

        Looper.loop();
    }
}
