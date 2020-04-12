package com.example.healthify.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.healthify.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class DashboardFragment extends Fragment
{

    private DashboardViewModel dashboardViewModel;
    private boolean activeOrder;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        TextView emailTextView = root.findViewById(R.id.textUpperDashboardFragment);
        TextView activeOrderTextView = root.findViewById(R.id.textLowerDashboardFragment);

        activeOrder = getArguments().getBoolean("activeOrder");
        emailTextView.setText(getArguments().getString("user_email"));
        activeOrderTextView.setText("Currently Active Order:" + new Boolean(activeOrder).toString());
        return root;
    }
    public void setActiveOrder(boolean activeOrder){
        this.activeOrder = activeOrder;
    }
    public void resetTextView(){
        TextView activeOrderTextView = getActivity().findViewById(R.id.textLowerDashboardFragment);
        activeOrderTextView.setText("Currently Active Order:" + new Boolean(activeOrder).toString());
    }

}