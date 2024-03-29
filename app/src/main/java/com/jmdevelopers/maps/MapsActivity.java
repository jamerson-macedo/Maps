package com.jmdevelopers.maps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button button;
    private Address address;
    private List<Address> listaendereco;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String[] permisoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        button = findViewById(R.id.buttom);


        Permissoes.validarPermissoes(permisoes, this, 1);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // validar as permissoes
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng atual = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(atual, 19));


                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        try {
                            mMap.clear();

                            listaendereco = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                            if (listaendereco != null & listaendereco.size() > 0) {
                                // pegando o primeiro resultado
                                address = listaendereco.get(0);
                                address.getAddressLine(0);
                                final LatLng atual = new LatLng(latLng.latitude, latLng.longitude);
                                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(atual, 20.2f));
                                mMap.addMarker(new MarkerOptions().position(atual).title(address.getAddressLine(0))).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        double putlatitude = atual.latitude;
                                        double putllogintude = atual.longitude;
                                        String putaddres = address.getAddressLine(0);
                                        Intent intent = new Intent(v.getContext(), MainActivity.class);
                                        intent.putExtra("latitude", putlatitude);
                                        intent.putExtra("longitude", putllogintude);
                                        intent.putExtra("addres", putaddres);
                                        startActivity(intent);
                                        finish();


                                    }
                                });


                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                });


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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, locationListener);
            if (locationManager != null) {
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            mMap.setMyLocationEnabled(true);
        }

        // Add a marker in Sydney and move the camera

        mMap.setMapType(mMap.MAP_TYPE_NORMAL); // Here is where you set the map type
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int permisaoresultado : grantResults) {
            if (permisaoresultado == PackageManager.PERMISSION_DENIED) {
// ALERTA

            } else if (permisaoresultado == PackageManager.PERMISSION_GRANTED) {
                // define o provedor
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, locationListener);

                }


            }

        }
    }
}
