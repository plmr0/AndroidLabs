package ru.mirea.lugovoy.laboratory1;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import ru.mirea.lugovoy.laboratory1.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener
{
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static final int REQUEST_CODE_PERMISSION = 100;
    private final String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    private boolean isWork;
    private ArrayList<MarkerOptions> markers = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setTrafficEnabled(true);
        isWork = hasPermissions(this, PERMISSIONS);

        if (!isWork)
        {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE_PERMISSION);
        }

        setUpMap();
        placeMarkers(markers);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng)
    {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(12).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        addNewMarker("Где я?", "Новое место", latLng);
    }

    private void placeMarkers(ArrayList<MarkerOptions> markers)
    {
        for (MarkerOptions marker: markers)
        {
            mMap.addMarker(marker);
        }
    }

    private void setUpMap()
    {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng mirea = new LatLng(55.670005, 37.479894);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(mirea).zoom(12).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        addNewMarker("МИРЭА", "Крупнейший политехнический ВУЗ", mirea);

        placeMarkers(markers);
    }

    private void addNewMarker(String title, String snippet, LatLng latLng)
    {
        MarkerOptions marker = new MarkerOptions().title(title).snippet(snippet).position(latLng);
        mMap.addMarker(marker);
        markers.add(marker);
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

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION)
        {
            isWork = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (isWork)
            {
                mMap.setMyLocationEnabled(true);
            }
        }
    }
}