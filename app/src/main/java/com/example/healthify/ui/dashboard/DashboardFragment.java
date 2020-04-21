package com.example.healthify.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthify.CustomerHome;
import com.example.healthify.DeliveryPartnerHome;
import com.example.healthify.R;
import com.example.healthify.ui.home.Adapter;
import com.example.healthify.ui.home.HomeFragment;
import com.google.android.gms.common.util.JsonUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.HashMap;

import Model.BaseFirestore;
import Model.Customer;
import Model.DeliveryPartner;
import Model.Order;

public class DashboardFragment extends Fragment
{

    private DashboardViewModel dashboardViewModel;
    private boolean activeOrder;
    private String deliveryPersonID;
    private HashMap<String, Long> orderName;
    static View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);

        View root = inflater.inflate(R.layout.new_fragment_dashboard, container, false);

        this.root = root;

        Button cancelButton = (Button) root.findViewById(R.id.cancelButtonDashboardFragment);
        TextView activeOrderTextView = root.findViewById(R.id.textLowerDashboardFragment);

        activeOrder = getArguments().getBoolean("activeOrder");
        System.out.println("called db fragment 67");
        resetTextView();
//        activeOrderTextView.setText("Currently Active Order:" + new Boolean(activeOrder).toString());

        //Cancel Button
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

                                //Start Deleting Files
                                for (DocumentSnapshot document : task.getResult()) {

                                    // Update alloted till now
                                    BaseFirestore.db.collection("DeliveryPartner").document(
                                            document.getString("partner")).update("alloted_till_now", FieldValue.increment(-1l));

                                    // Delete Order
                                    Customer.db.collection("Order").document(document.getId()).delete();
                                    setActiveOrder(false);
                                    System.out.println("called dbfragment 100");
                                    resetTextView();

                                    //Go back to Home Fragment
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

        FloatingActionButton floatingActionButton = root.findViewById(R.id.refreshButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTextView();
            }
        });
        return root;
    }
    public void setActiveOrder(boolean activeOrder){
        this.activeOrder = activeOrder;
    }
    public void resetTextView(){

        TextView activeOrderTextView = this.root.findViewById(R.id.textLowerDashboardFragment);
        TextView TotalValueTextView = this.root.findViewById(R.id.dashBoardTotalOrder);
        TextView DeliveryPersonTextView = this.root.findViewById(R.id.dashBoardDeliveryPersonName);
        TextView dashBoardDeliveryPersonPhone = this.root.findViewById(R.id.dashBoardDeliveryPersonPhone);
        TextView dashBoardDeliveryPersonOTP = this.root.findViewById(R.id.dashBoardDeliveryPersonOTP);
        TextView cancelButtonDashboardFragment = this.root.findViewById(R.id.cancelButtonDashboardFragment);
        TextView dashBoardDeliveryDetails = this.root.findViewById(R.id.dashBoardDeliveryDetails);;
        TextView orderStatusView = this.root.findViewById(R.id.textView);
        ListView listView =  this.root.findViewById(R.id.dashboardListView);

        activeOrderTextView.setText("There are no orders right now");
        if(this.activeOrder){

            TotalValueTextView.setVisibility(View.VISIBLE);
            DeliveryPersonTextView.setVisibility(View.VISIBLE);
            dashBoardDeliveryPersonPhone.setVisibility(View.VISIBLE);
            dashBoardDeliveryPersonOTP.setVisibility(View.VISIBLE);
            cancelButtonDashboardFragment.setVisibility(View.VISIBLE);
            dashBoardDeliveryDetails.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);

            System.out.println("resetTextView()" + this.activeOrder);
            BaseFirestore.db.collection("Order").whereEqualTo("customer_email", getArguments()
                    .getString("user_email")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            Order order = document.toObject(Order.class);
                            Adapter adapterDialog = new AdapterDashboard(order.getOrder_name());
                            ListView listView = (ListView) DashboardFragment.root.findViewById(R.id.dashboardListView);
                            TextView TotalValueTextView = DashboardFragment.root.findViewById(R.id.dashBoardTotalOrder);
                            TextView dashBoardDeliveryPersonOTP = DashboardFragment.root.findViewById(R.id.dashBoardDeliveryPersonOTP);

                            TotalValueTextView.setText("₹ "  +String.valueOf(order.getCost()));
                            dashBoardDeliveryPersonOTP.setText("OTP " + order.getOtp());
                            if(order.getStatus() == 0){
                                orderStatusView.setText("Food is being prepared!");
                            }
                            else{
                                orderStatusView.setText("Out for Delivery!");
                            }
                            listView.setAdapter(adapterDialog);
                            System.out.println("DeliveryPersonTextView.getText()" + DeliveryPersonTextView.getText());
                            BaseFirestore.db.collection("DeliveryPartner").whereEqualTo("email", order.getPartner()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    System.out.println("andar gya");
                                    if(task.isSuccessful()){
                                        for (QueryDocumentSnapshot document : task.getResult()){
                                            if(document.exists())
                                            {
                                                System.out.println("andar gya2");
                                                DeliveryPartner deliveryPartner = document.toObject(DeliveryPartner.class);
                                                TextView dashBoardDeliveryPersonPhone = DashboardFragment.root.findViewById(R.id.dashBoardDeliveryPersonPhone);
                                                TextView DeliveryPersonTextView = DashboardFragment.root.findViewById(R.id.dashBoardDeliveryPersonName);
                                                String phoneNumber = deliveryPartner.getMobile_number();
                                                System.out.println("Phone Number" + phoneNumber);
                                                dashBoardDeliveryPersonPhone.setText("" + phoneNumber);
                                                DeliveryPersonTextView.setText("" + deliveryPartner.getName());
                                            }
                                        }

                                    }
                                }
                            });
                        }

                    }
                }
            });
            activeOrderTextView.setVisibility(View.INVISIBLE);
            TotalValueTextView.setText("Total Cost    ₹"  + String.valueOf(getArguments().getInt("total")));
//            Adapter adapterDialog = new Adapter((HashMap<String, Long>) getArguments().getSerializable("HashMap"));

        }
        else{
            TotalValueTextView.setVisibility(View.INVISIBLE);
            DeliveryPersonTextView.setVisibility(View.INVISIBLE);
            dashBoardDeliveryPersonPhone.setVisibility(View.INVISIBLE);
            dashBoardDeliveryPersonOTP.setVisibility(View.INVISIBLE);
            cancelButtonDashboardFragment.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
            dashBoardDeliveryDetails.setVisibility(View.INVISIBLE);
            activeOrderTextView.setText("You have no pending orders right now");
        }

    }
    public void setOrderName(HashMap<String, Long> orderName){
        this.orderName = orderName;
    }

}