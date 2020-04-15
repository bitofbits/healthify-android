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

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

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
        return root;
    }
    public void setActiveOrder(boolean activeOrder){
        this.activeOrder = activeOrder;
    }
    public void resetTextView(){

        TextView activeOrderTextView = this.root.findViewById(R.id.textLowerDashboardFragment);
        TextView TotalValueTextView = this.root.findViewById(R.id.dashBoardTotalOrder);
        TextView DeliveryPersonTextView = this.root.findViewById(R.id.dashBoardDeliveryPersonName);
        ListView listView = (ListView) this.root.findViewById(R.id.dashboardListView);

        activeOrderTextView.setText("There are no orders right now");
        if(this.activeOrder){

            TotalValueTextView.setVisibility(View.VISIBLE);
            DeliveryPersonTextView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);

            System.out.println("resetTextView()" + this.activeOrder);
            BaseFirestore.db.collection("Order").whereEqualTo("customer_email", getArguments()
                    .getString("user_email")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            Order order = document.toObject(Order.class);
                            Adapter adapterDialog = new Adapter(order.getOrder_name());
                            ListView listView = (ListView) DashboardFragment.root.findViewById(R.id.dashboardListView);
                            TextView DeliveryPersonTextView = DashboardFragment.root.findViewById(R.id.dashBoardDeliveryPersonName);
                            TextView TotalValueTextView = DashboardFragment.root.findViewById(R.id.dashBoardTotalOrder);
                            DeliveryPersonTextView.setText("Delivery Person Email: " + order.getPartner());
                            TotalValueTextView.setText("Total Cost    ₹"  +String.valueOf(order.getCost()));
                            listView.setAdapter(adapterDialog);
                        }

                    }
                }
            });
            activeOrderTextView.setVisibility(View.GONE);
            TotalValueTextView.setText("Total Cost    ₹"  + String.valueOf(getArguments().getInt("total")));
//            Adapter adapterDialog = new Adapter((HashMap<String, Long>) getArguments().getSerializable("HashMap"));

        }
        else{
            TotalValueTextView.setVisibility(View.GONE);
            DeliveryPersonTextView.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            activeOrderTextView.setText("You have no pending orders right now");
        }

    }
    public void setOrderName(HashMap<String, Long> orderName){
        this.orderName = orderName;
    }

}