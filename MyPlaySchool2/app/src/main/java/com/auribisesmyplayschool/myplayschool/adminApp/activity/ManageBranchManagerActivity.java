package com.auribisesmyplayschool.myplayschool.adminApp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BranchBean;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.UserBean;
import com.auribisesmyplayschool.myplayschool.adminApp.classes.AdminUtil;
import com.auribisesmyplayschool.myplayschool.adminApp.fragment.CounsellorFragment;
import com.auribisesmyplayschool.myplayschool.adminApp.fragment.DriverFragment;
import com.auribisesmyplayschool.myplayschool.adminApp.fragment.InstructorFragment;
import com.auribisesmyplayschool.myplayschool.adminApp.fragment.MaidFragment;
import com.auribisesmyplayschool.myplayschool.adminApp.fragment.ManagerFragment;
import com.auribisesmyplayschool.myplayschool.bean.AdminBean;
import com.auribisesmyplayschool.myplayschool.bean.SignInBean;
import com.auribisesmyplayschool.myplayschool.bean.TeacherBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

import javax.annotation.Nullable;

import dmax.dialog.SpotsDialog;

public class ManageBranchManagerActivity extends AppCompatActivity implements View.OnClickListener {
    public static ArrayList<UserBean> managerBeanArrayList, instrucBeanArrayList, counsellorBeanArrayList, driverBeanArrayList, maidBeanArrayList;
    public static ArrayList<UserBean> userBeanArrayList;
    private UserBean userBean;
    private SharedPreferences preferences;
    public static AdminBean adminBean;
    ArrayList<String> branchNameArrayList;
    public static ArrayList<BranchBean> branchBeanArrayList;
    Intent rcv;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager mViewPager;
    private CounsellorFragment counsellorFragment;
    private ManagerFragment managerFragment;
    private InstructorFragment instructorFragment;
    private DriverFragment driverFragment;
    private MaidFragment maidFragment;
    FirebaseFirestore db;
    int userAdminId,userId;
    SignInBean signInBean;
    String branchName;
    int branchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_branch_manager);
        rcv = getIntent();

        //branchNameArrayList = (ArrayList<String>) rcv.getSerializableExtra(AdminUtil.TAG_BRANCHARRAYLIST);

       // Toast.makeText(ManageBranchManagerActivity.this,""+branchBeanArrayList.size(),Toast.LENGTH_SHORT).show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = FirebaseFirestore.getInstance();
        preferences = getSharedPreferences(AttUtil.shpREG, Context.MODE_PRIVATE);
        counsellorFragment = new CounsellorFragment();
        instructorFragment = new InstructorFragment();
        driverFragment = new DriverFragment();
        maidFragment = new MaidFragment();
        initViews();
        if (preferences.getInt(AttUtil.shpLoginType, 0) == 1){
            managerFragment = new ManagerFragment();
            adminBean = (AdminBean) rcv.getSerializableExtra(AdminUtil.TAG_ADMIN_BEAN);
            branchBeanArrayList = (ArrayList<BranchBean>) rcv.getSerializableExtra(AdminUtil.TAG_BRANCHARRAYLIST);
            AttUtil.progressDialog(this);
            AttUtil.pd(1);
            getUsers();
        }else{
            signInBean = (SignInBean) rcv.getSerializableExtra(AttUtil.signInBean);
            userAdminId = rcv.getIntExtra("adminId",0);
            userId = rcv.getIntExtra("userId",0);
            branchName = rcv.getStringExtra("branchName");
            branchId = rcv.getIntExtra("branchId",0);
            AttUtil.progressDialog(this);
            AttUtil.pd(1);
            getManagerUsers();
        }

    }


    void getManagerUsers() {
        db.collection(Constants.usersCollection).whereEqualTo("adminId",userAdminId)
                .whereEqualTo("branchName",signInBean.getBranchName()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        userBean = doc.toObject(UserBean.class);
                        userBeanArrayList.add(userBean);
                }

                if (userBeanArrayList.size() > 0 && userBeanArrayList != null){
                    for (int i = 0; i < userBeanArrayList.size(); i++) {
                        if (preferences.getInt(AttUtil.shpLoginType, 0) == 1) {
                            if (userBeanArrayList.get(i).getUserType() == 1)
                                managerBeanArrayList.add(userBeanArrayList.get(i));
                        }
                        if (userBeanArrayList.get(i).getUserType() == 2)
                            counsellorBeanArrayList.add(userBeanArrayList.get(i));
                        if (userBeanArrayList.get(i).getUserType() == 3)
                            instrucBeanArrayList.add(userBeanArrayList.get(i));
                        if (userBeanArrayList.get(i).getUserType() == 4)
                            driverBeanArrayList.add(userBeanArrayList.get(i));
                        if (userBeanArrayList.get(i).getUserType() == 5)
                            maidBeanArrayList.add(userBeanArrayList.get(i));
                    }
                    if (preferences.getInt(AttUtil.shpLoginType, 0) == 1) {
                        if (managerBeanArrayList.size() > 0)
                            managerFragment.initlist();
                    }
                    if (counsellorBeanArrayList.size() > 0)
                        counsellorFragment.initlist();
                    if (instrucBeanArrayList.size() > 0)
                        instructorFragment.initlist();
                    if (driverBeanArrayList.size() > 0)
                        driverFragment.initlist();
                    if (maidBeanArrayList.size() > 0)
                        maidFragment.initlist();

                    getSupportActionBar().setTitle("Counsellor" + " (" + counsellorBeanArrayList.size() + ")");
                }else {
                    getSupportActionBar().setTitle("Employee(0)");
                    // Toast.makeText(ManageBranchManagerActivity.this, "No Employee(s)  found", Toast.LENGTH_LONG).show();
                }
                AttUtil.pd(0);
            }
        });
    }

    public void initViews(){
        managerBeanArrayList = new ArrayList<>();
        instrucBeanArrayList = new ArrayList<>();
        counsellorBeanArrayList = new ArrayList<>();
        driverBeanArrayList = new ArrayList<>();
        maidBeanArrayList = new ArrayList<>();

        userBeanArrayList = new ArrayList<>();
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        if (preferences.getInt(AttUtil.shpLoginType, 0) == 1)
            mViewPager.setOffscreenPageLimit(4);
        else
            mViewPager.setOffscreenPageLimit(3);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (preferences.getInt(AttUtil.shpLoginType, 0) == 1) {
                    if (position == 0)
                        getSupportActionBar().setTitle("Managers(" + managerBeanArrayList.size() + ")");
                    if (position == 1)
                        getSupportActionBar().setTitle("Counsellors(" + counsellorBeanArrayList.size() + ")");
                    if (position == 2)
                        getSupportActionBar().setTitle("Instructors(" + instrucBeanArrayList.size() + ")");
                    if (position == 3)
                        getSupportActionBar().setTitle("Drivers(" + driverBeanArrayList.size() + ")");
                    if (position == 4)
                        getSupportActionBar().setTitle("Maids(" + maidBeanArrayList.size() + ")");
                } else {
                    if (position == 0)
                        getSupportActionBar().setTitle("Counsellors(" + counsellorBeanArrayList.size() + ")");
                    if (position == 1)
                        getSupportActionBar().setTitle("Instructors(" + instrucBeanArrayList.size() + ")");
                    if (position == 2)
                        getSupportActionBar().setTitle("Drivers(" + driverBeanArrayList.size() + ")");
                    if (position == 3)
                        getSupportActionBar().setTitle("Maids(" + maidBeanArrayList.size() + ")");

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void getUsers(){
        db.collection(Constants.usersCollection).whereEqualTo("adminId",adminBean.getAdminId()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                userBean = doc.toObject(UserBean.class);
                                userBeanArrayList.add(userBean);
                            }

                            if (userBeanArrayList.size() > 0 && userBeanArrayList != null){
                                for (int i = 0; i < userBeanArrayList.size(); i++) {
                                    if (preferences.getInt(AttUtil.shpLoginType, 0) == 1) {
                                        if (userBeanArrayList.get(i).getUserType() == 1)
                                            managerBeanArrayList.add(userBeanArrayList.get(i));
                                    }

                                    if (userBeanArrayList.get(i).getUserType() == 2)
                                        counsellorBeanArrayList.add(userBeanArrayList.get(i));
                                    if (userBeanArrayList.get(i).getUserType() == 3)
                                        instrucBeanArrayList.add(userBeanArrayList.get(i));
                                    if (userBeanArrayList.get(i).getUserType() == 4)
                                        driverBeanArrayList.add(userBeanArrayList.get(i));
                                    if (userBeanArrayList.get(i).getUserType() == 5)
                                        maidBeanArrayList.add(userBeanArrayList.get(i));
                                }
                                if (preferences.getInt(AttUtil.shpLoginType, 0) == 1) {
                                    if (managerBeanArrayList.size() > 0)
                                        managerFragment.initlist();
                                }
                                if (counsellorBeanArrayList.size() > 0)
                                    counsellorFragment.initlist();
                                if (instrucBeanArrayList.size() > 0)
                                    instructorFragment.initlist();
                                if (driverBeanArrayList.size() > 0)
                                    driverFragment.initlist();
                                if (maidBeanArrayList.size() > 0)
                                    maidFragment.initlist();

                                getSupportActionBar().setTitle("Managers" + " (" + managerBeanArrayList.size() + ")");
                            }else {
                                getSupportActionBar().setTitle("Employee(0)");
                                Toast.makeText(ManageBranchManagerActivity.this, "No Employee(s)  found", Toast.LENGTH_LONG).show();
                            }
                            AttUtil.pd(0);

                    }
                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == AdminUtil.REQ_CODE) && (resultCode == 1)) {
            userBean = (UserBean) data.getSerializableExtra(AdminUtil.TAG_USER);
            setResult(225,new Intent().putExtra("userBean",userBean));
            if (userBeanArrayList.size() > 0 && userBeanArrayList != null) {
                if (userBean.getUserType() == 1) {
                    managerBeanArrayList.add(userBean);
                    userId = userBean.getUserId();
                    managerFragment.updateListFunction();
                }
                if (userBean.getUserType() == 2) {
                    counsellorBeanArrayList.add(userBean);
                    counsellorFragment.updateListFunction();
                }
                if (userBean.getUserType() == 3) {
                    instrucBeanArrayList.add(userBean);
                    instructorFragment.updateListFunction();
                }
                if (userBean.getUserType() == 4) {
                    driverBeanArrayList.add(userBean);
                    driverFragment.updateListFunction();
                }
                if (userBean.getUserType() == 5) {
                    maidBeanArrayList.add(userBean);
                    maidFragment.updateListFunction();
                }
            } else {
                if (userBean.getUserType() == 1) {
                    managerBeanArrayList.add(userBean);
                    managerFragment.updateListFunction();
                }
                if (userBean.getUserType() == 2) {
                    counsellorBeanArrayList.add(userBean);
                    counsellorFragment.updateListFunction();
                }
                if (userBean.getUserType() == 3) {
                    instrucBeanArrayList.add(userBean);
                   // instructorFragment.updateListFunction();
                }
                if (userBean.getUserType() == 4) {
                    driverBeanArrayList.add(userBean);
                    driverFragment.updateListFunction();
                }
                if (userBean.getUserType() == 5) {
                    maidBeanArrayList.add(userBean);
                    maidFragment.updateListFunction();
                }
            }
        }

        if(requestCode == AdminUtil.REQ_CODE && resultCode == 370){
            TeacherBean teacherBean = (TeacherBean) data.getSerializableExtra("teacherBean");
            UserBean userBean = new UserBean();
            userBean.setUserName(teacherBean.getUserName());
            userBean.setUserId(teacherBean.getUserId());
            userBean.setBranchId(teacherBean.getBranchId());
            userBean.setBranchName(teacherBean.getBranchName());
            userBean.setBusId(0);
            userBean.setUserStatus(teacherBean.getUserStatus());
            userBean.setUserPassword(teacherBean.getUserPassword());
            userBean.setUserContact(teacherBean.getUserContact());
            userBean.setUserEmail(teacherBean.getUserEmail());
            userBean.setAdminId(teacherBean.getAdminId());
            userBean.setUserSalary(teacherBean.getUserSalary());
            userBean.setUserType(teacherBean.getUserType());
            instrucBeanArrayList.add(userBean);
            instructorFragment.updateListFunction();
            setResult(225,new Intent().putExtra("userBean",userBean));
        }
        FloatingActionMenu menu = findViewById(R.id.branchManagerFabMenu);
        if (menu.isOpened()) {
            menu.close(true);
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab1:
                Intent intent = new Intent(ManageBranchManagerActivity.this, AddUserActivity.class);
                if (preferences.getInt(AttUtil.shpLoginType, 0) == 1) {
                    intent.putExtra(AdminUtil.TAG_ADMIN_BEAN, adminBean);
                    intent.putExtra(AdminUtil.TAG_BRANCHARRAYLIST,branchBeanArrayList);
                }else {
                    intent.putExtra("adminId", userAdminId);
                    intent.putExtra("userId", userId);
                    intent.putExtra("branchName", branchName);
                    intent.putExtra("branchId",branchId);
                    //intent.putExtra(AttUtil.KEY_BRANCH_ARRAYLIST,branchBeanArrayList);
                }
                //intent.putExtra("lastUserId",userIdAdd);


                startActivityForResult(intent, AdminUtil.REQ_CODE);
                break;

            default:
                break;
        }
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = null;
            if (preferences.getInt(AttUtil.shpLoginType, 0) == 1){
                switch (position) {
                    case 0:
                        f = managerFragment;
                        break;
                    case 1:
                        f = counsellorFragment;
                        break;
                    case 2:
                        f = instructorFragment;
                        break;
                    case 3:
                        f = driverFragment;
                        break;
                    case 4:
                        f = maidFragment;
                        break;
                }
            }else{
                switch (position) {
                    case 0:
                        f = counsellorFragment;
                        break;
                    case 1:
                        f = instructorFragment;
                        break;
                    case 2:
                        f = driverFragment;
                        break;
                    case 3:
                        f = maidFragment;
                        break;

                }
            }
            return f;
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            if (preferences.getInt(AttUtil.shpLoginType, 0) == 1)
                return 5;
            else
                return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (preferences.getInt(AttUtil.shpLoginType, 0) == 1) {
                switch (position) {
                    case 0:
                        return "Managers";
                    case 1:
                        return "Counsellors";
                    case 2:
                        return "Instructors";
                    case 3:
                        return "Drivers";
                    case 4:
                        return "Maids";
                }
            } else {
                switch (position) {
                    case 0:
                        return "Counsellors";
                    case 1:
                        return "Instructors";
                    case 2:
                        return "Drivers";
                    case 3:
                        return "Maids";
                }
            }
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }


}
