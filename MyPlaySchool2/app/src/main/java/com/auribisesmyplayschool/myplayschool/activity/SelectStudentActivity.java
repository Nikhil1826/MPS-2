package com.auribisesmyplayschool.myplayschool.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adapter.SelectStudentAdapter;
import com.auribisesmyplayschool.myplayschool.bean.StudentBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SelectStudentActivity extends AppCompatActivity implements View.OnClickListener {
    //int batchId;
    ProgressDialog pd;
    ArrayList<StudentBean> studentList;
    ListView listViewStudents;
    SelectStudentAdapter adapter;
    StudentBean studentBean;
    FirebaseFirestore db;
    String batchName;

    void initViews(){
        AttUtil.progressDialog(this);
        db = FirebaseFirestore.getInstance();
        listViewStudents=(ListView)findViewById(R.id.listViewSelectStu);
        studentList = new ArrayList<>();
        studentBean = new StudentBean();
        pd=new ProgressDialog(this);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        batchName=getIntent().getStringExtra("batch_name");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_student);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        AttUtil.pd(1);
        fetchStudents();
    }

     void fetchStudents() {
        db.collection(Constants.student_collection).whereEqualTo("batch_title",batchName)
                .whereEqualTo("approved",1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    studentBean = doc.toObject(StudentBean.class);
                    studentList.add(studentBean);
                }

                AttUtil.pd(0);
                if(studentList.size()>0){
                    afterResponse();
                }else{
                    Toast.makeText(SelectStudentActivity.this,"No Student Found!",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

     void afterResponse() {
         adapter=new SelectStudentAdapter(this,R.layout.select_student_adapter,studentList);
         LayoutInflater inflater=getLayoutInflater();
         LinearLayout header=(LinearLayout) inflater.inflate(R.layout.select_student_header,null);
         LinearLayout footer=(LinearLayout) inflater.inflate(R.layout.select_student_footer,null);
         Button submit=(Button)footer.findViewById(R.id.btnSubmit);
         listViewStudents.addHeaderView(header);
         listViewStudents.addFooterView(footer);
         listViewStudents.setAdapter(adapter);
         submit.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnSubmit){
            setResult(102);
            finish();
        }
    }
}
