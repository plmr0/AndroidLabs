package ru.mirea.lugovoy.mireaproject.ui.maps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.yandex.mapkit.geometry.Point;

public class PointLiveData
{
    private static MutableLiveData<Point> data = new MutableLiveData<>();

    public static LiveData<Point> getPoint()
    {
        data.setValue(new Point());
        return data;
    }

    public static void setPoint(Point point)
    {
        data.setValue(point);
    }
}
