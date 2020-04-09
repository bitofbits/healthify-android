package com.example.healthify;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class CustomerHome extends AppCompatActivity
{
    final public Intent curr=getIntent();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //curr=getIntent();
        System.out.println("Called CustomerHome now!------------------ + "+this);
        setContentView(R.layout.activity_customer_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);


//        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
//        {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item)
//            {
//                Fragment selected=null;
//                switch (item.getItemId())
//                {
//                    case R.id.navigation_home:
//                        selected = new HomeFragment();
//                        break;
//                    case R.id.navigation_dashboard:
//                        selected = new DashboardFragment();
//                        break;
//                    case R.id.navigation_notifications:
//                        selected = new NotificationsFragment();
//                        break;
//                }
//                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,selected).commit();
//                return true;
//            }
//        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();*/
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }
}
