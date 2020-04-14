package com.example.healthify.ui.dashboard;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.healthify.R;
import com.example.healthify.ui.home.Adapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterDashboard extends Adapter {
    private final ArrayList mData;
    public AdapterDashboard(HashMap<String, ArrayList<String>> map){
        super(map);
        mData = new ArrayList();
        mData.addAll(map.entrySet());
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;
        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_dashboard_list, parent, false);
        } else {
            result = convertView;
        }
        Map.Entry<String, ArrayList<String>> item = getItem(position);
        Log.v("Adapter", "item.getKey() ka value" + item.getKey());
        Log.v("Adapter", "item.getValue() ka value" + item.getValue());
        TextView orderName = (TextView) result.findViewById(R.id.orderNameConfirmationDashboard);
        orderName.setText(item.getKey());
        TextView orderQuantity = (TextView) result.findViewById(R.id.orderQuantityConfirmationDashBoard);
        String value = item.getValue().get(0);
        orderQuantity.setText(value);
        TextView orderPrice = (TextView) result.findViewById(R.id.orderPriceConfirmationDashboard);
        String price = item.getValue().get(1);
        orderPrice.setText("₹ " + price);
        return result;
    }

}
