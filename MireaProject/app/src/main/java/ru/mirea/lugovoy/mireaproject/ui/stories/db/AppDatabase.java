package ru.mirea.lugovoy.mireaproject.ui.stories.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Story.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    public abstract StoryDao storyDao();
}
