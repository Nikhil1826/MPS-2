package com.auribisesmyplayschool.myplayschool.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.auribisesmyplayschool.myplayschool.R;


/**
 * Created by tania on 30/1/18.
 */

public class TransportAdapter extends RecyclerView.Adapter<TransportAdapter.ViewHolder>
{
    Context context;
    String[] list;
    int[] images;
    int[] color;


    public TransportAdapter(Context context , String[] list , int[] images, int[] color)
    {
        this.context=context;
        this.list = list;
        this.images=images;
        this.color=color;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_transport_item,parent,false);
        TransportAdapter.ViewHolder viewHolder = new TransportAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {

        holder.title.setText(list[position]);
        holder.image.setImageResource(images[position]);

//        if(list[position].equalsIgnoreCase("Drivers"))
//        {
//            holder.image.setBackgroundColor(color[0]);
//        }
//        else if(list[position].equalsIgnoreCase("Maids"))
//        {
//            holder.image.setBackgroundColor(color[1]);
//        }
//        else if(list[position].equalsIgnoreCase("Routes"))
//        {
//            holder.image.setBackgroundColor(color[2]);
//        }
//        else if(list[position].equalsIgnoreCase("Buses"))
//        {
//            holder.image.setBackgroundColor(color[3]);
//        }

    }

    @Override
    public int getItemCount()
    {
        return list.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView title;
        public ViewHolder(View itemView)
        {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.transport_image);
            title = (TextView)itemView.findViewById(R.id.transport_text);

        }
    }
}
