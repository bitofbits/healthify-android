package com.example.healthify.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.healthify.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private ArrayList<String> foodImg = new ArrayList<>();
    private ArrayList<String> foodName = new ArrayList<>();
    private ArrayList<Integer> foodPrice = new ArrayList<>();
    private Context context;
    public boolean activeOrder = false;
    public HashMap<String,Long> order_name = new HashMap<>();
    public int total = 0;
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
        holder.subtract.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                System.out.println(context.toString());
                long tmp =Long.parseLong(holder.quan.getText().toString());

                //Button work only if customer don't have an active order
                if(activeOrder) {
                    tmp = 0;
                    Toast.makeText(context,"Sorry! You have already placed an Order",Toast.LENGTH_SHORT).show();
                    HomeFragment.setFloatingActionButtonVisibility(false);
                }
                else
                    {
                        if(tmp>0) {
                            total-=Integer.parseInt(foodPrice.get(position).toString());
                            tmp--;
                        }
                }

                if(total == 0){
                    HomeFragment.setFloatingActionButtonVisibility(false);
                }
                holder.quan.setText(Long.toString(tmp));
                order_name.put(holder.name.getText().toString(),tmp);
                if(tmp==0)
                    order_name.remove(holder.name.getText().toString());
            }
        });
        holder.add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Long x = Long.parseLong(holder.quan.getText().toString());

                //Button work only if customer don't have an active order
                if(activeOrder) {
                    x = 0l;
                    Toast.makeText(context,"Sorry! You have already placed an Order",Toast.LENGTH_SHORT).show();
                    HomeFragment.setFloatingActionButtonVisibility(false);
                }
                else{
                    if(x < 5) {
                        x++;
                        total+=Integer.parseInt(foodPrice.get(position).toString());
                        HomeFragment.setFloatingActionButtonVisibility(true);
                    }
                    else {
                        Toast.makeText(context,"Dang! You can purchase only 5 item of same type",Toast.LENGTH_SHORT).show();
                    }
                }


                holder.quan.setText(Long.toString(x));
                order_name.put(holder.name.getText().toString(),x);
                if(x==0)
                    order_name.remove(holder.name.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return foodImg.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView name;
        TextView price;
        TextView quan;
        Button add;
        Button subtract;
        ConstraintLayout parentLayout;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            img =itemView.findViewById(R.id.food_image);
            name=itemView.findViewById(R.id.food_name);
            price=itemView.findViewById(R.id.food_cost);
            quan =itemView.findViewById(R.id.food_quantity);
            add = itemView.findViewById(R.id.food_add);
            subtract=itemView.findViewById(R.id.food_subtract);
            parentLayout=itemView.findViewById(R.id.parent_layout);
        }
    }
}
