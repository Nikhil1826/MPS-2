package com.auribisesmyplayschool.myplayschool.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adminApp.activity.AdminMainActivity;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BranchBean;
import com.auribisesmyplayschool.myplayschool.adminApp.classes.AdminUtil;
import com.auribisesmyplayschool.myplayschool.bean.AdminBean;
import com.auribisesmyplayschool.myplayschool.bean.SignInBean;
import com.auribisesmyplayschool.myplayschool.bean.TeacherBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText username, password;
    CheckBox chkShowPass;
    Button login;
    Spinner spinnerUser;
    SignInBean s_in_bean;
    int selectedUser,userType,userStatus;
    ArrayAdapter adapter;
    ArrayList<String> userList = new ArrayList<>();
    ArrayList<SignInBean> signInBeanArrayList = new ArrayList<>();
    FirebaseFirestore db;
    AdminBean adminBean;
    String email[];
    String pass[];
    String pass1;
    String email1,userContact,userName;
    int userAdminId,userId;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    int login_status;
    String[] options;
    int[] branchId;
    BranchBean branchBean;
    ArrayList<BranchBean> branchBeanArrayList;
    TeacherBean teacherBean;
    ArrayList<TeacherBean> teacherBeanArrayList;

   void initViews(){
       AttUtil.progressDialog(this);
       spinnerUser = (Spinner) findViewById(R.id.spinnerUserType);
       userList.add("--Select a user type to continue--");
       userList.add("Admin");
       userList.add("Manager");
       userList.add("Teacher");
       userList.add("Counsellor");
       adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, userList);
       spinnerUser.setAdapter(adapter);
       spinnerUser.setOnItemSelectedListener(this);
       s_in_bean = new SignInBean();
       username =  findViewById(R.id.etxt_username);
       password =  findViewById(R.id.etxt_password);
       chkShowPass =  findViewById(R.id.cbShowPwd);
       login =  findViewById(R.id.btn_login);
       login.setOnClickListener(onClickListener);
       pref = getSharedPreferences(AttUtil.shpREG, MODE_PRIVATE);
       login_status = pref.getInt(AttUtil.spLoginStatus, 0);
       editor = pref.edit();

       branchBean=new BranchBean();
       branchBeanArrayList=new ArrayList<>();
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = getSharedPreferences(AttUtil.spREG, MODE_PRIVATE);
        editor = pref.edit();
        getSupportActionBar().hide();
        initViews();
        db = FirebaseFirestore.getInstance();
        chkShowPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (!isChecked) {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

    }

    void show_options() {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("Choose a branch");
        build.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                s_in_bean = signInBeanArrayList.get(which);
                editor.putInt(AttUtil.shpLoginStatus, 1);
                editor.putInt(AttUtil.shpLoginType, 2);
                editor.putString(AttUtil.shpEmailId, s_in_bean.getUserEmail());
                editor.putInt(AttUtil.shpUserId, s_in_bean.getUserId());
                editor.putString(AttUtil.shpUserName, s_in_bean.getUserName());
                editor.putString(AttUtil.shpUserContact, s_in_bean.getUserContact());
                editor.putString(AttUtil.shpUserPassword, s_in_bean.getUserPassword());
                editor.putString(AttUtil.shpUserType, String.valueOf(s_in_bean.getUserType()));
                editor.putInt(AttUtil.shpUserStatus, s_in_bean.getUserStatus());
                editor.putInt(AttUtil.shpBranchId, s_in_bean.getBranchId());
                editor.putString(AttUtil.shpBranchName, s_in_bean.getBranchName());
                editor.putString(AttUtil.shpBranchAddress, s_in_bean.getBranchAddress());
                editor.putString(AttUtil.shpBranchContact, s_in_bean.getBranchContact());
                editor.commit();
//                Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_LONG).show();
                if (pref.getInt(AttUtil.shpUserStatus, 0) == 1) {

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.putExtra(AttUtil.signInBean,s_in_bean);
                    i.putExtra("adminId",userAdminId);
                    i.putExtra("userId",userId);
                    i.putExtra("branchName",s_in_bean.getBranchName());
                    i.putExtra("branchId",s_in_bean.getBranchId());
                    i.putExtra("branchBeanArrayList",branchBeanArrayList);
                    startActivity(i);
                    finish();
                } else {
                    //showContactMsg();
                    Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
            }
        });
        build.setCancelable(false);
        build.create().show();
    }

    boolean validateField() {

        boolean check = true;
        if ( username.getText().toString().isEmpty()) {       //s_in_bean.getUserEmail().isEmpty()
            check = false;
            username.setError("Email cannot be left blank");
        }
        /*if (!username.getText().toString().isEmpty()) {       //s_in_bean.getUserEmail().isEmpty()
            if (username.getText().toString().matches("[a-zA-Z0-9_.-]+@[a-z]+\\.+[a-z]+")) {
                check = false;
                username.setError("Invalid Email ID");
            }
        }*/

        if (password.getText().toString().isEmpty()) {      //s_in_bean.getUserPassword().isEmpty()
            check = false;
            password.setError("Password cannot be left blank");
        }

        if (!(password.getText().toString().isEmpty())) {      //s_in_bean.getUserPassword().isEmpty()
            if (password.length() < 4) {
                check = false;
                password.setError("Password length should be four or above");
            }
        }
        if (selectedUser == 0) {
            check = false;
            Toast.makeText(this, "Please Select User Type to Continue", Toast.LENGTH_LONG).show();
        }
        return check;
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
           // s_in_bean.setUserEmail(username.getText().toString().trim());
           // s_in_bean.setUserPassword(password.getText().toString().trim());

            if (validateField()) {
                //if (AttUtil.isNetworkConnected(LoginActivity.this))
                AttUtil.pd(1);
                    loginFunction();
                //else
                    //loginNetConnect();
            }

        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            selectedUser = position;
        } else {
            selectedUser = 0;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    void loginFunction(){

        if (selectedUser == 1){

            db.collection(Constants.adminCollection).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    email = new String[queryDocumentSnapshots.size()];
                    pass = new String[queryDocumentSnapshots.size()];

                    for(int i=0;i<queryDocumentSnapshots.size();i++){
                         email[i] = queryDocumentSnapshots.getDocuments().get(i).getString("adminEmail");
                         pass[i] = queryDocumentSnapshots.getDocuments().get(i).getString("adminPassword");
                    }

                    for (int i=0;i<queryDocumentSnapshots.size();i++){
                        if(username.getText().toString().equals(email[i]) && password.getText().toString().equals(pass[i])){
                            adminBean = queryDocumentSnapshots.getDocuments().get(i).toObject(AdminBean.class);
                            afterAdminLogin();

                        }
                    }
                    AttUtil.pd(0);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });

        }else if(selectedUser == 2){
             email1 = username.getText().toString().trim();
             pass1 = password.getText().toString().trim();

            db.collection(Constants.usersCollection).whereEqualTo("userEmail",email1)
                    .whereEqualTo("userPassword",pass1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                       userStatus = doc.getLong("userStatus").intValue();
                        userType = doc.getLong("userType").intValue();
                        userContact =  doc.getString("userContact");
                        userName = doc.getString("userName");
                        userAdminId = doc.getLong("adminId").intValue();
                        userId = doc.getLong("userId").intValue();
                       // s_in_bean.setUserId(doc.getLong("userId").intValue());
                       // s_in_bean.setUserName(doc.getString("userName"));
                       // s_in_bean.setUserEmail(email1);

                    }
                    getAssignedBranches();
                }
            });
        }else if(selectedUser == 3){
            teacherBean = new TeacherBean();
            teacherBeanArrayList = new ArrayList<>();
            email1 = username.getText().toString().trim();
            pass1 = password.getText().toString().trim();
            db.collection(Constants.usersCollection).whereEqualTo("userType",3).whereEqualTo("userEmail",email1)
                    .whereEqualTo("userPassword",pass1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                        teacherBean = doc.toObject(TeacherBean.class);
                        teacherBeanArrayList.add(teacherBean);
                    }

                    editor.putInt(AttUtil.shpLoginStatus, 1);
                    editor.putInt(AttUtil.shpLoginType, 3);
                    //editor.putInt(AttUtil.shpBranchId, teacherList.get(0).getBranchId());
                    //editor.putInt(AttUtil.shpUserId, teacherList.get(0).getUserId());
                    editor.commit();

                    if(teacherBeanArrayList.size()>0){
                        AttUtil.pd(0);
                        startActivity(new Intent(LoginActivity.this, TeacherHomeActivity.class)
                                .putExtra("teacher", 1).putExtra(AttUtil.TEACHERBEAN,teacherBean));
                    }else{
                        AttUtil.pd(0);
                        Toast.makeText(LoginActivity.this,"No Teacher found",Toast.LENGTH_SHORT).show();
                    }



                }
            });
        }

    }

    private void getAssignedBranches() {
        db.collection(Constants.branchCollection)
                .whereEqualTo("userId",userId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                signInBeanArrayList.clear();
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    /*s_in_bean.setBranchName(doc.getString("branchName"));
                    s_in_bean.setBranchId(doc.getLong("branchId").intValue());
                    s_in_bean.setBranchAddress(doc.getString("branchAddress"));*/
                    branchBean=doc.getDocument().toObject(BranchBean.class);
                    branchBeanArrayList.add(branchBean);
                    s_in_bean = doc.getDocument().toObject(SignInBean.class);
                    s_in_bean.setUserPassword(pass1);
                    s_in_bean.setUserContact(userContact);
                    s_in_bean.setUserType(userType);
                    s_in_bean.setUserStatus(userStatus);
                    s_in_bean.setUserEmail(email1);
                    s_in_bean.setUserName(userName);
                    signInBeanArrayList.add(s_in_bean);
                }
                int z = -1, optionsSize = 0;
                if (signInBeanArrayList.size() > 0 && signInBeanArrayList != null) {
                    for (int i = 0; i < signInBeanArrayList.size(); i++) {
                            optionsSize = optionsSize + 1;
                    }
                    //options = new String[signInBeanArrayList.size()];
                    options = new String[optionsSize];
                    for (int i = 0; i < signInBeanArrayList.size(); i++) {
                            z = z + 1;
                            options[z] = signInBeanArrayList.get(i).getBranchName();
                    }
                    if (options.length != 0) {
                        show_options();
                    } else {
                        Toast.makeText(LoginActivity.this, "No Branches Found  ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "No Branches Found", Toast.LENGTH_SHORT).show();
                }
                AttUtil.pd(0);
            }
        });
    }

    void afterAdminLogin() {
        editor.putInt(AttUtil.shpLoginStatus, 1);
        editor.putInt(AttUtil.shpLoginType, 1);
        editor.commit();
        finish();
        Intent intent = new Intent(LoginActivity.this,AdminMainActivity.class);
        intent.putExtra(AdminUtil.TAG_ADMIN_BEAN,adminBean);
        startActivity(intent);

    }


}
