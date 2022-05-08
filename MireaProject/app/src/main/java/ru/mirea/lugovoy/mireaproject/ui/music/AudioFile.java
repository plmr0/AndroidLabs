package ru.mirea.lugovoy.mireaproject.ui.music;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

public class AudioFile
{
    private String author;
    private String title;

    private int id;

    private Uri uri;

    private Bitmap image;

    private Context context;

    public AudioFile(Context context, int id)
    {
        this.context = context;
        this.id = id;

        getMetadata();
    }

    private void getMetadata()
    {
        this.uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + this.id);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(this.context, this.uri);

        this.title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        this.author = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

        byte[] data = mmr.getEmbeddedPicture();

        if (data != null)
        {
            this.image = BitmapFactory.decodeByteArray(data, 0, data.length);
        }
    }

    public String getAuthor()
    {
        return this.author;
    }

    public String getTitle()
    {
        return this.title;
    }

    public int getId()
    {
        return this.id;
    }

    public Uri getUri()
    {
        return this.uri;
    }

    public Bitmap getImage()
    {
        return this.image;
    }
}
