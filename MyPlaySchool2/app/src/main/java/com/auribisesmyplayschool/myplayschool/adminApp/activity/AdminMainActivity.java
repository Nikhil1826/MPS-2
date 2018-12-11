package com.auribisesmyplayschool.myplayschool.adminApp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.activity.LoginActivity;
import com.auribisesmyplayschool.myplayschool.adminApp.adapter.GridViewAdapter;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BranchBean;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.UserBean;
import com.auribisesmyplayschool.myplayschool.adminApp.classes.AdminUtil;
import com.auribisesmyplayschool.myplayschool.bean.AdminBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.Nullable;

import dmax.dialog.SpotsDialog;

public class AdminMainActivity extends AppCompatActivity {
    GridView gridView;
    AdminBean adminBean;
    Intent rcv;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    FirebaseFirestore db;
    BranchBean branchBean;
    ArrayList<String> branchNameArrayList;
    ArrayList<BranchBean> branchBeanArrayList;
    private String[] options;



    String[]list={"Branches","Classes","Employees","Routes","Collections","Profile",
            "Enquiries","Admissions","Messages","Fee Heads","Fee Categories","MPS"};
    int []img={R.drawable.branch,
            R.drawable.branchcourse,
            R.drawable.branchmanager,
            R.drawable.ic_route_main,
            R.drawable.collection,
            R.drawable.user,
            R.drawable.ic_enquiry,
            R.drawable.icon_admission,
            R.drawable.icon_digital_message,
            R.drawable.feeheads,
            R.drawable.categories,
            R.drawable.ic_app,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        prefs = getSharedPreferences(AttUtil.shpREG, Context.MODE_PRIVATE);
        rcv = getIntent();
        db = FirebaseFirestore.getInstance();
        branchBean = new BranchBean();
        branchNameArrayList = new ArrayList<>();
        branchBeanArrayList = new ArrayList<>();
        //adminName = rcv.getStringExtra("adminName");
        //adminId = rcv.getIntExtra("adminId",0);
        editor = prefs.edit();
        adminBean = (AdminBean) rcv.getSerializableExtra(AdminUtil.TAG_ADMIN_BEAN);
        getSupportActionBar().setTitle("Welcome, "+adminBean.getAdminName());
        gridView=findViewById(R.id.gridView);
        gridView.setAdapter(new GridViewAdapter(this, list, img));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:
                       //startActivityForResult(new Intent(AdminMainActivity.this,ManageBranchActivity.class)
                                //.putExtra(AdminUtil.TAG_BRANCHARRAYLIST,branchBeanArrayList),AdminUtil.REQ_CODE);
                       Intent intent2 = new Intent(AdminMainActivity.this,ManageBranchActivity.class);
                       intent2.putExtra(AdminUtil.TAG_ADMIN_BEAN,adminBean);
                       startActivityForResult(intent2,AdminUtil.REQ_CODE);
                       break;
                    case 1:
                        Intent intent = new Intent(AdminMainActivity.this,ManageCourseActivity.class);
                        //intent.putExtra("adminId",adminId);
                        intent.putExtra(AdminUtil.TAG_ADMIN_BEAN,adminBean);
                        startActivity(intent);
                        break;
                    case 2:
                        Intent intent1 = new Intent(AdminMainActivity.this,ManageBranchManagerActivity.class);
                        intent1.putExtra(AdminUtil.TAG_ADMIN_BEAN,adminBean);
                        intent1.putExtra(AdminUtil.TAG_BRANCHARRAYLIST,branchBeanArrayList);
                        startActivity(intent1);
                        break;
                    case 3:
                        showBranchOptions(3);
                      //startActivity(new Intent(AdminMainActivity.this,RouteBusesActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(AdminMainActivity.this,CollectionGraphActivity.class));
                        //Toast.makeText(AdminMainActivity.this, "Comming soon", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        startActivity(new Intent(AdminMainActivity.this,ChangeProfileActivity.class));
                        break;
                    case 6:
                        //startActivity(new Intent(AdminMainActivity.this,EnquiryGraphActivity.class));
                        Toast.makeText(AdminMainActivity.this, "Comming soon", Toast.LENGTH_SHORT).show();
                        break;
                    case 7:
                        //startActivity(new Intent(AdminMainActivity.this,AdmissionGraphActivity.class));
                        Toast.makeText(AdminMainActivity.this, "Comming soon", Toast.LENGTH_SHORT).show();
                        break;
                    case 8:
                        startActivity(new Intent(AdminMainActivity.this,DigitalMessageActivity.class)
                                .putExtra(AdminUtil.TAG_ADMIN_BEAN,adminBean));
                        break;
//                    case 8:
//                        showBranchOptions(3);
//                        break;
                    case 9:
                        showBranchOptions(1);
                        //startActivity(new Intent(AdminMainActivity.this,AddFeeHeadsActivity.class));
                        break;
                    case 10:
                        showBranchOptions(2);
                        //startActivity(new Intent(AdminMainActivity.this,AddFeeCategoryActivity.class));
                        break;
                    case 11:
                        startActivity(new Intent(AdminMainActivity.this,AdminAboutUsActivity.class));
                        break;


                }

            }
        });
        getBranchList();
    }

    void showBranchOptions(int request){
        int z=-1,optionsSize=0;
        if(branchBeanArrayList.size()>0&&branchBeanArrayList!=null){
            for(int i = 0;i<branchBeanArrayList.size();i++) {
                optionsSize = optionsSize+1;
            }
            options = new String[optionsSize];
            for(int i = 0;i<branchBeanArrayList.size();i++) {
                z = z+1;
                options[z] = branchBeanArrayList.get(i).getBranchName();
            }
            if(options.length!=0){
                show_options(request);
                //daybookDateRange();
            }else{
                Toast.makeText(AdminMainActivity.this, "No Branches Found  ", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(AdminMainActivity.this, "No Branches Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void show_options(final int request){
        AlertDialog.Builder build = new AlertDialog.Builder(AdminMainActivity.this);
        build.setTitle("Choose a branch");
        build.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(request==1){
                    alertOptions(branchBeanArrayList.get(which).getBranchId());
                }else if(request==2){
                    startActivity(new Intent(AdminMainActivity.this,AddFeeCategoryActivity.class)
                            .putExtra("branchId",branchBeanArrayList.get(which).getBranchId()));
                }else if(request==3)
                {
                    startActivity(new Intent(AdminMainActivity.this,RouteBusesActivity.class)
                            .putExtra("branchId",branchBeanArrayList.get(which).getBranchId()));
                }
                else
                {
                }
                /*startActivity(new Intent(AdminMainActivity.this,AdminAboutUsActivity.class)
                .putExtra(AdminUtil.TAG_BRANCH,branchBeanArrayList.get(which)));*/
            }
        });
        build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        build.setCancelable(false);
        build.create().show();
    }

    private void alertOptions(final int branchId){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        String options[]={"Add Fee Head","Manage Fee Heads"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    startActivity(new Intent(AdminMainActivity.this,AddFeeHeadsActivity.class)
                            .putExtra("branchId",branchId)
                    .putExtra("adminId",adminBean.getAdminId()));
                }else if(which==1){
                    startActivity(new Intent(AdminMainActivity.this,FeeHeadActivity.class)
                            .putExtra("branchId",branchId));
                }
            }
        });
        builder.create().show();
    }

    public void getBranchList() {
        db.collection(Constants.branchCollection).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        branchBean = doc.getDocument().toObject(BranchBean.class);
                        branchBeanArrayList.add(branchBean);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==AdminUtil.REQ_CODE){
            if(resultCode==AdminUtil.RES_CODE){
                branchBeanArrayList = (ArrayList<BranchBean>)data.getSerializableExtra(AdminUtil.TAG_BRANCHARRAYLIST);
               /* branchNameArrayList.clear();
                if(branchBeanArrayList.size()!=0){
                    for(int i=0;i<branchBeanArrayList.size();i++){
                        branchNameArrayList.add(branchBeanArrayList.get(i).getBranchName());
                    }
                }*/
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainactivity_menu_admin,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.log_off:
                confirm_logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void confirm_logout() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Do you wish to log out ?");
        dialog.setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editor.clear().commit();
                finish();
                startActivity(new Intent(AdminMainActivity.this,LoginActivity.class));
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.create().show();
    }
}
