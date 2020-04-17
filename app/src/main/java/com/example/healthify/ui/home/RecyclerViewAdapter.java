package com.example.healthify.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.healthify.R;
import com.google.android.gms.common.util.JsonUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private ArrayList<String> foodImg = new ArrayList<>();
    private ArrayList<String> foodName = new ArrayList<>();
    private ArrayList<Integer> foodPrice = new ArrayList<>();
    private Context context;
    public boolean activeOrder = false;
    public HashMap<String, ArrayList<String>> order_name = new HashMap<String, ArrayList<String>>();
    public int total = 0;
    private ArrayList<String> arrayList = new ArrayList<String>();
    private String orderSize = "Small";
    private double orderCost;
    private String selected;
    public RecyclerViewAdapter(Context context, ArrayList<String> foodImg, ArrayList<String> foodName,ArrayList<Integer> foodPrice)
    {
        this.foodImg = foodImg;
        this.foodName = foodName;
        this.context = context;
        this.foodPrice = foodPrice;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_list,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
    {
        if(foodImg.get(position).equals(""))
        {
            holder.img.setImageResource(R.drawable.capture);
        }
        else
        {
            Glide.with(context).asBitmap().load(foodImg.get(position)).into(holder.img);
        }
        holder.name.setText(foodName.get(position));
        holder.price.setText("₹ "+ foodPrice.get(position).toString());
        holder.parentLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(context,"Hello food item!", Toast.LENGTH_SHORT).show();
            }
        });

        if(activeOrder){
            HomeFragment.setFloatingActionButtonVisibility(false);
            holder.elegantNumberButton.setRange(0, 0);
            holder.elegantNumberButton.setOnClickListener(new ElegantNumberButton.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,"Sorry! You have already placed an Order",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            holder.elegantNumberButton.setRange(0, 5);
            holder.elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                    selected = holder.spinner.getSelectedItem().toString();
                    orderCost = foodPrice.get(position);
                    switch(holder.spinner.getSelectedItem().toString()) {
                        case("Small"):
                            total += (newValue - oldValue ) * orderCost;
                            break;
                        case("Medium"):
                            orderCost = orderCost * 1.5;
                            total += (newValue - oldValue ) * orderCost;
                            break;
                        case("Large"):
                            orderCost = orderCost * 2;
                            total += (newValue - oldValue ) * orderCost;
                            break;
                    }
                    if(newValue > 0)
                        HomeFragment.setFloatingActionButtonVisibility(true);
                    else if(newValue == 0 && total == 0)
                        HomeFragment.setFloatingActionButtonVisibility(false);

                    arrayList = new ArrayList<String>();
                    arrayList.add(holder.elegantNumberButton.getNumber());
                    arrayList.add(String.valueOf(orderCost));
                    arrayList.add(holder.spinner.getSelectedItem().toString());
                    order_name.put(holder.name.getText().toString() + " (" + arrayList.get(2) + ")", arrayList);

                    if(newValue == 0)
                        order_name.remove(holder.name.getText().toString() + " (" + selected + ")");
                }
            });
        }
