package com.auribisesmyplayschool.myplayschool.adminApp.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adminApp.adapter.BranchListAdapter;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BranchBean;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.UserBean;
import com.auribisesmyplayschool.myplayschool.adminApp.classes.AdminUtil;
import com.auribisesmyplayschool.myplayschool.bean.AdminBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class ManageBranchActivity extends AppCompatActivity implements View.OnClickListener{
    ListView listView;
    ArrayList<BranchBean> branchBeanArrayList;
    BranchListAdapter branchListAdapter;
    int branchId, branchStatus;
    String branchName, branchContact;
    BranchBean branchBean;
    int pos,posSpnBatch=0;
    String bStatus;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    ArrayList<String> spinList;
    Dialog dialog;
    FirebaseFirestore db;
    AdminBean adminBean;

    //ArrayList<BatchBean> batchBeanArrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_manage_branch);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        prefs = getSharedPreferences(AttUtil.shpREG, Context.MODE_PRIVATE);
        AttUtil.progressDialog(this);
        db = FirebaseFirestore.getInstance();
        Intent rcv = getIntent();
        adminBean = (AdminBean) rcv.getSerializableExtra(AdminUtil.TAG_ADMIN_BEAN);
        initViews();
       /* Intent i = getIntent();
        if(i.hasExtra(AdminUtil.TAG_BRANCHARRAYLIST)) {
            branchBeanArrayList = (ArrayList<BranchBean>) i.getSerializableExtra(AdminUtil.TAG_BRANCHARRAYLIST);
            if (branchBeanArrayList.isEmpty()) {
                getSupportActionBar().setTitle("Branch(0)");
                Toast.makeText(ManageBranchActivity.this, "No Branch(es) found", Toast.LENGTH_LONG).show();
            } else {
                getSupportActionBar().setTitle("Branch("+branchBeanArrayList.size()+")");
                branchListAdapter = new BranchListAdapter(ManageBranchActivity.this,
                        R.layout.admin_adapter_branch_listitem, branchBeanArrayList);
                listView.setAdapter(branchListAdapter);
            }
        }*/
       AttUtil.pd(1);
       retrieveBranch();

    }



    public void retrieveBranch() {
        db.collection(Constants.branchCollection).whereEqualTo("adminId",adminBean.getAdminId())
              .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            //size = queryDocumentSnapshots.size();
                            branchBean = doc.toObject(BranchBean.class);
                            branchBeanArrayList.add(branchBean);
                            branchListAdapter = new BranchListAdapter(ManageBranchActivity.this,
                                    R.layout.admin_adapter_branch_listitem, branchBeanArrayList);
                            listView.setAdapter(branchListAdapter);
                            branchListAdapter.notifyDataSetChanged();
                            getSupportActionBar().setTitle("Branch("+branchBeanArrayList.size()+")");
                        }else {
                            getSupportActionBar().setTitle("Branch(0)");
                            Toast.makeText(ManageBranchActivity.this, "No Branch(es) found", Toast.LENGTH_LONG).show();
                        }
                }
                AttUtil.pd(0);
            }
        });


    }

    public void initViews() {
       // batchBeanArrayList = new ArrayList<>();
        branchBeanArrayList = new ArrayList<>();
        branchBean = new BranchBean();
        //batchBeanArrayList = new ArrayList<>();
        spinList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);
        editor = prefs.edit();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                branchId = branchBeanArrayList.get(position).getBranchId();
                branchName = branchBeanArrayList.get(position).getBranchName();
                branchContact = branchBeanArrayList.get(position).getBranchContact();
                branchStatus = branchBeanArrayList.get(position).getBranchStatus();
                branchBean = branchBeanArrayList.get(position);
                if (branchBean.getBranchStatus() == 1) {
                    bStatus = "Deactivate";
                } else {
                    bStatus = "Activate";
                }
                show_options();
            }
        });
    }

    public void show_options() {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        String[] options = {"View Branch's Class(es)", "View Enquiry", "View Admission(s)", "Update Branch's Details", bStatus, "Make Call", "Send Message"};

        build.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    /*case 0:
                        show_branch_info();
                        break;*/
                    case 0:
                        Intent intent = new Intent(ManageBranchActivity.this,ManageBranchCourseActivity.class);
                        intent.putExtra(AdminUtil.branchId,branchBean.getBranchId());
                        intent.putExtra(AdminUtil.TAG_ADMIN_BEAN,adminBean);
                        intent.putExtra(AdminUtil.TAG_BRANCHARRAYLIST,branchBeanArrayList);
                        startActivity(intent);
                        break;
                    case 1:
//                        Intent i1 = new Intent(ManageBranchActivity.this,ManageEnquiryActivity.class);
//                        i1.putExtra("activitySignal",1);
//                        i1.putExtra(AdminUtil.branchId,branchBean.getBranchId());
//                        i1.putExtra("formSignal",2);
//                        startActivity(i1);
                        break;
                    case 2:
//                        if(AdminUtil.isNetworkConnected(ManageBranchActivity.this)){
//                            viewBatch();
//                        }else {
//                            batchNetConnect();
//                        }
                        break;
                    case 3:
                        Intent passBeanData = new Intent(ManageBranchActivity.this,AddBranchActivity.class);
                        passBeanData.putExtra(AdminUtil.TAG_BRANCH,branchBean);
                        passBeanData.putExtra(AdminUtil.TAG_ADMIN_BEAN,adminBean);
                        startActivityForResult(passBeanData,AdminUtil.REQ_CODE);
                        break;
                    case 4:
                        //responseSignal = 2;
                        if(AdminUtil.isNetworkConnected(ManageBranchActivity.this)){
                            AttUtil.progressDialog(ManageBranchActivity.this);
                            AttUtil.pd(1);
                            start_deactivate();
                        }/*else{
                            activateNetConnect();
                        }*/
                        break;
                    case 5:
                        Intent im = new Intent(Intent.ACTION_DIAL);
                        im.setData(Uri.parse("tel:"+branchBean.getBranchContact()));
                        startActivity(im);
                        break;
                    case 6:
                        Intent sms= new Intent(Intent.ACTION_SENDTO);
                        sms.setData(Uri.parse("smsto:" + Uri.encode(branchBean.getBranchContact())));
                        startActivity(sms);
                        break;
                    default:
                        break;
                }
            }
        });
        build.create().show();
    }

     void start_deactivate() {
        if(branchBean.getBranchStatus() == 1){
            branchBean.setBranchStatus(0);
        }else{
            branchBean.setBranchStatus(1);
        }

         db.collection(Constants.branchCollection).document(String.valueOf(branchBean.getBranchId()))
                 .update("branchStatus",branchBean.getBranchStatus()).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
                 Toast.makeText(ManageBranchActivity.this,"Branch Status changed",Toast.LENGTH_SHORT).show();
                 branchListAdapter.notifyDataSetChanged();
                 AttUtil.pd(0);
             }
         });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab1:
                Intent intent = new Intent(ManageBranchActivity.this,AddBranchActivity.class);
                intent.putExtra(AdminUtil.TAG_ADMIN_BEAN,adminBean);
                startActivityForResult(intent, AdminUtil.REQ_CODE);
                break;
            case R.id.fab2:
                //startActivity(new Intent(ManageBranchActivity.this,BranchCourseActivity.class).
                        //putExtra(AdminUtil.TAG_BRANCHARRAYLIST,branchBeanArrayList));
                Intent intent2 = new Intent(ManageBranchActivity.this,BranchCourseActivity.class);
                intent2.putExtra(AdminUtil.TAG_ADMIN_BEAN,adminBean);
                intent2.putExtra(AdminUtil.TAG_BRANCHARRAYLIST,branchBeanArrayList);
                //intent2.putExtra(AdminUtil.branchId,branchBean.getBranchId());
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        floatingbutton();
    }
    void floatingbutton(){
        FloatingActionMenu menu =(FloatingActionMenu)findViewById(R.id.fab);
        if(menu.isOpened()){
            menu.close(true);
        }else {
            Intent i = new Intent();
            i.putExtra(AdminUtil.TAG_BRANCHARRAYLIST,branchBeanArrayList);
            setResult(AdminUtil.RES_CODE,i);
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == AdminUtil.REQ_CODE)&&(resultCode == 102)){
            branchBean = (BranchBean) data.getSerializableExtra(AdminUtil.TAG_BRANCH);
            branchBeanArrayList.set(pos,branchBean);
            branchListAdapter.notifyDataSetChanged();
        } else if((requestCode == AdminUtil.REQ_CODE)&&(resultCode == AdminUtil.RES_CODE)){
            branchBean = (BranchBean) data.getSerializableExtra(AdminUtil.TAG_BRANCH);
            if(branchBeanArrayList.size()>0){
                branchBeanArrayList.add(branchBean);
                branchListAdapter.notifyDataSetChanged();
            }else{
                branchBeanArrayList.add(branchBean);
                branchListAdapter = new BranchListAdapter(ManageBranchActivity.this,
                        R.layout.admin_adapter_branch_listitem,branchBeanArrayList);
                listView.setAdapter(branchListAdapter);
            }
        }

        getSupportActionBar().setTitle("Branch(es)"+" ("+branchBeanArrayList.size()+")");
        FloatingActionMenu menu =(FloatingActionMenu)findViewById(R.id.fab);
        if(menu.isOpened()){
            menu.close(true);
        }

    }
}
