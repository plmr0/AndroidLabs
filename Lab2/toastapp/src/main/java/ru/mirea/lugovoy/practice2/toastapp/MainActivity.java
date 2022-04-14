package ru.mirea.lugovoy.practice2.toastapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void BtnClick(View view)
    {
        Toast toast = Toast.makeText(getApplicationContext(), "Здравствуй MIREA! Lugovoy I.I.", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);

        ImageView catImageView = new ImageView(getApplicationContext());
        catImageView.setImageResource(R.drawable.ic_launcher_foreground);

        toast.show();
    }
}