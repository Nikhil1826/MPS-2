package com.auribisesmyplayschool.myplayschool.adapter;

/**
 * Created by ITI on 11-08-2015.
 */

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
import com.auribisesmyplayschool.myplayschool.bean.BatchBean;

import java.util.ArrayList;

public class ViewBatchAdapter extends ArrayAdapter{

    ArrayList<BatchBean> list,searchlist;
    Context cxt;
    int lastPosition = -1;

    public ViewBatchAdapter(Context context, int resource, ArrayList<BatchBean> list) {
        super(context, resource,list);

        cxt = context;
        this.list = list;
        searchlist=new ArrayList<>();
        searchlist.addAll(list);

    }
    public void filter(String search){

        if(search.isEmpty()){
            list.clear();
            list.addAll(searchlist);
        }else{
            list.clear();
            for(int i = 0; i < searchlist.size() ; i++ ){
                if(searchlist.get(i).getBatch_title().toLowerCase().contains(search.toLowerCase())){
                    list.add(searchlist.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View myView;
        LayoutInflater inflater = (LayoutInflater)cxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myView  = inflater.inflate(R.layout.adapter_batch, parent,false);
        TextView batch = (TextView)myView.findViewById(R.id.txtBatch);
        TextView seat = (TextView)myView.findViewById(R.id.txtSeat);
        TextView tvBatchActiveStudent = (TextView)myView.findViewById(R.id.tvBatchActiveStudent);
        TextView tvBatchInactiveStudent = (TextView)myView.findViewById(R.id.tvBatchInactiveStudent);
        TextView tvBatchLaterJoiningStudent = (TextView)myView.findViewById(R.id.tvBatchLaterJoiningStudent);
        TextView course = (TextView)myView.findViewById(R.id.tv_b_course);
        View view = myView.findViewById(R.id.typeView);

        BatchBean batchBean = list.get(position);
        batch.setText(batchBean.getBatch_title());
        seat.setText("Seats: "+batchBean.getBatch_seat());
        tvBatchActiveStudent.setText("Active: "+batchBean.getActiveStudents());
        tvBatchInactiveStudent.setText("Inactive: "+batchBean.getInActiveStudents());
//        tvBatchLaterJoiningStudent.setText("Joining: "+batchBean.getLaterJoinings());
        tvBatchLaterJoiningStudent.setVisibility(View.GONE);
        course.setText("Class: "+batchBean.getCoursetitle());
        if(batchBean.getBatchStatus()==0){
            view.setBackgroundColor(ContextCompat.getColor(cxt, R.color.colorOrange));
        } else {
            view.setBackgroundColor(ContextCompat.getColor(cxt, R.color.colorGreen));
        }

        Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        myView.startAnimation(animation);
        lastPosition = position;
        return myView;
    }
}
