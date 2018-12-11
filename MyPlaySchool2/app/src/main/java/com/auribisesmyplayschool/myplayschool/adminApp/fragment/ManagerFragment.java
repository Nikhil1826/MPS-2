package com.auribisesmyplayschool.myplayschool.adminApp.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adminApp.activity.AddUserActivity;
import com.auribisesmyplayschool.myplayschool.adminApp.activity.ManageBranchManagerActivity;
import com.auribisesmyplayschool.myplayschool.adminApp.activity.ViewAssignedBranchesActivity;
import com.auribisesmyplayschool.myplayschool.adminApp.adapter.UserListAdapter;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.UserBean;
import com.auribisesmyplayschool.myplayschool.adminApp.classes.AdminUtil;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import dmax.dialog.SpotsDialog;

public class ManagerFragment extends Fragment {
    private UserBean userBean;
    private ListView listView;
    public UserListAdapter userListAdapter;
    int pos;
    private  String uStatus;
    FirebaseFirestore db;


    public ManagerFragment() {

    }

    public void initlist(){
         userListAdapter = new UserListAdapter(getActivity()
                    ,R.layout.admin_adapter_user_listitem, ManageBranchManagerActivity.managerBeanArrayList);
            listView.setAdapter(userListAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        View view = inflater.inflate(R.layout.admin_fragment_manager, container, false);
        listView = (ListView)view.findViewById(R.id.lvFragmentManager);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //userId = branchManagerList.get(position).getUserId();
                userBean = ManageBranchManagerActivity.managerBeanArrayList.get(position);
                if(userBean.getUserStatus() == 1){
                    uStatus = "Deactivate";
                } else {
                    uStatus = "Activate";
                }
                show_options();
            }
        });
        return view;
    }

    public void show_options(){
        AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
        String[] options = {"View Assigned Branches","Update Details",uStatus,"Resend Credentials"};

        build.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case 0:
                        startActivity(new Intent(getActivity(),ViewAssignedBranchesActivity.class)
                                .putExtra(AdminUtil.userId,userBean.getUserId()));
                        break;
                    case 1:
                        Intent passBeanData = new Intent(getActivity(),AddUserActivity.class);
                        passBeanData.putExtra(AdminUtil.TAG_USER,userBean);
                        passBeanData.putExtra(AdminUtil.TAG_BRANCHARRAYLIST,ManageBranchManagerActivity.branchBeanArrayList);
                        passBeanData.putExtra(AdminUtil.TAG_ADMIN_BEAN,ManageBranchManagerActivity.adminBean);
                        startActivityForResult(passBeanData,AdminUtil.REQ_CODE);
                        break;
                    case 2:
                        if (AdminUtil.isNetworkConnected(getActivity())){
                            AttUtil.progressDialog((Activity) getContext());
                            AttUtil.pd(1);
                            start_deactivate();
                        }
                        else
                            //activateNetConnect();
                        break;
                   /* case 3:
                        if (AdminUtil.isNetworkConnected(getActivity()))
                            resendCredentials();
                        else
                            resendNetConnect();
                        break;*/
                }
            }
        });

        build.create().show();
    }

    void start_deactivate() {
        if(userBean.getUserStatus()==1){
            userBean.setUserStatus(0);
        }else{
            userBean.setUserStatus(1);
        }
        db.collection(Constants.usersCollection).document(String.valueOf(userBean.getUserId()))
                .update("userStatus",userBean.getUserStatus()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AttUtil.pd(0);
                userListAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(),"User "+userBean.getUserName()+" status changed.",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void updateListFunction(){
        if(ManageBranchManagerActivity.managerBeanArrayList.size()>1&&ManageBranchManagerActivity.managerBeanArrayList!=null){
            userListAdapter.notifyDataSetChanged();
        }else{
            userListAdapter = new UserListAdapter(getActivity()
                    , R.layout.admin_adapter_user_listitem,
                    ManageBranchManagerActivity.managerBeanArrayList);
            listView.setAdapter(userListAdapter);
        }
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Managers"
                +" ("+ManageBranchManagerActivity.managerBeanArrayList.size()+")");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == AdminUtil.REQ_CODE)&&(resultCode == AdminUtil.RES_CODE)){
            userBean  = (UserBean) data.getSerializableExtra(AdminUtil.TAG_USER);
            ManageBranchManagerActivity.managerBeanArrayList.set(pos,userBean);
            ((AppCompatActivity)getActivity()).getSupportActionBar()
                    .setTitle("Managers" +" ("+ManageBranchManagerActivity.managerBeanArrayList.size()+")");
            this.userListAdapter.notifyDataSetChanged();
        }

    }
}
