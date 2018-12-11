package com.auribisesmyplayschool.myplayschool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.activity.TransportActivity;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.UserBean;

import java.util.ArrayList;

/**
 * Created by tania on 7/2/18.
 */

public class TvViewUserItemAdapter extends RecyclerView.Adapter<TvViewUserItemAdapter.ViewHolder>
{
    ArrayList<UserBean> arrayList,tempUserList;
    Context context;


    public TvViewUserItemAdapter(Context context, ArrayList<UserBean> arrayList)
    {
        this.arrayList = arrayList;
        this.context=context;
        tempUserList = new ArrayList<>();
        tempUserList.addAll(arrayList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listitem_view_users,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.txtUserName.setText(arrayList.get(position).getUserName());

        if(!(arrayList.get(position).getUserEmail().equals("")||arrayList.get(position).getUserEmail().equals(null)))
             holder.txtUserEmail.setText(arrayList.get(position).getUserEmail());
        else
             holder.txtUserEmail.setText("NA");

        if(arrayList.get(position).getBusId()==0 || String.valueOf(arrayList.get(position).getBusId()).equals(null))
        {

            holder.txtBusNumber.setText("NA");
            holder.txtMaidDriverEmail.setText("NA");
            holder.txtMaidDriverName.setText("NA");


            if(arrayList.get(position).getUserType()==4)
            {
                holder.txtTagMaidDriverEmail.setText("Maid Email:");
                holder.txtTagMaidDriverName.setText("Maid Name:");
            }
            else
            {
                holder.txtTagMaidDriverEmail.setText("Driver Email:");
                holder.txtTagMaidDriverName.setText("Driver Name:");
            }


        }

        else
        {
            int busId = arrayList.get(position).getBusId();
            holder.txtBusNumber.setText(TransportActivity.busesBeanHashMap.get(busId).getBusNumber());

            if(arrayList.get(position).getUserType()==4)
            {
                holder.txtTagMaidDriverEmail.setText("Maid Email:");
                holder.txtTagMaidDriverName.setText("Maid Name:");
                if(!(TransportActivity.busesBeanHashMap.get(busId).getDriverId()==0 ||
                        String.valueOf(TransportActivity.busesBeanHashMap.get(busId).getDriverId()).equals(null)))
                {
                    if(TransportActivity.busesBeanHashMap.get(busId).getMaidId()!=0)
                    {
                        int maidId = TransportActivity.busesBeanHashMap.get(busId).getMaidId();

                        holder.txtMaidDriverName.setText(TransportActivity.userBeanHashMap.get(maidId).getUserName());

                        holder.txtMaidDriverEmail.setText(TransportActivity.userBeanHashMap.get(maidId).getUserEmail());
                        if(!(TransportActivity.userBeanHashMap.get(maidId).getUserEmail().equals("")||TransportActivity.userBeanHashMap.get(maidId).getUserEmail().equals(null)))
                            holder.txtMaidDriverEmail.setText(TransportActivity.userBeanHashMap.get(maidId).getUserEmail());
                        else
                            holder.txtMaidDriverEmail.setText("NA");
                    }
                    else
                    {
                        holder.txtMaidDriverEmail.setText("NA");
                        holder.txtMaidDriverName.setText("NA");
                    }


                }

                else
                {
                    holder.txtMaidDriverEmail.setText("NA");
                    holder.txtMaidDriverName.setText("NA");

                }


            }
            else
            {
                holder.txtTagMaidDriverEmail.setText("Driver Email:");
                holder.txtTagMaidDriverName.setText("Driver Name:");

                if(!(TransportActivity.busesBeanHashMap.get(busId).getMaidId()==0 ||
                        String.valueOf(TransportActivity.busesBeanHashMap.get(busId).getMaidId()).equals(null)))
                {
                    if(TransportActivity.busesBeanHashMap.get(busId).getMaidId()!=0) {


                        int driverId = TransportActivity.busesBeanHashMap.get(busId).getDriverId();
                        holder.txtMaidDriverName.setText(TransportActivity.userBeanHashMap.get(driverId).getUserName());

                        holder.txtMaidDriverEmail.setText(TransportActivity.userBeanHashMap.get(driverId).getUserEmail());

                        if (!(TransportActivity.userBeanHashMap.get(driverId).getUserEmail().equals("") || TransportActivity.userBeanHashMap.get(driverId).getUserEmail().equals(null)))
                            holder.txtMaidDriverEmail.setText(TransportActivity.userBeanHashMap.get(driverId).getUserEmail());
                        else
                            holder.txtMaidDriverEmail.setText("NA");
                    }
                    else
                    {
                        holder.txtMaidDriverEmail.setText("NA");
                        holder.txtMaidDriverName.setText("NA");
                    }
                }
                else
                 {
                     holder.txtMaidDriverEmail.setText("NA");
                     holder.txtMaidDriverName.setText("NA");
                 }

            }
        }
    }

    @Override
    public int getItemCount()
    {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtUserName;
        TextView txtUserEmail;
        TextView txtMaidDriverEmail;
        TextView txtMaidDriverName;
        TextView txtBusNumber;
        TextView txtTagMaidDriverEmail;
        TextView txtTagMaidDriverName;

        public ViewHolder(View itemView)
        {
            super(itemView);

            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtUserEmail = itemView.findViewById(R.id.txtUserEmail);
            txtBusNumber = itemView.findViewById(R.id.txtBusNumber);
            txtMaidDriverName = itemView.findViewById(R.id.txtDriverMaidName);
            txtMaidDriverEmail = itemView.findViewById(R.id.txtDriverMaidEmail);
            txtTagMaidDriverName = itemView.findViewById(R.id.txtTagDriverMaidName);
            txtTagMaidDriverEmail = itemView.findViewById(R.id.txtTagDriverMaidEmail);

        }
    }


    public void filter(String str){

        // Optimise Search Algo here

        if (str.isEmpty())
        {
            arrayList.clear();
            arrayList.addAll(tempUserList);
        }
        else
        {
            arrayList.clear();
            for (int i = 0; i < tempUserList.size(); i++)
            {
                String name = tempUserList.get(i).getUserName();
                String email= tempUserList.get(i).getUserEmail();

                try
                {
                    name = String.valueOf(tempUserList.get(i).getUserName());
                    email= String.valueOf(tempUserList.get(i).getUserEmail());;
                }
                catch(Exception e)
                {
                    name="";
                    email="";
                }


                if (name.toLowerCase().contains(str.toLowerCase())||
                        email.toLowerCase().contains(str.toLowerCase()))

                {
                    arrayList.add(tempUserList.get(i));
                }
            }
        }
        notifyDataSetChanged();

    }
}
