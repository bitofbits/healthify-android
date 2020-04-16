package com.example.healthify;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthify.ui.dashboard.DashboardFragment;
import com.example.healthify.ui.home.HomeFragment;
import com.example.healthify.ui.notifications.NotificationsFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import Model.Customer;

public class CustomerHome extends AppCompatActivity{
    public boolean activeOrder = false;
    private String emailAddress;
    Fragment homeFragment;
    Fragment dashboardFragment;
    Fragment notificationFragment;
    Fragment selectedFragment;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private Bundle mBundle = new Bundle();
    int PERMISSION_ID = 44;
    TextView latTextView;
    FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        ImageButton imageButton = (ImageButton)findViewById(R.id.setCurrentLocationButton);
        latTextView = (TextView) findViewById(R.id.setCurrentLocationText);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });

        emailAddress = getIntent().getStringExtra("user_email");
        Customer.db.collection("Order").whereEqualTo("customer_email", emailAddress).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    if(task.getResult().isEmpty())
                    {
                        Toast.makeText(getApplicationContext(),"isEmpty",Toast.LENGTH_SHORT).show();
                        activeOrder = false;
                    }
                    else
                    {
                        activeOrder = true;
                    }
                    Log.v("CustomerHome", "Order Found");
                    mBundle.putString("user_email", emailAddress);
                    mBundle.putBoolean("activeOrder", activeOrder);
                    InitFragments();
//                    homeFragment.setArguments(mBundle);
                    System.out.println("tasksuccessfulklk ke andar----------------111111111 : " + activeOrder);
                }
                else{
                    Log.v("CustomerHome", "Order NOT Found");//wrong log
                    activeOrder = false;
                    mBundle.putString("user_email", emailAddress);
                    mBundle.putBoolean("activeOrder", activeOrder);
                    InitFragments();
                }
            }
        });

        System.out.println("IN CUSTOMER HOME ACTIVE ORDER------------2222222222222222 : " + activeOrder);

        //Initialize and populate BottomNavView
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener);

    }

    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    latTextView.setText(location.getLatitude()+" " + location.getLongitude());
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latTextView.setText(mLastLocation.getLatitude()+" " + mLastLocation.getLongitude());
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        //Define Botttom Nav View click action
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            if (item.getItemId() == R.id.navigation_home) {
                System.out.println("Should be just before HomeFragment entering -------");
                fragmentManager.beginTransaction().hide(selectedFragment).show(homeFragment).commit();
                selectedFragment = homeFragment;
                Log.v("NavigationHome", "Navigation Home");
            }
            else if (item.getItemId() == R.id.navigation_dashboard) {
                fragmentManager.beginTransaction().hide(selectedFragment).show(dashboardFragment).commit();
                selectedFragment = dashboardFragment;
                Log.v("NavigationDashboard", "Navigation Dashboard");
            }
            else {
                fragmentManager.beginTransaction().hide(selectedFragment).show(notificationFragment).commit();
                selectedFragment = notificationFragment;
                Log.v("NavigationNotifications", "Navigation Notifications");
            }
            return true;
        }
    };
    private void InitFragments()
    {
        //Initialize all fragments and hide it except the HomeFragment
        homeFragment = new HomeFragment();
        System.out.println("Inside InitFragments with "+this+ "home : "+homeFragment);
        dashboardFragment = new DashboardFragment();
        notificationFragment = new NotificationsFragment();
        homeFragment.setArguments(mBundle);
        dashboardFragment.setArguments(mBundle);

        selectedFragment = homeFragment;

        fragmentManager.beginTransaction().add(R.id.fragmentContainer, notificationFragment,
                "NotificationFragment").hide(notificationFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentContainer, dashboardFragment,
                "DashboardFragment").hide(dashboardFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragmentContainer, homeFragment,"HomeFragment").commit();
        System.out.println("Last in InitFragment func -------------------");

        if(activeOrder) {
            BottomNavigationView mBottomNavigationView = findViewById(R.id.nav_view);
            mBottomNavigationView.findViewById(R.id.navigation_dashboard).performClick();
        }
    }
}

