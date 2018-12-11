package com.auribisesmyplayschool.myplayschool.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adapter.DigitalMsgAdapter;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BranchBean;
import com.auribisesmyplayschool.myplayschool.bean.BatchBean;
import com.auribisesmyplayschool.myplayschool.bean.MessageBean;
import com.auribisesmyplayschool.myplayschool.bean.TeacherBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UpdateMsgActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ListView listViewMsgs;
    MessageBean messageBeanUpdated;
    MessageBean messageBean;
    DigitalMsgAdapter adapter;
    ArrayList<MessageBean> messageList,tempList;
    int updateDelete,posi,update,branchId;
    SharedPreferences preferences;
    String message;
    FirebaseFirestore db;
    ArrayList<BatchBean> batchBeanArrayList;
    TeacherBean teacherBean;

    void initViews(){
        db = FirebaseFirestore.getInstance();
        preferences=getSharedPreferences(AttUtil.shpREG,MODE_PRIVATE);
        branchId = preferences.getInt(AttUtil.shpBranchId, 0);
        batchBeanArrayList = (ArrayList<BatchBean>) getIntent().getSerializableExtra(AttUtil.BATCH_LIST);
        AttUtil.progressDialog(this);
        messageBean = new MessageBean();
        messageList=new ArrayList<>();
        tempList=new ArrayList<>();
        listViewMsgs=(ListView)findViewById(R.id.listViewMsg);
        listViewMsgs.setOnItemClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_msg);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Message");
        initViews();
        AttUtil.progressDialog(this);
        AttUtil.pd(1);
        if(preferences.getInt(AttUtil.shpLoginType,0) == 2){
            retrieveMsgsManager();
        }else{
            teacherBean = (TeacherBean) getIntent().getSerializableExtra(AttUtil.TEACHERBEAN);
            retrieveMsgTeacher();
        }

    }

     void retrieveMsgTeacher() {
         db.collection(Constants.messages_collection).whereEqualTo("branchId",teacherBean.getBranchId())
                 .whereEqualTo("userId",teacherBean.getUserId()).get()
                 .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
             @Override
             public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                 for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                     messageBean = doc.toObject(MessageBean.class);
                     messageList.add(messageBean);
                 }
                 if (messageList.size()>0){
                     tempList.addAll(messageList);
                     adapter=new DigitalMsgAdapter(UpdateMsgActivity.this, R.layout.adapter_digital_msg,messageList);
                     listViewMsgs.setAdapter(adapter);
                 }else {
                     Toast.makeText(UpdateMsgActivity.this,"No Message Found!",Toast.LENGTH_LONG).show();
                 }
                 AttUtil.pd(0);
             }
         });

    }

    void retrieveMsgsManager() {
         updateDelete=0;
         db.collection(Constants.messages_collection).whereEqualTo("branchId",branchId).get()
                 .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
             @Override
             public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                 for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                     messageBean = doc.toObject(MessageBean.class);
                     messageList.add(messageBean);
                 }
                 if (messageList.size()>0){
                     tempList.addAll(messageList);
                     adapter=new DigitalMsgAdapter(UpdateMsgActivity.this, R.layout.adapter_digital_msg,messageList);
                     listViewMsgs.setAdapter(adapter);
                 }else {
                     Toast.makeText(UpdateMsgActivity.this,"No Message Found!",Toast.LENGTH_LONG).show();
                 }
                 AttUtil.pd(0);
             }
         });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        posi=position;
        messageBean=messageList.get(position);
        alertOptions(messageBean.getId(),messageBean.getMessage(),messageBean.getType());
    }

    void alertOptions(final int msgId,final String msg, final int type){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Please choose an action");
        CharSequence options[]={"UPDATE MESSAGE","DELETE MESSAGE"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                switch (position){
                    case 0:
                        updateMsg(msgId,msg,type);
                        break;
                    case 1:
                        deletemsg(msgId,type);
                        break;
                }
            }
        });
        builder.create().show();
    }

     void deletemsg(int msgId, int type) {
         AlertDialog.Builder builder=new AlertDialog.Builder(this);
         builder.setMessage("Are you sure you want to delete?");
         builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 AttUtil.pd(1);
                 deleteFromDB();
             }
         });
         builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 dialogInterface.dismiss();
             }
         });
         builder.create().show();
    }

     void deleteFromDB() {
        db.collection(Constants.messages_collection).document(String.valueOf(messageBean.getId()))
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AttUtil.pd(0);
                messageList.remove(posi);
                adapter.notifyDataSetChanged();
            }
        });
    }

    void updateMsg(int msgId, String msg, int type) {
         AlertDialog.Builder builder=new AlertDialog.Builder(this);
         builder.setMessage("Update the message :");
         LinearLayout linearLayout=new LinearLayout(this);
         linearLayout.setOrientation(LinearLayout.VERTICAL);
         LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
         final EditText editText=new EditText(this);
         editText.setText(msg);
         linearLayout.setPadding(80,10,10,50);
         linearLayout.addView(editText,params);
         builder.setView(linearLayout);
         builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 if (editText.getText().toString().isEmpty()) {
                     //editText.setError("Can't leave this field empty");
                 }else{
                     AttUtil.pd(1);
                     update=1;
                     message=editText.getText().toString();
                     messageBean.setMessage(message);
                     updateDatatoDB();
                 }

             }
         });
         builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 dialogInterface.dismiss();
             }
         });
         builder.create().show();
    }

     void updateDatatoDB() {
        db.collection(Constants.messages_collection).document(String.valueOf(messageBean.getId()))
                .update("message",messageBean.getMessage()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AttUtil.pd(0);
                messageList.remove(posi);
                messageList.add(posi,messageBean);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_update_post,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }else if(item.getItemId()== R.id.action_filter){
            alertFilter();
        }
        return super.onOptionsItemSelected(item);
    }

     void alertFilter() {
        //CHECK FILTER FOR TEACHER FINTOTEAM
         AlertDialog.Builder builder=new AlertDialog.Builder(this);
         if(preferences.getInt(AttUtil.shpLoginType,0)==2){
             final CharSequence options[]=new CharSequence[batchBeanArrayList.size()+2];
             for (int i = 0; i< batchBeanArrayList.size(); i++){
                 options[i]= batchBeanArrayList.get(i).getBatch_title();
             }
             options[batchBeanArrayList.size()]="All";
             options[batchBeanArrayList.size()+1]="Student";
             builder.setItems(options, new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialogInterface, int position) {
                     messageList.clear();
                     if(position < batchBeanArrayList.size()){
                         /*messageList.addAll(tempList);
                         adapter.notifyDataSetChanged();
                         dialogInterface.dismiss();*/
                         for(int i=0;i<tempList.size();i++){
                             if(tempList.get(i).getBatch_title().equals(batchBeanArrayList.get(position).getBatch_title()) &&
                                     tempList.get(i).getType() == 2){
                                 messageList.add(tempList.get(i));
                             }
                         }

                         adapter.notifyDataSetChanged();
                         dialogInterface.dismiss();
                     }else if(options[position] == "All"){
                         for (int i=0;i<tempList.size();i++){
                             if(tempList.get(i).getType()==1){
                                 messageList.add(tempList.get(i));
                             }
                         }
                         adapter.notifyDataSetChanged();
                         dialogInterface.dismiss();
                     }else{
                         for (int i=0;i<tempList.size();i++){
                             if(tempList.get(i).getType()==3){
                                 messageList.add(tempList.get(i));
                             }
                         }
                         adapter.notifyDataSetChanged();
                         dialogInterface.dismiss();
                     }
                 }
             });
         }

         builder.create().show();
    }


}
