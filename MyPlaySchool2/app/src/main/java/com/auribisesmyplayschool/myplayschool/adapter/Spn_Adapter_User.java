package com.auribisesmyplayschool.myplayschool.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.UserBean;

import java.util.ArrayList;

/**
 * Created by tania on 31/1/18.
 */

public class Spn_Adapter_User extends BaseAdapter
{
    Context context;
    int resource;
    ArrayList<UserBean> userBeanArrayList;


    public Spn_Adapter_User(@NonNull Context context, int resource, @NonNull ArrayList<UserBean> objects)
    {
        this.resource = resource;
        this.context = context;
        this.userBeanArrayList = objects;

    }

    @Override
    public int getCount() {
        return userBeanArrayList.size();
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

        TextView userName = view.findViewById(R.id.sp_user_name);
        userName.setText(userBeanArrayList.get(position).getUserName());

        TextView userEmail = view.findViewById(R.id.sp_user_email);

        if(userBeanArrayList.get(position).getUserEmail().equals("")||userBeanArrayList.get(position).getUserEmail().equals(null))
            userEmail.setText("NA");
        else
            userEmail.setText(userBeanArrayList.get(position).getUserEmail());

        TextView userContact = view.findViewById(R.id.sp_user_contact);
        if(userBeanArrayList.get(position).getUserContact().equals("")||userBeanArrayList.get(position).getUserContact().equals(null))
            userContact.setText("NA");
        else
            userContact.setText(userBeanArrayList.get(position).getUserContact());
        View view1 = view.findViewById(R.id.vSpnUser);
        if(userBeanArrayList.get(position).getUserStatus()==0){
            view1.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrange));
        }


        return view;
    }


}
