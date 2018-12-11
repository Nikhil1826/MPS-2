package com.auribisesmyplayschool.myplayschool.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adminApp.adapter.GridViewAdapter;
import com.auribisesmyplayschool.myplayschool.bean.BatchBean;
import com.auribisesmyplayschool.myplayschool.bean.TeacherBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    GridView gridViewOptions;
    GridViewAdapter gridViewAdapter;
    public static int upload;
    ProgressDialog pd;
    SharedPreferences preferences;
    ArrayList<BatchBean> batchBeanArrayList;
    TeacherBean teacherBean;

    String optionsUpload[]={"Upload","View"};
    int imgUpload[]={R.drawable.ic_upload_information, R.drawable.ic_user_fee_summary};

    String options[]={"New Post","Update Post"};
    int img[]={R.drawable.ic_mail, R.drawable.ic_search_message};

    void initViews(){
        preferences=getSharedPreferences(AttUtil.shpREG, MODE_PRIVATE);
        pd=new ProgressDialog(this);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        //upload - 0 : Messages
        //upload - 1 : Assignments
        if(upload==1){
            getSupportActionBar().setTitle("Media");
            gridViewOptions=findViewById(R.id.gridViewMsg);
            gridViewAdapter=new GridViewAdapter(this,optionsUpload,imgUpload);
        }else{
            if(preferences.getInt(AttUtil.shpLoginType,0) == 2){
                batchBeanArrayList = (ArrayList<BatchBean>) getIntent().getSerializableExtra(AttUtil.BATCH_LIST);
            }else{
                teacherBean = (TeacherBean) getIntent().getSerializableExtra(AttUtil.TEACHERBEAN);
            }

            getSupportActionBar().setTitle("Digital Message");
            gridViewOptions=findViewById(R.id.gridViewMsg);
            gridViewAdapter=new GridViewAdapter(this,options,img);
        }
        gridViewOptions.setAdapter(gridViewAdapter);
        gridViewOptions.setOnItemClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        if(getIntent().hasExtra("upload"))
            upload=getIntent().getIntExtra("upload",0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        switch(position){
            case 0:
                if(DashboardActivity.upload==1){
                    intent=new Intent(this, AudioUploadActivity2.class);
                    intent.putExtra(AttUtil.BATCH_LIST,batchBeanArrayList);
                    startActivity(intent);
                }else{
                    if(preferences.getInt(AttUtil.shpLoginType,0) == 2){
                        intent=new Intent(this, SendDigitalMsgActivity.class);
                        intent.putExtra(AttUtil.BATCH_LIST,batchBeanArrayList);
                        this.startActivity(intent);
                    }else{
                        intent=new Intent(this, SendDigitalMsgActivity.class);
                        intent.putExtra(AttUtil.TEACHERBEAN,teacherBean);
                        this.startActivity(intent);
                    }

                }
                break;

            case 1:
                if(DashboardActivity.upload==1){
                    //alertBatches(1);
                }else{
                    if(preferences.getInt(AttUtil.shpLoginType,0) == 2){
                        intent=new Intent(this, UpdateMsgActivity.class);
                        intent.putExtra(AttUtil.BATCH_LIST,batchBeanArrayList);
                        this.startActivity(intent);
                    }else{
                        intent=new Intent(this, UpdateMsgActivity.class);
                        intent.putExtra(AttUtil.TEACHERBEAN,teacherBean);
                        this.startActivity(intent);
                    }

                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        upload=0;
    }
}
