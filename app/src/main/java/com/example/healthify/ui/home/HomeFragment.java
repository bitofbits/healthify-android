package com.example.healthify.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthify.Confirmation;
import com.example.healthify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Model.BaseFirestore;
import Model.Product;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment
{

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private FloatingActionButton confirm_button;
    private ArrayList<String> img_urls = new ArrayList<>();
    private ArrayList<String> fd_name = new ArrayList<>();
    private ArrayList<Integer> fd_price = new ArrayList<>();
    private Intent pay;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState)
    {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        confirm_button = root.findViewById(R.id.order_check_out);
        //final TextView textView = root.findViewById(R.id.text_home);
        recyclerView = root.findViewById(R.id.recycle_view_menu);
        initData();
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>()
        {
            @Override
            public void onChanged(@Nullable String s)
            {
                //textView.setText(s);
                //initData();
            }
        });
        confirm_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

               // String email = savedInstanceState.get("user_name").toString();
                //System.out.println("Home Fragment--------- :"+getActivity().toString());
                //Toast.makeText(getContext(),getActivity().toString(), Toast.LENGTH_SHORT).show();
                //adapter.notifyDataSetChanged();
                System.out.println(adapter.order_name + " total : "+adapter.total);
                Bundle data = new Bundle();
                //data.putSerializable("HashMap",adapter.order_name);
                data.putInt("total",adapter.total);
                pay = new Intent(getContext(),Confirmation.class);
                pay.putExtras(data);
                startActivity(pay);
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
                                img_urls.add(d.getImg_url());
                                fd_name.add(d.getName());
                                fd_price.add(d.getPrice());
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            InitRecyclerView();
                        }
                        else {

                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });





//        DocumentReference d = BaseFirestore.db.collection("Product").document("Paneer Butter Masala");
//        d.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
//        {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task)
//            {
//                if(task.isSuccessful())
//                {
//                    DocumentSnapshot documentSnapshot = task.getResult();
//                    Product d = new Product();
//                    d = documentSnapshot.toObject(Product.class);
//                    System.out.println("d = "+d);
//                    System.out.println("Doc id  : "+d.getImg_url());
//                    img_urls.add(d.getImg_url());
//                    fd_name.add(d.getName());
//                    fd_price.add(d.getPrice());
//                    InitRecyclerView();
//                }
//                else
//                {
//                    //error bringing menu from database
//                }
//            }
//        });

        //InitRecyclerView();
    }
    private void InitRecyclerView()
    {
        System.out.println("In InitRecyclerView------------------");
        System.out.println(getContext().toString());
        adapter = new RecyclerViewAdapter(getContext(),img_urls,fd_name,fd_price);
        recyclerView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}