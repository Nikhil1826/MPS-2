package com.auribisesmyplayschool.myplayschool.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.bean.TeacherBean;

import java.util.ArrayList;

public class AssignTeacherAdapter extends ArrayAdapter {
    Context context;
    int resource;
    ArrayList<TeacherBean> teacherBeanArrayList;

    public AssignTeacherAdapter(Context context, int resource, ArrayList<TeacherBean> teacherBeanArrayList) {
        super(context, resource, teacherBeanArrayList);
        this.context=context;
        this.resource=resource;
        this.teacherBeanArrayList=teacherBeanArrayList;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resource,null);
        CheckBox cbAssign = (CheckBox)rowView.findViewById(R.id.cbAssign);
        TextView txtPhone = (TextView)rowView.findViewById(R.id.txtTeacherPhone);
        //cbAssign.setText(teacherBeanArrayList.get(position).getUserName());
        txtPhone.setText(" Name: "+teacherBeanArrayList.get(position).getUserName()+
                "\n Phone: "+teacherBeanArrayList.get(position).getUserContact()+
                "\n No. of sections: "+teacherBeanArrayList.get(position).getBatchCount()+
                "\n sections: "+teacherBeanArrayList.get(position).getBatchGroup());

        if(teacherBeanArrayList.get(position).isEnabled())
            cbAssign.setChecked(true);
        else
            cbAssign.setChecked(false);

        cbAssign.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    teacherBeanArrayList.get(position).setEnabled(true);
                }else{
                    teacherBeanArrayList.get(position).setEnabled(false);
                }
            }
        });
        return rowView;
    }
}
