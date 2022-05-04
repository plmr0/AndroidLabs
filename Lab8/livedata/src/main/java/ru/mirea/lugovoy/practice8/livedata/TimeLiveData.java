package ru.mirea.lugovoy.practice8.livedata;

import android.annotation.SuppressLint;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeLiveData
{
    private static MutableLiveData<Long> data = new MutableLiveData<Long>();

    static LiveData<Long> getTime()
    {
        data.setValue(new Date().getTime());
        return data;
    }

    static void setTime()
    {
        data.setValue(new Date().getTime());
    }

    @SuppressLint("SimpleDateFormat")
    private static LiveData<String> getStringTime = Transformations.map(data, input ->
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(calendar.getTime());
    });

    static LiveData<String> getDate()
    {
        return getStringTime;
    }
}