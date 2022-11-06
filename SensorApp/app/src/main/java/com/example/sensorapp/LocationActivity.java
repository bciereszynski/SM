package com.example.sensorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LocationActivity extends AppCompatActivity {

    private Button getLocationButton;
    private final int REQUEST_LOCATION_PERMISION = 1;

    private Location lastLocation;
    private TextView locationTextView;
    private FusedLocationProviderClient fusedLocationClient;

    private TextView addressTextView;
    private Button getAddressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        locationTextView = findViewById(R.id.location_text);
        getLocationButton = findViewById(R.id.location_button);
        fusedLocationClient= LocationServices.getFusedLocationProviderClient(this);

        addressTextView = findViewById(R.id.address_text);
        getAddressButton = findViewById(R.id.address_button);

        getLocationButton.setOnClickListener(v->getLocation());
        getAddressButton.setOnClickListener(v->executeGeocoding());


    }
    private String LocationGeocoding(Context context, Location location){

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        String resultMessage = "";
        try{
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);

        }catch (IOException ioException){
            resultMessage= context.getString(R.string.service_not_available);
            Log.e("ERROR", resultMessage, ioException);
        }
        if (addresses == null || addresses.isEmpty()) {
            if(resultMessage.isEmpty()){
                resultMessage = context.getString(R.string.no_address);
                Log.e("ERROR", resultMessage);
            }
        }
        else{
            Address address = addresses.get(0);
            List<String> addressParts = new ArrayList<>();

            for(int i = 0; i<= address.getMaxAddressLineIndex(); i++){
                addressParts.add(address.getAddressLine(i));
            }
            resultMessage= TextUtils.join("\n", addressParts);
        }
        return resultMessage;
    }

    private void executeGeocoding(){
        if(lastLocation!=null){
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<String> returnedAddress = executor.submit(()->LocationGeocoding(getApplicationContext(),lastLocation));
            try{
                String result = returnedAddress.get();
                addressTextView.setText(getString(R.string.address_text,result,System.currentTimeMillis()));
            }
            catch (ExecutionException|InterruptedException e){
                Log.e("ERROR", e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
        }
    }

    private void getLocation(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISION);
        }
        else{
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if(location!=null){
                    lastLocation=location;
                    locationTextView.setText(
                            getString(R.string.location_text,
                                    location.getLatitude(),
                                    location.getLongitude(),
                                    location.getTime())
                    );
                }
                else{
                    locationTextView.setText(R.string.no_location);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch(requestCode){
            case REQUEST_LOCATION_PERMISION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getLocation();
                }else{
                    Toast.makeText(this, R.string.location_permission_denied,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}