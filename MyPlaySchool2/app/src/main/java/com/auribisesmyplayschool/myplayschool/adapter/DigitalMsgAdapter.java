package com.auribisesmyplayschool.myplayschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.bean.MessageBean;

import java.util.ArrayList;

/**
 * Created by Eshaan on 09-Dec-16.
 */
public class DigitalMsgAdapter extends ArrayAdapter {
    Context context;
    ArrayList<MessageBean> messageList;

    public DigitalMsgAdapter(Context context, int resource, ArrayList<MessageBean> messageList) {
        super(context, resource, messageList);
        this.context=context;
        this.messageList=messageList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.adapter_digital_msg,null);
        TextView msg=(TextView)rowView.findViewById(R.id.textViewMsg);
        TextView date=(TextView)rowView.findViewById(R.id.textViewDate);
        TextView textViewUserName=(TextView)rowView.findViewById(R.id.textViewUserName);
        msg.setText(messageList.get(position).getMessage().toString().replace("$&*","'"));
        textViewUserName.setText(messageList.get(position).getDate()+" "+messageList.get(position).getTime());
        if(messageList.get(position).getUserId()==0)
            date.setText("By Institute");
        else
            date.setText("By Teacher("+messageList.get(position).getUserName()+")");
        return rowView;
    }
}
