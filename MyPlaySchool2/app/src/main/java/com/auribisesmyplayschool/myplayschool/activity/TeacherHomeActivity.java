package com.auribisesmyplayschool.myplayschool.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.bean.TeacherBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;

public class TeacherHomeActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private CardView cardDigital,cardAssignment;//,cardTest;
    private int reqCode;
    private SwipeRefreshLayout waveSwipeRefreshLayout;
    TeacherBean teacherBean;

    private void initViews(){

        preferences=getSharedPreferences(AttUtil.shpREG,MODE_PRIVATE);
        teacherBean = (TeacherBean) getIntent().getSerializableExtra(AttUtil.TEACHERBEAN);
        editor=preferences.edit();
        cardDigital=findViewById(R.id.cardDigital);
        cardAssignment=findViewById(R.id.cardAssignment);
        //cardTest=(CardView)findViewById(R.id.cardTest);
        cardDigital.setOnClickListener(this);
        cardAssignment.setOnClickListener(this);
        findViewById(R.id.cardAttendance).setOnClickListener(this);
        //cardTest.setOnClickListener(this);

        waveSwipeRefreshLayout = findViewById(R.id.mainPageSwipeTeacher);
        waveSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, android.R.color.white));
//        waveSwipeRefreshLayout.setWaveColor(ContextCompat.getColor(this, R.color.colorPrimary));
//        waveSwipeRefreshLayout.setShadowRadius(0);
        try{
            waveSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    waveSwipeRefreshLayout.setRefreshing(true);
                   // syncTeacher();
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
        initViews();
        getSupportActionBar().setTitle(teacherBean.getUserName());
       // syncTeacher();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cardDigital:
                if(!waveSwipeRefreshLayout.isRefreshing())
                    startActivity(new Intent(this,DashboardActivity.class)
                    .putExtra(AttUtil.TEACHERBEAN,teacherBean));
                break;
            case R.id.cardAssignment:
                if(!waveSwipeRefreshLayout.isRefreshing())
                    startActivity(new Intent(this,DashboardActivity.class)
                            .putExtra("upload",1));
                break;
            case R.id.cardAttendance:
                if(!waveSwipeRefreshLayout.isRefreshing())
                    startActivity(new Intent(this,StudentAttendanceActivity.class));
                break;
//            case R.id.cardTest:
//                Intent testInent=new Intent(this,DashboardActivity.class);
//                testInent.putExtra("upload",2);
//                startActivity(testInent);
//                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_teacher_home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.action_logout){
            if(!waveSwipeRefreshLayout.isRefreshing())
                logout();
        }
        return super.onOptionsItemSelected(item);
    }

    void logout(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editor.clear().commit();
               // AttUtil.batchBeanArrayListTeacher.clear();
                startActivity(new Intent(TeacherHomeActivity.this,LoginActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
