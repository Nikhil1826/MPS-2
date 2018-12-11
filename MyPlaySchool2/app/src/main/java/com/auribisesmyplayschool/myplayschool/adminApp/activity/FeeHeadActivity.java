package com.auribisesmyplayschool.myplayschool.adminApp.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adminApp.adapter.FeeHeadAdapter;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.FeeCostBean;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.FeeHeadBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class FeeHeadActivity extends AppCompatActivity implements View.OnClickListener {

    private int branchId,request;
    private ListView listViewHeads;
    private Button buttonUpdate;
    private ArrayList<FeeHeadBean> feeHeadBeanArrayList;
    private ArrayList<FeeCostBean> feeCostBeanArrayList;
    private FeeHeadAdapter adapter;
    FirebaseFirestore db;
    FeeHeadBean feeHeadBean;
    int check;

    private void initViews(){
        branchId=getIntent().getIntExtra("branchId",0);
        db = FirebaseFirestore.getInstance();
        feeHeadBean = new FeeHeadBean();
        feeHeadBeanArrayList = new ArrayList<>();
        listViewHeads=(ListView)findViewById(R.id.listViewHeads);
        buttonUpdate=(Button)findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_head);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        AttUtil.progressDialog(this);
        AttUtil.pd(1);
        retrieveFeeHeads();
    }

     void retrieveFeeHeads() {
        db.collection(Constants.fee_head_collection).whereEqualTo("branchId",branchId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    feeHeadBean = doc.toObject(FeeHeadBean.class);
                    feeHeadBeanArrayList.add(feeHeadBean);
                }

                if(feeHeadBeanArrayList.size()>0){
                    /*for(int i=0;i<feeHeadBeanArrayList.size();i++){
                        for(int j=0;j<feeHeadBeanArrayList.get(i).getFeeCostBeanArrayList().size();j++){
                            if(feeHeadBeanArrayList.get(i).getFeeCostBeanArrayList().get(j).getCost()==0){
                                feeHeadBeanArrayList.get(i).getFeeCostBeanArrayList().remove(j);
                            }
                        }

                    }*/
                    check = feeHeadBeanArrayList.size();
                    AttUtil.pd(0);
                    adapter=new FeeHeadAdapter(FeeHeadActivity.this,R.layout.adapter_fee_heads_layout,feeHeadBeanArrayList);
                    listViewHeads.setAdapter(adapter);
                }else{
                    AttUtil.pd(0);
                    Toast.makeText(FeeHeadActivity.this,"No Fee Head found.",Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonUpdate){
            AttUtil.progressDialog(this);
            AttUtil.pd(1);
            updateFeeHeads();
        }
    }

     void updateFeeHeads() {
        for(int i=0;i<feeHeadBeanArrayList.size();i++){
            feeHeadBean = new FeeHeadBean();
            feeHeadBean.setAdminId(feeHeadBeanArrayList.get(i).getAdminId());
            feeHeadBean.setHeadName(feeHeadBeanArrayList.get(i).getHeadName());
            feeHeadBean.setHeadId(feeHeadBeanArrayList.get(i).getHeadId());
            feeHeadBean.setBranchId(feeHeadBeanArrayList.get(i).getBranchId());
            feeHeadBean.setFeeType(feeHeadBeanArrayList.get(i).getFeeType());
            feeHeadBean.setFeeCostBeanArrayList(feeHeadBeanArrayList.get(i).getFeeCostBeanArrayList());
            updateDataToDB();
        }
    }

     void updateDataToDB() {
        db.collection(Constants.fee_head_collection).document(String.valueOf(feeHeadBean.getHeadId())).set(feeHeadBean)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        check = check - 1;
                        if(check == 0){
                            AttUtil.pd(0);
                            AlertDialog.Builder builder=new AlertDialog.Builder(FeeHeadActivity.this);
                            builder.setMessage("Fee Heads Updated!");
                            builder.setCancelable(false);
                            builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FeeHeadActivity.this.finish();
                                }
                            });
                            builder.create().show();
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
