package com.auribisesmyplayschool.myplayschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.activity.SendDigitalMsgActivity;
import com.auribisesmyplayschool.myplayschool.bean.StudentBean;

import java.util.ArrayList;

/**
 * Created by Eshaan on 05-Dec-16.
 */
public class SelectStudentAdapter extends ArrayAdapter {

    ArrayList<StudentBean> list;
    Context context;

    public SelectStudentAdapter(Context context, int resource, ArrayList<StudentBean> list) {
        super(context, resource, list);
        this.context=context;
        this.list=list;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.select_student_adapter,null);
        CheckBox checkBox=rowView.findViewById(R.id.cbStu);
        TextView name=rowView.findViewById(R.id.textViewName);
        TextView phone=rowView.findViewById(R.id.textViewPhone);
        name.setText(list.get(position).getStuName());
        phone.setText(list.get(position).getBatch_title());
        if(SendDigitalMsgActivity.selectedStudents.contains(list.get(position))){
            checkBox.setChecked(true);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean selected) {
                if(selected){
                    SendDigitalMsgActivity.selectedStudents.add(list.get(position));
                }else{
                    SendDigitalMsgActivity.selectedStudents.remove(list.get(position));
                }
            }
        });
        return rowView;
    }

}
