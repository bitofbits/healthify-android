package com.example.healthify.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthify.CustomerHome;
import com.example.healthify.DeliveryPartnerHome;
import com.example.healthify.R;
import com.example.healthify.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import Model.Customer;

public class DashboardFragment extends Fragment
{

    private DashboardViewModel dashboardViewModel;
    private boolean activeOrder;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Button cancelButton = (Button) root.findViewById(R.id.cancelButtonDashboardFragment);
        TextView emailTextView = root.findViewById(R.id.textUpperDashboardFragment);
        TextView activeOrderTextView = root.findViewById(R.id.textLowerDashboardFragment);

        activeOrder = getArguments().getBoolean("activeOrder");
        emailTextView.setText(getArguments().getString("user_email"));
        activeOrderTextView.setText("Currently Active Order:" + new Boolean(activeOrder).toString());

        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Query orderQuery = Customer.db.collection("Order").whereEqualTo("customer_email",
                        getArguments().getString("user_email"));
                orderQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().isEmpty()) {
                            }
                            else {
                                Toast.makeText(getContext(),"Deleting Order...",Toast.LENGTH_SHORT).show();
                                for (DocumentSnapshot document : task.getResult()) {
                                    Customer.db.collection("Order").document(document.getId()).delete();
                                    setActiveOrder(false);
                                    resetTextView();
//                                    HomeFragment homeFragment = (HomeFragment) getActivity().getSupportFragmentManager().findFragmentByTag("HomeFragment");
//
//                                    homeFragment = new HomeFragment();
//                                    Bundle mBundle = getArguments();
//                                    mBundle.putBoolean("activeOrder", false);
//                                    homeFragment.setArguments(mBundle);
//                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
//                                            homeFragment,"HomeFragment").commit();
//
//                                    BottomNavigationView mBottomNavigationView = getActivity().findViewById(R.id.nav_view);
//                                    mBottomNavigationView.findViewById(R.id.navigation_home).performClick();
//                                    System.out.println("get arguments" + getArguments());
                                    Intent i = new Intent(getContext(), CustomerHome.class);
                                    i.putExtra("user_email",getArguments().getString("user_email"));
                                    startActivity(i);
                                }
                            }
                        }
                        else{
                                Log.v("CancelButtonDashboard", "Didn't find order");
                        }
                    }
                });
            }
        });
        return root;
    }
    public void setActiveOrder(boolean activeOrder){
        this.activeOrder = activeOrder;
    }
    public void resetTextView(){
        TextView activeOrderTextView = getActivity().findViewById(R.id.textLowerDashboardFragment);
        activeOrderTextView.setText("Currently Active Order:" + new Boolean(this.activeOrder).toString());
    }

}