package ru.mirea.lugovoy.mireaproject.recorder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import ru.mirea.lugovoy.mireaproject.R;
import ru.mirea.lugovoy.mireaproject.music.MusicService;

public class RecorderFragment extends Fragment
{
    private static final int REQUEST_CODE_PERMISSION = 100;
    
    private final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
    
    private boolean isWork;
    private boolean isBound;

    @SuppressLint("StaticFieldLeak") public static Button startRecordingButton;
    @SuppressLint("StaticFieldLeak") public static Button stopRecordingButton;
    @SuppressLint("StaticFieldLeak") public static Button startPlayingButton;
    @SuppressLint("StaticFieldLeak") public static Button stopPlayingButton;

    private TextView textView;

    private MediaRecorder mediaRecorder;
    
    private File audioFile;

    private MusicService musicService;

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

        bindService();

        this.isWork = hasPermissions(requireContext(), PERMISSIONS);

        if (!this.isWork)
        {
            ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_recorder, container, false);

        this.textView = (TextView) view.findViewById(R.id.audio_record_text);

        startRecordingButton = (Button) view.findViewById(R.id.start_recording_button);
        stopRecordingButton = (Button) view.findViewById(R.id.stop_recording_button);
        startPlayingButton = (Button) view.findViewById(R.id.start_playing_button);
        stopPlayingButton = (Button) view.findViewById(R.id.stop_playing_button);
        
        startRecordingButton.setOnClickListener(this::onRecordStart);
        stopRecordingButton.setOnClickListener(this::onRecordStop);
        startPlayingButton.setOnClickListener(this::onRecordStartPlaying);
        stopPlayingButton.setOnClickListener(this::onRecordStopPlaying);

        startPlayingButton.setEnabled(false);
        stopPlayingButton.setEnabled(false);
        stopRecordingButton.setEnabled(false);

        this.mediaRecorder = new MediaRecorder();

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION)
        {
            this.isWork = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
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
        Intent intent = new Intent(requireContext(), MusicService.class);
        requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private void unbindService()
    {
        if (isBound)
        {
            requireActivity().unbindService(connection);
            isBound = false;
        }
    }

    public static boolean hasPermissions(Context context, String... permissions)
    {
        if (context != null && permissions != null)
        {
            for (String permission: permissions)
            {
                if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED)
                {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    private void onRecordStart(View view)
    {
        startRecordingButton.setEnabled(false);
        stopRecordingButton.setEnabled(true);
        startPlayingButton.setEnabled(false);
        stopPlayingButton.setEnabled(false);

        stopRecordingButton.requestFocus();

        try
        {
            startRecording();
        }
        catch (IOException ignored) { }
    }

    private void onRecordStop(View view)
    {
        startRecordingButton.setEnabled(true);
        stopRecordingButton.setEnabled(false);
        startPlayingButton.setEnabled(true);
        stopPlayingButton.setEnabled(false);

        startRecordingButton.requestFocus();

        stopRecording();
        processAudioFile();
    }

    private void onRecordStartPlaying(View view)
    {
        startRecordingButton.setEnabled(false);
        stopRecordingButton.setEnabled(false);
        startPlayingButton.setEnabled(false);
        stopPlayingButton.setEnabled(true);

        try
        {
            this.musicService.play(new FileInputStream(this.audioFile).getFD());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void onRecordStopPlaying(View view)
    {
        startRecordingButton.setEnabled(true);
        stopRecordingButton.setEnabled(false);
        startPlayingButton.setEnabled(true);
        stopPlayingButton.setEnabled(false);

        this.musicService.stop();
    }

    private void startRecording() throws IOException
    {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
        {
            this.mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            this.mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            this.mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            if (this.audioFile == null)
            {
                this.audioFile = new File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC), "mirea.3gp");
            }

            this.mediaRecorder.setOutputFile(this.audioFile.getAbsolutePath());

            this.mediaRecorder.prepare();
            this.mediaRecorder.start();
        }
    }

    private void stopRecording()
    {
        if (this.mediaRecorder != null)
        {
            this.mediaRecorder.stop();
            this.mediaRecorder.reset();
        }
    }

    @SuppressLint("SetTextI18n")
    private void processAudioFile()
    {
        long current = System.currentTimeMillis();

        ContentValues values = new ContentValues(4);

        values.put(MediaStore.Audio.Media.TITLE, "audio" + this.audioFile.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
        values.put(MediaStore.Audio.Media.DATA, this.audioFile.getAbsolutePath());

        ContentResolver contentResolver = requireActivity().getContentResolver();

        Uri baseUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(baseUri, values);

        this.textView.setText(this.audioFile.getAbsolutePath() + "/" + this.audioFile.getName());

        requireActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
    }
}