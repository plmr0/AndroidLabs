package ru.mirea.lugovoy.mireaproject.music;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class MusicService extends Service
{
    private final IBinder binder = new LocalBinder();

    private MediaPlayer mediaPlayer = new MediaPlayer();

    public class LocalBinder extends Binder
    {
        MusicService getService()
        {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return binder;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    public void play(AudioFile audioFile)
    {
        createMediaPlayer(audioFile.getUri());
        mediaPlayer.start();
    }

    public void pause()
    {
        if (this.mediaPlayer != null)
        {
            this.mediaPlayer.pause();
        }
    }

    public void proceed()
    {
        if (this.mediaPlayer != null)
        {
            this.mediaPlayer.start();
        }
    }

    public void stop()
    {
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer()
    {
        if (this.mediaPlayer != null)
        {
            this.mediaPlayer.reset();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
    }

    public void createMediaPlayer(Uri uri)
    {
        this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );

        try
        {
            this.mediaPlayer.setDataSource(getApplicationContext(), uri);
            this.mediaPlayer.prepare();
            this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                @Override
                public void onCompletion(MediaPlayer mp)
                {
                    releaseMediaPlayer();
                    stopSelf();
                    MusicFragment.setButtonPlay();
                }
            });

        }
        catch (IOException e)
        {
            e.getStackTrace();
        }
    }
}