package com.auribisesmyplayschool.myplayschool.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.bean.EnquiryBean;
import com.auribisesmyplayschool.myplayschool.bean.StudentBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;

import java.util.ArrayList;

public class EduEnquiryAdapter extends ArrayAdapter {
    int resource;
    ArrayList<StudentBean> studentBeanArrayList, searchlist;
    int lastPosition = -1;
    Context context;

    public EduEnquiryAdapter(Context context, int resource, ArrayList<StudentBean> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        studentBeanArrayList = objects;
        searchlist = new ArrayList<>();
        searchlist.addAll(objects);
    }

    public void filter(String search) {
        if (search.isEmpty()) {
            studentBeanArrayList.clear();
            studentBeanArrayList.addAll(searchlist);
        } else {
            studentBeanArrayList.clear();
            for (int i = 0; i < searchlist.size(); i++) {
                if (searchlist.get(i).getStuName().toLowerCase().contains(search.toLowerCase()) ||
                        searchlist.get(i).getEnquiryBean().getCourseName().toLowerCase().contains(search.toLowerCase())) {
                    studentBeanArrayList.add(searchlist.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View myview = null;
        myview = LayoutInflater.from(context).inflate(resource, parent, false);
        TextView textView = (TextView) myview.findViewById(R.id.tvEnqName);
        TextView textView1 = (TextView) myview.findViewById(R.id.tvEnqSubject);
        TextView textView2 = (TextView) myview.findViewById(R.id.tvEnqDateTime);
        TextView tvEnqNumber = (TextView) myview.findViewById(R.id.tvEnqNumber);
        View view = (View) myview.findViewById(R.id.view_enquiry_item);
        StudentBean bean = studentBeanArrayList.get(position);
        textView.setText(bean.getStuName());
        textView2.setText(AttUtil.getFormattedDate( bean.getCurDate()) + " - " + AttUtil.getFormattedTime(bean.getCurTime()));
        //if(bean.getCourseid()==1){
        textView1.setText(bean.getEnquiryBean().getCourseName());
        int number = bean.getEnquiryBean().getEnqid();
        tvEnqNumber.setText("Enquiry Number: " + (number));
        /*
         * getStatus = 0 : enquiry
         * getStatus = 1 : register
         * getStatus = 2 : discard
         * getUserType = 0 : enquiry
         * getUserType = 2 : counselling
         * getUserType = 3 : demo
         * */
        if (bean.getEnquiryBean().getEnqStatus() == 0) {
            //view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSeaGreen));
            if (bean.getEnquiryBean().getUserType() == 0) {
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSeaGreen));
            }
            if (bean.getEnquiryBean().getUserType() == 2) {
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorYellow));
            }
            if (bean.getEnquiryBean().getUserType() == 3) {
                view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlue));
            }
        } else if (bean.getEnquiryBean().getEnqStatus() == 1) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen));
        } else if (bean.getEnquiryBean().getEnqStatus() == 2) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrange));
        }

        /*if(bean.getStuid()==0){
            textView3.setText("New User");
        }else{
            textView3.setText("Finto User");
        }*/

        Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        myview.startAnimation(animation);
        lastPosition = position;

        return myview;
    }
}
