package com.auribisesmyplayschool.myplayschool.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.RoutesBean;

import java.util.ArrayList;

/**
 * Created by tania on 31/1/18.
 */

public class Spn_Adapter_Route extends BaseAdapter {
    Context context;
    int resource;
    ArrayList<RoutesBean> routeBeanArrayList;


    public Spn_Adapter_Route(@NonNull Context context, int resource, @NonNull ArrayList<RoutesBean> objects) {
        this.resource = resource;
        this.context = context;
        this.routeBeanArrayList = objects;
    }


    @Override
    public int getCount() {
        return routeBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        view = LayoutInflater.from(context).inflate(resource, parent, false);

        TextView busNumber = view.findViewById(R.id.sp_route_name);
        busNumber.setText(routeBeanArrayList.get(position).getRouteName());

        TextView busDesc = view.findViewById(R.id.sp_route_desc);
        busDesc.setText(routeBeanArrayList.get(position).getRouteDesc());

        View view1 = view.findViewById(R.id.vSpnRoute);
        if(routeBeanArrayList.get(position).getActivate()==0){
            view1.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrange));
        }

        return view;
    }
}
