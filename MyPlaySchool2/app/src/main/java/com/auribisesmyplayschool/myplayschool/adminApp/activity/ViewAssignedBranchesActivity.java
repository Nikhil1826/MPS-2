package com.auribisesmyplayschool.myplayschool.adminApp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adminApp.adapter.BranchListAdapter;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BranchBean;
import com.auribisesmyplayschool.myplayschool.adminApp.classes.AdminUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class ViewAssignedBranchesActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<BranchBean> branchBeanArrayList;
    BranchListAdapter branchListAdapter;
    int branchId,userId=0;
    FirebaseFirestore db;
    BranchBean branchBean;
    SpotsDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assigned_branches);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = FirebaseFirestore.getInstance();
        initViews();
        getData();

        if (AdminUtil.isNetworkConnected(this)){
            progressDialog = new SpotsDialog(ViewAssignedBranchesActivity.this, R.style.Custom);
            progressDialog.show();
            retrieveBranch();
        }
    }

    public void retrieveBranch() {
        db.collection(Constants.branchCollection).whereEqualTo("userId",userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                progressDialog.dismiss();
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            branchBean = doc.getDocument().toObject(BranchBean.class);
                            branchBeanArrayList.add(branchBean);
                            branchListAdapter = new BranchListAdapter(ViewAssignedBranchesActivity.this,
                                    R.layout.admin_adapter_branch_listitem, branchBeanArrayList);
                            listView.setAdapter(branchListAdapter);
                            branchListAdapter.notifyDataSetChanged();
                            getSupportActionBar().setTitle("Branch("+branchBeanArrayList.size()+")");
                        }else{
                            Toast.makeText(ViewAssignedBranchesActivity.this,"No branches assigned.",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void initViews() {
        branchBeanArrayList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.lvBranch);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                branchId = branchList.get(position).getBranchId();
                branchName = branchList.get(position).getBranchName();
                branchContact = branchList.get(position).getBranchContact();
                branchBean = branchList.get(position);
                Log.i("BNAME",branchName);
                registerForContextMenu(listView);
                view.showContextMenu();
            }
        });*/
    }

    public void getData(){
        Intent rcvi = getIntent();
        if(rcvi.hasExtra(AdminUtil.userId)){
            userId = rcvi.getIntExtra(AdminUtil.userId,0);
        }else{
            finish();
            Toast.makeText(ViewAssignedBranchesActivity.this,
                    AdminUtil.Error_message, Toast.LENGTH_SHORT).show();
        }
    }


}
