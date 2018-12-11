package com.auribisesmyplayschool.myplayschool.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.auribisesmyplayschool.myplayschool.adapter.AssignTeacherAdapter;
import com.auribisesmyplayschool.myplayschool.bean.BatchBean;
import com.auribisesmyplayschool.myplayschool.bean.TeacherBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class AssignInstructorActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listViewTeachers;
    private SharedPreferences preferences;
    private ArrayList<TeacherBean> teacherBeanArrayList,selectedTeacherList;
    private AssignTeacherAdapter adapter;
    private int request,batchId;
    private ArrayList<Integer> userIds=new ArrayList<>();
    private Button assignButton;
    private String tempUserIds="",branchName,batch_title;
    FirebaseFirestore db;
    TeacherBean teacherBean;
    BatchBean batchBean;
    ArrayList<BatchBean> batchBeanArrayList,batchBeanArrayListActive;
    List<String> assignedBatchesList;
    List<Integer> assignedBatchesBranCourId;
    int check;

    private void initViews(){
        batchId=getIntent().getIntExtra("batchId",0);
        branchName = getIntent().getStringExtra("branchName");
        batchBeanArrayList = (ArrayList<BatchBean>) getIntent().getSerializableExtra("batchList");
        teacherBean = new TeacherBean();

        //CHECK AFTER
        assignedBatchesList = new ArrayList<>();
        assignedBatchesBranCourId = new ArrayList<>();

        batchBean = (BatchBean) getIntent().getSerializableExtra("batchBean");
        batch_title = getIntent().getStringExtra("batch_title");
        teacherBeanArrayList = new ArrayList<>();
        batchBeanArrayListActive = new ArrayList<>();
        selectedTeacherList = new ArrayList<>();
        getSupportActionBar().setTitle(getIntent().getStringExtra("batch_title"));
        db = FirebaseFirestore.getInstance();
        assignButton=(Button)findViewById(R.id.btnAssignTeachers);
        preferences=getSharedPreferences(AttUtil.shpREG, Context.MODE_PRIVATE);
        listViewTeachers=(ListView)findViewById(R.id.listViewTeachers);
        assignButton.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_instructor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        AttUtil.progressDialog(this);
        AttUtil.pd(1);
        retrieveTeachers();
    }

     void retrieveTeachers() {
        db.collection(Constants.usersCollection).whereEqualTo("userType",3)
                .whereEqualTo("branchName",branchName).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                            teacherBean = doc.toObject(TeacherBean.class);
                            teacherBeanArrayList.add(teacherBean);
                        }
                        AttUtil.pd(0);
                        adapter=new AssignTeacherAdapter(AssignInstructorActivity.this, R.layout.adapter_assign_teacher_layout,teacherBeanArrayList);
                        listViewTeachers.setAdapter(adapter);
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.btnAssignTeachers){
            AttUtil.progressDialog(this);
            AttUtil.pd(1);
            assignTeachers();
        }
    }

     void assignTeachers() {
         for(int i=0;i<teacherBeanArrayList.size();i++){
             if(teacherBeanArrayList.get(i).isEnabled()){
                 userIds.add(teacherBeanArrayList.get(i).getUserId());
                 selectedTeacherList.add(teacherBeanArrayList.get(i));
                 if(i==0)
                     tempUserIds=""+teacherBeanArrayList.get(i).getUserId();
                 else
                     tempUserIds=tempUserIds+","+teacherBeanArrayList.get(i).getUserId();
             }

         }

         for(int i=0;i<selectedTeacherList.size();i++){
             getAssignedBatchesList(selectedTeacherList.get(i).getUserId(),selectedTeacherList.get(i).getUserName(),i);
         }
         /*AlertDialog.Builder builder=new AlertDialog.Builder(this);
         builder.setCancelable(false);
         builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 dialog.dismiss();
                 finish();
             }
         });*/

         /*for(int i=0;i<batchBeanArrayList.size();i++){
             if(batchBeanArrayList.get(i).getBatchId()==batchId){
                 //Log.i("test",AttUtil.batchBeanArrayList.get(i).getBatcTeachGroup()+"");
                 if(batchBeanArrayList.get(i).getBatcTeachGroup()!=null){
                     batchBeanArrayList.get(i).setBatcTeachGroup(
                             batchBeanArrayList.get(i).getBatcTeachGroup());//+","+
                                    // jsonObject.getString("batchTeacherId").toString());
                     batchBeanArrayList.get(i).setUserGroup(
                             batchBeanArrayList.get(i).getUserGroup()+","+
                                     tempUserIds);
                 }else{
                     batchBeanArrayList.get(i).setBatcTeachGroup(String.valueOf(selectedTeacherList));
                     batchBeanArrayList.get(i).setUserGroup(
                             tempUserIds);
                 }
                 batchBeanArrayList.get(i).setUserId(userIds.get(0));
             }
         }
         //batchBeanArrayListActive.clear();
         for (int i=0;i<batchBeanArrayList.size();i++){
             if(batchBeanArrayList.get(i).getBatchStatus()==1)
                 batchBeanArrayListActive.add(batchBeanArrayList.get(i));
         }*/

         //updateData();
         //builder.setMessage("Teacher(s) assigned");
         //builder.create().show();
    }

    void getAssignedBatchesList(final int userId, final String userName,final int i) {
        db.collection(Constants.usersCollection).document(String.valueOf(userId)).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        assignedBatchesList = (List<String>) documentSnapshot.get("batchGroup");
                        assignedBatchesBranCourId = (List<Integer>) documentSnapshot.get("branCourId");

                        if(assignedBatchesList.size()!=0){
                            for(int i=0;i<assignedBatchesList.size();i++){
                                if(assignedBatchesList.get(i).equals(batch_title)){
                                    check = 0;
                                    break;
                                }else{
                                    check = 1;
                                }
                            }
                        }else{
                            check = 2;
                        }

                        if(check!=0){
                            assignedBatchesList.add(batch_title);
                            assignedBatchesBranCourId.add(batchBean.getBranCourId());
                            db.collection(Constants.usersCollection).document(String.valueOf(userId))
                                    .update("batchGroup",assignedBatchesList,"batchCount",assignedBatchesList.size(),"branCourId",assignedBatchesBranCourId)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //assignBatchCount(userId);
                                    assignedBatchesList.clear();
                                    assignedBatchesBranCourId.clear();
                                    //selectedTeacherList.get(i).setBatchCount(assignedBatchesList.size());
                                    //assignBatchCount(userId,i);
                                    AttUtil.pd(0);
                                    Toast.makeText(AssignInstructorActivity.this,userName+" assigned to "+batch_title,Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                        }else{
                            AttUtil.pd(0);
                            Toast.makeText(AssignInstructorActivity.this,batch_title+" is already assigned to "+userName,Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

     void assignBatchCount(int userId,int i) {
         db.collection(Constants.usersCollection).document(String.valueOf(userId))
                 .update("batchCount",selectedTeacherList.get(i).getBatchCount()).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
                 assignedBatchesList.clear();
                 Toast.makeText(AssignInstructorActivity.this,"Teacher assigned!!",Toast.LENGTH_LONG).show();
                 AttUtil.pd(0);
                 finish();
             }
         });
    }

    void updateData() {
        db.collection(Constants.branchCollection).document(String.valueOf(batchBean.getBranchId()))
                .collection(Constants.branch_course_collection).document(String.valueOf(batchBean.getBranCourId()))
                .collection(Constants.batch_section_collection).document(String.valueOf(batchBean.getBatchId()))
                .update("batcTeachGroup",selectedTeacherList).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AssignInstructorActivity.this,"Updated",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
