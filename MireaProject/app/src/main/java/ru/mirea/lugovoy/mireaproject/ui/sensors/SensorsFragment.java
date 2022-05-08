package ru.mirea.lugovoy.mireaproject.ui.sensors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.mirea.lugovoy.mireaproject.R;

public class SensorsFragment extends Fragment implements SensorEventListener
{
    private SensorManager sensorManager;

    private Sensor pressureSensor;
    private Sensor lightSensor;
    private Sensor magneticFieldSensor;

    private TextView pressure;
    private TextView light;
    private TextView magneticField;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);

        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

        magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, magneticFieldSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);

        this.pressure = (TextView) view.findViewById(R.id.textViewPressure);
        this.light = (TextView) view.findViewById(R.id.textViewLight);
        this.magneticField = (TextView) view.findViewById(R.id.textViewMagneticField);

        setDefault();

        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        int eventType = event.sensor.getType();
        float value = event.values[0];

        switch (eventType)
        {
            case Sensor.TYPE_PRESSURE:
                setPressure(value);
                break;
            case Sensor.TYPE_LIGHT:
                setLight(value);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                setMagneticField(value);
                break;
            default:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }

    private String getStringResource(int id)
    {
        return getResources().getString(id);
    }

    @SuppressLint("SetTextI18n")
    private void setDefault()
    {
        String pressure = getStringResource(R.string.sensor_pressure);
        String light = getStringResource(R.string.sensor_light);
        String magnetic = getStringResource(R.string.sensor_magnetic_field);

        this.pressure.setText(String.format("%s --- hPa", pressure));
        this.light.setText(String.format("%s --- lx", light));
        this.magneticField.setText(String.format("%s --- μT", magnetic));
    }

    @SuppressLint("DefaultLocale")
    private void setPressure(float data)
    {
        String text = getStringResource(R.string.sensor_pressure);
        this.pressure.setText(String.format("%s %f hPa", text, data));
    }

    @SuppressLint("DefaultLocale")
    private void setLight(float data)
    {
        String text = getStringResource(R.string.sensor_light);
        this.light.setText(String.format("%s %f lx", text, data));
    }

    @SuppressLint("DefaultLocale")
    private void setMagneticField(float data)
    {
        String text = getStringResource(R.string.sensor_magnetic_field);
        this.magneticField.setText(String.format("%s %f μT", text, data));
    }
}