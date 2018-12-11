package com.auribisesmyplayschool.myplayschool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.activity.TransportActivity;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BusesBean;

import java.util.ArrayList;

import static java.sql.Types.NULL;

/**
 * Created by tania on 7/2/18.
 */

public class TvViewBusItemAdapter extends RecyclerView.Adapter<TvViewBusItemAdapter.ViewHolder>
{
    ArrayList<BusesBean> busBeanArrayList, tempBusList;
    Context context;


    public TvViewBusItemAdapter(ArrayList<BusesBean> arrayList, Context context)
    {
        this.busBeanArrayList = arrayList;
        this.context=context;
        tempBusList = new ArrayList<>();
        tempBusList.addAll(arrayList);

        Log.i("show","adapter value "+String.valueOf(busBeanArrayList));
    }

    @Override
    public TvViewBusItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listitem_view_buses,parent,false);
        TvViewBusItemAdapter.ViewHolder viewHolder = new TvViewBusItemAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TvViewBusItemAdapter.ViewHolder holder, int position)
    {
        holder.txtBusNumber.setText(busBeanArrayList.get(position).getBusNumber());
        holder.txtBusDesc.setText(busBeanArrayList.get(position).getBusDesc());

        if(busBeanArrayList.get(position).getMaidId()==0 ||
                busBeanArrayList.get(position).getMaidId()==NULL)
            holder.txtMaidName.setText("NA");
        else
            holder.txtMaidName.setText(TransportActivity.userBeanHashMap.get
                    (busBeanArrayList.get(position).getMaidId()).getUserName());

        if(busBeanArrayList.get(position).getDriverId()==0 ||
                busBeanArrayList.get(position).getDriverId()==NULL)
            holder.txtDriverName.setText("NA");
        else
            holder.txtDriverName.setText(TransportActivity.userBeanHashMap.get
                    (busBeanArrayList.get(position).getDriverId()).getUserName());

        if(busBeanArrayList.get(position).getRouteId()==0
                ||busBeanArrayList.get(position).getRouteId()==NULL)
            holder.txtRouteName.setText("NA");
        else
            holder.txtRouteName.setText(TransportActivity.routesBeanHashMap
                .get(busBeanArrayList.get(position).getRouteId())
                .getRouteName());

    }

    @Override
    public int getItemCount()
    {
        return busBeanArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtRouteName;
        TextView txtBusDesc;
        TextView txtMaidName;
        TextView txtDriverName;
        TextView txtBusNumber;


        public ViewHolder(View itemView)
        {
            super(itemView);

            txtRouteName = itemView.findViewById(R.id.txtRouteName);
            txtBusDesc = itemView.findViewById(R.id.txtBusDesc);
            txtBusNumber = itemView.findViewById(R.id.txtBusNumber);
            txtDriverName= itemView.findViewById(R.id.txtDriverName);
            txtMaidName = itemView.findViewById(R.id.txtMaidName);

        }
    }


    public void filter(String str)
    {
        // Optimise Search Algo here
        busBeanArrayList.clear();

        if(str.length()==0){
            busBeanArrayList.addAll(tempBusList);
        }else{
            for(BusesBean busesBean : tempBusList){
                if(busesBean.getBusNumber().toLowerCase().contains(str.toLowerCase())
                        ||busesBean.getBusDesc().toLowerCase().contains(str.toLowerCase()))
                {
                    busBeanArrayList.add(busesBean);
                }
            }
        }

        notifyDataSetChanged();

        // Optimise Search Algo here
        if (str.isEmpty())
        {
            busBeanArrayList.clear();
            busBeanArrayList.addAll(tempBusList);
        }
        else
        {
            busBeanArrayList.clear();
            for (int i = 0; i < tempBusList.size(); i++)
            {
                String name = tempBusList.get(i).getBusNumber();
                String desc= tempBusList.get(i).getBusDesc();

                try
                {
                    name = String.valueOf(tempBusList.get(i).getBusNumber());
                    desc= String.valueOf(tempBusList.get(i).getBusDesc());;
                }
                catch(Exception e)
                {
                    name="";
                    desc="";
                }


                if (name.toLowerCase().contains(str.toLowerCase())||
                        desc.toLowerCase().contains(str.toLowerCase()))

                {
                    busBeanArrayList.add(tempBusList.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }
}
