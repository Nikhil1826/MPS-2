package com.auribisesmyplayschool.myplayschool.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.bean.TeacherBean;

import java.util.ArrayList;

/**
 * Created by kshitij on 04/04/17.
 */

public class TeacherAdapter extends ArrayAdapter {
    Context context;
    int resource;
    ArrayList<TeacherBean> teacherBeanArrayList;

    public TeacherAdapter(Context context, int resource, ArrayList<TeacherBean> teacherBeanArrayList) {
        super(context, resource, teacherBeanArrayList);
        this.context=context;
        this.resource=resource;
        this.teacherBeanArrayList=teacherBeanArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(resource,null);
        TextView txtName=(TextView)rowView.findViewById(R.id.txtName);
        TextView txtPhone=(TextView)rowView.findViewById(R.id.txtPhone);
        TextView txtEmail=(TextView)rowView.findViewById(R.id.txtEmail);
        TeacherBean teacherBean=teacherBeanArrayList.get(position);
        txtName.setText(teacherBean.getUserName());
        txtPhone.setText(teacherBean.getUserContact());
        txtEmail.setText(teacherBean.getUserEmail());
        return rowView;
    }
}
