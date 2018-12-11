package com.auribisesmyplayschool.myplayschool.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adapter.ApproveAdapter;
import com.auribisesmyplayschool.myplayschool.bean.StudentBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;

import java.util.ArrayList;

public class StudentFeeListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listViewStudents;
    ArrayList<StudentBean> studentList;
    ApproveAdapter studentListAdapter;
    Intent intent;
    SharedPreferences pref;
    int batchId,branchId,position;
    boolean approvedStudents;
    ArrayList<StudentBean> joiningDateList,joinedStudentsList;
    StudentBean studentBean;

    void initViews(){
        studentBean = new StudentBean();
        pref=getSharedPreferences(AttUtil.shpREG,MODE_PRIVATE);
        intent=getIntent();
        batchId=intent.getIntExtra(AttUtil.KEY_BATCH_ID, 0);
        branchId=pref.getInt(AttUtil.shpBranchId,0);
        studentList = (ArrayList<StudentBean>) intent.getSerializableExtra("studentList");
        joiningDateList=new ArrayList<>();
        joinedStudentsList=new ArrayList<>();
        if(getIntent().getIntExtra("approved_students",0)==1)
            approvedStudents=true;
        listViewStudents=(ListView)findViewById(R.id.listViewStudents);
        listViewStudents.setOnItemClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_fee_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        //retrieveStudents();
        studentListAdapter = new ApproveAdapter(this, R.layout.adapter_approve_students,studentList);
        listViewStudents.setAdapter(studentListAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.position=position;
        if(approvedStudents){
            /*Intent intent=new Intent(this,FeeRecordActivity.class);
            intent.putExtra("admissionId",studentList.get(position).getAdmissionId());
            intent.putExtra("studentId",studentList.get(position).getStudentId());
            intent.putExtra("feeCategory",studentList.get(position).getFeeCategorySelected());
            startActivity(intent);*/
        }else{
            Intent intent=new Intent(this,FeeSelectActivty.class);
            intent.putExtra("studentId",studentList.get(position).getStudentId());
            intent.putExtra("admissionId",studentList.get(position).getAdmissionId());
            intent.putExtra("studentbean",studentList.get(position));
            intent.putExtra("activitySignal",1);
            startActivityForResult(intent,301);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==301 && resultCode==201){
            studentBean = (StudentBean) data.getSerializableExtra(AttUtil.TAG_STUDENTBEAN);
            setResult(920,new Intent().putExtra(AttUtil.TAG_STUDENTBEAN,studentBean).putExtra("position",position));
            studentList.remove(position);
            studentListAdapter.notifyDataSetChanged();
            if(studentList.size()==0)
                finish();
        }
    }


}
