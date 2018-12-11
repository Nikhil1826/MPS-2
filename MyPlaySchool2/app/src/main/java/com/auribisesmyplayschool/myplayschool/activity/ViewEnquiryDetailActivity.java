package com.auribisesmyplayschool.myplayschool.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.UserBean;
import com.auribisesmyplayschool.myplayschool.bean.EnquiryBean;
import com.auribisesmyplayschool.myplayschool.bean.StuEduBean;
import com.auribisesmyplayschool.myplayschool.bean.StuExpBean;
import com.auribisesmyplayschool.myplayschool.bean.StudentBean;
import com.auribisesmyplayschool.myplayschool.bean.UserVisitBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

import static com.auribisesmyplayschool.myplayschool.classes.AttUtil.getFormattedDate;

public class ViewEnquiryDetailActivity extends AppCompatActivity {

    private TextView tvStuPersDetails,tvStuEnqDetails,tvStuEnqVisit,
            tvRegisterStu,tvUpdateVisit,tvLogVisit,tvDiscardEnquiry,
            tvManageEnquiryStuName;
    private EditText edtNextDate,edtNextVisitDesc,edtDiscardComment;
    private StudentBean studentBean;
    private ArrayList<com.auribisesmyplayschool.myplayschool.bean.CourseBean> courseBeanArrayList;
    private ArrayList<UserBean> teacherBeanArrayList,counsellorArrayList;
    private ArrayList<String> teacherNameArrayList,counsellorNameArrayList;
    private ArrayList<StuEduBean> stuEduBeanArrayList;
    private ArrayList<StuExpBean> stuExpBeanArrayList;
    private ArrayList<UserVisitBean> userVisitBeanArrayList;
    private int reqCode=0,responseSignal=0,updateVisit=0,posSpnUser=0,logVisitSignal=0,dateSignal=0,activitySignal=0,
            cYear,cMonth,cDay;
    private String EnquiryDescription="";
    private Dialog dialog;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private ImageView ivEnquiryStudent;
    private SharedPreferences preferences;
    FirebaseFirestore db;
    SpotsDialog progressDialog;
    String prevEnquiryDesc="";
    EnquiryBean enquiryBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_enquiry_detail);
        preferences=getSharedPreferences(AttUtil.shpREG, MODE_PRIVATE);
        initview();
        setData();
        //getDetails();
    }

    void getDetails() {
        if(updateVisit==1){
            //studentBean.setNextVisit(edtNextDate.getText().toString().trim());
            //studentBean.setEnqdesc(EnquiryDescription);
            setData();
            final AlertDialog.Builder builder =
                    new AlertDialog.Builder(ViewEnquiryDetailActivity.this);
            builder.setMessage("Next Visit Updated");
            builder.setCancelable(false);
            builder.setPositiveButton("ok",null);
            builder.setCancelable(false);
            builder.create().show();
        }else if(updateVisit==2){

        }

    }

    void initview(){
        db = FirebaseFirestore.getInstance();
        calendar = Calendar.getInstance();
        cYear = calendar.get(Calendar.YEAR);
        cMonth = calendar.get(Calendar.MONTH);
        cDay = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(this,onDateSetListener,cYear,cMonth,cDay);
        teacherBeanArrayList = new ArrayList<>();
        courseBeanArrayList = new ArrayList<>();
        stuExpBeanArrayList = new ArrayList<>();
        stuEduBeanArrayList = new ArrayList<>();
        userVisitBeanArrayList = new ArrayList<>();
        teacherNameArrayList = new ArrayList<>();
        counsellorArrayList = new ArrayList<>();
        counsellorNameArrayList = new ArrayList<>();
        studentBean = new StudentBean();
        enquiryBean = new EnquiryBean();
        ivEnquiryStudent = findViewById(R.id.ivEnquiryStudent);
        tvManageEnquiryStuName = findViewById(R.id.tvManageEnquiryStuName);
        tvStuPersDetails = findViewById(R.id.tvManageEnqPersDetail);
        tvStuEnqDetails = findViewById(R.id.tvManageEnqDetail);
        tvStuEnqVisit = findViewById(R.id.tvManageEnqVisitDetail);
        tvRegisterStu = findViewById(R.id.tvEnquiryRegStu);
        tvUpdateVisit = findViewById(R.id.tvEnquiryNextVisit);
        tvLogVisit = findViewById(R.id.tvEnquiryLogVisit);
        tvDiscardEnquiry = findViewById(R.id.tvEnquiryDiscard);
        tvRegisterStu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(studentBean.getEnquiryBean().getEnqStatus()==2||studentBean.getEnquiryBean().getEnqStatus()==1) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ViewEnquiryDetailActivity.this);
                    dialog.setMessage("Enquiry is Processed");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("ok",null);
                    dialog.setCancelable(false);
                    dialog.create().show();
                }else{
                    if(studentBean.getEnquiryBean().getEnqStatus()!=1){
                        startActivityForResult(new Intent(ViewEnquiryDetailActivity.this, AddStudentActivity.class)
                                .putExtra(AttUtil.KEY_COURSE_ARRAYLIST,courseBeanArrayList)
                                .putExtra(AttUtil.TAG_STUDENTBEAN,studentBean)
                               // .putExtra(AttUtil.TAG_STUEDU,stuEduBeanArrayList)
                               // .putExtra(AttUtil.TAG_STUEXP,stuExpBeanArrayList)
                                .putExtra("signal",1),AttUtil.REQ_CODE);
                    }else{
                        AlertDialog.Builder dialog =
                                new AlertDialog.Builder(ViewEnquiryDetailActivity.this);
                        dialog.setMessage("Enquiry is Processed");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("ok",null);
                        dialog.setCancelable(false);
                        dialog.create().show();
                    }

                    //signal=1 update student in enquiry
                    //signal=0 normal update
                }
            }
        });
        tvUpdateVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // EnquiryDescription=studentBean.getEnqdesc();
                if(studentBean.getEnquiryBean().getEnqStatus()==2||studentBean.getEnquiryBean().getEnqStatus()==1) {
                    AlertDialog.Builder dialog =
                            new AlertDialog.Builder(ViewEnquiryDetailActivity.this);
                    dialog.setMessage("Enquiry is Processed");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("ok",null);
                    dialog.setCancelable(false);
                    dialog.create().show();
                }else{updateNextVisitDialog();}
            }
        });

        tvLogVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  EnquiryDescription=studentBean.getEnqdesc();
                if(studentBean.getEnquiryBean().getEnqStatus()==2||studentBean.getEnquiryBean().getEnqStatus()==1) {
                    AlertDialog.Builder dialog =
                            new AlertDialog.Builder(ViewEnquiryDetailActivity.this);
                    dialog.setMessage("Enquiry is Processed");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("ok",null);
                    dialog.setCancelable(false);
                    dialog.create().show();
                }else{logVisitDialog();}
            }
        });

        tvDiscardEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EnquiryDescription=studentBean.getEnquiryBean().getEnqdesc();
                if(studentBean.getEnquiryBean().getEnqStatus() == 1) {
                    AlertDialog.Builder dialog =
                            new AlertDialog.Builder(ViewEnquiryDetailActivity.this);
                    dialog.setMessage("Enquiry is Processed");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("ok",null);
                    dialog.setCancelable(false);
                    dialog.create().show();
                }else{
                    discardEnquiryDialog();
                }
            }
        });

        Intent ircv = getIntent();
        if(ircv.hasExtra(AttUtil.TAG_STUDENTBEAN)){
            studentBean = (StudentBean)ircv.getSerializableExtra(AttUtil.TAG_STUDENTBEAN);
           // getSupportActionBar().setTitle(studentBean.getName()+"'s Details");
            courseBeanArrayList = (ArrayList<com.auribisesmyplayschool.myplayschool.bean.CourseBean>)ircv.getSerializableExtra(AttUtil.KEY_COURSE_ARRAYLIST);
            //teacherBeanArrayList = (ArrayList<UserBean>)ircv.getSerializableExtra(AttUtil.KEY_USER_ARRAYLIST);
            counsellorArrayList = (ArrayList<UserBean>)ircv.getSerializableExtra(AttUtil.KEY_COUNSELLOR_ARRAYLIST);
            Log.i("test",counsellorArrayList.toString());
        }
        if(ircv.hasExtra("activitySignal")){
            activitySignal = ircv.getIntExtra("activitySignal",0);
            if(activitySignal == 1){
                studentBean = (StudentBean)ircv.getSerializableExtra(AttUtil.TAG_STUDENTBEAN);
                LinearLayout lBtnSetOne = (LinearLayout)findViewById(R.id.lBtnSetOne);
                LinearLayout lBtnSetTwo = (LinearLayout)findViewById(R.id.lBtnSetTwo);
                lBtnSetTwo.setVisibility(View.GONE);
                lBtnSetOne.setVisibility(View.GONE);

            }
        }
        if(activitySignal==0){
            teacherNameArrayList.clear();
            teacherNameArrayList.add("--Select Instuctor--");
            counsellorNameArrayList.clear();
            counsellorNameArrayList.add("--Select Counsellor--");
            for(int i=0;i<teacherBeanArrayList.size();i++){
                teacherNameArrayList.add(teacherBeanArrayList.get(i).getUserName());
            }
            for(int i=0;i<counsellorArrayList.size();i++){
                counsellorNameArrayList.add(counsellorArrayList.get(i).getUserName());
            }
        }
        findViewById(R.id.ivmessageEnquiry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callParentOptions(1);
            }
        });
        findViewById(R.id.ivcallEnquiry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callParentOptions(0);
            }
        });

        if(studentBean.getEnquiryBean().getEnqStatus()==2) {
            tvDiscardEnquiry.setText("Reinstate");
        }

        if(AttUtil.isNetworkConnected(ViewEnquiryDetailActivity.this)){
           // retrieveFromDb();
        }else{
           // retrieveNetConnect();
        }
    }

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
            monthOfYear=monthOfYear+1;
            String cDayString,cMonthString;
            if(dayOfMonth<=9){
                cDayString = "0"+dayOfMonth;
            }else{
                cDayString = String.valueOf(dayOfMonth);
            }
            if(monthOfYear<=9){
                cMonthString = "0"+monthOfYear;
            }else{
                cMonthString = String.valueOf(monthOfYear);
            }
            if(dateSignal==3){
                edtNextDate.setText(year+"-"+cMonthString+"-"+cDayString);
            }
        }
    };

    void updateNextVisitDialog(){
        final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);
        dialogBuilder.setTitle("Update Next Visit Date");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 20, 10, 0);
        edtNextDate = new EditText(this);
        edtNextVisitDesc = new EditText(this);
        edtNextVisitDesc.setHint("Comment(if any)");
        edtNextDate.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        edtNextDate.setFocusableInTouchMode(false);
        edtNextDate.setHint("Next visit date");
        edtNextDate.setText(studentBean.getEnquiryBean().getNextVisit());
        layout.addView(edtNextDate, params);
        layout.addView(edtNextVisitDesc,params);
        edtNextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateSignal=3;
                datePickerDialog.show();
            }
        });
        dialogBuilder.setView(layout);
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!edtNextDate.getText().toString().trim().isEmpty()){
                    if(AttUtil.isNetworkConnected(ViewEnquiryDetailActivity.this)){
                        EnquiryDescription = EnquiryDescription+
                                ","+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+": "+
                                edtNextVisitDesc.getText().toString().trim();
                        updateVisit = 1;
                        //updatenextDate();
                    }else{
                        //updateNextDateNetConnect();
                    }
                }
            }
        });
        android.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    void logVisitDialog() {
        dialog = new Dialog(this);
        View view = null;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        view = getLayoutInflater().inflate(R.layout.custom_log_visit, null);
        builder.setTitle("Log Visit");
        builder.setView(view);
        final RadioButton rbtnEnquiryVisit = view.findViewById(R.id.rbtnEnquiryVisit);
        final RadioButton rbtnDemoVisit = view.findViewById(R.id.rbtnDemoVisit);
        final RadioButton rbtnCounsellingVisit = view.findViewById(R.id.rbtnCounsellingVisit);
        final Spinner spnUserEnquiry = view.findViewById(R.id.spnUserEnquiry);
        final TextView tvVisitNote = view.findViewById(R.id.tvVisitNote);
        final EditText edtLogVisitComment = view.findViewById(R.id.edtLogVisitComment);
        TextView tvCancelVisit = view.findViewById(R.id.tvCancelVisit);
        TextView tvLogVisit = view.findViewById(R.id.tvLogVisit);
        final ArrayList<String> tempArrayList = new ArrayList<>();
        tempArrayList.clear();
        if (preferences.getInt(AttUtil.shpLoginType, 0) == 4){
            spnUserEnquiry.setVisibility(View.GONE);
            rbtnDemoVisit.setVisibility(View.GONE);
            rbtnEnquiryVisit.setVisibility(View.GONE);
            rbtnCounsellingVisit.setVisibility(View.GONE);
        }
        tempArrayList.add("--Choose An Option--");
        spnUserEnquiry.setAdapter(new ArrayAdapter(ViewEnquiryDetailActivity.this,android.R.layout.simple_dropdown_item_1line,
                tempArrayList));
        spnUserEnquiry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {posSpnUser = i;            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {            }
        });
        rbtnEnquiryVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvVisitNote.setText("Enquiry Visit");
                spnUserEnquiry.setEnabled(false);
                tempArrayList.clear();
                tempArrayList.add("--Choose An Option--");
                spnUserEnquiry.setAdapter(new ArrayAdapter(ViewEnquiryDetailActivity.this,android.R.layout.simple_dropdown_item_1line,
                        tempArrayList));
            }
        });
        rbtnDemoVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(studentBean.getEnquiryBean().getUserType()==3){
                 //   tvVisitNote.setText("Demo is already provided to "+studentBean.getName());
                }
                spnUserEnquiry.setEnabled(true);
                if(rbtnDemoVisit.isChecked()){
                    if(teacherBeanArrayList.size()==0){
                        teacherNameArrayList.clear();
                        teacherNameArrayList.add("--No Teacher--");
                    }
                    spnUserEnquiry.setAdapter(new ArrayAdapter(ViewEnquiryDetailActivity.this,android.R.layout.simple_dropdown_item_1line,
                            teacherNameArrayList));
                }
            }
        });
        rbtnCounsellingVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(studentBean.getEnquiryBean().getUserType()==3){
                  //  tvVisitNote.setText("Counselling is already provided to "+studentBean.getName());
                }
                spnUserEnquiry.setEnabled(true);
                if(rbtnCounsellingVisit.isChecked()){
                    if(rbtnCounsellingVisit.isChecked()){
                        if(counsellorArrayList.size()==0){
                            counsellorNameArrayList.clear();
                            counsellorNameArrayList.add("--No Counsellor--");
                        }
                        spnUserEnquiry.setAdapter(new ArrayAdapter(ViewEnquiryDetailActivity.this,android.R.layout.simple_dropdown_item_1line,
                                counsellorNameArrayList));
                    }
                }
            }
        });
        tvLogVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preferences.getInt(AttUtil.shpLoginType, 0) == 4)
                    rbtnCounsellingVisit.setChecked(true);
                String str = "";
                if(edtLogVisitComment.getText().toString().trim().length()>0)
                    str = edtLogVisitComment.getText().toString().trim();
                else
                    str = "N/A";
                String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ": ";
                if(rbtnEnquiryVisit.isChecked()){
                    updateVisit = 2;
                    logVisitSignal=1;

                    if(AttUtil.isNetworkConnected(ViewEnquiryDetailActivity.this)) {
                        EnquiryDescription = EnquiryDescription +
                                "," + datetime + str;
                        //updatenextDate();
                    }else {
                        //updateLogNetConnect();
                    }
                }
                if(rbtnDemoVisit.isChecked()){
                    if(posSpnUser == 0){
                        tvVisitNote.setText("Error: Select Instructor");
                    }else{
                        updateVisit = 2;
                        logVisitSignal=2;
                        if(AttUtil.isNetworkConnected(ViewEnquiryDetailActivity.this)) {
                            EnquiryDescription = EnquiryDescription +
                                    "," + datetime + str;
                            //updatenextDate();
                        }else {
                            //updateLogNetConnect();
                        }
                    }
                }
                if(rbtnCounsellingVisit.isChecked()){

                    if(posSpnUser == 0&&preferences.getInt(AttUtil.shpLoginType, 0) == 2){
                        tvVisitNote.setText("Error: Select Counsellor");
                    }else{
                        updateVisit = 2;
                        logVisitSignal=3;
                        /*String str = "";
                        if(edtLogVisitComment.getText().toString().trim().length()==0)
                            str = edtLogVisitComment.getText().toString().trim();
                        else
                            str = "N/A";*/
                        if(AttUtil.isNetworkConnected(ViewEnquiryDetailActivity.this)) {
                            EnquiryDescription = EnquiryDescription +
                                    "," + datetime + str;
                           // updatenextDate();
                        }else {
                            //updateLogNetConnect();
                        }
                    }
                }
            }
        });
        tvCancelVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    void discardEnquiryDialog(){
        final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);
        if(studentBean.getEnquiryBean().getEnqStatus() == 0)
            dialogBuilder.setTitle("Are you sure to discard this enquiry?");
        else
            dialogBuilder.setTitle("Are you sure to reinstate this enquiry?");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 20, 10, 0);
        edtDiscardComment = new EditText(this);
        edtDiscardComment.setHint("Comment(if any)");
        layout.addView(edtDiscardComment, params);
        dialogBuilder.setView(layout);
        dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateVisit = 3;
                if(AttUtil.isNetworkConnected(ViewEnquiryDetailActivity.this)){

                    prevEnquiryDesc = studentBean.getEnquiryBean().getEnqdesc();
                    if(edtDiscardComment.getText().toString().trim().length()>0){
                        EnquiryDescription = EnquiryDescription +
                                "," + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ": " +
                                edtDiscardComment.getText().toString().trim();
                    }else{
                        EnquiryDescription = EnquiryDescription +
                                "," + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ": " +
                                edtDiscardComment.getText().toString().trim()+ ": N/A";
                    }
                    /*EnquiryDescription = EnquiryDescription +
                            "," + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ": " +
                            edtDiscardComment.getText().toString().trim();*/
                    prevEnquiryDesc = prevEnquiryDesc+EnquiryDescription;
                    AttUtil.progressDialog(ViewEnquiryDetailActivity.this);
                    AttUtil.pd(1);
                    discardEnquiry();
                } else {
                    //updateLogNetConnect();
                }
            }
        });
        android.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

     void discardEnquiry() {
         enquiryBean.setEnqid(studentBean.getEnquiryBean().getEnqid());
         enquiryBean.setNextVisit(studentBean.getEnquiryBean().getNextVisit());
         enquiryBean.setEnqdesc(studentBean.getEnquiryBean().getEnqdesc());
         enquiryBean.setType(studentBean.getEnquiryBean().getType());
         enquiryBean.setEnqdesc(prevEnquiryDesc);
        if(studentBean.getEnquiryBean().getEnqStatus() != 2){
            enquiryBean.setEnqStatus(2);
            studentBean.setEnquiryBean(enquiryBean);
            db.collection(Constants.student_collection)
                    .document(String.valueOf(studentBean.getEnquiryBean().getEnqid()))
                    .update("enquiryBean.enqStatus",studentBean.getEnquiryBean().getEnqStatus(),
                            "enquiryBean.enqdesc",studentBean.getEnquiryBean().getEnqdesc())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    setData();
                    AttUtil.pd(0);
                    tvDiscardEnquiry.setText("Reinstate");
                    Toast.makeText(ViewEnquiryDetailActivity.this,"Enquiry is discarded.",Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            enquiryBean.setEnqStatus(0);
            studentBean.setEnquiryBean(enquiryBean);
            db.collection(Constants.student_collection)
                    .document(String.valueOf(studentBean.getEnquiryBean().getEnqid()))
                    .update("enquiryBean.enqStatus",studentBean.getEnquiryBean().getEnqStatus(),
                            "enquiryBean.enqdesc",studentBean.getEnquiryBean().getEnqdesc())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    setData();
                    AttUtil.pd(0);
                    tvDiscardEnquiry.setText("Discard Enquiry");
                    Toast.makeText(ViewEnquiryDetailActivity.this,"Enquiry is reinstated.",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra(AttUtil.TAG_STUDENTBEAN,studentBean);
        setResult(AttUtil.RES_CODE, i);
        finish();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

    void callParentOptions(final int callMsg){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ViewEnquiryDetailActivity.this);
        String[] options = {"Father", "Mother"};
        if(callMsg==0)
            options = new String[]{"Call Father", "Call Mother"};
        else
            options = new String[]{"Message Father", "Message Mother"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listParentPhoneNumbers(which,callMsg);
            }
        });
        builder.create().show();
    }

    void listParentPhoneNumbers(int signal,final int callMsg){
        if(signal==0){
            if(studentBean.getFatherPhone().toString().length()==0)
                Toast.makeText(this, "No contact details", Toast.LENGTH_SHORT).show();
            else{
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ViewEnquiryDetailActivity.this);
                String[] options = studentBean.getFatherPhone().split(",");
                final String[] finalOptions = options;
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(callMsg==0)
                            callFunction(finalOptions[which]);
                        if(callMsg==1)
                            msgFunction(finalOptions[which]);
                    }
                });
                builder.create().show();
            }
        }else if(signal==1){
            if(studentBean.getMotherPhone().toString().length()==0)
                Toast.makeText(this, "No contact details", Toast.LENGTH_SHORT).show();
            else{
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ViewEnquiryDetailActivity.this);
                String[] options = studentBean.getMotherPhone().split(",");
                final String[] finalOptions = options;
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(callMsg==0)
                            callFunction(finalOptions[which]);
                        if(callMsg==1)
                            msgFunction(finalOptions[which]);
                    }
                });
                builder.create().show();
            }
        }

    }

    void callFunction(String phone){
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone.toString().trim()));
        startActivity(i);
    }

    void msgFunction(String phone){
        Intent j = new Intent(Intent.ACTION_VIEW);
        j.setType("vnd.android-dir/mms-sms");
        j.putExtra("address", phone.toString().trim());
        startActivity(j);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AttUtil.REQ_CODE && resultCode == AttUtil.RES_CODE){
            studentBean = (StudentBean)data.getSerializableExtra(AttUtil.TAG_STUDENTBEAN);
            setData();
        }
        if(requestCode == 2 && resultCode == AttUtil.RES_CODE){
            studentBean = (StudentBean)data.getSerializableExtra(AttUtil.TAG_STUDENTBEAN);
            getSupportActionBar().setTitle(studentBean.getStuName()+"'s Details");
            setData();
        }
        if(requestCode == AttUtil.REQ_CODE && resultCode == 201){
            studentBean = (StudentBean)data.getSerializableExtra(AttUtil.TAG_STUDENTBEAN);
           // studentBean.setEnqStatus(1);
            setData();
        }

        if(requestCode==AttUtil.REQ_CODE && resultCode == 300){
            studentBean = (StudentBean)data.getSerializableExtra(AttUtil.TAG_STUDENTBEAN);
            setData();
        }
    }

    void setData(){
        getSupportActionBar().setTitle(studentBean.getStuName()+"'s Details");
        try{
            if(studentBean.getImage().toString().length()>0)
                Picasso.with(this).load(studentBean.getImage()).networkPolicy(NetworkPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_STORE).fit().into(ivEnquiryStudent);
        }catch(Exception e){
            e.printStackTrace();
        }
       int number=studentBean.getEnquiryBean().getEnqid();
        tvManageEnquiryStuName.setText(studentBean.getStuName());
        String str="";
        str = str+"Enquiry Number:"+number;
        if(studentBean.getDob().toString().contains("0000-00-00"))
            str=str+"\nDate of Birth: NA";
        else
            str=str+"\nDate of Birth: "+studentBean.getDob();

        if(studentBean.getGender().length()>0)
            str = str+"\nGender: "+studentBean.getGender();
        else
            str = str+"\nGender: NA";

        if(studentBean.getFatherName().length()>0)
            str = str+"\nFather's Name: "+studentBean.getFatherName();
        else
            str = str+"\nFather's Name: NA";

        try {
            if(studentBean.getfQualification().length()>0)
                str = str+"\nFather's Qualification: "+studentBean.getfQualification();
            else
                str = str+"\nFather's Qualification: NA";
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(studentBean.getfOccupation().length()>0)
                str = str+"\nFather's Occupation: "+studentBean.getfOccupation();
            else
                str = str+"\nFather's Occupation: NA";
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(studentBean.getfCompanyName().length()>0)
                str = str+"\nFather's Company Name: "+studentBean.getfCompanyName();
            else
                str = str+"\nFather's Company Name: NA";
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(studentBean.getfOfficeAddress().length()>0)
                str = str+"\nFather's Office Address: "+studentBean.getfOfficeAddress();
            else
                str = str+"\nFather's Office Address: NA";
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(studentBean.getFatherPhone().length()>0)
                str = str+"\nFather's Phone Number: "+studentBean.getFatherPhone();
            else
                str = str+"\nFather's Phone Number: NA";
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(studentBean.getFatherEmail().length()>0)
            str = str+"\nFather's E-mail: "+studentBean.getFatherEmail();
        else
            str = str+"\nFather's E-mail: NA";

      if(studentBean.getMotherName().length()>0)
            str = str+"\nMother's Name: "+studentBean.getMotherName();
        else
            str = str+"\nMother's Name: NA";

        try {
            if(studentBean.getmQualification().length()>0)
                str = str+"\nMother's Qualification: "+studentBean.getmQualification();
            else
                str = str+"\nMother's Qualification: NA";
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(studentBean.getmOccupation().length()>0)
                str = str+"\nMother's Occupation: "+studentBean.getmOccupation();
            else
                str = str+"\nMother's Occupation: NA";
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(studentBean.getmCompanyName().length()>0)
                str = str+"\nMother's Company Name: "+studentBean.getmCompanyName();
            else
                str = str+"\nMother's Company Name: NA";
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(studentBean.getmOfficeAddress().length()>0)
                str = str+"\nMother's Office Address: "+studentBean.getmOfficeAddress();
            else
                str = str+"\nMother's Office Address: NA";
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(studentBean.getMotherPhone().length()>0)
            str = str+"\nMother's Phone Number: "+studentBean.getMotherPhone();
        else
            str = str+"\nMother's Phone Number: NA";

        if(studentBean.getMotherEmail().length()>0)
            str = str+"\nMother's E-mail: "+studentBean.getMotherEmail();
        else
            str = str+"\nMother's E-mail: NA";

        if(studentBean.getAddress().length()>0)
            str = str+"\nResidential Address: "+studentBean.getAddress();
        else
            str = str+"\nResidential Address: NA";

        try {
            if(studentBean.getRecommendSomeone().length()>0)
                str = str+"\nRecommendation: "+studentBean.getRecommendSomeone();
            else
                str = str+"\nRecommendation: NA";
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvStuPersDetails.setText(str);
        str = "";
        if(studentBean.getEnquiryBean().getEnqStatus()==1){
            str = "Registered";
        }else if(studentBean.getEnquiryBean().getEnqStatus() == 0){
            if(studentBean.getEnquiryBean().getUserType() == 3){
                str = "Demo";
            }else if(studentBean.getEnquiryBean().getUserType() == 0){
                str = "Enquiry";
            }else if(studentBean.getEnquiryBean().getUserType() == 2){
                str = "Counselling";
            }
        }else if(studentBean.getEnquiryBean().getEnqStatus() == 2){
            str = "Discared";
        }
        String str1="";
        str1="Course Name: "+studentBean.getEnquiryBean().getCourseName();

        if(studentBean.getEnquiryBean().getNextVisit().equals("0000-00-00"))
            str1 = str1+"\nNext Follow Up: NA\nStatus: "+str;
        else
            str1 = str1+"\nNext Follow Up: "+getFormattedDate(studentBean.getEnquiryBean().getNextVisit())+"\nStatus: "+str;

        str1=str1+"\nReference: "+studentBean.getReference();
//        if(studentBean.getReferenceBy().length()>0)
//            str1=str1+"\nReference by: "+studentBean.getReferenceBy();



        //CHECK
        if(studentBean.getEnquiryBean().getEnqdesc().toString().contains(",")){
            String[] str2 = studentBean.getEnquiryBean().getEnqdesc().split(",");
            //str1 = str1+"Description: "+studentBean.getEnqDescription();
            for(int i = 0;i < str2.length;i++){
                if(i == 0)
                    str1 = str1+"\nComment: "+str2[i];
                else
                    str1 = str1+"\n-"+str2[i];
            }
        }else{
            str1 = str1+"\nComment: "+studentBean.getEnquiryBean().getEnqdesc();
        }
        tvStuEnqDetails.setText(str1);
        str = "";
        for(int i=0;i<userVisitBeanArrayList.size();i++){
            if(userVisitBeanArrayList.get(i).getUserType() == 3)
                str = str + (i+1)+": "+getFormattedDateTime(userVisitBeanArrayList.get(i).getVisitDateTime())
                        +" Demo("+userVisitBeanArrayList.get(i).getUserName()+")";
            else if(userVisitBeanArrayList.get(i).getUserType() == 2)
                str = str + (i+1)+": "+getFormattedDateTime(userVisitBeanArrayList.get(i).getVisitDateTime())
                        +" Counselling("+userVisitBeanArrayList.get(i).getUserName()+")";
            else
                str = str + (i+1)+": "+getFormattedDateTime(userVisitBeanArrayList.get(i).getVisitDateTime())+" Enquiry";
            if((i+1)!=userVisitBeanArrayList.size())
                str = str +"\n";
        }
        tvStuEnqVisit.setText(str);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(activitySignal!=1){
            menu.add(0,1,0,"Update Details")
                    .setIcon(R.drawable.icon_edit_user)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return super.onCreateOptionsMenu(menu);
    }



    String getFormattedDateTime(String dateTimeRec){
        String array[]= dateTimeRec.split(" ");
        String newDate=array[0];
        String newTime=array[1];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfn = new SimpleDateFormat("d MMM, yyyy", Locale.ENGLISH);
        SimpleDateFormat stf = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat stfn = new SimpleDateFormat("hh:mm aa");

        Date date;
        try{
            date = sdf.parse(newDate);
            newDate = sdfn.format(date);
        }catch(ParseException e){
            e.printStackTrace();
        }

        Date time;
        try{
            time = stf.parse(newTime);
            newTime = stfn.format(time);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return newDate+" "+newTime;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        if(item.getItemId()==1){
            Intent i = new Intent(ViewEnquiryDetailActivity.this, AddStudentActivity.class);
            i.putExtra(AttUtil.TAG_STUDENTBEAN, studentBean);
            i.putExtra(AttUtil.KEY_COURSE_ARRAYLIST, courseBeanArrayList);
            i.putExtra("viewDetail", 1);
            startActivityForResult(i,AttUtil.REQ_CODE);
        }
        return super.onOptionsItemSelected(item);
    }






}
