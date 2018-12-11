package com.auribisesmyplayschool.myplayschool.adminApp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adapter.DigitalMsgAdapter;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BranchBean;
import com.auribisesmyplayschool.myplayschool.adminApp.classes.AdminUtil;
import com.auribisesmyplayschool.myplayschool.bean.AdminBean;
import com.auribisesmyplayschool.myplayschool.bean.BatchBean;
import com.auribisesmyplayschool.myplayschool.bean.MessageBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DigitalMessageActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listViewMsgs;
    MessageBean messageBeanUpdated;
    MessageBean messageBean;
    DigitalMsgAdapter adapter;
    ArrayList<MessageBean> messageList,tempList,tempList2;
    int updateDelete,posi,update,responseSignal=0,posBranch = 0,reqCode = 0;
    String message;
    ArrayList<BranchBean> branchBeanArrayList;
    ArrayList<BatchBean> batchBeanArrayList,batchBeanArrayListTemp;
    ArrayList<String> branchNameArrayList;
    SharedPreferences preferences;
    AlertDialog selectBranchAlertDialog;
    FirebaseFirestore db;
    AdminBean adminBean;
    BranchBean branchBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_message);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Digital Messages");
        preferences = getSharedPreferences(AttUtil.shpREG, Context.MODE_PRIVATE);
        initViews();
        messageList = new ArrayList<>();
        branchBean = new BranchBean();
        messageBean = new MessageBean();
        branchBeanArrayList = new ArrayList<>();
        branchNameArrayList = new ArrayList<>();
        branchNameArrayList.add("--No Branch Found--");
        batchBeanArrayList = new ArrayList<>();
        batchBeanArrayListTemp = new ArrayList<>();
        AttUtil.pd(1);
        retrieveBranchAndBatch();
    }

     void retrieveBranchAndBatch() {
        db.collection(Constants.branchCollection).whereEqualTo("adminId",adminBean.getAdminId()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    branchBean = doc.toObject(BranchBean.class);
                    branchBeanArrayList.add(branchBean);
                }
                AttUtil.pd(0);
                if(branchBeanArrayList.size()>0||branchBeanArrayList!=null){
                    branchNameArrayList.clear();
                    branchNameArrayList.add("--Select a branch--");
                    for(int i = 0; i<branchBeanArrayList.size();i++){
                        branchNameArrayList.add(branchBeanArrayList.get(i).getBranchName());
                    }
                    selectBranchDialog();
                }else{
                    Toast.makeText(DigitalMessageActivity.this, "No record of branch(es) found.",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    void initViews(){
        db = FirebaseFirestore.getInstance();
        adminBean = (AdminBean) getIntent().getSerializableExtra(AdminUtil.TAG_ADMIN_BEAN);
        AttUtil.progressDialog(this);
        tempList=new ArrayList<>();
        tempList2 = new ArrayList<>();
        listViewMsgs=(ListView)findViewById(R.id.listViewMsg);
    }

    void selectBranchDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DigitalMessageActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.admin_custom_branch_batch_spn, null);
        dialogBuilder.setTitle("Select a  Branch");
        dialogBuilder.setView(dialogView);
        Spinner spinner = (Spinner)dialogView.findViewById(R.id.spnBranchMsgAdmin);
        spinner.setAdapter(new ArrayAdapter(DigitalMessageActivity.this,
                android.R.layout.simple_spinner_dropdown_item,branchNameArrayList));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                posBranch = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        dialogBuilder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(posBranch>0){
                    AttUtil.progressDialog(DigitalMessageActivity.this);
                    AttUtil.pd(1);
                    selectMessages();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        selectBranchAlertDialog = dialogBuilder.create();
        selectBranchAlertDialog.setCancelable(false);
        selectBranchAlertDialog.show();
    }

     void selectMessages() {
        db.collection(Constants.messages_collection).whereEqualTo("branchId",branchBeanArrayList.get(posBranch-1).getBranchId())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    messageBean = doc.toObject(MessageBean.class);
                    messageList.add(messageBean);
                }
                AttUtil.pd(0);
                if(messageList.size()>0||messageList!=null){
                    tempList.addAll(messageList);
                    adapter=new DigitalMsgAdapter(DigitalMessageActivity.this,
                            R.layout.admin_adapter_digital_msg,messageList);
                    listViewMsgs.setAdapter(adapter);
                }else{
                    posBranch = 0;
                    selectBranchDialog();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,1,"Choose").setIcon(R.drawable.filter).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }else if(item.getItemId()==1){
            alertFilter();
        }
        return super.onOptionsItemSelected(item);
    }

     void alertFilter() {
         AlertDialog.Builder builder=new AlertDialog.Builder(this);
         final CharSequence options[]=new CharSequence[2];
         options[0] = "All";
         options[1] = "Student";

         builder.setItems(options, new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int position) {
                 tempList2.clear();
                 tempList2.addAll(messageList);
                 messageList.clear();
                /* if(position==batchBeanArrayListTemp.size()){
                     messageList.addAll(tempList);
                     if(messageList.size()>0||messageList!=null){
                         adapter.notifyDataSetChanged();
                     }else{
                         Toast.makeText(DigitalMessageActivity.this, "No message(s) for this batch.", Toast.LENGTH_SHORT).show();
                         messageList.addAll(tempList2);
                         adapter.notifyDataSetChanged();
                     }
                     dialogInterface.dismiss();
                 }else*/ if(options[position]=="All"){
                     for (int i=0;i<tempList.size();i++){
                         if(tempList.get(i).getType()==1){
                             messageList.add(tempList.get(i));
                         }
                         if(messageList.size()>0||messageList!=null){
                             adapter.notifyDataSetChanged();
                         }else{
                             Toast.makeText(DigitalMessageActivity.this, "No message(s) for this batch.", Toast.LENGTH_SHORT).show();
                             messageList.addAll(tempList2);
                             adapter.notifyDataSetChanged();
                         }
                     }
                     dialogInterface.dismiss();
                 }else{
                     for (int i=0;i<tempList.size();i++){
                         if(tempList.get(i).getType() == 3){
                             messageList.add(tempList.get(i));
                         }
                     }
                     adapter.notifyDataSetChanged();
                     dialogInterface.dismiss();
                 }
             }
         });
         builder.create().show();
    }
}
