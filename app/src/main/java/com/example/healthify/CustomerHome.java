package com.example.healthify;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.healthify.ui.dashboard.DashboardFragment;
import com.example.healthify.ui.home.HomeFragment;
import com.example.healthify.ui.notifications.NotificationsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import Model.Customer;

public class CustomerHome extends AppCompatActivity {
    public boolean activeOrder = false;
    private String emailAddress;
    Fragment homeFragment;
    Fragment dashboardFragment;
    Fragment notificationFragment;
    Fragment selectedFragment;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private Bundle mBundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_home);
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

