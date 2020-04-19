package com.example.healthify;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;

import Model.BaseFirestore;
import Model.Customer;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Use the place picker functionality inside of the Places Plugin, to show UI for
 * choosing a map location. Once selected, return to the previous location with a
 * CarmenFeature to extract information from for whatever use that you want.
 */
public class PickPlace extends AppCompatActivity {

    private static final int REQUEST_CODE = 5678;
    FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        requestLocationPermission();

    }
    public void pickerActivity(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            startActivityForResult(
                                    new PlacePicker.IntentBuilder()
                                            .accessToken(getString(R.string.access_token))
                                            .placeOptions(PlacePickerOptions.builder()
                                                    .statingCameraPosition(new CameraPosition.Builder()
                                                            .target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(16).build())
                                                    .build())
                                            .build(PickPlace.this), REQUEST_CODE);
                        }
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            Button goToPickerActivityButton = findViewById(R.id.go_to_picker_button);
//            goToPickerActivityButton.setVisibility(View.VISIBLE);
        } else if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            CarmenFeature carmenFeature = PlacePicker.getPlace(data);

            if (carmenFeature != null) {
                System.out.println("CarmernO/P" + carmenFeature.toJson());
                BaseFirestore.db.collection((getIntent().getStringExtra("signupType"))).document(getIntent().getStringExtra("user_email"))
                        .update("latitude", carmenFeature.center().latitude());
                BaseFirestore.db.collection((getIntent().getStringExtra("signupType"))).document(getIntent().getStringExtra("user_email"))
                        .update("longitude", carmenFeature.center().longitude());
                BaseFirestore.db.collection((getIntent().getStringExtra("signupType"))).document(getIntent().getStringExtra("user_email"))
                        .update("address", carmenFeature.placeName());

            }
        }

        finish();
        Intent intent;
        if(getIntent().getStringExtra("signupType") == "Customer"){
            intent = new Intent(PickPlace.this, CustomerHome.class);
            intent.putExtra("user_email", getIntent().getStringExtra("user_email"));
        }
        else{
            intent = new Intent(PickPlace.this, DeliveryPartnerHome.class);
            intent.putExtra("user_name", getIntent().getStringExtra("user_email"));
        }
        startActivity(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(REQUEST_CODE)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
            pickerActivity();
        }
        else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_CODE, perms);
        }
    }
}