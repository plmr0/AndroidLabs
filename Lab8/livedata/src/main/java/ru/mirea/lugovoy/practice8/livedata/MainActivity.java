package ru.mirea.lugovoy.practice8.livedata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Observer<String>
{
    private TextView networkNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkNameTextView = findViewById(R.id.textView);

        TimeLiveData.getDate().observe(this, this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                TimeLiveData.setTime();
            }
        }, 5000);
    }

    @Override
    public void onChanged(String s)
    {
        networkNameTextView.setText(s);
    }
}
