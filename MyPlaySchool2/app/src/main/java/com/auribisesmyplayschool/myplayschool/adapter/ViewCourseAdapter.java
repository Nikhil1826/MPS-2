package com.auribisesmyplayschool.myplayschool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BranCourBean;

import java.util.ArrayList;

public class ViewCourseAdapter extends ArrayAdapter{
    ArrayList<BranCourBean> courseBeanArrayList,searchlist;
    Context context;
    int resource;
    int lastPosition = -1;
    public ViewCourseAdapter(Context context, int resource, ArrayList<BranCourBean> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        courseBeanArrayList = objects;
        searchlist=new ArrayList<>();
        searchlist.addAll(objects);
    }
    public void filter(String search){

        if(search.isEmpty()){
            courseBeanArrayList.clear();
            courseBeanArrayList.addAll(searchlist);
        }else{
            courseBeanArrayList.clear();
            for(int i = 0; i < searchlist.size() ; i++ ){
                if(searchlist.get(i).getCourseName().toLowerCase().contains(search.toLowerCase())){
                    courseBeanArrayList.add(searchlist.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View myview = null;
        myview = LayoutInflater.from(context).inflate(resource,parent,false);
        TextView tvcoursetitle = (TextView)myview.findViewById(R.id.tvcourse_title);
        TextView tvcourseduration = (TextView)myview.findViewById(R.id.tvduration);
        TextView tvcoursehour = (TextView)myview.findViewById(R.id.tvhour);
        TextView tvcoursefee = (TextView)myview.findViewById(R.id.tvcoursefee);
        View view = (View)myview.findViewById(R.id.typeView);
        BranCourBean courseBean = courseBeanArrayList.get(position);
        tvcoursetitle.setText(courseBean.getCourseName());
        tvcourseduration.setText(courseBean.getCourseDuration());
        tvcoursehour.setText(courseBean.getCourseHours());
        tvcoursefee.setText(courseBean.getCourseFees());

//        if(courseBean.getCourseStatus()==1){
//            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSeaGreen));
//
//        }else if(courseBean.getCourseStatus()==0){
//            view.setBackgroundColor(Color.parseColor("#ff848484"));
//
//        }

        Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        myview.startAnimation(animation);
        lastPosition = position;

        return myview;
    }

}
