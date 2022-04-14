package ru.mirea.lugovoy.practice2.independentwork;

import android.app.DatePickerDialog;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class MyDateDialogFragment extends DialogFragment
{
    Calendar time = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day)
        {
            time.set(Calendar.YEAR,year);
            time.set(Calendar.MONTH,month);
            time.set(Calendar.DAY_OF_MONTH,day);
        }
    };
}
