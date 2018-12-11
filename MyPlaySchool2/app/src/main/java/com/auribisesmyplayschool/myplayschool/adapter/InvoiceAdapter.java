package com.auribisesmyplayschool.myplayschool.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.bean.FeeCostBean;

import java.util.ArrayList;

/**
 * Created by amandeep on 29/08/17.
 */

public class InvoiceAdapter extends ArrayAdapter {
    Context context;
    int resource;
    ArrayList<FeeCostBean> feeCostBeanArrayList;
    int lastPosition = -1;

    public InvoiceAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<FeeCostBean> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        feeCostBeanArrayList = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View myview = null;
        myview = LayoutInflater.from(context).inflate(resource,parent,false);
        TextView tvInvoiceTitle = (TextView)myview.findViewById(R.id.tvInvoiceTitle);
        TextView tvInvoiceDate = (TextView)myview.findViewById(R.id.tvInvoiceDate);
        TextView tvInvoiceBalance = (TextView)myview.findViewById(R.id.tvInvoiceBalance);
        tvInvoiceTitle.setText("Invoice - "+(position+1));
        tvInvoiceDate.setText("Date: "+feeCostBeanArrayList.get(position).getInvoiceDate());
        tvInvoiceBalance.setText((feeCostBeanArrayList.get(position).getTotalSellingCost()-feeCostBeanArrayList.get(position).getTotalCreditAmount())+"");
        Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        myview.startAnimation(animation);
        lastPosition = position;

        return myview;
    }
}
