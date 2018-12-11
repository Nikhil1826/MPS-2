package com.auribisesmyplayschool.myplayschool.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adapter.ViewBatchAdapter;
import com.auribisesmyplayschool.myplayschool.bean.BatchBean;
import com.auribisesmyplayschool.myplayschool.bean.TeacherBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class LinkTeacherSection extends Activity {
    ListView lv;
    ViewBatchAdapter adapter;
    Intent i;
    int userId=0,pos;
    TeacherBean rcvTeacherBean;
    FirebaseFirestore db;
    BatchBean batchBean,removedBean;
    ArrayList<BatchBean> batchBeanArrayList;
    Toolbar toolbar;
    List<String> assignedBatchList;
    List<Integer> branCourIdList;
    int batchCount;


    void initViews() {
        i = getIntent();
        lv = (ListView)findViewById(R.id.lv);
        rcvTeacherBean = (TeacherBean) i.getSerializableExtra("teacherBean");
        db = FirebaseFirestore.getInstance();
        batchBean = new BatchBean();
        removedBean = new BatchBean();
        batchBeanArrayList = new ArrayList<>();
        assignedBatchList = new ArrayList<>();
        branCourIdList = new ArrayList<>();

        /*if (i.hasExtra("userId")) {
            userId = i.getIntExtra("userId", 0);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    pos = position;
                    //userIdList = userBatchList.get(pos).getUserGroup().split(",");
                    //batchTeachIdList = userBatchList.get(pos).getBatcTeachGroup().split(",");
                    AlertDialog.Builder builder = new AlertDialog.Builder(LinkTeacherSection.this);
                    String options[] = {"Un-Link Teacher"};
                    builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                unlinkBatch(userBatchList.get(position).getBatchId());
            }
        });
        builder.create().show();
    }
});
        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_teacher_section);
        initViews();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        AttUtil.progressDialog(this);
        AttUtil.pd(1);
        //getBatchCount();
        getLinkedSections();

    }

     void getBatchCount() {
        db.collection(Constants.usersCollection).document(String.valueOf(rcvTeacherBean.getUserId()))
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                batchCount = documentSnapshot.getLong("batchCount").intValue();
                rcvTeacherBean.setBatchCount(batchCount);
                getLinkedSections();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    void getLinkedSections() {
        for(int i=0;i<rcvTeacherBean.getBatchCount();i++){
            db.collection(Constants.branchCollection).document(String.valueOf(rcvTeacherBean.getBranchId()))
                    .collection(Constants.branch_course_collection)
                    .document(String.valueOf(rcvTeacherBean.getBranCourId().get(i)))
                    .collection(Constants.batch_section_collection)
                    .whereEqualTo("batch_title",rcvTeacherBean.getBatchGroup().get(i))
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                        batchBean = doc.toObject(BatchBean.class);
                        batchBeanArrayList.add(batchBean);
                    }
                    toolbar.setTitle("Section("+batchBeanArrayList.size()+")");
                    toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
                    adapter = new ViewBatchAdapter(LinkTeacherSection.this, R.layout.adapter_batch,batchBeanArrayList);
                    lv.setAdapter(adapter);
                    AttUtil.pd(0);
                    lv.setOnItemClickListener(itemclk);
                }
            });
        }

    }

    AdapterView.OnItemClickListener itemclk=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            pos=position;
            AlertDialog.Builder builder=new AlertDialog.Builder(LinkTeacherSection.this);
            String options[]={"Un-Link Teacher"};
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AttUtil.progressDialog(LinkTeacherSection.this);
                    AttUtil.pd(1);
                    removedBean =  batchBeanArrayList.remove(position);
                    //branCourIdList.remove(position);
                    assignBatchName();
                    unlinkBatch(rcvTeacherBean.getUserId());
                }
            });
            builder.create().show();
        }
    };

     void assignBatchName() {
         for(int i=0;i<batchBeanArrayList.size();i++){
             assignedBatchList.add(batchBeanArrayList.get(i).getBatch_title());
             branCourIdList.add(batchBeanArrayList.get(i).getBranCourId());
         }
    }

    void unlinkBatch(int userId) {
         db.collection(Constants.usersCollection).document(String.valueOf(userId))
                 .update("batchGroup",assignedBatchList,"batchCount",assignedBatchList.size(),"branCourId",branCourIdList)
                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void aVoid) {
                         AttUtil.pd(0);
                         /*rcvTeacherBean.setBatchCount(assignedBatchList.size());
                         rcvTeacherBean.setBatchGroup(assignedBatchList);
                         rcvTeacherBean.setBranCourId(branCourIdList);*/
                         assignedBatchList.clear();
                         branCourIdList.clear();
                         adapter.notifyDataSetChanged();
                         //setResult(333,new Intent().putExtra("teachBean",rcvTeacherBean));
                         Toast.makeText(LinkTeacherSection.this,rcvTeacherBean.getUserName()+" is unlinked from "+removedBean.getBatch_title(),Toast.LENGTH_SHORT).show();
                     }
                 });
    }


}
