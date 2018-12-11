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
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BusesBean;

import java.util.ArrayList;

/**
 * Created by tania on 31/1/18.
 */

public class Spn_Adapter_Bus extends BaseAdapter
{
    Context context;
    int resource;
    ArrayList<BusesBean> busBeanArrayList;


    public Spn_Adapter_Bus(@NonNull Context context, int resource, @NonNull ArrayList<BusesBean> objects)
    {
        this.resource = resource;
        this.context = context;
        this.busBeanArrayList = objects;
    }


    @Override
    public int getCount() {
        return busBeanArrayList.size();
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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View view = null;
        view = LayoutInflater.from(context).inflate(resource,parent,false);

        TextView busNumber = view.findViewById(R.id.sp_bus_number);
        busNumber.setText(busBeanArrayList.get(position).getBusNumber());

        TextView busDesc = view.findViewById(R.id.sp_bus_desc);
        busDesc.setText(busBeanArrayList.get(position).getBusDesc());
        View view1 = view.findViewById(R.id.vSpnBus);
        if(busBeanArrayList.get(position).getActivate()==0){
            view1.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrange));
        }

        return view;
    }


}
