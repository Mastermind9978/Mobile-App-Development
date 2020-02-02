package com.example.mapproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    TextView Longitude;
    TextView Latitude;
    TextView Address;
    TextView Distance;
    Button button;
    LocationManager locationManager;
    LocationListener locationListener;
    double lat;
    double lon;
    float dis;
    Geocoder geocoder;
    List<Address> addresses;
    List<Location> locations;
    String strAdd = "";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        Latitude = findViewById(R.id.Latitude);
        Longitude = findViewById(R.id.Longitude);
        Address = findViewById(R.id.Address);
        Distance = findViewById(R.id.Distance);
        button = findViewById(R.id.button);
        locations = new ArrayList<>();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dis = 0;
                Distance.setText(""+dis);
                locations.clear();
            }
        });
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Log.d("hello", String.valueOf(location));
                    lon = location.getLongitude();
                    lat = location.getLatitude();
                    Latitude.setText("" + lat);
                    Longitude.setText("" + lon);

                    geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    addresses = geocoder.getFromLocation(lat, lon, 1);
                    Address.setText(addresses.get(0).getAddressLine(0));

                    locations.add(location);

                    if(locations.size()>1){
                        dis += location.distanceTo(locations.get(locations.size()-2));
                        Distance.setText(String.valueOf(dis));
                    }



                } catch (SecurityException E) {
                    E.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (Exception Ee) {
                    Ee.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        try {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 2000, 2, locationListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CONTACTS)) {

            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            }
        } else {

        }
    }
}

