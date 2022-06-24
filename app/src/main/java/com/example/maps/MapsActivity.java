package com.example.maps;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


import com.example.maps.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;

import org.json.JSONObject;

import java.io.IOException;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private String[] permissoes = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET};
    private LocationManager locationManager;
    private LocationListener locationListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        Permissoes.validarPermissoes(permissoes, this, 1);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(@NonNull Location location) {

                Log.d("Localizacao", "onLocationChanged" + location.toString());

            }

        };

    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(@NonNull Location location) {
                double Latitude = location.getLatitude();
                double Longitude = location.getLongitude();
                mMap.clear();
                LatLng ifrn = new LatLng(-5.7895135028995774,-35.33840312982514);
                //-5.7895135028995774, -35.33840312982514
                LatLng minhalocalizacao = new LatLng(Latitude,Longitude);
                double distance = SphericalUtil.computeDistanceBetween(minhalocalizacao,ifrn);
                Log.i("Distancia","A distancia é: " + NumberFormat.getIntegerInstance().format(distance));

                mMap.addMarker(new MarkerOptions().position(minhalocalizacao).title("eu"));



                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> listaendereço = geocoder.getFromLocation(Latitude,Longitude,
                            1);
                    if (listaendereço != null && listaendereço.size() > 0){
                        Address endereço = listaendereço.get(0);
                        Log.d("endereço", "onLocationChanged: " + endereço.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates
                    (LocationManager.GPS_PROVIDER,
                            1000, 5, locationListener);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoresultado : grantResults) {
            if (permissaoresultado == PackageManager.PERMISSION_DENIED) {

                //ACEITA ISSO
                Toast.makeText(this, "Você precisa aceitar amigo",
                        Toast.LENGTH_SHORT).show();
                finish();

            } else if (permissaoresultado == PackageManager.PERMISSION_GRANTED) {
                //RECUPERARLOCALIZAÇÃO

                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates
                            (LocationManager.GPS_PROVIDER,
                            1000, 10, locationListener);
                }

            }

        }

    }
}
















