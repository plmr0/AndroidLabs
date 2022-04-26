package ru.mirea.lugovoy.mireaproject.music;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Deque;
import java.util.LinkedList;

import ru.mirea.lugovoy.mireaproject.R;

public class MusicFragment extends Fragment implements View.OnClickListener
{
    private boolean isBound;
    private boolean isPaused;

    @SuppressLint("StaticFieldLeak") public static ImageButton previousButton;
    @SuppressLint("StaticFieldLeak") public static ImageButton statusButton;
    @SuppressLint("StaticFieldLeak") public static ImageButton nextButton;

    @SuppressLint("StaticFieldLeak") private static TextView author;
    @SuppressLint("StaticFieldLeak") private static TextView title;

    @SuppressLint("StaticFieldLeak") private static ImageView image;

    @SuppressLint("StaticFieldLeak") private static AudioFile currentTrack;

    private static Deque<AudioFile> deque = new LinkedList<>();

    private static MusicService musicService;

    private ServiceConnection connection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            isBound = false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        View view = getView();
        TextView author = (TextView) view.findViewById(R.id.authorName);
        TextView track = (TextView) view.findViewById(R.id.trackName);
        ImageView image = (ImageView) view.findViewById(R.id.albumImage);

        outState.putString("author", author.getText().toString());
        outState.putString("track", track.getText().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_music, container, false);

        previousButton = view.findViewById(R.id.previousTrackButton);
        previousButton.setOnClickListener(this);

        statusButton = view.findViewById(R.id.trackStatusButton);
        statusButton.setTag(R.drawable.ic_music_play);
        statusButton.setOnClickListener(this);

        nextButton = view.findViewById(R.id.nextTrackButton);
        nextButton.setOnClickListener(this);

        author = (TextView) view.findViewById(R.id.authorName);
        title = (TextView) view.findViewById(R.id.trackName);

        image = (ImageView) view.findViewById(R.id.albumImage);

        loadSongs();
        loadFirstSong();
        bindService();

        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.previousTrackButton:
                setPreviousSong();
                playCurrentTrack();
                break;
            case R.id.trackStatusButton:
                statusButtonClicked();
                break;
            case R.id.nextTrackButton:
                setNextSong();
                playCurrentTrack();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unbindService();
        this.isBound = false;
    }

    private void bindService()
    {
        Intent intent = new Intent(getContext(), MusicService.class);
        getContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private void unbindService()
    {
        if (isBound)
        {
            getContext().unbindService(connection);
            isBound = false;
        }
    }

    private void loadSongs()
    {
        final Class<R.raw> c = R.raw.class;
        final Field[] fields = c.getDeclaredFields();

        for (Field field : fields)
        {
            try
            {
                deque.add(new AudioFile(getContext(), field.getInt(R.raw.class)));
            }
            catch (Exception ignored) { }
        }
    }

    private static void setSongData(AudioFile audioFile)
    {
        title.setText(audioFile.getTitle());
        author.setText(audioFile.getAuthor());
        image.setImageBitmap(audioFile.getImage());
    }

    private void loadFirstSong()
    {
        currentTrack = deque.getFirst();
        setSongData(currentTrack);
    }

    private void statusButtonClicked()
    {
        int source = (Integer) statusButton.getTag();

        if (source == R.drawable.ic_music_play)
        {
            setButtonPause();

            if (isPaused)
            {
                musicService.proceed();
                isPaused = false;
            }
            else
            {
                playCurrentTrack();
            }
        }
        else if (source == R.drawable.ic_music_pause)
        {
            setButtonPlay();

            musicService.pause();
            this.isPaused = true;
        }
    }

    private static void playCurrentTrack()
    {
        musicService.play(currentTrack);
        setButtonPause();
    }

    private static void setNextSong()
    {
        setButtonPlay();
        musicService.stop();
        deque.add(deque.pollFirst());
        currentTrack = deque.getFirst();
        setSongData(currentTrack);
    }

    private static void setPreviousSong()
    {
        setButtonPlay();
        musicService.stop();
        deque.addFirst(deque.pollLast());
        currentTrack = deque.getLast();
        setSongData(currentTrack);
    }

    public static void setButtonPlay()
    {
        statusButton.setImageResource(R.drawable.ic_music_play);
        statusButton.setTag(R.drawable.ic_music_play);
    }

    public static void setButtonPause()
    {
        statusButton.setImageResource(R.drawable.ic_music_pause);
        statusButton.setTag(R.drawable.ic_music_pause);
    }
}