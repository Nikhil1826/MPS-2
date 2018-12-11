package com.auribisesmyplayschool.myplayschool.adminApp.classes;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AdminUtil {
    public static String TAG_COURSE = "course";
    public static String TAG_ADMIN_BEAN = "admin_details";
    public static String TAG_BRANCH_BEAN = "branch_details";
    public static String TAG_BRANCHARRAYLIST = "branchArray";
    public static String branchId="branchId";
    public static String TAG_BRANCH = "branch";
    public static String TAG_BRANCOUR = "brancourse";
    public static final String Error_message= "Error while loading data. Please try again later.";
    public static String TAG_USER = "user";
    public static String userId="userId";
    public static int REQ_CODE = 1;
    public static int RES_CODE = 2;


    public static boolean isNetworkConnected(Activity activity)
    {
        ConnectivityManager cmgr;
        cmgr= (ConnectivityManager)activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=cmgr.getActiveNetworkInfo();
        if(info!=null){
            if(info.isConnected()){
                return true;
            }else{
                return false;
            }
        }else {
            return false;
        }
    }

}
