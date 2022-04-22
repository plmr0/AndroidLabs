package ru.mirea.lugovoy.practice4.looper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;

public class MainActivity extends AppCompatActivity
{
    MyLooper myLooper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myLooper = new MyLooper();
        myLooper.start();
    }

    public void onClick(View view)
    {
        int age = 22;

        Message msg = new Message();
        Bundle bundle = new Bundle();

        bundle.putString("KEY", "mirea");
        bundle.putString("WORK", "student");
        bundle.putInt("AGE", age);

        msg.setData(bundle);

        synchronized (this)
        {
            try
            {
                wait(age * 1000);
            }
            catch (Exception ignored) { }
        }

        if (myLooper != null)
        {
            myLooper.handler.sendMessage(msg);
        }
    }
}