package com.example.healthify.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthify.Confirmation;
import com.example.healthify.R;
import com.example.healthify.ui.dashboard.DashboardFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import Model.BaseFirestore;
import Model.Order;
import Model.Product;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment
{

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private FloatingActionButton confirmButton;
    private ArrayList<String> imgUrls = new ArrayList<>();
    private ArrayList<String> fdName = new ArrayList<>();
    private ArrayList<Integer> fdPrice = new ArrayList<>();
    private Intent pay;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState)
    {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        confirmButton = root.findViewById(R.id.order_check_out);
        //final TextView textView = root.findViewById(R.id.text_home);
        recyclerView = root.findViewById(R.id.recycle_view_menu);
        initData();
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>()
//        {
//            @Override
//            public void onChanged(@Nullable String s)
//            {
//                //textView.setText(s);
//                //initData();
//            }
//        });
        confirmButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

               // String email = savedInstanceState.get("user_name").toString();
                //System.out.println("Home Fragment--------- :"+getActivity().toString());
                //Toast.makeText(getContext(),getActivity().toString(), Toast.LENGTH_SHORT).show();
                //adapter.notifyDataSetChanged();
                System.out.println(adapter.order_name + " total : "+adapter.total);
                Bundle orderData = new Bundle();
                orderData.putSerializable("HashMap",adapter.order_name);
                orderData.putInt("total",adapter.total);
                orderData.putString("user_email",getArguments().get("user_email").toString());
                System.out.println(getArguments().get("user_email"));
                confirmationFragment newDialogFragment = new confirmationFragment();
                newDialogFragment.setArguments(orderData);
                newDialogFragment.show(getChildFragmentManager(),"confirationDialog");
                System.out.println("After showing dialog box");
                //recyclerView.setAdapter(null);
                //recyclerView.setLayoutManager(null);
                recyclerView.setAdapter(adapter);
                //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                //adapter.notifyDataSetChanged();
                //Payment Page;
                //go to order confirmation page.
               // BaseFirestore.db.collection("Customer")
            }
        });
        return root;
    }
    private void initData()
    {
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
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            InitRecyclerView();
                        }
                        else {

                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    private void InitRecyclerView()
    {
        System.out.println("In InitRecyclerView------------------");
        System.out.println(getContext().toString());
        adapter = new RecyclerViewAdapter(getContext(),imgUrls, fdName, fdPrice);
        recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    public static class confirmationFragment extends DialogFragment{
        ListView listView;
        @Nullable
        @Override

        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            System.out.println("Inside OnCreateView-------------------------121212121212");
            final Bundle mBundle = getArguments();
            View rootView = inflater.inflate(R.layout.content_confirmation, container, false);

            listView = (ListView) rootView.findViewById(R.id.orderListView);
            TextView setTotalValue = (TextView) rootView.findViewById(R.id.orderTotalValue);
            setTotalValue.setText("Total Cost       ₹"  + String.valueOf(mBundle.getInt("total")));
            Adapter adapter = new Adapter((HashMap<String, Integer>) mBundle.getSerializable("HashMap"));
            listView.setAdapter(adapter);

            Log.v("confirmationFragment", "Currently Inside confirmationFragment");
            getDialog().setTitle("Hello");

            Button placeOrder = rootView.findViewById(R.id.orderButtonDialog);
            placeOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DocumentReference documentReference = BaseFirestore.db.collection("Order").document(Integer.toString(mBundle.get("user_email").toString().hashCode()));
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task)
                        {
                            if(task.isSuccessful())
                            {
                                DocumentSnapshot doc = task.getResult();
                                if(doc.exists())
                                {
                                    Toast.makeText(getContext(),"Cannot order due to pending order.",Toast.LENGTH_SHORT).show();
                                    getDialog().dismiss();
                                }
                                else
                                {
                                    Order send = new Order(mBundle.get("user_email").toString(),mBundle.getInt("total"),(HashMap<String,Integer>)mBundle.getSerializable("HashMap"));
                                    send.sendToFirestore();
                                    Toast.makeText(getContext(),"Ordered Successfuly , thanks for trusting us!",Toast.LENGTH_SHORT).show();
                                    getDialog().dismiss();
                                }
                            }
                            else
                            {
                                Toast.makeText(getContext(),"Low Network connectivity, Please try again later!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
//                    Order send = new Order(mBundle.get("user_email").toString(),mBundle.getInt("total"),(HashMap<String,Integer>)mBundle.getSerializable("HashMap"));
//                    send.sendToFirestore();
//                    getDialog().dismiss();
                    //final NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    //Bundle b = new Bundle();
                    //System.out.println();
                    //b.putString("user_emails","UTSAV TESTING");

                    //navController.navigate(R.id.navigation_dashboard);
                }
            });

            return rootView;
        }
    }
}