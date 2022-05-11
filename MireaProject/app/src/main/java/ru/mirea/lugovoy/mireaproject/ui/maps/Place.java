package ru.mirea.lugovoy.mireaproject.ui.maps;

import com.yandex.mapkit.geometry.Point;

public class Place
{
    private Point point;
    private String address;

    private String name;
    private String establishment;

    public Place(String name, String address, String establishment, Point point)
    {
        this.name = name;
        this.address = address;
        this.establishment = establishment;
        this.point = point;
    }

    public Point getPoint()
    {
        return this.point;
    }

    public String getAddress()
    {
        return this.address;
    }

    public String getEstablishment()
    {
        return this.establishment;
    }

    public String getName()
    {
        return this.name;
    }
}
