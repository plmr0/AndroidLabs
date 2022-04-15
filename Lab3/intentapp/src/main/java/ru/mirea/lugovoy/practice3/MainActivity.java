package ru.mirea.lugovoy.practice3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SuppressLint("SimpleDateFormat")
    public void onClickOpenSecondActivity(View view)
    {
        String format = "yyyy-MM-dd HH:mm:ss";

        long dateInMillis = System.currentTimeMillis();
        final SimpleDateFormat sdf = new SimpleDateFormat(format);

        String dateString = sdf.format(new Date(dateInMillis));

        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("dateString", dateString);

        startActivity(intent);
    }
}