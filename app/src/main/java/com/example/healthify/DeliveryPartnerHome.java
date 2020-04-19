package com.example.healthify;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthify.ui.home.Adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import Model.BaseFirestore;
import Model.Customer;
import Model.DeliveryPartner;
import Model.PromoCodes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class DeliveryPartnerHome extends AppCompatActivity
{
ArrayList<String> order_list = new ArrayList<>();
ListView display;
Switch aSwitch;
Bundle info = new Bundle();
Context context = this;
String email;
FloatingActionButton fab;
//    @Override
//    protected void onStop()
//    {
//        super.onStop();
//        if(aSwitch.isChecked())
//        {
//            aSwitch.setChecked(false);
//            BaseFirestore.db.collection("DeliveryPartner").document(email).update("isonline", false);
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_partner_home);
        email = getIntent().getStringExtra("user_name");
        System.out.println("-------------------------Email is : "+email);
        fab = findViewById(R.id.fab);
        display = findViewById(R.id.list_view_delivery);
        aSwitch = findViewById(R.id.switch_DeliveryPartner);
        System.out.println("Clicking fab inside oncreate od DeliveryPartnerHome----------");
        fab.performClick();
        DocumentReference doc = BaseFirestore.db.collection("DeliveryPartner").document(email);
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    DocumentSnapshot d = task.getResult();
                    DeliveryPartner x = d.toObject(DeliveryPartner.class);
                    if(x.isIsonline())
                    {
                        aSwitch.setTextOn("Online");
                        aSwitch.setChecked(true);
                    }
                    else
                    {
                        aSwitch.setChecked(false);
                        aSwitch.setText("Offline");
                        //aSwitch.setTextOff("Offline---");

                    }
                }
                else
                {
                    Toast.makeText(context, "Network Problem, status not updated properly", Toast.LENGTH_SHORT).show();

                }
            }
        });
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if(!b)
                {
                    BaseFirestore.db.collection("DeliveryPartner").document(email).update("isonline",false);
                    aSwitch.setText("Offline");
                    System.out.println("You are offline, please complete the current deliveries.");
                }
                else {
                    BaseFirestore.db.collection("DeliveryPartner").document(email).update("isonline",true);
                    System.out.println("You are online");
                    aSwitch.setText("Online");
                }

            }
        });
        //fab.performClick();
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
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
                                Toast.makeText(context, "No orders to show!", Toast.LENGTH_SHORT).show();
                                System.out.println("Inside isEmpty()-----------------------");
                                order_list.clear();
                                ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,order_list);
                                System.out.println("---------------------order_list.size() : "+order_list.size());
                                display.setAdapter(arrayAdapter);
                                listener();
                            }
                            else
                            {
                                //info.clear();
                                System.out.println("Inside if statement before order_list.clear()-----------------------------");
                                order_list.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    order_list.add("Order id : "+document.get("order_id").toString());
                                    info.putSerializable(document.get("order_id").toString(),(HashMap<String,Long>)document.get("order_name"));
                                    info.putInt("cost"+document.get("order_id").toString(),Integer.parseInt(document.get("cost").toString()));
                                    info.putString("cust_email"+document.get("order_id").toString(),document.get("customer_email").toString());
                                    info.putString("OTP"+document.get("order_id").toString(),document.get("otp").toString());
                                    info.putLong("status"+document.get("order_id").toString(), (Long) document.get("status"));
                                    //System.out.println("inside doc : "+info.getSerializable(document.get("order_id").toString()) + info.getInt(document.get("order_id").toString()));
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
        });
