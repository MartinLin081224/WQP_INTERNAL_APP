package com.example.a10609516.wqp_internal_app.Works;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import com.example.a10609516.wqp_internal_app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.fragment.app.FragmentActivity;

public class GuestMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String addressString, guest;

    private String LOG = "GuestMapsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(LOG, "onCreate");
        setContentView(R.layout.activity_eng_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
       /* Bundle bundle = getIntent().getExtras();
        String gps_location1 = bundle.getString("gps_location"+ 1).substring(6);
        String gps_location4 = bundle.getString("gps_location"+ 4).substring(7);
        Log.e(LOG, gps_location4);
        String gps_x = gps_location4.substring(0, gps_location4.indexOf(","));
        String gps_y = gps_location4.substring(gps_location4.indexOf(",") + 1, gps_location4.length());
        Log.e(LOG, gps_x);
        Log.e(LOG, gps_y);
        double x= Double.parseDouble(gps_x);
        double y= Double.parseDouble(gps_y);*/

        mMap = googleMap;

        //接收MissionReportActivity傳過來的值
        Bundle bundle = getIntent().getExtras();
        addressString = bundle.getString("LOCATION").trim();
        guest = bundle.getString("GUEST").trim();
        //addressString = "台北市大安區仁愛路四段85號";

        Geocoder geoCoder = new Geocoder(GuestMapsActivity.this, Locale.getDefault());
        List<Address> addressLocation = null;
        try {
            addressLocation = geoCoder.getFromLocationName(addressString, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        double latitude = addressLocation.get(0).getLatitude();
        double longitude = addressLocation.get(0).getLongitude();

        Log.e(LOG, "GPS : " + latitude + "," + longitude);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("客編 : " + guest));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setMyLocationEnabled(true); // 右上角的定位功能；這行會出現紅色底線，不過仍可正常編譯執行
        mMap.getUiSettings().setZoomControlsEnabled(true);  // 右下角的放大縮小功能
        mMap.getUiSettings().setCompassEnabled(true);       // 左上角的指南針，要兩指旋轉才會出現
        mMap.getUiSettings().setMapToolbarEnabled(true);    // 右下角的導覽及開啟 Google Map功能

        Log.d(LOG, "最高放大層級："+mMap.getMaxZoomLevel());
        Log.d(LOG, "最低放大層級："+mMap.getMinZoomLevel());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));     // 放大地圖到 16 倍大
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(LOG, "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(LOG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(LOG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(LOG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(LOG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(LOG, "onDestroy");
    }
}