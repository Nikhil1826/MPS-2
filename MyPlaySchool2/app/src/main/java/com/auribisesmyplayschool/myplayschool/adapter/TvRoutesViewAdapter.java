package com.auribisesmyplayschool.myplayschool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.activity.TransportActivity;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BusesBean;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.RoutesBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tania on 7/2/18.
 */

public class TvRoutesViewAdapter extends RecyclerView.Adapter<TvRoutesViewAdapter.ViewHolder>
{
    private ArrayList<RoutesBean> routesBeanArrayList,tempRoutesList ;
    Context context;


    public TvRoutesViewAdapter(ArrayList<RoutesBean> routesBeanArrayList, Context context) {
        this.routesBeanArrayList = routesBeanArrayList;
        this.context = context;
        tempRoutesList = new ArrayList<>();
        tempRoutesList.addAll(routesBeanArrayList);


    }

    @Override
    public TvRoutesViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listitem_view_routes,parent,false);
        TvRoutesViewAdapter.ViewHolder viewHolder = new TvRoutesViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TvRoutesViewAdapter.ViewHolder holder, int position)
    {
        StringBuilder busNumber =new StringBuilder();

        holder.txtRouteName.setText(routesBeanArrayList.get(position).getRouteName());
        holder.txtRouteDesc.setText(routesBeanArrayList.get(position).getRouteDesc());

        for (HashMap.Entry<Integer,BusesBean> u : TransportActivity.busesBeanHashMap.entrySet())
        {
            if(routesBeanArrayList.get(position).getRouteId()!=0
                    || String.valueOf(routesBeanArrayList.get(position).getRouteId()).equals(null))
                if(u.getValue().getRouteId()==routesBeanArrayList.get(position).getRouteId())
                    busNumber.append(","+u.getValue().getBusNumber());
        }

        if(busNumber.length()<=0 || busNumber.equals("") || busNumber.equals(null))
            holder.txtBuses.setText("NA");
        else
            holder.txtBuses.setText(busNumber);

    }

    @Override
    public int getItemCount()
    {
        return routesBeanArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtRouteName;
        TextView txtRouteDesc;
        TextView txtBuses;


        public ViewHolder(View itemView)
        {
            super(itemView);

            txtRouteName = itemView.findViewById(R.id.txtRouteName);
            txtRouteDesc = itemView.findViewById(R.id.txtRouteDesc);
            txtBuses = itemView.findViewById(R.id.txtBusNames);


        }
    }


    public void filter(String str)
    {

        // Optimise Search Algo here
        if (str.isEmpty())
        {
            routesBeanArrayList.clear();
            routesBeanArrayList.addAll(tempRoutesList);
        }
        else
        {
            routesBeanArrayList.clear();
            for (int i = 0; i < tempRoutesList.size(); i++)
            {
                String name = tempRoutesList.get(i).getRouteName();
                String desc= tempRoutesList.get(i).getRouteDesc();

                try
                {
                    name = String.valueOf(tempRoutesList.get(i).getRouteName());
                    desc= String.valueOf(tempRoutesList.get(i).getRouteDesc());;
                }
                catch(Exception e)
                {
                    name="";
                    desc="";
                }


                if (name.toLowerCase().contains(str.toLowerCase())||
                        desc.toLowerCase().contains(str.toLowerCase()))

                {
                    routesBeanArrayList.add(tempRoutesList.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }
}
