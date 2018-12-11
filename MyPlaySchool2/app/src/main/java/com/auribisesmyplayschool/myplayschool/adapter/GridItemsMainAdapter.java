package com.auribisesmyplayschool.myplayschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.bean.GridItemUserHomeBean;

import java.util.ArrayList;
import java.util.List;

public class GridItemsMainAdapter extends ArrayAdapter<GridItemUserHomeBean> {
    Context context;
    int resourse;
    ArrayList<GridItemUserHomeBean> listGridItems;

    public GridItemsMainAdapter(Context context, int resource, List<GridItemUserHomeBean> objects) {
        super(context,resource,objects);
        this.context=context;
        this.resourse=resource;
        this.listGridItems=(ArrayList)objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View myView=null;

        myView= LayoutInflater.from(context).inflate(resourse,parent,false);

        TextView txtTitle=  myView.findViewById(R.id.textViewGridItemUserHome);
        ImageView imgView = myView.findViewById(R.id.imageViewGridItemUserHome);

        GridItemUserHomeBean gridItem=listGridItems.get(position);

        txtTitle.setText(gridItem.getTitle());
        imgView.setImageResource(gridItem.getImgId());

        return myView;
    }
}
