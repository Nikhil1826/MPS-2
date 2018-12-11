package com.auribisesmyplayschool.myplayschool.adminApp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BranchBean;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.UserBean;
import com.auribisesmyplayschool.myplayschool.adminApp.classes.AdminUtil;
import com.auribisesmyplayschool.myplayschool.bean.AdminBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

import dmax.dialog.SpotsDialog;

public class AddBranchActivity extends AppCompatActivity {
    EditText edBranchName,edBranchAddress,edBranchContact;
    Button btnAddBranch;
    Spinner spinnerBranchManager;
    ArrayList <UserBean> usersBeanArrayList;
    ArrayList<String> arrayListBranchManager;
    BranchBean branchBean;
    int pos=0,updatePos=0,userId=0;
    TextView txtBranchName;
    SharedPreferences preferences;
    FirebaseFirestore db;
    AdminBean adminBean;
    UserBean userBean;
    boolean updateFalg = false;
    ArrayList<Integer> listId;
    int branch_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_add_branch);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AttUtil.progressDialog(this);
        listId = new ArrayList<>();
        Intent rcvi = getIntent();
        adminBean = (AdminBean) rcvi.getSerializableExtra(AdminUtil.TAG_ADMIN_BEAN);
        db = FirebaseFirestore.getInstance();
        preferences = getSharedPreferences(AttUtil.shpREG, Context.MODE_PRIVATE);
        initViews();
        userBean = new UserBean();
        if(rcvi.hasExtra(AdminUtil.TAG_BRANCH)){
            updateFalg = true;
            getSupportActionBar().setTitle("Update Branch's Details");
        }else
            getSupportActionBar().setTitle("Add Branch");
        if(updateFalg){
            TextView textView =(TextView) findViewById(R.id.textView2);
            textView.setText("Update Branch Details");
            branchBean = (BranchBean) rcvi.getSerializableExtra(AdminUtil.TAG_BRANCH);
            userId = branchBean.getUserId();
            edBranchContact.setText(branchBean.getBranchContact());
            edBranchAddress.setText(branchBean.getBranchAddress());
            edBranchName.setText(branchBean.getBranchName());
            /*try{
                Picasso.with(this).load(branchBean.getImg_url())
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .placeholder(R.drawable.icon_branch_logo_default).resize(140,140)
                        .into(imageViewbranchIcon);
            }catch (Exception e){
                e.printStackTrace();
            }*/
        }

        AttUtil.pd(1);
       // if(!updateFalg){
            retrieve_users();
        //}else{
           // progressDialog.dismiss();
       // }

        arrayListBranchManager.clear();
        arrayListBranchManager.add("-- Select Branch Manager--");
    }

    public void retrieve_users() {
        db.collection(Constants.usersCollection).whereEqualTo("adminId",adminBean.getAdminId())
                .whereEqualTo("userType",1).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        userBean = doc.getDocument().toObject(UserBean.class);
                        usersBeanArrayList.add(userBean);
                    }
                }
                if(usersBeanArrayList.size()>0 || usersBeanArrayList != null){
                    for (int i = 0; i < usersBeanArrayList.size(); i++) {
                        arrayListBranchManager.add(usersBeanArrayList.get(i).getUserName());
                        if (updateFalg) {
                            if (usersBeanArrayList.get(i).getUserId() == branchBean.getUserId()) {
                                updatePos = i + 1;
                            }
                        }
                    }
                    spinnerBranchManager.setAdapter(new ArrayAdapter(AddBranchActivity.this,
                            android.R.layout.simple_dropdown_item_1line, arrayListBranchManager));
                    if (updateFalg) {
                        spinnerBranchManager.setSelection(updatePos);
                        pos = updatePos;
                    }
                }
                AttUtil.pd(0);
            }
        });
    }


    public void initViews(){
        //Components
        branchBean = new BranchBean();
        arrayListBranchManager = new ArrayList<>();
        usersBeanArrayList = new ArrayList<>();
        edBranchName = (EditText) findViewById(R.id.edBranchName);
        edBranchAddress = (EditText) findViewById(R.id.edBranchAddress);
        edBranchContact = (EditText) findViewById(R.id.edBranchContact);
        spinnerBranchManager = (Spinner) findViewById(R.id.spinnerBranchManager);
        txtBranchName = (TextView) findViewById(R.id.textView2);
        btnAddBranch = (Button) findViewById(R.id.button);
        /*imageViewbranchIcon = (ImageView)findViewById(R.id.imageViewbranchIcon);
        gestureDetector=new GestureDetector(this, new GestureListener());
        filePath= Environment.getExternalStorageDirectory().getAbsolutePath() + "/TEMPIMG";
        File file=new File(filePath);
        if(!file.exists())
            file.mkdirs();
        imageViewbranchIcon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });*/
        btnAddBranch.setOnClickListener(click);
        spinnerBranchManager.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
                if(pos>0){
                    branchBean.setUserId(usersBeanArrayList.get(pos-1).getUserId());
                    branchBean.setUserName(usersBeanArrayList.get(pos-1).getUserName());
                    branchBean.setUserEmail(usersBeanArrayList.get(pos-1).getUserEmail());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                if(checkEmptyValues()){
                    if(AdminUtil.isNetworkConnected(AddBranchActivity.this)){
                        AttUtil.pd(1);
                        if(!updateFalg){
                            getLastBranchId();
                           // add_branch_thread();
                        }
                        else{
                            setDataToBean();
                        }

                    }else{
                        //addNetConnect();
                    }
                }


        }
    };

    void setDataToBean(){
        branchBean.setBranchName(edBranchName.getText().toString().trim());
        branchBean.setBranchAddress(edBranchAddress.getText().toString().trim());
        branchBean.setBranchContact(edBranchContact.getText().toString().trim());
        branchBean.setAdminId(adminBean.getAdminId());
        branchBean.setBranchStatus(1);
        if(!updateFalg){
            branchBean.setBranchId(branch_id+1);
            add_branch_thread();
        }else {
            branchBean.setBranchId(branchBean.getBranchId());
            update_branch_thread();
        }
    }

    void getLastBranchId(){
        db.collection(Constants.branchCollection).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    if(queryDocumentSnapshots.size()>0){
                        int chkId = doc.getLong("branchId").intValue();
                        listId.add(chkId);
                    }else {
                        branch_id = 0;
                    }
                }
                if(listId.size()>0){
                    branch_id = Collections.max(listId);
                }else{
                    branch_id = 0;
                }
                setDataToBean();
            }
        });
    }

    public void update_branch_thread() {
        db.collection(Constants.branchCollection).document(String.valueOf(branchBean.getBranchId())).set(branchBean).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AttUtil.pd(0);
                Toast.makeText(AddBranchActivity.this,"Updated",Toast.LENGTH_SHORT).show();
                setResult(102,new Intent().putExtra(AdminUtil.TAG_BRANCH,branchBean));
                finish();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void add_branch_thread() {
        db.collection(Constants.branchCollection).document(String.valueOf(branchBean.getBranchId())).set(branchBean)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AttUtil.pd(0);
                listId.clear();
                Toast.makeText(AddBranchActivity.this,"Added",Toast.LENGTH_SHORT).show();
                setResult(AdminUtil.RES_CODE,new Intent().putExtra(AdminUtil.TAG_BRANCH,branchBean));
                finish();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        AttUtil.pd(0);
                        Toast.makeText(AddBranchActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public boolean checkEmptyValues(){
        boolean flag = true;
        String msg = "Error:";
        if(edBranchName.getText().toString().length() == 0 ) {
            msg = msg +"\nEmpty Branch Name";
            flag = false;
        }else {

            edBranchName.setError(null);
        }
        if(edBranchAddress.getText().toString().length() == 0 ){
            flag = false;
            msg = msg +"\nEmpty Branch Address";
        } else {

            edBranchAddress.setError(null);
        }
        if(edBranchContact.getText().toString().length() < 10 ){
            flag = false;
            msg = msg +"\nInvalid Branch Contact";
        } else {

            edBranchContact.setError(null);
        }
        if(pos==0){
            flag = false;
            msg = msg +"\nSelect Branch Manager";
        }
        if(!flag){
            Toast.makeText(AddBranchActivity.this,msg, Toast.LENGTH_LONG).show();
        }
        return flag;
    }
}
