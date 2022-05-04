package ru.mirea.lugovoy.practice8.viewmodel;

import android.os.Handler;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProgressViewModel extends ViewModel
{
    private static MutableLiveData<Boolean> isShowProgress = new MutableLiveData<>();

    void showProgress()
    {
        isShowProgress.postValue(true);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                isShowProgress.postValue(false);
            }
        }, 10000);
    }

    MutableLiveData<Boolean> getProgressState()
    {
        return isShowProgress;
    }
}
