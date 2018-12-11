package com.auribisesmyplayschool.myplayschool.adminApp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.auribisesmyplayschool.myplayschool.bean.TeacherBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.ObjectArrays;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class AddUserActivity extends AppCompatActivity {
    EditText edManagerName,edManagerEmail,edManagerContact,edtUserSalary;
    Button addManager;
    UserBean userBean,getUserBean;
    boolean flag;
    TextView txtUserName;
    SharedPreferences preferences;
    ArrayList<BranchBean> branchBeanArrayList;
    boolean bool = false;
    ArrayList<String> usertypeArrayList,branchNameArrayList;
    Spinner spnUserType,spnBranchName;
    int posSpnUserType=0,posSpnBranch=0,lastUserId,userAdminId,userId;
    FirebaseFirestore db;
    AdminBean adminBean;
    Intent rcv;
    String branchName;
    int branchId,userAddId;
    TeacherBean teacherBean;
    List<String> batchGroup;
    List<Integer> branCourId;
    ArrayList<Integer> listId;
    int chkId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R .layout.activity_add_user);
        preferences = getSharedPreferences(AttUtil.shpREG, Context.MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();
        rcv = getIntent();
        listId = new ArrayList<>();

        if (preferences.getInt(AttUtil.shpLoginType, 0) == 1) {
            adminBean = (AdminBean) rcv.getSerializableExtra(AdminUtil.TAG_ADMIN_BEAN);
            branchBeanArrayList = (ArrayList<BranchBean>)rcv.getSerializableExtra(AdminUtil.TAG_BRANCHARRAYLIST);
        }else{
            userAdminId = rcv.getIntExtra("adminId",0);
            userId = rcv.getIntExtra("userId",0);
            branchName = rcv.getStringExtra("branchName");
            branchId = rcv.getIntExtra("branchId",0);
        }
        initViews();
        getBeanData();

        addManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setDataIntoBean();
                if(AdminUtil.isNetworkConnected(AddUserActivity.this)){
                    AttUtil.progressDialog(AddUserActivity.this);
                    AttUtil.pd(1);
                    getLastUserId();

                }else {
                    //addNetConnect();
                }
            }
        });
        //Toast.makeText(AddUserActivity.this,branchNameArrayList.get(1),Toast.LENGTH_SHORT).show();

    }

     void getLastUserId() {
        db.collection(Constants.usersCollection).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    chkId = doc.getLong("userId").intValue();
                    listId.add(chkId);
                }
                if(listId.size()>0){
                    userAddId = Collections.max(listId);
                }else{
                    userAddId = 0;
                }
                setDataIntoBean();
            }
        });
    }


    void addTeacher() {
         if(!bool){
             if(checkEmptyValues()){
                 db.collection(Constants.usersCollection)
                         .document(String.valueOf(teacherBean.getUserId()))
                         .set(teacherBean).addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void aVoid) {
                         AttUtil.pd(0);
                         Toast.makeText(AddUserActivity.this,"Employee Added",Toast.LENGTH_SHORT).show();
                        // if (preferences.getInt(AttUtil.shpLoginType, 0) == 1){
                             sendBackActivity();
                         //}
                         finish();
                     }
                 })
                         .addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 AttUtil.pd(0);
                                 Toast.makeText(AddUserActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                             }
                         });

             }
         }
    }


    public void userAddOrUpdate() {
        if(!bool){
            if(checkEmptyValues()){
                db.collection(Constants.usersCollection)
                        .document(String.valueOf(userBean.getUserId()))
                        .set(userBean).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AttUtil.pd(0);
                        listId.clear();
                        Toast.makeText(AddUserActivity.this,"Employee Added",Toast.LENGTH_SHORT).show();
                       // if (preferences.getInt(AttUtil.shpLoginType, 0) == 1){
                            sendBackActivity();
                        //}
                        finish();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                AttUtil.pd(0);
                                Toast.makeText(AddUserActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        }else {
            if(checkEmptyValues()){
                db.collection(Constants.usersCollection).document(String.valueOf(getUserBean.getUserId()))
                        .set(userBean).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AttUtil.pd(0);
                        Toast.makeText(AddUserActivity.this,"Updated",Toast.LENGTH_SHORT).show();
                        sendBack();
                        finish();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                AttUtil.pd(0);
                                Toast.makeText(AddUserActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    public void sendBackActivity() {
        Intent intent=new Intent();
        intent.putExtra("teacherBean",teacherBean);
        setResult(370,intent);
    }


    public void initViews(){
        batchGroup = new ArrayList<>();
        branCourId = new ArrayList<>();
        teacherBean = new TeacherBean();
        usertypeArrayList = new ArrayList<>();
        usertypeArrayList.add("--Select Type of User--");
        if (preferences.getInt(AttUtil.shpLoginType, 0) == 1)
            usertypeArrayList.add("Manager");
        usertypeArrayList.add("Counsellor");
        usertypeArrayList.add("Instructor");
        usertypeArrayList.add("Driver");
        usertypeArrayList.add("Maid");
        branchNameArrayList = new ArrayList<>();
        edManagerName =  findViewById(R.id.edsubAdminName);
        edManagerEmail =  findViewById(R.id.edSubAdminEmail);
        edManagerContact =  findViewById(R.id.edSubAdminContact);
        edtUserSalary = findViewById(R.id.edtUserSalary);
        addManager =  findViewById(R.id.btnAddSubAdmin);
        txtUserName =  findViewById(R.id.textView3);
        spnUserType = findViewById(R.id.spnUserType);
        spnUserType.setAdapter(new ArrayAdapter(AddUserActivity.this,android.R.layout.simple_dropdown_item_1line,
                usertypeArrayList));
        spnBranchName = findViewById(R.id.spnBranchName);
        branchNameArrayList.add("--Select a Branch--");


        if (preferences.getInt(AttUtil.shpLoginType, 0) == 1) {
            if(!bool){
                for (int i=0;i<branchBeanArrayList.size();i++){
                    branchNameArrayList.add(branchBeanArrayList.get(i).getBranchName());
                }
                spnBranchName.setAdapter(new ArrayAdapter(AddUserActivity.this,android.R.layout.simple_dropdown_item_1line,
                        branchNameArrayList));
            }else{
                getBranchNameMethod();
            }
        }


       /* if(bool){
            for(int i = 0;i<branchBeanArrayList.size();i++) {
                if(branchBeanArrayList.get(i).getBranchId()==getUserBean.getBranchId()){
                    spnBranchName.setSelection(i+1);
                    posSpnBranch=i+1;
                }
            }
        }*/

        spnUserType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                posSpnUserType = i+1;
                Toast.makeText(AddUserActivity.this,""+posSpnUserType,Toast.LENGTH_SHORT).show();
                if(posSpnUserType>1){
                    spnBranchName.setEnabled(true);
                }

                else
                    spnBranchName.setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnBranchName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                posSpnBranch = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if (preferences.getInt(AttUtil.shpLoginType, 0) == 2)
            spnBranchName.setVisibility(View.GONE);
        spnBranchName.setEnabled(false);
        userBean = new UserBean();
    }

    public void getBranchNameMethod(){
        for (int i=0;i<branchBeanArrayList.size();i++){
            branchNameArrayList.add(branchBeanArrayList.get(i).getBranchName());
        }
        spnBranchName.setAdapter(new ArrayAdapter(AddUserActivity.this,android.R.layout.simple_dropdown_item_1line,
                branchNameArrayList));
        if(bool){
            for(int i = 0;i<branchBeanArrayList.size();i++) {
                if(branchBeanArrayList.get(i).getBranchId()==getUserBean.getBranchId()){
                    spnBranchName.setSelection(i+1);
                    posSpnBranch=i+1;
                }
            }
        }
    }


    public  void getBeanData(){
        Intent ircv = getIntent();
        if(ircv.hasExtra(AdminUtil.TAG_USER)){
            bool = true;
        }
        if(bool){
            getUserBean = (UserBean) ircv.getSerializableExtra(AdminUtil.TAG_USER);
            edManagerName.setText(getUserBean.getUserName().toString());
            edManagerEmail.setText(getUserBean.getUserEmail().toString());
            edManagerContact.setText(getUserBean.getUserContact().toString());
            edtUserSalary.setText(getUserBean.getUserSalary()+"");
           if(preferences.getInt(AttUtil.shpLoginType, 0) == 1){
               spnUserType.setSelection(getUserBean.getUserType());
               spnBranchName.setSelection(getUserBean.getBranchId());
           }else{
               spnUserType.setSelection(getUserBean.getUserType()-1);
           }
            posSpnUserType = getUserBean.getUserType();
            Toast.makeText(AddUserActivity.this,"user "+posSpnUserType,Toast.LENGTH_SHORT).show();
            txtUserName.setText("Edit "+getUserBean.getUserName()+"\'s Details");
            addManager.setText("Update Details");
        }
    }

    public boolean checkEmptyValues(){
        String msg = "Error:";
        flag = true;
        if(edManagerName.getText().toString().length() == 0 ) {
            msg = msg+"\nEmpty Name";
            flag = false;
        }
        if (preferences.getInt(AttUtil.shpLoginType, 0) == 1){
            Log.i("test","if");
            if(edManagerEmail.getText().toString().length() == 0 ) {
                if(posSpnUserType<4){
                    msg = msg+"\nEmpty E-mail Id";
                    flag = false;
                }
            }
        }else{
            Log.i("test","else");
            if(edManagerEmail.getText().toString().length() == 0 ) {
                if(posSpnUserType<3){
                    msg = msg+"\nEmpty E-mail Id";
                    flag = false;
                }
            }
        }
        if(edManagerContact.getText().toString().length() == 0 ) {
            msg = msg+"\nEmpty Contact Number";
            flag = false;
        }else if(edManagerContact.getText().toString().length()<9){
            msg = msg+"\nInvalid Contact Number";
            flag = false;
        }else if(edtUserSalary.getText().toString().length()<0){
            msg = msg+"\nInvalid Salary";
            flag = false;
        }else if(posSpnUserType==0){
            msg = msg+"\nSelect Type of User";
            flag = false;
        }
        if (preferences.getInt(AttUtil.shpLoginType, 0) == 1){
            if(posSpnUserType>1&&posSpnBranch==0){
                msg = msg+"\nSelect a Branch";
                flag = false;
            }
        }
        if(!flag){
            Toast.makeText(this,msg, Toast.LENGTH_LONG).show();
        }
        return flag;
    }

    public void setDataIntoBean(){
        if(!bool) {
            if (posSpnUserType != 3) {
                String[] nam1 = new String[3];
                String[] p1 = new String[3];
                userBean.setUserName(edManagerName.getText().toString().trim());
                userBean.setUserEmail(edManagerEmail.getText().toString().trim());
                userBean.setUserContact(edManagerContact.getText().toString().trim());
                if (edtUserSalary.getText().toString().trim().length() == 0)
                    userBean.setUserSalary(0);
                else
                    userBean.setUserSalary(Integer.parseInt(edtUserSalary.getText().toString().trim()));

                userBean.setUserStatus(1);
                if (userBean.getUserType() == 1) {
                    userBean.setBranchName("");
                    userBean.setBranchId(0);
                } else {
                    //After Manager
                    if (preferences.getInt(AttUtil.shpLoginType, 0) == 1) {
                        userBean.setUserType(posSpnUserType-1);
                        userBean.setBranchId(branchBeanArrayList.get(posSpnBranch - 1).getBranchId());
                        userBean.setBranchName(branchBeanArrayList.get(posSpnBranch - 1).getBranchName());
                    } else {
                        userBean.setUserType(posSpnUserType);
                        userBean.setBranchName(branchName);
                        userBean.setBranchId(branchId);
                    }
                }
                if (preferences.getInt(AttUtil.shpLoginType, 0) == 1) {
                    userBean.setAdminId(adminBean.getAdminId());
                } else {
                    userBean.setAdminId(userAdminId);
                }
                for (int i = 0; i < 3; i++) {
                    nam1[i] = String.valueOf(userBean.getUserName().charAt(i));
                }
                for (int i = 0; i < 3; i++) {
                    p1[i] = String.valueOf(userBean.getUserContact().charAt(i));
                }
                String[] password = ObjectArrays.concat(nam1, p1, String.class);
                String pass1 = "";
                for (int i = 0; i < password.length; i++) {
                    pass1 += "" + password[i];
                }
                userBean.setUserPassword(pass1);
                userBean.setUserId(userAddId + 1);
                userAddOrUpdate();
            }else{
                String[] nam1 = new String[3];
                String[] p1 = new String[3];
                teacherBean.setUserName(edManagerName.getText().toString().trim());
                teacherBean.setUserEmail(edManagerEmail.getText().toString().trim());
                teacherBean.setUserContact(edManagerContact.getText().toString().trim());

                if (edtUserSalary.getText().toString().trim().length() == 0)
                    teacherBean.setUserSalary(0);
                else
                    teacherBean.setUserSalary(Integer.parseInt(edtUserSalary.getText().toString().trim()));

                teacherBean.setUserStatus(1);

                if (preferences.getInt(AttUtil.shpLoginType, 0) == 1) {
                    teacherBean.setUserType(posSpnUserType-1);
                    teacherBean.setBranchId(branchBeanArrayList.get(posSpnBranch - 1).getBranchId());
                    teacherBean.setBranchName(branchBeanArrayList.get(posSpnBranch - 1).getBranchName());
                } else {
                    // userBean.setBranchId();
                    teacherBean.setUserType(posSpnUserType);
                    teacherBean.setBranchName(branchName);
                    teacherBean.setBranchId(branchId);
                }

                if (preferences.getInt(AttUtil.shpLoginType, 0) == 1) {
                    teacherBean.setAdminId(adminBean.getAdminId());
                } else {
                    teacherBean.setAdminId(userAdminId);
                }

                for (int i = 0; i < 3; i++) {
                    nam1[i] = String.valueOf(teacherBean.getUserName().charAt(i));
                }

                for (int i = 0; i < 3; i++) {
                    p1[i] = String.valueOf(teacherBean.getUserContact().charAt(i));
                }

                String[] password = ObjectArrays.concat(nam1, p1, String.class);
                String pass1 = "";
                for (int i = 0; i < password.length; i++) {
                    pass1 += "" + password[i];
                }
                teacherBean.setUserPassword(pass1);
                teacherBean.setUserId(userAddId + 1);
                teacherBean.setBatchGroup(batchGroup);
                teacherBean.setBranCourId(branCourId);
                addTeacher();
            }
        }
        else{
            userBean.setUserName(edManagerName.getText().toString().trim());
            userBean.setUserEmail(edManagerEmail.getText().toString().trim());
            userBean.setUserContact(edManagerContact.getText().toString().trim());

            if(edtUserSalary.getText().toString().trim().length()==0)
                userBean.setUserSalary(0);
            else
                userBean.setUserSalary(Integer.parseInt(edtUserSalary.getText().toString().trim()));

            if (preferences.getInt(AttUtil.shpLoginType, 0) == 1) {
                userBean.setUserType(posSpnUserType-1);
                if(userBean.getUserType()==1){
                    userBean.setBranchName("");
                    userBean.setBranchId(0);
                }else{
                    userBean.setBranchName(branchBeanArrayList.get(posSpnBranch - 1).getBranchName());
                    userBean.setBranchId(branchBeanArrayList.get(posSpnBranch - 1).getBranchId());
                }
            }else{
                userBean.setUserType(posSpnUserType);
                userBean.setBranchId(getUserBean.getBranchId());
                userBean.setBranchName(getUserBean.getBranchName());
            }

            userBean.setAdminId(getUserBean.getAdminId());
            userBean.setUserId(getUserBean.getUserId());
            userBean.setUserPassword(getUserBean.getUserPassword());
            userBean.setUserStatus(getUserBean.getUserStatus());
            userAddOrUpdate();
        }

    }

    public void sendBack(){
        Intent intent=new Intent();
        intent.putExtra(AdminUtil.TAG_USER,userBean);
        setResult(AdminUtil.RES_CODE,intent);
        finish();
    }


}
