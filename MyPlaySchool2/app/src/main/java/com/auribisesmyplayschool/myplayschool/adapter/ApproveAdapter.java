package com.auribisesmyplayschool.myplayschool.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.bean.StudentBean;

import java.util.ArrayList;

/**
 * Created by Eshaan on 27-Dec-16.
 */
public class ApproveAdapter extends ArrayAdapter {
    Context context;
    int resource, balShowSignal = 1;
    ArrayList<StudentBean> studentBeanArrayList, tmplist, searchlist;
    int lastPosition = -1;
    StudentBean bean;
    ProgressDialog pd;
    String bal = "";

    public ApproveAdapter(Context context, int resource, ArrayList<StudentBean> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        studentBeanArrayList = objects;
        searchlist = new ArrayList<>();
        searchlist.addAll(objects);
        pd = new ProgressDialog(context);
    }

    public ApproveAdapter(Context context, int resource, ArrayList<StudentBean> objects, int balShowSignal) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        studentBeanArrayList = objects;
        searchlist = new ArrayList<>();
        searchlist.addAll(objects);
        pd = new ProgressDialog(context);
        this.balShowSignal = balShowSignal;
    }

    public void filter(String search) {

        if (search.isEmpty()) {
            studentBeanArrayList.clear();
            studentBeanArrayList.addAll(searchlist);
        } else {
            studentBeanArrayList.clear();
            for (int i = 0; i < searchlist.size(); i++) {
                if (searchlist.get(i).getStuName().toLowerCase().contains(search.toLowerCase())) {
                    studentBeanArrayList.add(searchlist.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View myview = null;
        myview = LayoutInflater.from(context).inflate(R.layout.adapter_approve_students, parent, false);
        TextView textView = (TextView) myview.findViewById(R.id.txtName);
        TextView textView1 = (TextView) myview.findViewById(R.id.txtRegId);
        TextView textView2 = (TextView) myview.findViewById(R.id.txtPhone);
        TextView textView3 = (TextView) myview.findViewById(R.id.txtEmailId);
        TextView textView4 = (TextView) myview.findViewById(R.id.txtBalance);
        TextView tvAppInstallOrNot = (TextView) myview.findViewById(R.id.tvAppInstallOrNot);
        Button btnApprove = (Button) myview.findViewById(R.id.btnApprove);
        //TextView textView5 = (TextView)myview.findViewById(R.id.txtTotal);
        View view = (View) myview.findViewById(R.id.typeView);
        bean = studentBeanArrayList.get(position);

        textView.setText(bean.getStuName() + "");
        textView1.setText(bean.getRegId() + "");
        textView3.setText(bean.getFatherEmail() + "");
        if (bean.getBatchId() == 0) {
            textView2.setText("Registration Date : " + bean.getDate());
            textView4.setText("Joining Date : " + bean.getJoinDate());
        } else {
            if (bean.getAdmStatus() == 1) {
                textView4.setText("Registration Date : " + bean.getDate());
                textView2.setText("Joining Date : " + bean.getJoinDate());
            } else if (bean.getAdmStatus() == 2) {
                textView4.setText("Registration Date : " + bean.getDate());
                textView2.setText("Starting Date : " + bean.getStartingDate());
            } else if (bean.getAdmStatus() == 3) {
                textView4.setText("Starting Date : " + bean.getStartingDate());
                textView2.setText("Finish Date : " + bean.getFinishDate());
            } else if (bean.getAdmStatus() == 4) {
                textView4.setText("Starting Date : " + bean.getStartingDate());
                textView2.setText("Finish Date : " + bean.getFinishDate() + " -  promoted");
            }
        }
        String s = "Class: " + bean.getCourseName() + " | Section: " + bean.getBatch_title();
        if (bean.getAdmStatus() == 2 || bean.getAdmStatus() == 3 || bean.getAdmStatus() == 4) {
            try {
                bal = "" + (bean.getTotalSellingCost() - bean.getTotalCreditAmount());
                if (bal.toString().equals("0"))
                    bal = " | OutStanding: Not Available";
                else
                    bal = " | OutStanding: " + bal;
            } catch (Exception e) {
                bal = " | OutStanding: Not Available";
            }
            try {
                if (balShowSignal == 1) {
                    if (studentBeanArrayList.get(position).getDevice_id().toString().length() > 0)
                        s = s + "\nApp Installed ";

                    else
                        s = s + "\nApp Not Installed ";
                } else if (balShowSignal == 2) {
                    s = s + "\n" + studentBeanArrayList.get(position).getAddress();
                } else {
                    if (studentBeanArrayList.get(position).getDevice_id().toString().length() > 0)
                        s = s + "\nApp Installed " + bal;
                    else
                        s = s + "\nApp Not Installed " + bal;
                }

            } catch (Exception e) {
                if (balShowSignal == 1)
                    s = s + "\nApp Not Installed ";
                else if (balShowSignal == 2)
                    s = s + "\n" + studentBeanArrayList.get(position).getAddress();
                else
                    s = s + "\nApp Not Installed " + bal;

            }
            tvAppInstallOrNot.setText(s);
        } else
            tvAppInstallOrNot.setVisibility(View.GONE);

        return myview;
    }

}
