package com.example.healthify.ui.home;

import android.app.Application;
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
import com.example.healthify.JavaMailAPI;
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

import org.w3c.dom.Text;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import Model.BaseFirestore;
import Model.Customer;
import Model.DeliveryPartner;
import Model.Order;
import Model.Product;
import Model.PromoCodes;
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
    private static Customer cust_details;
    private static String otp="";
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
        DocumentReference dr = BaseFirestore.db.collection("Customer").document(getArguments().getString("user_email"));
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    DocumentSnapshot ds = task.getResult();
                    if(ds.exists())
                    {
                        cust_details = ds.toObject(Customer.class);
                    }
                    else
                    {
                        cust_details = new Customer();
                    }
                }
                else
                {
                    cust_details = new Customer();
                }
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

        });
    }
    public static class confirmationFragment extends DialogFragment{
        //private String allot="dp";
        ListView listView;
        TextView promo;
        Button promo_button;
        @Nullable
        @Override

        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            System.out.println("Inside OnCreateView-------------------------121212121212" + getArguments());

            //Create a new Bundle
            final Bundle mBundle = getArguments();
            View rootView = inflater.inflate(R.layout.content_confirmation, container, false);
            listView = (ListView) rootView.findViewById(R.id.orderListView);
            final TextView setTotalValue = (TextView) rootView.findViewById(R.id.orderTotalValue);
            final TextView orderTotalWithoutDiscount = (TextView) rootView.findViewById(R.id.orderTotalWithoutDiscount);
            final TextView orderDiscount = (TextView) rootView.findViewById(R.id.totalDiscount);

            promo = rootView.findViewById(R.id.promoCode);
            promo_button=rootView.findViewById(R.id.promo_button);


            final Adapter adapterDialog = new Adapter((HashMap<String, ArrayList<String>>) mBundle.getSerializable("HashMap"));
            listView.setAdapter(adapterDialog);
            orderTotalWithoutDiscount.setText("Order Total    ₹ " +  String.valueOf(mBundle.getInt("total")));

            Log.v("confirmationFragment", "Currently Inside confirmationFragment");
            getDialog().setTitle("Hello");
            mBundle.putInt("preferred_discount",0);
            if(cust_details.isPreferred_customer())
            {
                int totalDiscount = mBundle.getInt("total") - (int)Math.floor(mBundle.getInt("total")* 0.9);
                mBundle.putInt("preferred_discount",10);
                setTotalValue.setText("Total Payable Amount    ₹ "  + String.valueOf((int)Math.floor(mBundle.getInt("total")* 0.9)));
                orderDiscount.setText("Savings   ₹ " + totalDiscount);
                mBundle.putInt("totalPayable", (int)Math.floor(mBundle.getInt("total")* 0.9));
                Toast.makeText(getContext(), "10% regular customer discount added!", Toast.LENGTH_LONG).show();
            }
            else{
                setTotalValue.setText("Total Payable Amount    " + " ₹"  + String.valueOf(mBundle.getInt("total")));
                mBundle.putInt("totalPayable", (int)Math.floor(mBundle.getInt("total")));
            }
            final Button placeOrder = rootView.findViewById(R.id.orderButtonDialog);

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
                            otp=generateOTP();
                            mBundle.putString("OTP",otp);
                            int totalDiscount = mBundle.getInt("promo_discount") + mBundle.getInt("preferred_discount");
                            Order createNewOrder = new Order(mBundle.get("user_email").toString(),deliveryPersonID,mBundle.getInt("totalPayable"),(HashMap<String,ArrayList<String>>)mBundle.getSerializable("HashMap"),otp, totalDiscount, 0);
                            createNewOrder.sendToFirestore();
                            // Send a confirmation email
                            HashMap<String , ArrayList<String>> od = (HashMap<String, ArrayList<String>>) mBundle.getSerializable("HashMap");
                            String adding="";
                            for (Map.Entry<String,ArrayList<String>> entry : od.entrySet())
                            {
                                adding=adding+ entry.getKey() +"    Q : "+entry.getValue().get(0)/*+"    ₹:"+(Integer.parseInt(entry.getValue().get(0)) * Long.parseLong(entry.getValue().get(1)))*/+"<br>";
                                System.out.println("----------------Key = " + entry.getKey() +
                                        ", Value = " + entry.getValue().get(0)+"-----------------------");
                            }
                            JavaMailAPI obj = new JavaMailAPI(getActivity(),
                                    "utsavshah99@live.com",
                                    "Order Placed",
                                    "Hola, <b>"+/*createNewOrder.getCustomer_email()*/cust_details.getName()+"</b><br>"
                                            +"Thanks for ordering from Healthify-the new healthy eating joint!<br><br>"+adding+"<br><br>"
                                            +"We have received your order and putting our heart and soul to create it!<br><br>"
                                    +"-Healthify Team"
                            );
                            obj.execute();
                            Toast.makeText(getContext(),"Ordered Successfully , thanks for trusting us!",Toast.LENGTH_SHORT).show();
                            getDialog().dismiss();

                            //Refresh View
                            recyclerView.setAdapter(adapter);
                            
                            adapter.resetParams();

                            //Redirect to Order Confirmation Page a.k.a dashboard page
                            DashboardFragment dashboardFragment = (DashboardFragment) getActivity().getSupportFragmentManager().findFragmentByTag("DashboardFragment");
                            HomeFragment homeFragment = (HomeFragment) getActivity().getSupportFragmentManager().findFragmentByTag("HomeFragment");
                            dashboardFragment.setActiveOrder(activeOrder);
                            dashboardFragment.setArguments(mBundle);
                            dashboardFragment.resetTextView();
                            BottomNavigationView mBottomNavigationView = getActivity().findViewById(R.id.nav_view);
                            mBottomNavigationView.findViewById(R.id.navigation_dashboard).performClick();
                            confirmButton.hide();
                            if(!promo.getText().toString().isEmpty()) {
                                BaseFirestore.db.collection("PromoCodes").document(promo.getText().toString()).delete();
                            }
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
            promo_button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(promo.getText().toString().equals(""))
                    {
                        Toast.makeText(getActivity(), "Enter PromoCode!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        placeOrder.setClickable(false);
                        System.out.println("Text entered  :  "+promo.getText().toString());
                        BaseFirestore.db.collection("PromoCodes").document(promo.getText().toString())
//                                .whereEqualTo("Code",promo.getText().toString())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            DocumentSnapshot document = task.getResult();
                                            if(document.exists())
                                            {
                                                final TextView orderDiscount = (TextView) rootView.findViewById(R.id.totalDiscount);

                                                PromoCodes pr;
                                                pr = document.toObject(PromoCodes.class);

                                                System.out.println("------------disc : "+pr.getDiscount_percent());
                                                double regularDiscount = (double)mBundle.getInt("preferred_discount")/100;
                                                int final_val = (int)Math.floor((1-(pr.getDiscount_percent()+regularDiscount))*mBundle.getInt("total"));
                                                int totalDiscountVal = mBundle.getInt("total") - final_val;

                                                orderDiscount.setText("Savings   ₹ " + totalDiscountVal);
                                                setTotalValue.setText("Total Payable Amount    ₹ "  + String.valueOf(final_val));
                                                mBundle.putInt("promo_discount",(int) (pr.getDiscount_percent()* 100));
                                                mBundle.putInt("totalPayable", final_val);

                                                promo_button.setClickable(false);
                                                placeOrder.setClickable(true);
                                                Toast.makeText(getActivity(), "Voila, Promo Applied!", Toast.LENGTH_SHORT).show();

                                            }
                                            else
                                            {
                                                Toast.makeText(getActivity(), "Invalid PromoCode!", Toast.LENGTH_SHORT).show();
                                                placeOrder.setClickable(true);
                                            }
                                        }
                                    }
                                });
                    }
                }
            });
            return rootView;
        }
        public static String generateOTP()
        {
            String ALL = "1234567890";
            StringBuilder generate = new StringBuilder();
            Random rnd = new Random();
            while (generate.length() < 4) { // length of the random string.
                int index = rnd.nextInt(10);
                generate.append(ALL.charAt(index));
            }
            String gen = generate.toString();
            return gen;
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