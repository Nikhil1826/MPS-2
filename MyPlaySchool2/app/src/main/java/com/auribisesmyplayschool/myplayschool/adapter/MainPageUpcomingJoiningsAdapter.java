package com.auribisesmyplayschool.myplayschool.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.bean.StudentBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;

import java.util.ArrayList;

/**
 * Created by aman on 30/7/17.
 */

public class MainPageUpcomingJoiningsAdapter extends RecyclerView.Adapter<MainPageUpcomingJoiningsAdapter.CustomViewHolder>{
    ArrayList<StudentBean> stuUpcomingJoiningsArrayList;
    Context context;
    View itemView;
    SharedPreferences preferences;

    public MainPageUpcomingJoiningsAdapter(ArrayList<StudentBean> stuUpcomingJoiningsArrayList, Context context) {
        this.stuUpcomingJoiningsArrayList = stuUpcomingJoiningsArrayList;
        this.context = context;
        preferences=context.getSharedPreferences(AttUtil.shpREG, context.MODE_PRIVATE);

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_edu_enquiry_listitem_small, parent, false);
        return new CustomViewHolder(itemView);

    }



    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        StudentBean studentBean=stuUpcomingJoiningsArrayList.get(position);
        try{
            if(studentBean.getImage().toString().length()>0){
//                Picasso.with(context).load(studentBean.getImage())
//                        .into(holder.imageView);
            }else{
                /*Picasso.with(context).load(R.drawable.app_icon)
                        .into(holder.imageView);*/
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        holder.textViewName.setText(studentBean.getStuName());
        holder.textViewDateTime.setText(studentBean.getJoinDate());
        holder.textViewEnqSubject.setVisibility(View.GONE);
//        holder.tvEnqDays.setVisibility(View.GONE);

    }



    @Override
    public int getItemCount() {
        return stuUpcomingJoiningsArrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName,textViewDateTime,textViewEnqSubject,tvEnqDays;
        View view;
        //CircleImageView imageView;

        public CustomViewHolder(View itemView) {
            super(itemView);
            //imageView = (CircleImageView)itemView.findViewById(R.id.tvEnqImage);
            textViewName = (TextView)itemView.findViewById(R.id.tvEnqName);
            textViewEnqSubject = (TextView)itemView.findViewById(R.id.tvEnqSubject);
            textViewDateTime = (TextView)itemView.findViewById(R.id.tvEnqDateTime);
//            tvEnqDays = (TextView)itemView.findViewById(R.id.tvEnqDays);
            view = itemView.findViewById(R.id.view_enquiry_item);
        }
    }
}
