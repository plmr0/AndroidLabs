package ru.mirea.lugovoy.mireaproject.ui.music;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import ru.mirea.lugovoy.mireaproject.ui.photo.BitmapUtility;

public class MusicFragment extends Fragment implements View.OnClickListener
{
    private static boolean isBound;
    private boolean isPaused;

    @SuppressLint("StaticFieldLeak") public static ImageButton previousButton;
    @SuppressLint("StaticFieldLeak") public static ImageButton statusButton;
    @SuppressLint("StaticFieldLeak") public static ImageButton nextButton;

    @SuppressLint("StaticFieldLeak") private static TextView author;
    @SuppressLint("StaticFieldLeak") private static TextView title;

    @SuppressLint("StaticFieldLeak") private static ImageView image;

    @SuppressLint("StaticFieldLeak") private static AudioFile currentTrack;

    private static Deque<AudioFile> deque = new LinkedList<>();

    public static MusicService musicService;

    private static ServiceConnection connection = new ServiceConnection()
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

    @SuppressLint("StaticFieldLeak") private static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = getContext();
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

        author = view.findViewById(R.id.authorName);
        title = view.findViewById(R.id.trackName);

        image = view.findViewById(R.id.albumImage);

        loadPreviousState(savedInstanceState);

        return view;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unbindService();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        Bitmap img = ((BitmapDrawable) image.getDrawable()).getBitmap();

        outState.putString("author", author.getText().toString());
        outState.putString("title", title.getText().toString());

        outState.putByteArray("image", BitmapUtility.getBytes(img));

        outState.putInt("button", (int) statusButton.getTag());

        super.onSaveInstanceState(outState);
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

    private void loadPreviousState(Bundle state)
    {
        loadSongs();

        if (state == null)
        {
            loadFirstSong();
            bindService();
        }
        else
        {
            title.setText(state.getString("title"));
            author.setText(state.getString("author"));
            image.setImageBitmap(BitmapUtility.getImage(state.getByteArray("image")));

            int button = state.getInt("button");
            if (button == R.drawable.ic_music_play)
            {
                setButtonPlay();
            }
            else if (button == R.drawable.ic_music_pause)
            {
                setButtonPause();
            }
        }
    }

    private void bindService()
    {
        Intent intent = new Intent(requireContext(), MusicService.class);
        requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private static void unbindService()
    {
        if (isBound)
        {
            context.unbindService(connection);
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

    public static void destroyService()
    {
        if (musicService != null)
        {
            musicService.stop();
        }
        unbindService();
    }
}