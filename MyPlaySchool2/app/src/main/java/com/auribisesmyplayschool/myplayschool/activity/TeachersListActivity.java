package com.auribisesmyplayschool.myplayschool.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adapter.TeacherAdapter;
import com.auribisesmyplayschool.myplayschool.bean.TeacherBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;

import java.io.Serializable;
import java.util.ArrayList;

public class TeachersListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private SharedPreferences preferences;
    private ListView listViewTeachers;
    private ArrayList<TeacherBean> teacherBeanArrayList;
    private TeacherAdapter adapter;

    Intent rcv;

    private void initViews(){
        listViewTeachers = (ListView)findViewById(R.id.listViewTeachers);
        listViewTeachers.setOnItemClickListener(this);
        preferences=getSharedPreferences(AttUtil.shpREG, Context.MODE_PRIVATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rcv = getIntent();
        teacherBeanArrayList = (ArrayList<TeacherBean>) rcv.getSerializableExtra("teacherBeanArrayList");
        initViews();
        receiveTeachers();
    }

    private void receiveTeachers(){
        adapter = new TeacherAdapter(this, R.layout.adapter_teacher_layout,teacherBeanArrayList);
        getSupportActionBar().setTitle("Teacher("+teacherBeanArrayList.size()+")");
        listViewTeachers.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(this,LinkTeacherSection.class);
        intent.putExtra("teacherBean", (Serializable) teacherBeanArrayList.get(position));
        startActivityForResult(intent,AttUtil.REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AttUtil.REQ_CODE && resultCode == 333){
            TeacherBean teacherBean = (TeacherBean) data.getSerializableExtra("teachBean");
            for(int i=0;i<teacherBeanArrayList.size();i++){
                if(teacherBean.getUserId() == teacherBeanArrayList.get(i).getUserId()){
                    teacherBeanArrayList.get(i).setBatchCount(teacherBean.getBatchCount());
                    teacherBeanArrayList.get(i).setBranCourId(teacherBean.getBranCourId());
                    teacherBeanArrayList.get(i).setBatchGroup(teacherBean.getBatchGroup());
                    Toast.makeText(TeachersListActivity.this,"BC "+teacherBeanArrayList.get(i).getBatchGroup().size(),Toast.LENGTH_SHORT).show();
                    break;
                }
            }

        }
    }
}
