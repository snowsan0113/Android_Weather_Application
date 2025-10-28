package snowsan0113.weather_app.android.listener;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import snowsan0113.weather_app.android.R;

public class TabClickListener implements TabLayout.OnTabSelectedListener {

    private Activity activity;
    private FusedLocationProviderClient fusedLocationClient;

    public TabClickListener(Activity activity) {
        this.activity = activity;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            //現在位置
            LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0)
                    .setMaxUpdates(1)
                    .build();

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        1000);
            }

            Log.d("a", "select");
            fusedLocationClient.requestLocationUpdates(locationRequest,  new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    for (Location location : locationResult.getLocations()) {
                        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(
                                    location.getLatitude(),
                                    location.getLongitude(),
                                    1
                            );
                            tab.setText(addresses.get(0).getAdminArea());
                            Log.d("a", "select1");
                        } catch (IOException e) {
                            Log.d("a", "select2");
                            AlertDialog builder = new AlertDialog.Builder(activity)
                                    .setMessage("位置情報を取得できませんでした")
                                    .setPositiveButton("再取得", (dialogInterface, i) -> {

                                    })
                                    .setNegativeButton("閉じる", (dialogInterface, i) -> {

                                    }).create();
                            builder.show();
                            tab.setText("失敗");
                            break;
                        }

                    }
                }
            }, Looper.getMainLooper());
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
