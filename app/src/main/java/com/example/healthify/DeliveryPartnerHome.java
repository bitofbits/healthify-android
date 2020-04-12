package com.example.healthify;

import android.app.Dialog;
import android.os.Bundle;

import com.example.healthify.ui.home.Adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import Model.BaseFirestore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class DeliveryPartnerHome extends AppCompatActivity
{
ArrayList<String> order_list = new ArrayList<>();
ListView display;
Bundle info = new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_partner_home);
        FloatingActionButton fab = findViewById(R.id.fab);
        display = findViewById(R.id.list_view_delivery);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        BaseFirestore.db.collection("Order").whereEqualTo("partner",getIntent().getStringExtra("user_name")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if(task.isSuccessful())
                {
                    if(task.getResult().isEmpty())
                    {
                        //no order to show to this delivery partner
                    }
                    else
                    {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            order_list.add("Order id : "+document.get("order_id").toString());
                            info.putSerializable(document.get("order_id").toString(),(HashMap<String,Integer>)document.get("order_name"));
                            info.putInt("cost"+document.get("order_id").toString(),Integer.parseInt(document.get("cost").toString()));
                            info.putString("cust_email"+document.get("order_id").toString(),document.get("customer_email").toString());
                            System.out.println("inside doc : "+info.getSerializable(document.get("order_id").toString()) + info.getInt(document.get("order_id").toString()));
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,order_list);
                        System.out.println(order_list.size());
                        display.setAdapter(arrayAdapter);
                        listener();
                    }
                }
                else
                {
                    Toast.makeText(DeliveryPartnerHome.this, "Error connecting to database", Toast.LENGTH_SHORT).show();
                    //error connecting to database
                }
            }
        });

    }
    private void listener()
    {
        display.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                String tmp = order_list.get(i);
                String val="";
                for(int k=11;k<tmp.length();k++)
                {
                    val = val + tmp.charAt(k);
                }
                Bundle specific = new Bundle();
                specific.putSerializable("HashMap",info.getSerializable(val));
                specific.putInt("cost"+val,info.getInt("cost"+val));
                specific.putString("cust_email"+val,info.getString("cust_email"+val));
                specific.putString("KEY",val);
                Details dialog = new Details();
                dialog.setArguments(specific);
                dialog.show(getSupportFragmentManager(),"Details Box");
                //dialog.show(getCallingActivity().get, "confirmationDialog");
                //System.out.println(info.getSerializable(val));
            }
        });
    }
    public static class Details extends DialogFragment
    {
        Adapter adapter;
        ListView details;
        TextView total;
        TextView cname;
        TextView cphone;
        TextView cadd;
        Button delivered;
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            System.out.println("inside onCreateView--------------------; "+this);
            View rootView = inflater.inflate(R.layout.content_delivery_dialogbox, container, false);
            Bundle properties = getArguments();
            System.out.println("inside oncreateView   "+properties);
            details = rootView.findViewById(R.id.order_details_deliverypartner);
            total = rootView.findViewById(R.id.total_DeliveryParter);
            cname = rootView.findViewById(R.id.cust_name_DeliveryPartner);
            cphone= rootView.findViewById(R.id.cust_phone_DeliveryPartner);
            cadd = rootView.findViewById(R.id.cust_add_DeliveryPartner);
            delivered=rootView.findViewById(R.id.deliverdone_DeliveryPartner);
            String many = properties.get("KEY").toString();
            total.setText("Total         ₹"+Integer.toString(properties.getInt("cost"+many)));
            cname.setText(properties.getString("cust_email"+many));
            adapter = new Adapter((HashMap<String, Integer>) properties.getSerializable("HashMap"));
            details.setAdapter(adapter);
            getDialog().setTitle("Details");
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

}