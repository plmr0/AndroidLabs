package ru.mirea.lugovoy.mireaproject.db;

import android.app.Application;

import androidx.room.Room;

import ru.mirea.lugovoy.mireaproject.ui.stories.db.AppDatabase;

public class App extends Application
{
    public static App instance;
    private AppDatabase database;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .allowMainThreadQueries()
                .build();
    }

    public static App getInstance()
    {
        return instance;
    }
    public AppDatabase getDatabase()
    {
        return database;
    }
}
