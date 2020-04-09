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

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private ArrayList<String> food_img = new ArrayList<>();
    private ArrayList<String> food_name = new ArrayList<>();
    private ArrayList<Integer> food_price = new ArrayList<>();
    private Context context;
    public HashMap<String,Integer> order_name = new HashMap<>();
    public int total=0;
    public RecyclerViewAdapter(Context context, ArrayList<String> image, ArrayList<String> img_name,ArrayList<Integer> food_price)
    {
        this.food_img = image;
        this.food_name = img_name;
        this.context = context;
        this.food_price = food_price;
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
    {
        if(food_img.get(position).equals(""))
        {
            holder.img.setImageResource(R.drawable.capture);
        }
        else
        {
            Glide.with(context).asBitmap().load(food_img.get(position)).into(holder.img);
        }
        holder.name.setText(food_name.get(position));
        holder.price.setText("₹ "+food_price.get(position).toString());
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
                int tmp =Integer.parseInt(holder.quan.getText().toString());
                if(tmp>0)
                {
                    total-=Integer.parseInt(food_price.get(position).toString());
                    tmp--;
                }
                holder.quan.setText(Integer.toString(tmp));
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
                int x =Integer.parseInt(holder.quan.getText().toString());
                if(x<5)
                {
                    total+=Integer.parseInt(food_price.get(position).toString());
                    x++;
                }
                holder.quan.setText(Integer.toString(x));
                order_name.put(holder.name.getText().toString(),x);
                if(x==0)
                    order_name.remove(holder.name.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return food_img.size();
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
