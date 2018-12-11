package com.auribisesmyplayschool.myplayschool.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.bean.AccountBean;

import java.util.ArrayList;

/**
 * Created by amandeep on 23/08/17.
 */

public class AccountDetailsAdapter extends ArrayAdapter {
    Context context;
    int resource;
    ArrayList<AccountBean> accountBeanArrayList;
    int lastPosition = -1;

    public AccountDetailsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<AccountBean> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        accountBeanArrayList = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View myview = null;
        myview = LayoutInflater.from(context).inflate(resource,parent,false);
        TextView tvAccountBalAmount = (TextView)myview.findViewById(R.id.tvAccountBalAmount);
        TextView tvAccountType = (TextView)myview.findViewById(R.id.tvAccountType);
        TextView tvAccountDateTime = (TextView)myview.findViewById(R.id.tvAccountDateTime);
        LinearLayout rlAccountBalance = (LinearLayout)myview.findViewById(R.id.rlAccountBalance);
        RelativeLayout rlAccountBalAmount = (RelativeLayout)myview.findViewById(R.id.rlAccountBalAmount);
        tvAccountBalAmount.setText(accountBeanArrayList.get(position).getAmount()+"");
        if(accountBeanArrayList.get(position).getInOutType()==0)
            tvAccountType.setText("By Cheque");
        if(accountBeanArrayList.get(position).getInOutType()==1)
            tvAccountType.setText("By Cash");
        if(accountBeanArrayList.get(position).getInOutType()==2)
            tvAccountType.setText("By NEFT");

        if(accountBeanArrayList.get(position).getSignal()==0)
            tvAccountDateTime.setText(""+accountBeanArrayList.get(position).getDate()+" "
                    +accountBeanArrayList.get(position).getTime()+"\nCancelled");
        else
            tvAccountDateTime.setText(""+accountBeanArrayList.get(position).getDate()+" "+accountBeanArrayList.get(position).getTime());
        if(accountBeanArrayList.get(position).getInOutSignal()==0){
            rlAccountBalance.setGravity(Gravity.RIGHT);
            rlAccountBalAmount.setGravity(Gravity.RIGHT);
            tvAccountDateTime.setGravity(Gravity.RIGHT);
            tvAccountType.setGravity(Gravity.RIGHT);
        }
        if(accountBeanArrayList.get(position).getInOutSignal()==1) {
            rlAccountBalance.setGravity(Gravity.LEFT);
            rlAccountBalAmount.setGravity(Gravity.LEFT);
            tvAccountDateTime.setGravity(Gravity.LEFT);
            tvAccountType.setGravity(Gravity.LEFT);
        }

        Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        myview.startAnimation(animation);
        lastPosition = position;

        return myview;
    }
}
