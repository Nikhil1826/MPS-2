package com.auribisesmyplayschool.myplayschool.adminApp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BranCourBean;

import java.util.ArrayList;

public class BranchCourseAdapter extends ArrayAdapter {
    Context cxt;
    ArrayList<BranCourBean> list,searchlist;
    BranCourBean branCourBean;
    int resource;
    public BranchCourseAdapter(Context context, int resource, ArrayList<BranCourBean> objects) {
        super(context, resource,objects);
        cxt = context;
        list = objects;
        searchlist=new ArrayList<>();
        searchlist.addAll(list);
        this.resource = resource;
    }
    public void filter(String search){

        if(search.isEmpty()){
            list.clear();
            list.addAll(searchlist);
        }else{
            list.clear();
            for(int i = 0; i < searchlist.size() ; i++ ){
                if(searchlist.get(i).getCourseName().toLowerCase().contains(search.toLowerCase())){
                    list.add(searchlist.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View myView = null;
        LayoutInflater inflater = (LayoutInflater) cxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myView = inflater.inflate(resource, parent, false);

        TextView branchName,courseName,courseFees,courseDuration,courseHours,courseDiscount;
        View view;

        //branchName = (TextView) myView.findViewById(R.id.branchName);
        courseName = (TextView) myView.findViewById(R.id.courseName);
        courseFees = (TextView) myView.findViewById(R.id.courseFees);
        courseDuration = (TextView) myView.findViewById(R.id.courseDuration);
        courseHours = (TextView) myView.findViewById(R.id.courseHours);
        view = (View)myView.findViewById(R.id.view);

        branCourBean = list.get(position);

        if(branCourBean.getCourseStatus() == 0){
            view.setBackgroundColor(Color.parseColor("#FFC62828"));
        } else {
            view.setBackgroundColor(Color.parseColor("#FF64DD17"));
        }

        String toshowstring = "<font color='red'>Course Name: </font>";

        if (branCourBean.getCourseName() == null)
            courseName.setText("Course Name: </font>" + "NA");
        else
            courseName.setText( branCourBean.getCourseName());

        if (branCourBean.getCourseFees() == null)
            courseFees.setText("Fee: " + "NA");
        else
            courseFees.setText("Fee: " + branCourBean.getCourseFees() + "/-");

        if (branCourBean.getCourseDuration() == null)
            courseDuration.setText("Day's: " + "NA");
        else
            courseDuration.setText("Day's: " + branCourBean.getCourseDuration());

        if (branCourBean.getCourseHours() == null)
            courseHours.setText("Hour's: " + "NA");
        else
            courseHours.setText("Hour's: " + branCourBean.getCourseHours() + "/day");


        return myView;
    }
}
