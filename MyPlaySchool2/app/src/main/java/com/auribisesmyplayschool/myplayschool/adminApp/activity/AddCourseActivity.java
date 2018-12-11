package com.auribisesmyplayschool.myplayschool.adminApp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.CourseBean;
import com.auribisesmyplayschool.myplayschool.adminApp.classes.AdminUtil;
import com.auribisesmyplayschool.myplayschool.bean.AdminBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

import dmax.dialog.SpotsDialog;

public class AddCourseActivity extends AppCompatActivity {
    EditText edCourseName;
    Button btnAddcourse;
    TextView txtCourseName;
    boolean bool = false;
    CourseBean courseBean, rcvCourseBean;
    FirebaseFirestore db;
    AdminBean adminBean;
    Intent rcvi;
    ArrayList<Integer> listId;
    int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
         rcvi = getIntent();
        adminBean = (AdminBean) rcvi.getSerializableExtra(AdminUtil.TAG_ADMIN_BEAN);
        db = FirebaseFirestore.getInstance();

        if(rcvi.hasExtra(AdminUtil.TAG_COURSE)){
            bool=true;
            getSupportActionBar().setTitle("Update Class Details");
        }else
            getSupportActionBar().setTitle("Add Class");

        if(bool){
            rcvCourseBean = (CourseBean) rcvi.getSerializableExtra(AdminUtil.TAG_COURSE);
            courseBean = rcvCourseBean;
            edCourseName.setText(courseBean.getCourseName().toString());
            txtCourseName.setText("Edit Class Details");
            btnAddcourse.setText("Update");
        }


    }

    public void initViews(){
        listId = new ArrayList<>();
        rcvCourseBean = new CourseBean();
        edCourseName = (EditText) findViewById(R.id.edcourseName);
        btnAddcourse = (Button) findViewById(R.id.button);
        txtCourseName = (TextView) findViewById(R.id.textView2);
        btnAddcourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edCourseName.getText().toString().length() != 0 ) {
                    courseBean.setCourseName(edCourseName.getText().toString().trim());
                    /*if (AdminUtil.isNetworkConnected(AddCourseActivity.this)){
                        //add_course_thread();
                    }else{
                        //addNetConnect();
                    }*/
                    AttUtil.progressDialog(AddCourseActivity.this);
                    AttUtil.pd(1);
                    if(bool){
                        updateCourse();
                    }else {
                        //addCourse();
                        getLastCourseId();
                    }
                }else{
                    Toast.makeText(AddCourseActivity.this,
                            "Error:\nEmpty Class Name", Toast.LENGTH_SHORT).show();
                }
            }
        });
        courseBean = new CourseBean();
    }

     void getLastCourseId() {
        db.collection(Constants.adminCollection).document(String.valueOf(adminBean.getAdminId()))
                .collection(Constants.coursesCollection).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    if (queryDocumentSnapshots.size()>0){
                        int chkId = doc.getLong("courseId").intValue();
                        listId.add(chkId);
                    }else{
                        courseId = 1;
                    }
                }
                if(listId.size()>0){
                    courseId = Collections.max(listId);
                    courseId = courseId + 1;
                }else{
                    courseId = 1;
                }
                setDataToBean();
            }
        });
    }

    void updateCourse(){
        db.collection(Constants.adminCollection).document(String.valueOf(adminBean.getAdminId())).collection(Constants.coursesCollection).document(String.valueOf(courseBean.getCourseId())).update("courseName",edCourseName.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AttUtil.pd(0);
                Toast.makeText(AddCourseActivity.this, "Class Updated ", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent();
                intent.putExtra(AdminUtil.TAG_COURSE,courseBean);
                setResult(102,intent);
                finish();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddCourseActivity.this, "Something Went Wrong!! ", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void setDataToBean(){
        courseBean.setAdminId(adminBean.getAdminId());
        courseBean.setCourseName(edCourseName.getText().toString().trim());
        courseBean.setCourseId(courseId);
        addCourse();
    }

    void addCourse(){
        db.collection(Constants.adminCollection).document(String.valueOf(adminBean.getAdminId()))
                .collection(Constants.coursesCollection).document(String.valueOf(courseBean.getCourseId()))
                .set(courseBean).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AttUtil.pd(0);
                Toast.makeText(AddCourseActivity.this, "New Class Added ", Toast.LENGTH_SHORT).show();
                listId.clear();
                setResult(AdminUtil.RES_CODE,new Intent().putExtra(AdminUtil.TAG_COURSE,courseBean));
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




}