//        holder.subtract.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View view)
//            {
//                System.out.println(context.toString());
//                long quantityTemp = Long.parseLong(holder.quan.getText().toString());
//                selected = holder.spinner.getSelectedItem().toString();
//
//                //Button work only if customer don't have an active order
//                if(activeOrder) {
//                    quantityTemp = 0;
//                    Toast.makeText(context,"Sorry! You have already placed an Order",Toast.LENGTH_SHORT).show();
//                    HomeFragment.setFloatingActionButtonVisibility(false);
//                }
//                else
//                {
//                    if(quantityTemp > 0) {
//
//
//                        orderCost = foodPrice.get(position);
//                        switch(holder.spinner.getSelectedItem().toString()) {
//                            case("Small"):
//                                total -= orderCost;
//                                break;
//                            case("Medium"):
//                                orderCost = orderCost * 1.5;
//                                total -= orderCost;
//                                break;
//                            case("Large"):
//                                orderCost = orderCost * 2;
//                                total -= orderCost;
//                                break;
//                        }
//                        quantityTemp--;
//                    }
//                }
//
//                if(total == 0){
//                    HomeFragment.setFloatingActionButtonVisibility(false);
//                }
//                holder.quan.setText(Long.toString(quantityTemp));
//                arrayList = new ArrayList<String>();
//                arrayList.add(Long.toString(quantityTemp));
//                arrayList.add(String.valueOf(orderCost));
//                arrayList.add(holder.spinner.getSelectedItem().toString());
//
//                order_name.put(holder.name.getText().toString() + " (" + arrayList.get(2) + ")", arrayList);
//
//                if(quantityTemp == 0)
//                    order_name.remove(holder.name.getText().toString() + " (" + selected + ")");
//            }
//        });
//        holder.add.setOnClickListener(new View.OnClickListener()
//        {
//
//            @Override
//            public void onClick(View view)
//            {
//                Long quantityTemp = Long.parseLong(holder.quan.getText().toString());
//
//                //Button work only if customer don't have an active order
//                if(activeOrder) {
//                    quantityTemp = 0l;
//                    Toast.makeText(context,"Sorry! You have already placed an Order",Toast.LENGTH_SHORT).show();
//                    HomeFragment.setFloatingActionButtonVisibility(false);
//                }
//                else{
//                    if(quantityTemp < 5) {
//
//                        orderCost = foodPrice.get(position);
//                        switch(holder.spinner.getSelectedItem().toString()) {
//                            case("Small"):
//                                total += orderCost;
//                                break;
//                            case("Medium"):
//                                orderCost = orderCost * 1.5;
//                                total += orderCost;
//                                break;
//                            case("Large"):
//                                orderCost = orderCost * 2;
//                                total += orderCost;
//                                break;
//                        }
//
//                        quantityTemp++;
//                        HomeFragment.setFloatingActionButtonVisibility(true);
//                    }
//                    else {
//                        Toast.makeText(context,"Dang! You can purchase only 5 item of same type",Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                holder.quan.setText(Long.toString(quantityTemp));
//                arrayList = new ArrayList<String>();
//                arrayList.add(Long.toString(quantityTemp));
//                arrayList.add(String.valueOf(orderCost));
//                arrayList.add(holder.spinner.getSelectedItem().toString());
//                order_name.put(holder.name.getText().toString() + " (" + arrayList.get(2) + ")", arrayList);
//
//                if(quantityTemp == 0)
//                    order_name.remove(holder.name.getText().toString());
//
//            }
//        });

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                System.out.println(order_name+"ordername");
                orderCost = foodPrice.get(position);
                selected = parent.getItemAtPosition(pos).toString();
                if(order_name.containsKey(holder.name.getText().toString() + " (" + selected + ")"))
                {
                    String orderName = holder.name.getText().toString() + " (" + selected + ")";
                    holder.elegantNumberButton.setNumber(order_name.get(orderName).get(0));
                    HomeFragment.setFloatingActionButtonVisibility(true);
                }
                else{
                    holder.elegantNumberButton.setNumber("0");
                }
                switch(selected) {
                    case("Small"):
                        holder.price.setText("₹ "+ orderCost);
                        break;
                    case("Medium"):
                        orderCost = orderCost * 1.5;
                        holder.price.setText("₹ "+ orderCost);
                        break;
                    case("Large"):
                        orderCost = orderCost * 2;
                        holder.price.setText("₹ "+ orderCost);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                String selected = "Small";
            }


        });

    }

    public void resetParams(){
        order_name = new HashMap<>();
        int total = 0;
    }

    @Override
    public int getItemCount()
    {
        return foodImg.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView img;
        TextView name;
        TextView price;
        TextView quan;
        Button add;
        Button subtract;
        Spinner spinner;
        ConstraintLayout parentLayout;
        ElegantNumberButton elegantNumberButton;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            img =itemView.findViewById(R.id.food_image);
            name=itemView.findViewById(R.id.food_name);
            price=itemView.findViewById(R.id.food_cost);
//            quan =itemView.findViewById(R.id.food_quantity);
//            add = itemView.findViewById(R.id.food_add);
//            subtract=itemView.findViewById(R.id.food_subtract);
            elegantNumberButton = itemView.findViewById(R.id.setQuantityButton);
            parentLayout=itemView.findViewById(R.id.parent_layout);
            spinner = itemView.findViewById(R.id.spinner);
        }
    }
}
