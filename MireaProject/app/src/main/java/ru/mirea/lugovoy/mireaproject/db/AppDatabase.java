package ru.mirea.lugovoy.mireaproject.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.mirea.lugovoy.mireaproject.ui.photo.db.Photo;
import ru.mirea.lugovoy.mireaproject.ui.photo.db.PhotoDao;
import ru.mirea.lugovoy.mireaproject.ui.stories.db.Story;
import ru.mirea.lugovoy.mireaproject.ui.stories.db.StoryDao;

@Database(entities = {Story.class, Photo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    public abstract StoryDao storyDao();
    public abstract PhotoDao photoDao();
}
