package ru.mirea.lugovoy.mireaproject.ui.stories.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Story
{
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String subject;
    public String text;
}
