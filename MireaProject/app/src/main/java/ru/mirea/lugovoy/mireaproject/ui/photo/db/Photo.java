package ru.mirea.lugovoy.mireaproject.ui.photo.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Photo
{
    public Photo(byte[] photo)
    {
        this.photo = photo;
    }

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] photo;
}
