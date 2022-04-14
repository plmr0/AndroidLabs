package ru.mirea.lugovoy.practice2.independentwork;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickTimeDialog(View view)
    {
        MyTimeDialogFragment timeDialogFragment = new MyTimeDialogFragment();
        new TimePickerDialog(this,
                timeDialogFragment.onTimeSetListener,
                timeDialogFragment.time.get(Calendar.HOUR_OF_DAY),
                timeDialogFragment.time.get(Calendar.MINUTE),
                true)
                .show();
    }

    public void onClickDateDialog(View view)
    {
        MyDateDialogFragment dateDialogFragment = new MyDateDialogFragment();
        new DatePickerDialog(this,
                dateDialogFragment.dateSetListener,
                dateDialogFragment.time.get(Calendar.YEAR),
                dateDialogFragment.time.get(Calendar.MONTH),
                dateDialogFragment.time.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void onClickProgressDialog(View view)
    {
        // Устарело
        ProgressDialog mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Processing...");
        mProgressDialog.setButton(Dialog.BUTTON_NEGATIVE, "Close", (DialogInterface.OnClickListener) (dialog, which) -> { });
        mProgressDialog.show();
    }
}