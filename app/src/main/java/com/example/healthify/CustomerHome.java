package com.example.healthify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthify.ui.dashboard.DashboardFragment;
import com.example.healthify.ui.home.HomeFragment;
import com.example.healthify.ui.notifications.NotificationsFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import Model.Customer;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import Model.Customer;

public class CustomerHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
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
    DrawerLayout drawerLayout;
    Customer det = new Customer();
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        emailAddress = getIntent().getStringExtra("user_email");
        Customer.db.collection("Order").whereEqualTo("customer_email", emailAddress).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    if(task.getResult().isEmpty())
                    {
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
        Customer.db.collection("Customer").document(emailAddress).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                TextView navDrawerCustomerName = findViewById(R.id.nav_header_textView);
                Customer customer = task.getResult().toObject(Customer.class);
                navDrawerCustomerName.setText(customer.getName());
                System.out.println("customer.getName()" + customer.getName());
            }
        });


        //Initialize and populate BottomNavView
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener);
        NavigationView navigationView = findViewById(R.id.nav_drawer);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        System.out.println("cyka");
        switch (item.getItemId()) {
            case R.id.drawer_about_us:
                startActivity(new Intent(this, AboutUs.class), mBundle);
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.drawer_profile:
                intent = new Intent(getApplication(),Profile_UpdatePage.class);
                intent.putExtra("username",getIntent().getStringExtra("user_email"));
                intent.putExtra("CustomerType", "Customer");
                System.out.println("BEFORE-----------------------");
                startActivity(intent);
                System.out.println("AFTER-----------------------");
                break;
            case R.id.drawer_pick_place:
                intent = new Intent(getApplication(),PickPlace.class);
                intent.putExtra("user_email",getIntent().getStringExtra("user_email"));
                intent.putExtra("signupType", "Customer");
                System.out.println("BEFORE-----------------------");
                startActivity(intent);
                System.out.println("AFTER-----------------------");
                break;
            case R.id.drawer_search_place:
                intent = new Intent(getApplication(),Autocomplete.class);
                intent.putExtra("user_email",getIntent().getStringExtra("user_email"));
                intent.putExtra("signupType", "Customer");
                System.out.println("BEFORE-----------------------");
                startActivity(intent);
                System.out.println("AFTER-----------------------");
                break;
            case R.id.drawer_logout:
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
                sharedPreferences.edit();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                intent = new Intent(getApplication(), login.class);
                startActivity(intent);
                finish();

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

//    private void getLastLocation(){
//        if (checkPermissions()) {
//            if (isLocationEnabled()) {
//                mFusedLocationClient.getLastLocation().addOnCompleteListener(
//                        new OnCompleteListener<Location>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Location> task) {
//                                Location location = task.getResult();
//                                if (location == null) {
//                                    requestNewLocationData();
//                                } else {
//                                    latTextView.setText(location.getLatitude()+" " + location.getLongitude());
//                                }
//                            }
//                        }
//                );
//            } else {
//                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
//            }
//        } else {
//            requestPermissions();
//        }
//    }
//    @SuppressLint("MissingPermission")
//    private void requestNewLocationData(){
//
//        LocationRequest mLocationRequest = new LocationRequest();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(0);
//        mLocationRequest.setFastestInterval(0);
//        mLocationRequest.setNumUpdates(1);
//
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        mFusedLocationClient.requestLocationUpdates(
//                mLocationRequest, mLocationCallback,
//                Looper.myLooper()
//        );
//
//    }
//    private LocationCallback mLocationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            Location mLastLocation = locationResult.getLastLocation();
//            latTextView.setText(mLastLocation.getLatitude()+" " + mLastLocation.getLongitude());
//        }
//    };
//
//    private boolean checkPermissions() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        return false;
//    }
//
//    private void requestPermissions() {
//        ActivityCompat.requestPermissions(
//                this,
//                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
//                PERMISSION_ID
//        );
//    }
//
//    private boolean isLocationEnabled() {
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
//                LocationManager.NETWORK_PROVIDER
//        );
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_ID) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getLastLocation();
//            }
//        }
//    }
//
//    @Override
//    public void onResume(){
//        super.onResume();
//        if (checkPermissions()) {
//            getLastLocation();
//        }
//
//    }


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
        notificationFragment.setArguments(mBundle);
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