//        BaseFirestore.db.collection("Order").whereEqualTo("partner",getIntent().getStringExtra("user_name")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
//        {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task)
//            {
//                if(task.isSuccessful())
//                {
//                    if(task.getResult().isEmpty())
//                    {
//                        //no order to show to this delivery partner
//                    }
//                    else
//                    {
//                        info.clear();
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            order_list.add("Order id : "+document.get("order_id").toString());
//                            info.putSerializable(document.get("order_id").toString(),(HashMap<String,Long>)document.get("order_name"));
//                            info.putInt("cost"+document.get("order_id").toString(),Integer.parseInt(document.get("cost").toString()));
//                            info.putString("cust_email"+document.get("order_id").toString(),document.get("customer_email").toString());
//                            //System.out.println("inside doc : "+info.getSerializable(document.get("order_id").toString()) + info.getInt(document.get("order_id").toString()));
//                        }
//                        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,order_list);
//                        System.out.println(order_list.size());
//                        display.setAdapter(arrayAdapter);
//                        listener();
//                    }
//                }
//                else
//                {
//                    Toast.makeText(DeliveryPartnerHome.this, "Error connecting to database", Toast.LENGTH_SHORT).show();
//                    //error connecting to database
//                }
//            }
//        });

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
                specific.putString("OTP"+val,info.get("OTP"+val).toString());
                specific.putInt("status"+val,info.getInt("status"+val));
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
        Button pick;
        Customer current_customer;
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {

            System.out.println("inside onCreateView--------------------; "+this);
            View rootView = inflater.inflate(R.layout.content_delivery_dialogbox, container, false);
            Bundle properties = getArguments();
            System.out.println("inside oncreateView   "+properties);
            pick = rootView.findViewById(R.id.pickup_DeliveryPartner);
            details = rootView.findViewById(R.id.order_details_deliverypartner);
            total = rootView.findViewById(R.id.total_DeliveryParter);
            cname = rootView.findViewById(R.id.cust_name_DeliveryPartner);
            cphone= rootView.findViewById(R.id.cust_phone_DeliveryPartner);
            cadd = rootView.findViewById(R.id.cust_add_DeliveryPartner);
            delivered=rootView.findViewById(R.id.deliverdone_DeliveryPartner);
            String many = properties.get("KEY").toString();
            total.setText("Total             ₹"+Integer.toString(properties.getInt("cost"+many)));
            //cname.setText(properties.getString("cust_email"+many));
            cadd.setText("OTP: "+properties.getString("OTP"+many));
            adapter = new Adapter((HashMap<String, ArrayList<String>>) properties.getSerializable("HashMap"));
            details.setAdapter(adapter);
            final DocumentReference customer_ref = BaseFirestore.db.collection("Customer").document(properties.getString("cust_email"+many));
            customer_ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task)
                {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists())
                    {
                        current_customer = doc.toObject(Customer.class);
                        cphone.setText("Phone: "+current_customer.getPhone_number());
                        cname.setText("Name: "+current_customer.getName());
                    }
                    else
                    {
                        System.out.println("Some Error, Please try again later!");
                    }
                }
            });

            pick.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    BaseFirestore.db.collection("Order").document(Integer.toString(current_customer.getEmail().hashCode())).update("status",1);
                    Toast.makeText(getActivity(), "Picked up updated!", Toast.LENGTH_SHORT).show();
                }
            });
            delivered.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    current_customer.setPast_orders(current_customer.getPast_orders()+1);
                    if(current_customer.getPast_orders()>=3)
                        current_customer.setPreferred_customer(true);
                    current_customer.sendToFirestore();
                    Query orderQuery = Customer.db.collection("Order").whereEqualTo("customer_email",
                            current_customer.getEmail());

                    orderQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if(task.getResult().isEmpty()) {

                                }
                                else {

                                    //Toast.makeText(getActivity(),"Updated Database!",Toast.LENGTH_SHORT).show();

                                    //Start Deleting Files
                                    for (DocumentSnapshot document : task.getResult()) {

                                        // Update alloted till now
                                        BaseFirestore.db.collection("DeliveryPartner").document(
                                                document.getString("partner")).update("alloted_till_now", FieldValue.increment(-1l));

                                        // Delete Order
                                        BaseFirestore.db.collection("Order").document(document.getId()).delete();
                                        //setActiveOrder(false);
                                    }
                                    PromoCodes code = new PromoCodes(generateString(),generate_disc());
                                    JavaMailAPI obj = new JavaMailAPI(getActivity(),
                                            "utsavshah99@live.com",
                                            "Order Delivered!",
                                            "Hola, <b>"+current_customer.getName()+"</b><br>"
                                                    +"Thanks for trusting us!<br><br>"
                                                    +"Your order has just been delivered! Hope you enjoy your meal<br><br>"
                                                    +"A sweet dessert for you! Apply promo code : "+code.getCode()+" for a discount of "+code.getDiscount_percent()*100+ "% on next order!<br><br>"
                                                    +"-Healthify Team"
                                    );
                                    obj.execute();
                                    code.sendToFirestore();
                                }
                            }
                            else{
                                Log.v("DeliveryPartnerHome", "didn't find order");
                            }
                        }
                    });
                    System.out.println("Clicking fab inside Dialog-----------------");
                    //fab.performClick();
                    getDialog().dismiss();
                }
            });
            getDialog().setTitle("Details");
            return rootView;
        }
        public static String generateString()
        {
            String ALL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
            StringBuilder generate = new StringBuilder();
            Random rnd = new Random();
            while (generate.length() < 7) { // length of the random string.
                int index = (int) (rnd.nextFloat() * ALL.length());
                generate.append(ALL.charAt(index));
            }
            String gen = generate.toString();
            return gen;
        }
        public static double generate_disc()
        {
            Random r = new Random();
            int gen = r.nextInt(3);
            if(gen==0)
                return 0.10;
            else if(gen==1)
                return 0.15;
            else
                return 0.20;
        }
    }

}