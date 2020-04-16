package com.example.healthify.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthify.Confirmation;
import com.example.healthify.CustomerHome;
import com.example.healthify.R;
import com.example.healthify.ui.dashboard.DashboardFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import Model.BaseFirestore;
import Model.DeliveryPartner;
import Model.Order;
import Model.Product;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;
import androidx.transition.Transition;

public class HomeFragment extends Fragment
{
    private static String deliveryPersonID;
    private static int deliveryOrderAllotedTillNow;
    private HomeViewModel homeViewModel;
    private static RecyclerView recyclerView;
    private static RecyclerViewAdapter adapter;
    public  static FloatingActionButton confirmButton;
    private ArrayList<String> imgUrls = new ArrayList<>();
    private ArrayList<String> fdName = new ArrayList<>();
    private ArrayList<Integer> fdPrice = new ArrayList<>();
    static boolean activeOrder = false;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState)
    {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        deliveryPersonID = null;
        deliveryOrderAllotedTillNow = Integer.MAX_VALUE;
        //Make a root View
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //Check if the customer has active order or not
        this.activeOrder = getArguments().getBoolean("activeOrder");
        Toast.makeText(getContext(),"Active Order : "+activeOrder,Toast.LENGTH_SHORT).show();
        System.out.println("Inside HomFragment activity with "+this+" ,activeOrder : "+activeOrder);

        //Initialize the view
        confirmButton = root.findViewById(R.id.order_check_out);
        confirmButton.setVisibility(View.INVISIBLE);
        recyclerView = root.findViewById(R.id.recycle_view_menu);

        //Initialize the data
        initData();

        //Define Click Action
        confirmButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(activeOrder){
                    Toast.makeText(getContext(),"Sorry! You have already placed an Order",Toast.LENGTH_SHORT).show();
                }
                else {
                    System.out.println(adapter.order_name + " total : " + adapter.total);

                    //Make a new Bundle to pass data easily
                    Bundle orderData = new Bundle();
                    orderData.putSerializable("HashMap", adapter.order_name);
                    orderData.putInt("total", adapter.total);
                    orderData.putString("user_email", getArguments().getString("user_email"));
                    orderData.putBoolean("activeOrder", getArguments().getBoolean("activeOrder"));
                    System.out.println(getArguments().get("user_email"));

                    //DialogFragment Code
                    confirmationFragment newDialogFragment = new confirmationFragment();
                    newDialogFragment.setArguments(orderData);
                    newDialogFragment.show(getActivity().getSupportFragmentManager(), "confirmationDialog");
                }

                adapter.activeOrder = activeOrder;

            }
        });
        return root;
    }
    private void initData()
    {
        BaseFirestore.db.collection("DeliveryPartner")
                .whereEqualTo("isonline", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DeliveryPartner t = document.toObject(DeliveryPartner.class);
                                if(t.getAlloted_till_now() < deliveryOrderAllotedTillNow)
                                {
                                    System.out.println(deliveryPersonID + "deliveryPersonId" + t.getAlloted_till_now());
                                    deliveryPersonID = t.getID();
                                    deliveryOrderAllotedTillNow = t.getAlloted_till_now();
                                }
                            }
                        } else {
                        }
                    }
                });
        //Initialize the data: Fetch from FireStore
        BaseFirestore.db.collection("Product")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                Product d = document.toObject(Product.class);
                                imgUrls.add(d.getImg_url());
                                fdName.add(d.getName());
                                fdPrice.add(d.getPrice());
                            }
                            InitRecyclerView();
                        }
                    }
                });

    }
    private void InitRecyclerView()
    {
        //Initialize Recycler View
        System.out.println("In InitRecyclerView------------------");
        System.out.println(getContext().toString());
        adapter = new RecyclerViewAdapter(getContext(),imgUrls, fdName, fdPrice);
        adapter.activeOrder = activeOrder;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == recyclerView.SCROLL_STATE_IDLE)
                {
//                    confirmButton.setVisibility(View.VISIBLE);
//                    getActivity().findViewById(R.id.setCurrentLocationButton).setVisibility(View.VISIBLE);
//                    getActivity().findViewById(R.id.setCurrentLocationText).setVisibility(View.VISIBLE);
//                    getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy != 0){
//                    confirmButton.setVisibility(View.GONE);
//                    getActivity().findViewById(R.id.setCurrentLocationButton).setVisibility(View.GONE);
//                    getActivity().findViewById(R.id.setCurrentLocationText).setVisibility(View.GONE);
//                    getActivity().findViewById(R.id.nav_view).setVisibility(View.GONE);
                }
                else{
//                    confirmButton.setVisibility(View.VISIBLE);
//                    getActivity().findViewById(R.id.setCurrentLocationButton).setVisibility(View.VISIBLE);
//                    getActivity().findViewById(R.id.setCurrentLocationText).setVisibility(View.VISIBLE);
//                    getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
                }

            }
        });
    }
    public static class confirmationFragment extends DialogFragment{
        //private String allot="dp";
        ListView listView;
        @Nullable
        @Override

        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            System.out.println("Inside OnCreateView-------------------------121212121212" + getArguments());

            //Create a new Bundle
            final Bundle mBundle = getArguments();
            View rootView = inflater.inflate(R.layout.content_confirmation, container, false);
            listView = (ListView) rootView.findViewById(R.id.orderListView);
            TextView setTotalValue = (TextView) rootView.findViewById(R.id.orderTotalValue);
            setTotalValue.setText("Total Cost       ₹"  + String.valueOf(mBundle.getInt("total")));
            final Adapter adapterDialog = new Adapter((HashMap<String, ArrayList<String>>) mBundle.getSerializable("HashMap"));
            listView.setAdapter(adapterDialog);

            Log.v("confirmationFragment", "Currently Inside confirmationFragment");
            getDialog().setTitle("Hello");

            Button placeOrder = rootView.findViewById(R.id.orderButtonDialog);
            placeOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mBundle.getBoolean("activeOrder")) {
                        Toast.makeText(getContext(),"Cannot order due to pending order.",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                        if(deliveryPersonID!=null)
                        {
                            BaseFirestore.db.collection("DeliveryPartner").document(deliveryPersonID).update("alloted_till_now",deliveryOrderAllotedTillNow + 1);
                            activeOrder = true;
                            adapter.activeOrder = true;
                            mBundle.putBoolean("activeOrder", true);
                            Order createNewOrder = new Order(mBundle.get("user_email").toString(),deliveryPersonID,mBundle.getInt("total"),(HashMap<String,ArrayList<String>>)mBundle.getSerializable("HashMap"));
                            createNewOrder.sendToFirestore();
                            Toast.makeText(getContext(),"Ordered Successfully , thanks for trusting us!",Toast.LENGTH_SHORT).show();
                            getDialog().dismiss();

                            //Refresh View
                            recyclerView.setAdapter(adapter);
                            
                            adapter.resetParams();

                            //Redirect to Order Confirmation Page a.k.a dashboard page
                            DashboardFragment dashboardFragment = (DashboardFragment) getActivity().getSupportFragmentManager().findFragmentByTag("DashboardFragment");
                            HomeFragment homeFragment = (HomeFragment) getActivity().getSupportFragmentManager().findFragmentByTag("HomeFragment");
                            System.out.println("called from home fragment");
                            dashboardFragment.setActiveOrder(activeOrder);
                            dashboardFragment.setArguments(mBundle);
                            dashboardFragment.resetTextView();
                            BottomNavigationView mBottomNavigationView = getActivity().findViewById(R.id.nav_view);
                            mBottomNavigationView.findViewById(R.id.navigation_dashboard).performClick();
                            confirmButton.hide();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "No delivery partners online, please try again later", Toast.LENGTH_SHORT).show();
                            activeOrder = false;
                            adapter.activeOrder = false;
                            getDialog().dismiss();
                        }

                        mBundle.putBoolean("activeOrder", activeOrder);
                    }
                }
            });

            return rootView;
        }

    }
    public static void setFloatingActionButtonVisibility(boolean visibility){

        if(visibility){
            confirmButton.show();
        }
        else{
            confirmButton.hide();
        }
    }
    public void setActiveOrder(boolean activeOrder) {
        this.activeOrder = activeOrder;
    }
}