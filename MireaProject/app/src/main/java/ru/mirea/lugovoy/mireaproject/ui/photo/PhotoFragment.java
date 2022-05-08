package ru.mirea.lugovoy.mireaproject.ui.photo;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

import ru.mirea.lugovoy.mireaproject.MainActivity;
import ru.mirea.lugovoy.mireaproject.R;
import ru.mirea.lugovoy.mireaproject.ui.photo.db.Photo;
import ru.mirea.lugovoy.mireaproject.ui.photo.db.PhotoDao;

public class PhotoFragment extends Fragment
{
    private final int REQUEST_CODE_PERMISSION_CAMERA = 100;
    private final int REQUEST_IMAGE_CAPTURE = 1;

    private LinearLayout linearLayout;
    private FloatingActionButton floatingActionButton;

    private PhotoDao photoDao;

    private boolean isWork = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        photoDao = MainActivity.db.photoDao();

        checkPermission();

        if (!this.isWork)
        {
            requestPermission();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);

        this.linearLayout = view.findViewById(R.id.photosLinearLayout);
        this.floatingActionButton = view.findViewById(R.id.test_addPhoto);

        this.floatingActionButton.setOnClickListener(this::addPhoto);

        loadData();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            byte[] bytes = BitmapUtility.getBytes(imageBitmap);

            photoDao.insert(new Photo(bytes));
        }

        loadData();
    }

    private void checkPermission()
    {
        int cameraPermissionStatus = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA);
        this.isWork = cameraPermissionStatus == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[] { Manifest.permission.CAMERA },
                REQUEST_CODE_PERMISSION_CAMERA);
    }

    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try
        {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        catch (ActivityNotFoundException ignored) { }
    }

    private void addPhoto(View view)
    {
        checkPermission();

        if (this.isWork)
        {
            dispatchTakePictureIntent();
        }
        else
        {
            Toast.makeText(getActivity(), getText(R.string.photo_camera_no_access), Toast.LENGTH_SHORT).show();
            requestPermission();
        }
    }

    private void clearView()
    {
        this.linearLayout.removeAllViewsInLayout();
    }

    public void loadData()
    {
        clearView();

        List<Photo> photos = photoDao.getAll();
        Collections.reverse(photos);

        for (Photo photo : photos)
        {
            Bitmap bitmap = BitmapUtility.getImage(photo.photo);

            ImageView imageView = new ImageView(requireContext());
            imageView.setImageBitmap(bitmap);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(1000, 1000);
            layoutParams.setMargins(0, 10, 0, 10);
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;

            imageView.setLayoutParams(layoutParams);

            this.linearLayout.addView(imageView);
        }
    }
}