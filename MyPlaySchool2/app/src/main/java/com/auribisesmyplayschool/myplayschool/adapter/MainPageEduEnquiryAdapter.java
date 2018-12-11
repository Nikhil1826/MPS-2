package com.auribisesmyplayschool.myplayschool.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
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
 * Created by Amandeep on 25-Jan-17.
 */
public class MainPageEduEnquiryAdapter extends RecyclerView.Adapter<MainPageEduEnquiryAdapter.CustomViewHolder>{
    ArrayList<StudentBean> stuEduEnquiryArrayList;
    Context context;
    View itemView;
    SharedPreferences preferences;

    public MainPageEduEnquiryAdapter(ArrayList<StudentBean> stuEduEnquiryArrayList, Context context ) {
        this.stuEduEnquiryArrayList=stuEduEnquiryArrayList;
        this.context=context;
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
        StudentBean bean = stuEduEnquiryArrayList.get(position);
        try{
            if(bean.getImage().toString().length()>0){
//                Picasso.with(context).load(bean.getImage())
//                        .into(holder.imageView);
            }else{
                /*Picasso.with(context).load(R.drawable.app_icon)
                        .into(holder.imageView);*/
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        holder.textView.setText(bean.getStuName());
        holder.textView2.setText(bean.getCurDate()+" "+bean.getCurTime());
        holder.textView1.setText(bean.getCourseName());
//        holder.tvEnqDays.setVisibility(View.GONE);
        /*if(bean.getEnqStatus()==0){
            if(bean.getUserType()==0){
                holder.view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSeaGreenLight));
            }
            if(bean.getUserType()==2){
                holder.view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorYellowLight));
            }
            if(bean.getUserType()==3) {
                holder.view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBlueLight));
            }
        }
        else if(bean.getEnqStatus()==1){
            holder.view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreenLight));
        }
        else if(bean.getEnqStatus()==2){
            holder.view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorOrangeLight));
        }*/
        holder.textView.setSelected(true);
        holder.textView1.setSelected(true);
        holder.textView2.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return stuEduEnquiryArrayList.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder{
        TextView textView,textView1,textView2,tvEnqDays;
        CardView view;
        //CircleImageView imageView;

        public CustomViewHolder(View itemView) {
            super(itemView);
            //imageView = (CircleImageView)itemView.findViewById(R.id.tvEnqImage);
            textView = itemView.findViewById(R.id.tvEnqName);
            textView2 = itemView.findViewById(R.id.tvEnqSubject);
            textView1 = itemView.findViewById(R.id.tvEnqDateTime);
//            tvEnqDays = itemView.findViewById(R.id.tvEnqDays);
            view = itemView.findViewById(R.id.view_enquiry_item);
        }
    }
}
