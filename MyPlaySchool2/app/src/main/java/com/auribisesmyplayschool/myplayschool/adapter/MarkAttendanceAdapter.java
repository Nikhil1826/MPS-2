package com.auribisesmyplayschool.myplayschool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.bean.StuAttendanceBean;
import com.auribisesmyplayschool.myplayschool.bean.StudentBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.auribisesmyplayschool.myplayschool.classes.MarkAttendanceInterface;

import java.util.ArrayList;

/**
 * Created by amandeep on 30/01/18.
 */

public class MarkAttendanceAdapter extends RecyclerView.Adapter<MarkAttendanceAdapter.CustomViewHolder> {
    ArrayList<StudentBean> studentBeanArrayList;
    ArrayList<StuAttendanceBean> stuAttendanceBeanArrayList;
    Context context;
    View itemView;
    MarkAttendanceInterface markAttendanceInterface;
    int actionChoser=0;

    public MarkAttendanceAdapter(ArrayList<StudentBean> studentBeanArrayList, Context context,
                                 MarkAttendanceInterface markAttendanceInterface, int actionChoser) {
        this.studentBeanArrayList = studentBeanArrayList;
        this.context = context;
        this.markAttendanceInterface = markAttendanceInterface;
        this.actionChoser =actionChoser;
    }

    public MarkAttendanceAdapter(ArrayList<StuAttendanceBean> stuAttendanceBeanArrayList,
                                 Context context, int actionChoser,
                                 MarkAttendanceInterface markAttendanceInterface) {
        this.stuAttendanceBeanArrayList = stuAttendanceBeanArrayList;
        this.context = context;
        this.actionChoser =actionChoser;
        this.markAttendanceInterface = markAttendanceInterface;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_mark_attendance, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
//        Log.i("test",position+ " - "+studentBeanArrayList.size());
//        if(position==(studentBeanArrayList.size()-1))
//            holder.vMarkAtteendance.setVisibility(View.GONE);
        if(actionChoser==0){
            String d = AttUtil.getFormattedDate(studentBeanArrayList.get(position).getDob());
            holder.tvMAStuName.setText(studentBeanArrayList.get(position).getStuName());
            holder.tvMAFatherName.setText(studentBeanArrayList.get(position).getFatherName());
            holder.tvMAMotherName.setText(studentBeanArrayList.get(position).getMotherName());
            holder.tvMADOBName.setText(d+"");
            if(studentBeanArrayList.get(position).isMarkAttendance())
                holder.cbMAttendance.setChecked(true);
            else
                holder.cbMAttendance.setChecked(false);

            holder.cbMAttendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(studentBeanArrayList.get(position).isMarkAttendance())
                        markAttendanceInterface.markAttendance(false,position);
                    else
                        markAttendanceInterface.markAttendance(true,position);
                }
            });
        }else if(actionChoser==1){
            String d = AttUtil.getFormattedDate(stuAttendanceBeanArrayList.get(position).getDate());
            String t = AttUtil.getFormattedTime(stuAttendanceBeanArrayList.get(position).getTime());
            holder.tvMAStuName.setText(d +" "+ t);
            holder.tvMAFatherName.setText("By: "+stuAttendanceBeanArrayList.get(position).getUserName()+"");
            if(stuAttendanceBeanArrayList.get(position).getStatus()==0)
                holder.tvMAMotherName.setText("Discarded");
            else
                holder.tvMAMotherName.setVisibility(View.GONE);
            holder.tvMADOBName.setVisibility(View.GONE);
            if(stuAttendanceBeanArrayList.get(position).isMarked())
                holder.cbMAttendance.setChecked(true);
            else
                holder.cbMAttendance.setChecked(false);
            holder.cbMAttendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(stuAttendanceBeanArrayList.get(position).isMarked())
                        markAttendanceInterface.markAttendance(false,position);
                    else
                        markAttendanceInterface.markAttendance(true,position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(actionChoser==0)
            return studentBeanArrayList.size();
        else
            return stuAttendanceBeanArrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tvMAStuName, tvMAFatherName, tvMAMotherName, tvMADOBName;
        View vMarkAtteendance;
        CheckBox cbMAttendance;

        public CustomViewHolder(View itemView) {
            super(itemView);
            tvMAStuName = this.itemView.findViewById(R.id.tvMAStuName);
            tvMAFatherName = this.itemView.findViewById(R.id.tvMAFatherName);
            tvMAMotherName = this.itemView.findViewById(R.id.tvMAMotherName);
            tvMADOBName = this.itemView.findViewById(R.id.tvMADOBName);
            cbMAttendance = this.itemView.findViewById(R.id.cbMAttendance);
//            vMarkAtteendance = this.itemView.findViewById(R.id.vMarkAtteendance);
        }
    }
}

