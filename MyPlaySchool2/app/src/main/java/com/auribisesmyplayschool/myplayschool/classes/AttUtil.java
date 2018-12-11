package com.auribisesmyplayschool.myplayschool.classes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AttUtil {
    public static String shpREG = "REG";
    public static String spLoginStatus = "login_status";
    public static String shpLoginStatus = "login_status";
    public static String spREG = "REG";
    public static final String shpLoginType = "loginType";
    public static ProgressDialog pd;
    public static String error = "Field cannot be empty";
    public static String KEY_BATCH = "batchBean";
    public static String BATCH_LIST = "batch_List";
    public static final String TAG_INVOICEBEAN = "invoicebean";

    public static final int ATT_REQ = 101;
    public static final int ATT_RES = 102;

    public static String shpEmailId = "EmailId";
    public static String shpUserId = "userId";
    public static String shpUserName = "username";
    public static String shpUserContact = "usercontact";
    public static String shpUserPassword = "UserPassword";
    public static String shpUserType = "userType";
    public static String shpUserStatus = "userStatus";
    public static String shpBranchId = "branchid";
    public static String shpBranchName = "branchname";
    public static String shpBranchAddress = "branchAddress";
    public static String shpBranchContact = "branchContact";
    public static final String shpSignInBean = "signInBean";
    public static String shpCounsellorBean = "shpCounsellorBean";
    public static String signInBean = "signInBean";
    public static String KEY_COURSE_ARRAYLIST = "courseBeanArrayList";
    public static String shpDefaultEnquiryFollowUp = "shpDefaultEnquiryFollowUp";
    public static String KEY_COUNSELLOR_ARRAYLIST = "counsellorBeanArrayList";
    public static String KEY_STUDENT_LATE_JOINING_ARRAYLIST = "studentLateJoiningList";
    public static String KEY_BRANCH_ARRAYLIST = "branchBeanArrayList";
    public static final String branchId = "branchid";
    public static final String branCourId = "branCourId";
    public static final String reference = "reference";
    public static final String studentId = "studentId";
    public static final String TAG_STUDENTBEAN = "student";
    public static final String TAG_ADMISSIONBEAN = "admission";
    public static int REQ_CODE = 1;
    public static int RES_CODE = 2;
    public static int laterJoinings = 0;
    public static String KEY_BATCH_ID = "batchId";
    public static String KEY_BATCH_NAME = "batchName";
    public static String UPDATED_BATCH = "updatedBatch";
    public static String CHECK = "check";
    public static String TEACHERBEAN = "teacherBean";

    public static boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cmgr;
        cmgr = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cmgr.getActiveNetworkInfo();
        if (info != null) {
            if (info.isConnected()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }



    public static void pd(int flag) {
        if (flag == 1) {
            pd.show();
        } else if (flag == 0) {
            pd.dismiss();
        }
    }

    public static String getFormattedDate(String dateRec) {
        String newDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfn = new SimpleDateFormat("d MMM, yyyy", Locale.ENGLISH);

        Date date;
        try {
            date = sdf.parse(dateRec);
            newDate = sdfn.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    public static String getFormattedTime(String timeRec) {
        String newTime = "";
        SimpleDateFormat stf = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat stfn = new SimpleDateFormat("hh:mm aa");

        Date time;
        try {
            time = stf.parse(timeRec);
            newTime = stfn.format(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newTime;
    }

    public static void progressDialog(final Activity activity) {

        pd = new ProgressDialog(activity);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
    }




}
