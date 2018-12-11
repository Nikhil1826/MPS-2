package com.auribisesmyplayschool.myplayschool.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adapter.ViewBatchAdapter;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BranCourBean;
import com.auribisesmyplayschool.myplayschool.bean.BatchBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BatchListActivity extends AppCompatActivity {
    ListView lv;
    ArrayList<BatchBean> batchList,userBatchList;
    ArrayList<String> userGrouparrayList,batchTeachGroupArrayList;
    ViewBatchAdapter adapter;
    BatchBean batchBean;
    BranCourBean branCourBean;
    int pos,reqCode,userId=0,responseSignal=0;
    String batch_act_deact,branchName;
    SearchView searchView;
    SharedPreferences preferences;
    String userIdList[],batchTeachIdList[];
    ArrayList<BranCourBean> courBeanArrayList;
    FirebaseFirestore db;
    //int branchId;

    void initViews(){
        db = FirebaseFirestore.getInstance();
        searchView = new SearchView(this);
       // batchList = new ArrayList<>();
        batchBean = new BatchBean();
        branCourBean = new BranCourBean();
        userBatchList = new ArrayList<>();
        userGrouparrayList= new ArrayList<>();
        batchTeachGroupArrayList= new ArrayList<>();
        preferences=getSharedPreferences(AttUtil.shpREG, MODE_PRIVATE);
        lv = (ListView)findViewById(R.id.lv);
        //requestQ_v= Volley.newRequestQueue(BatchListActivity.this);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        batchList = (ArrayList<BatchBean>) i.getSerializableExtra(AttUtil.BATCH_LIST);
        courBeanArrayList = (ArrayList<BranCourBean>) i.getSerializableExtra(AttUtil.KEY_COURSE_ARRAYLIST);
        branchName = i.getStringExtra("branchName");
       // branchId = i.getIntExtra("branchId",0);
        /*if(i.hasExtra("userId")){
            userId=i.getIntExtra("userId",0);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    pos=position;
                    userIdList = userBatchList.get(pos).getUserGroup().split(",");
                    batchTeachIdList = userBatchList.get(pos).getBatcTeachGroup().split(",");
                    AlertDialog.Builder builder=new AlertDialog.Builder(BatchListActivity.this);
                    String options[]={"Un-Link Teacher"};
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                          //  unlinkBatch(userBatchList.get(position).getBatchId());
                        }
                    });
                    builder.create().show();
                }
            });

          //  batchList.addAll(AttUtil.batchBeanArrayList);
            for(int j=0;j<batchList.size();j++){
                if (batchList.get(j).getUserId()>0){
                    userBatchList.add(batchList.get(j));
                    userGrouparrayList.add(batchList.get(j).getUserGroup());
                    batchTeachGroupArrayList.add(batchList.get(j).getBatcTeachGroup());
                }
            }
            batchList.clear();
            for(int j=0;j<userBatchList.size();j++){
                String strArrayOne[]= userGrouparrayList.get(j).split(",");
                for(int x=0;x<strArrayOne.length;x++){
                    if(strArrayOne[x].toString().equals(String.valueOf(userId))){
                        batchList.add(userBatchList.get(j));
                    }
                }
            }
            userBatchList.clear();
            userBatchList.addAll(batchList);
            batchList.clear();
            if(userBatchList.size()>0){
                getSupportActionBar().setTitle("View Section("+userBatchList.size()+")");
                adapter = new ViewBatchAdapter(this, R.layout.adapter_batch,userBatchList);
                lv.setAdapter(adapter);
            }else{
                Toast.makeText(this, "No Section is linked to this teacher", Toast.LENGTH_SHORT).show();
            }
        }else {
            if(AttUtil.isNetworkConnected(this)){
            //    viewBatch();
            }else {
                //batchNetConnect();
            }
        }*/
    }

    void initAdapter() {
        getSupportActionBar().setTitle("Section("+batchList.size()+")");
        adapter = new ViewBatchAdapter(this, R.layout.adapter_batch,batchList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(itemclk);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_list);
        initViews();
        viewBatch();

    }

    void viewBatch(){
        //batchList = AttUtil.batchBeanArrayList;
        if(batchList.size()>0&&batchList!=null){
            initAdapter();
        }else{
            Toast.makeText(BatchListActivity.this, "No Section Found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            adapter.filter(newText.toString());
            Log.i("test","e");
            adapter.notifyDataSetChanged();
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_batch_menu,menu);
        MenuItem searchitem=menu.findItem(R.id.Search_batch);
        searchitem.setActionView(searchView);
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(onQueryTextListener);
        searchView.setQueryHint("Search");
        return true;
    }


    @Override
    public void onBackPressed() {
        setResult(965,new Intent().putExtra("callTeacher",1));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    AdapterView.OnItemClickListener itemclk=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            pos=position;
            batchBean=batchList.get(position);
            if(batchBean.getBatchStatus()==1){
                batch_act_deact="Deactivate Section";
            }else{
                batch_act_deact="Activate Section";
            }
            show_options(batchBean.getBatchStatus()==1);
        }
    };

    void show_options(boolean active) {
        AlertDialog.Builder build = new AlertDialog.Builder(this);

        String[] options;
        if(active){
            options=new String[4];
            options[0]="Update Section";
            options[1]=batch_act_deact;
            options[3]="Assign Teacher";
            options[2]="Day walkers";
        }
        else{
            options=new String[3];
            options[0]="Update Section";
            options[1]=batch_act_deact;
            options[2]="Day walkers";
        }

        build.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    //SKIPPED DO AFTER
                    case 0:
                        updateActivity();
                        break;
                    case 1:
                        if(AttUtil.isNetworkConnected(BatchListActivity.this)){
                            AttUtil.progressDialog(BatchListActivity.this);
                            AttUtil.pd(1);
                            batchStatus();
                        }else{
                            //signalBatchNetConnect();
                        }
                        break;
                        //CONTINUE
                    case 3:
                       /* BatchListActivity.this.startActivity(new Intent(BatchListActivity.this,AssignInstructorActivity.class)
                                .putExtra("batchId",batchBean.getBatchId())
                                .putExtra("batch_title",batchBean.getBatch_title())
                                .putExtra("branchName",branchName)
                                .putExtra("batchBean",batchBean)
                                .putExtra("batchList",batchList));*/

                        startActivityForResult(new Intent(BatchListActivity.this,AssignInstructorActivity.class)
                                .putExtra("batchId",batchBean.getBatchId())
                                .putExtra("batch_title",batchBean.getBatch_title())
                                .putExtra("branchName",branchName)
                                .putExtra("batchBean",batchBean)
                                .putExtra("batchList",batchList),AttUtil.REQ_CODE);
                        break;
                    case 2:
                        /*startActivity(new Intent(BatchListActivity.this,StudentRouteListActivity.class)
                                .putExtra("batchId",batchBean.getBatchId()));*/
                        break;
                }
            }
        });
        build.create().show();
    }
     void batchStatus() {
        if(batchBean.getBatchStatus()==1){
            batchBean.setBatchStatus(0);
        }else{
            batchBean.setBatchStatus(1);
        }
        db.collection(Constants.branchCollection).document(String.valueOf(batchBean.getBranchId())).collection(Constants.branch_course_collection)
                .document(String.valueOf(batchBean.getBranCourId())).collection(Constants.batch_section_collection)
                .document(String.valueOf(batchBean.getBatchId())).update("batchStatus",batchBean.getBatchStatus())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AttUtil.pd(0);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(BatchListActivity.this,"Batch Status Changed",Toast.LENGTH_LONG).show();
                    }
                });
    }
        void updateActivity(){
            startActivityForResult(new Intent(BatchListActivity.this,AddBatchActivity.class)
                    .putExtra(AttUtil.KEY_BATCH, (Serializable) batchBean)
                            .putExtra(AttUtil.KEY_COURSE_ARRAYLIST,courBeanArrayList), AttUtil.ATT_REQ);
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == AttUtil.ATT_REQ && resultCode == AttUtil.ATT_RES){
            BatchBean bean = (BatchBean)data.getSerializableExtra(AttUtil.KEY_BATCH);
            int check = data.getIntExtra(AttUtil.CHECK,0);
            setResult(950,new Intent().putExtra(AttUtil.UPDATED_BATCH,bean).putExtra(AttUtil.CHECK,check));
            if(check != 1){
                batchList.set(pos, bean);
            }else{
                batchList.remove(pos);
            }
            getSupportActionBar().setTitle("Section("+batchList.size()+")");
            adapter.notifyDataSetChanged();
        }
    }


}
