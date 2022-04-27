package ru.mirea.lugovoy.mireaproject.camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import ru.mirea.lugovoy.mireaproject.R;

public class CameraFragment extends Fragment implements View.OnClickListener
{
    private static final int REQUEST_CODE_PERMISSION_CAMERA = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private boolean isWork = false;

    private String currentPhotoPath;

    private ImageView photo;

    private Button takePhotoBtn;

    private Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        this.photo = view.findViewById(R.id.photo);

        this.takePhotoBtn = view.findViewById(R.id.take_photo);

        this.takePhotoBtn.setOnClickListener(this);

        int cameraPermissionStatus = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (cameraPermissionStatus == PackageManager.PERMISSION_GRANTED && storagePermissionStatus == PackageManager.PERMISSION_GRANTED)
        {
            this.isWork = true;
        }
        else
        {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    REQUEST_CODE_PERMISSION_CAMERA);
        }

        return view;
    }

    @SuppressLint("SimpleDateFormat")
    private File createImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                timeStamp,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    public void takePhoto()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try
        {
            photoFile = createImageFile();
        }
        catch (IOException ignored) { }

        if (photoFile != null)
        {
            this.imageUri = FileProvider.getUriForFile(requireContext(), Objects.requireNonNull(this.getClass().getPackage()).getName() + ".Provider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, this.imageUri);

            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void setPhoto()
    {
        int targetW = photo.getWidth();
        int targetH = photo.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        photo.setImageBitmap(bitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK)
        {
            setPhoto();
            Toast.makeText(getActivity(), String.format("Saved to %s", this.currentPhotoPath), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION_CAMERA)
        {
            isWork = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.take_photo)
        {
            takePhoto();
        }
    }
}