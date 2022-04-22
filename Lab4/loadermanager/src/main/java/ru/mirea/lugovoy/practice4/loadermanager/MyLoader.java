package ru.mirea.lugovoy.practice4.loadermanager;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;

import java.util.concurrent.ThreadLocalRandom;

public class MyLoader extends AsyncTaskLoader<String>
{
    private String editText;
    public static final String ARG_WORD = "word";
    private StringBuffer word;
    private String result;
    private int count;
    private int max;

    public MyLoader(@NonNull Context context, Bundle args)
    {
        super(context);

        if (args != null)
        {
            editText = args.getString(ARG_WORD);
        }
    }

    @Override
    protected void onStartLoading()
    {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground()
    {
        result = "";

        word = new StringBuffer(editText);
        max = word.length();

        for (int i = 0; i < max; i++)
        {
            count = ThreadLocalRandom.current().nextInt(word.length());
            result += word.charAt(count);
            word.deleteCharAt(count);
        }

        return result;
    }
}
