package com.example.sensorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SensorsDetailsActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensorLight;
    private Sensor sensorGravity;
    private TextView sensorLightTextView;
    private TextView sensorGravityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors_details);

        sensorLightTextView = findViewById(R.id.light_sensor_details_text);
        sensorGravityTextView = findViewById(R.id.temperature_sensor_details_text);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorGravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        if(sensorLight==null){
            sensorLightTextView.setText(R.string.sensor_missing);
        }
        if(sensorGravity ==null){
            sensorGravityTextView.setText(R.string.sensor_missing);
        }
    }
    @Override
    protected void onStart(){
        super.onStart();
        if(sensorLight != null){
            sensorManager.registerListener(this,sensorLight,SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(sensorGravity != null){
            sensorManager.registerListener(this, sensorGravity,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        int sensorType = sensorEvent.sensor.getType();
        float currentValue = sensorEvent.values[0];

        switch (sensorType){
            case Sensor.TYPE_LIGHT:
                sensorLightTextView.setText(getResources().getString(R.string.light_sensor_label, currentValue));
                break;
            case Sensor.TYPE_GRAVITY:
                sensorGravityTextView.setText(getResources().getString(R.string.gravity_sensor_label, currentValue, sensorEvent.values[1]));
                break;
            default:
        }
    }
    //called at the start of activity
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.i("DETAILS", "ACCURACY CHANGES");
    }

    @Override
    protected void onStop(){
        super.onStop();
        sensorManager.unregisterListener(this);
    }
}