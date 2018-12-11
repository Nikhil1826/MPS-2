package com.auribisesmyplayschool.myplayschool.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BranCourBean;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.CourseBean;
import com.auribisesmyplayschool.myplayschool.bean.AdmissionBean;
import com.auribisesmyplayschool.myplayschool.bean.EnquiryBean;
import com.auribisesmyplayschool.myplayschool.bean.StudentBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

public class AddStudentActivity extends AppCompatActivity {
    Spinner spnCourses, spnBatchs;
    ImageView imageViewStu;
    TextView tvBatchNotice;
    RadioButton radioRegMale, radioRegFemale;

    SharedPreferences pref;
    ArrayList<String> arrayListcourse, arrayListBatch;
    Button btnRegister;
    SharedPreferences preferences;
    int reqCode, tId, var4, var5, var6, dateSetSignal = 0;
    String cMonthString, cDayString, imageBase64;
    DatePickerDialog datePickerDialog;
    //old
    ArrayList<BranCourBean> courseBeanArrayList;
   // ArrayList<BatchBean> batchBeanArrayList;
    String strBirthDate = "", reg_Number = "";
    int check,checkTwo;

    EditText txtRegFName, txtRegDOB, edtFatherName, edtFatherQualification, edtFatherOccupation, edtFatherCompanyName,
            edtFatherOfficeAddess, edtFatherEmail, edtFatherPhone, edtMotherName, edtMotherQualification,
            edtMotherOccupation, edtMotherCompanyName, edtMotherOfficeAddess, edtMotherEmail, edtMotherPhone,
            edtMonthlyIncome, edtAddress, edtSiblingDetails;
    StudentBean studentBeanApproval;
    StudentBean studentBeanDetails;
    EnquiryBean enquiryBean;

    FirebaseFirestore db;

    boolean validate() {
        String msg = "Error:";
        boolean check = true, ch = true;

        if (getIntent().getIntExtra("viewDetail", 0) != 1) {
//            if(image.toString().isEmpty()){
//                ch = false;
//                msg=msg+"\nImage";
//            }
        }
        /*if(getIntent().getIntExtra("viewDetail",0)!=1){
            if(studentBeanDetails.getBatchId()==0){
                msg = msg+"\nSelect a Batch";
                ch=false;
            }
        }*/

        if (studentBeanDetails.getStuName().toString().length() == 0) {
            check = false;
            msg = msg + "\nInvalid Child's name";
        }
        if (studentBeanDetails.getDob().toString().length() == 0) {
            check = false;
            msg = msg + "\nInvalid D.O.B.'s name";
        }
        if (studentBeanDetails.getGender().toString().length() == 0) {
            check = false;
            msg = msg + "\nInvalid Gender";
        }
        if (studentBeanDetails.getFatherName().toString().length() == 0) {
            check = false;
            msg = msg + "\nInvalid Father's name";
        }
        if (studentBeanDetails.getfQualification().toString().length() == 0) {
            check = false;
            msg = msg + "\nInvalid Father's Quaification";
        }
        if (edtFatherPhone.getText().toString().length() <= 0) {
            check = false;
            msg = msg + "\nInvalid Father's Phone Number";
        }
        if(studentBeanDetails.getfOccupation().toString().length()==0){
            check=false;
            msg=msg+"\nInvalid Father's Occupation";
        }if(studentBeanDetails.getfCompanyName().toString().length()==0){
            check=false;
            msg=msg+"\nInvalid Father's Company Name";
        }
        if(studentBeanDetails.getfOfficeAddress().toString().length()==0){
            check=false;
            msg=msg+"\nInvalid Father's office Address";
        }
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(studentBeanDetails.getFatherEmail());
        if (!matcher.matches()) {
            check = false;
            msg = msg + "\nInvalid Father's E-mail ";
        }
       if (studentBeanDetails.getMotherName().toString().length() == 0) {
            check = false;
            msg = msg + "\nInvalid Mother's name";
        }
        if (studentBeanDetails.getmQualification().toString().length() == 0) {
            check = false;
            msg = msg + "\nInvalid Mother's Quaification";
        }
        if(studentBeanDetails.getfOccupation().toString().length()==0){
            check=false;
            msg=msg+"\nInvalid Mother's Occupation";
        }if(studentBeanDetails.getfCompanyName().toString().length()==0){
            check=false;
            msg=msg+"\nInvalid Mother's Company Name";
        }
        if(studentBeanDetails.getfOfficeAddress().toString().length()==0){
            check=false;
            msg=msg+"\nInvalid Mother's office Address";
        }
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(studentBeanDetails.getMotherEmail());
        if (!matcher.matches()) {
            check = false;
            msg = msg + "\nInvalid Mother's E-mail ";
        }
        if (edtMotherPhone.getText().toString().length() <= 0) {
            check = false;
            msg = msg + "\nInvalid Mother's Phone Number";
        }
        if (studentBeanDetails.getMonthlyIncome().toString().length() == 0) {
            check = false;
            msg = msg + "\nInvalid Monthly Income";
        }
        if (studentBeanDetails.getBranCourId() == 0) {
            msg = msg + "\nSelect a Course";
            ch = false;
        }
        if (!check) {
            Toast.makeText(AddStudentActivity.this, msg, Toast.LENGTH_LONG).show();
        }
        return check;
    }

    void initViews() {
        Log.i("test",getIntent().getIntExtra("viewDetail", 0)+" - viewDetail");
        //batchBeanArrayList = new ArrayList<>();
       // courseBeanArrayList = new ArrayList<>();
        tvBatchNotice = (TextView) findViewById(R.id.batchNoticeId);
        tvBatchNotice.setVisibility(View.GONE);

        Calendar cal = Calendar.getInstance();
        var6 = cal.get(Calendar.DAY_OF_MONTH);
        var4 = cal.get(Calendar.MONTH);
        var5 = cal.get(Calendar.YEAR);
        enquiryBean = new EnquiryBean();

        imageViewStu = (ImageView) findViewById(R.id.imageViewStudent);
        imageViewStu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(AddStudentActivity.this,
                        ImageCaptureActivity.class), AttUtil.REQ_CODE);
            }
        });

        arrayListBatch = new ArrayList<>();
        arrayListcourse = new ArrayList<>();
        preferences = getSharedPreferences(AttUtil.shpREG, MODE_PRIVATE);
        txtRegFName = (EditText) findViewById(R.id.txtRegFName);
        txtRegDOB = (EditText) findViewById(R.id.txtRegDOB);
        edtFatherName = (EditText) findViewById(R.id.edtFatherName);
        edtFatherQualification = (EditText) findViewById(R.id.edtFatherQualification);
        edtFatherOccupation = (EditText) findViewById(R.id.edtFatherOccupation);
        edtFatherCompanyName = (EditText) findViewById(R.id.edtFatherCompanyName);
        edtFatherOfficeAddess = (EditText) findViewById(R.id.edtFatherOfficeAddess);
        edtFatherEmail = (EditText) findViewById(R.id.edtFatherEmail);
        edtFatherPhone = (EditText) findViewById(R.id.edtFatherPhone);
        edtMotherName = (EditText) findViewById(R.id.edtMotherName);
        edtMotherQualification = (EditText) findViewById(R.id.edtMotherQualification);
        edtMotherOccupation = (EditText) findViewById(R.id.edtMotherOccupation);
        edtMotherCompanyName = (EditText) findViewById(R.id.edtMotherCompanyName);
        edtMotherOfficeAddess = (EditText) findViewById(R.id.edtMotherOfficeAddess);
        edtMotherEmail = (EditText) findViewById(R.id.edtMotherEmail);
        edtMotherPhone = (EditText) findViewById(R.id.edtMotherPhone);
        edtMonthlyIncome = (EditText) findViewById(R.id.edtMonthlyIncome);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtSiblingDetails = (EditText) findViewById(R.id.edtSiblingDetails);

        spnCourses = (Spinner) findViewById(R.id.spnCourses);
        spnBatchs = (Spinner) findViewById(R.id.spnBatches);
        spnBatchs.setVisibility(View.GONE);
        radioRegMale = (RadioButton) findViewById(R.id.radioRegMale);
        radioRegFemale = (RadioButton) findViewById(R.id.radioRegFemale);

        radioRegMale.setOnClickListener(onClickListener);
        radioRegFemale.setOnClickListener(onClickListener);

        btnRegister = (Button) findViewById(R.id.ivRegister);
        //rg = (RadioGroup)findViewById(R.id.radioGender);
        datePickerDialog = new DatePickerDialog(this, onDateSetListener, var5, var4, var6);
        txtRegDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
                dateSetSignal = 1;
            }
        });
        //studentBean = new StudentBean();
        imageViewStu.requestFocus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        pref = getSharedPreferences(AttUtil.shpREG, MODE_PRIVATE);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = FirebaseFirestore.getInstance();
        initViews();
        initList();
        setEnquiryData();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View var1) {

                    if (AttUtil.isNetworkConnected(AddStudentActivity.this)) {
                        AttUtil.progressDialog(AddStudentActivity.this);
                        AttUtil.pd(1);
                        getStudentBeanData();
                        //setAdmissionBean();
                        if(validate()){
                            if(getIntent().getIntExtra("viewDetail",0)!=1){
                                insertThread();
                            }else{
                                //getStudentBeanData();
                                updateThread();
                            }
                        }
                    } else {
                        //regNetConnect();
                    }

            }
        });
    }




    void updateThread() {
        db.collection(Constants.student_collection)
                .document(String.valueOf(studentBeanDetails.getStudentId())).set(studentBeanDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        /*if(studentBeanDetails.getEnqStatus()==1){
                            if(getIntent().getIntExtra("viewDetail",0)==1){
                                check = 1;
                                insertThread();
                            }
                        }else{
                            AttUtil.pd(0);
                            Toast.makeText(AddStudentActivity.this,"UPDATED",Toast.LENGTH_SHORT).show();
                            setResult(AttUtil.RES_CODE, new Intent().putExtra(AttUtil.TAG_STUDENTBEAN, studentBeanDetails));
                            finish();
                        }

                        if(checkTwo == 1){
                            Intent intent = new Intent();
                            intent.putExtra(AttUtil.TAG_STUDENTBEAN,studentBeanDetails);
                            setResult(300,intent);
                           // finish();
                            alertRegisterSuccess(studentBeanDetails.getStudentId());
                        }*/
                        AttUtil.pd(0);
                        Toast.makeText(AddStudentActivity.this,"UPDATED",Toast.LENGTH_SHORT).show();
                        setResult(AttUtil.RES_CODE, new Intent().putExtra(AttUtil.TAG_STUDENTBEAN, studentBeanDetails));
                        finish();

                    }
                });
    }

    void insertThread() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        studentBeanDetails.setDate(year+"-"+month+"-"+day);
        //studentBeanDetails.setDate(year+"-"+month+"-"+day);
        db.collection(Constants.student_collection).document(String.valueOf(studentBeanDetails.getStudentId()))
                .set(studentBeanDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                /*if(check == 1){
                    AttUtil.pd(0);
                    Toast.makeText(AddStudentActivity.this,"UPDATED",Toast.LENGTH_SHORT).show();
                    setResult(AttUtil.RES_CODE, new Intent().putExtra(AttUtil.TAG_STUDENTBEAN, studentBeanDetails));
                    finish();
                }else{
                    checkTwo = 1;
                   // updateThread();
                }*/
                Intent intent = new Intent();
                intent.putExtra(AttUtil.TAG_STUDENTBEAN,studentBeanDetails);
                setResult(300,intent);
                //getLastAdmissionId();
                alertRegisterSuccess(studentBeanDetails.getStudentId());
            }
        });
    }

    /* void getLastAdmissionId() {
        listAdmissionId = new ArrayList<>();
        db.collection(Constants.admission_collection).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    if(!queryDocumentSnapshots.isEmpty()){
                        admissionId = doc.getLong("admissionId").intValue();
                        listAdmissionId.add(admissionId);
                    }else{
                        admissionId = 1;
                    }
                }
                if(listAdmissionId.size()>0){
                    admissionId = 0;
                    admissionId = Collections.max(listAdmissionId);
                    admissionId = admissionId + 1;
                }else{
                    admissionId = 1;
                }
                insertIntoAdmissionDB();
            }
        });
    }

     void insertIntoAdmissionDB() {

        db.collection(Constants.admission_collection).document(String.valueOf(admissionBean.getAdmissionId()))
                .set(admissionBean).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                alertRegisterSuccess(studentBeanDetails.getStudentId());
            }
        });
    }*/

    void alertRegisterSuccess(final int studentId) {
        AttUtil.pd(0);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Fee Structure");
        builder.setMessage(studentBeanDetails.getStuName() + " is successfully registered.");
        builder.setCancelable(false);
        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(AddStudentActivity.this, FeeSelectActivty.class);
               // intent.putExtra("admissionId", admnId);
                intent.putExtra("studentId", studentId);
                intent.putExtra("studentbean", studentBeanDetails);
                intent.putExtra("signal",1);
                startActivityForResult(intent, 200);
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setResult(201, new Intent().putExtra(AttUtil.TAG_STUDENTBEAN, studentBeanDetails));
                finish();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.radioRegMale) {
                studentBeanDetails.setGender("Male");
            }
            if (v.getId() == R.id.radioRegFemale) {
                studentBeanDetails.setGender("Female");
            }
        }
    };

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear = monthOfYear + 1;
            if (dayOfMonth <= 9) {
                cDayString = "0" + dayOfMonth;
            } else {
                cDayString = String.valueOf(dayOfMonth);
            }
            if (monthOfYear <= 9) {
                cMonthString = "0" + monthOfYear;
            } else {
                cMonthString = String.valueOf(monthOfYear);
            }
            strBirthDate = year + "-" + cMonthString + "-" + cDayString;
            if (dateSetSignal == 1) {
                txtRegDOB.setText(strBirthDate);
            }

        }
    };



    void initList() {
        Intent intent = getIntent();
        if (intent.hasExtra(AttUtil.KEY_COURSE_ARRAYLIST)) {
            courseBeanArrayList = (ArrayList<BranCourBean>) intent.getSerializableExtra(AttUtil.KEY_COURSE_ARRAYLIST);
            if (courseBeanArrayList.size() > 0) {
                arrayListcourse.add("--Select Class--");
                for (int i = 0; i < courseBeanArrayList.size(); i++) {
                    arrayListcourse.add(courseBeanArrayList.get(i).getCourseName());
                }
            } else {
                arrayListcourse.add("--No Class Found--");
            }
            arrayListBatch.add("--Select Section First--");
            spnBatchs.setAdapter(new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, arrayListBatch));
            spnCourses.setAdapter(new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, arrayListcourse));

            spnCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0) {
                        //studentBeanDetails.setBranCourId(courseBeanArrayList.get(position - 1).getBranCourId());
                        //studentBeanDetails.setCourseName(courseBeanArrayList.get(position-1).getCourseName());
                        studentBeanDetails.setBranCourId(courseBeanArrayList.get(position - 1).getBranCourId());
                        studentBeanDetails.setCourseName(courseBeanArrayList.get(position - 1).getCourseName());
                        /*for (int i = 0; i < AttUtil.batchBeanArrayListActive.size(); i++) {
                            if (AttUtil.batchBeanArrayListActive.get(i).getBranCourId() == courseBeanArrayList.get(position - 1).getBranCourId()) {
                                batchBeanArrayList.add(AttUtil.batchBeanArrayListActive.get(i));
                            }
                        }
                        arrayListBatch.clear();
                        arrayListBatch.add("--Select Section--");
                        for (BatchBean bb : batchBeanArrayList) {
                            if (bb.getBatchStatus() == 1) {
                                arrayListBatch.add(bb.getBatch_title());
                            }
                        }*/
                        if (arrayListBatch.size() == 1) {
                            arrayListBatch.clear();
                            arrayListBatch.add("--No Section is Available --");
                        }

                    } else if (position == 0) {
                        arrayListBatch.clear();
                        arrayListBatch.add("--Select Class--");
                        studentBeanDetails.setBranCourId(0);
                        spnBatchs.setAdapter(new ArrayAdapter(AddStudentActivity.this, android.R.layout.simple_dropdown_item_1line, arrayListBatch));
                        spnBatchs.setSelection(0);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    /*void setData() {
        registrationNumber();
        studentBeanDetails.setName(txtRegFName.getText().toString().trim());
        if (radioRegMale.isChecked())
            studentBeanDetails.setGender("Male");
        else
            studentBeanDetails.setGender("Female");
        studentBeanDetails.setDob(txtRegDOB.getText().toString().trim());
        studentBeanDetails.setFather_name(edtFatherName.getText().toString().trim());
        studentBeanDetails.setFather_qualification(edtFatherQualification.getText().toString().trim());
        studentBeanDetails.setFather_occupation(edtFatherOccupation.getText().toString().trim());
        studentBeanDetails.setFather_company_name(edtFatherCompanyName.getText().toString().trim());
        studentBeanDetails.setfOfficeAddress(edtFatherOfficeAddess.getText().toString());
        studentBeanDetails.setFather_email(edtFatherEmail.getText().toString());
        studentBeanDetails.setFather_phone(edtFatherPhone.getText().toString());
        studentBeanDetails.setMother_name(edtMotherName.getText().toString());
        studentBeanDetails.setmQualification(edtMotherQualification.getText().toString());
        studentBeanDetails.setmOccupation(edtMotherOccupation.getText().toString());
        studentBeanDetails.setmCompanyName(edtMotherCompanyName.getText().toString().trim());
        studentBeanDetails.setmOfficeAddress(edtMotherOfficeAddess.getText().toString().trim());
        studentBeanDetails.setMotherEmail(edtMotherEmail.getText().toString().trim());
        studentBeanDetails.setMotherPhone(edtMotherPhone.getText().toString().trim());
        studentBeanDetails.setMonthlyIncome(edtMonthlyIncome.getText().toString().trim());
        studentBeanDetails.setAddress(edtAddress.getText().toString());
        studentBeanDetails.setSiblingDetails(edtSiblingDetails.getText().toString());
    }*/

    void registrationNumber() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (day < 10 && month < 10) {
            reg_Number = "MPS-" + year + "0" + month + "0" + day + "-"+studentBeanDetails.getStudentId();
        } else if (month < 10) {
            reg_Number = "MPS-" + year + "0" + month + day + "-"+studentBeanDetails.getStudentId();
        } else if (day < 10) {
            reg_Number = "MPS-" + year + month + "0" + day + "-"+studentBeanDetails.getStudentId();
        } else {
            reg_Number = "MPS-" + year + month + day + "-"+studentBeanDetails.getStudentId();
        }

        studentBeanDetails.setRegId(reg_Number);

    }

    private void setEnquiryData() {
        studentBeanDetails = (StudentBean) getIntent().getSerializableExtra(AttUtil.TAG_STUDENTBEAN);

        if(getIntent().getIntExtra("viewDetail", 0) != 1){
            registrationNumber();
        }else{
            btnRegister.setText("UPDATE");
            findViewById(R.id.cardViewClass).setVisibility(View.GONE);
            getSupportActionBar().setTitle("Update Student");
        }

        /*if (getIntent().getIntExtra("viewDetail", 0) == 1) {
            btnRegister.setText("UPDATE");
            findViewById(R.id.cardViewClass).setVisibility(View.GONE);
            getSupportActionBar().setTitle("Update Student");
        }*/
        if (studentBeanDetails.getImage().toString().length() > 0) {
            try {
                Picasso.with(this)
                        .load(studentBeanDetails.getImage())
                        .memoryPolicy(MemoryPolicy.NO_STORE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(imageViewStu);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        txtRegFName.setText(studentBeanDetails.getStuName());
        if (studentBeanDetails.getGender().toLowerCase().equals("male")) {
            radioRegMale.setChecked(true);
        } else
            radioRegFemale.setChecked(true);

        txtRegDOB.setText(studentBeanDetails.getDob().toString());
        edtFatherName.setText(studentBeanDetails.getFatherName());
        edtFatherQualification.setText(studentBeanDetails.getfQualification());
        edtFatherOccupation.setText(studentBeanDetails.getfOccupation());
        edtFatherCompanyName.setText(studentBeanDetails.getfCompanyName());
        edtFatherOfficeAddess.setText(studentBeanDetails.getfOfficeAddress());
        edtFatherPhone.setText(studentBeanDetails.getFatherPhone());
        edtFatherEmail.setText(studentBeanDetails.getFatherEmail());
        edtMotherName.setText(studentBeanDetails.getMotherName());
        edtMotherQualification.setText(studentBeanDetails.getmQualification());
        edtMotherOccupation.setText(studentBeanDetails.getmOccupation());
        edtMotherCompanyName.setText(studentBeanDetails.getmCompanyName());
        edtMotherOfficeAddess.setText(studentBeanDetails.getmOfficeAddress());
        edtMotherEmail.setText(studentBeanDetails.getMotherEmail());
        edtMotherPhone.setText(studentBeanDetails.getMotherPhone());
        edtMonthlyIncome.setText(studentBeanDetails.getMonthlyIncome());
        edtAddress.setText(studentBeanDetails.getAddress());
        edtAddress.setText(studentBeanDetails.getAddress());
        edtSiblingDetails.setText(studentBeanDetails.getSiblingDetails());
    }

    void getStudentBeanData() {
        studentBeanDetails.setStuName(txtRegFName.getText().toString().trim());

        if(radioRegMale.isChecked()){
            studentBeanDetails.setGender("Male");
        }else{
            studentBeanDetails.setGender("Female");
        }
        studentBeanDetails.setDescription("");
        studentBeanDetails.setDob(txtRegDOB.getText().toString().trim());
        studentBeanDetails.setFatherName(edtFatherName.getText().toString().trim());
        studentBeanDetails.setfQualification(edtFatherQualification.getText().toString().trim());
        studentBeanDetails.setfOccupation(edtFatherOccupation.getText().toString().trim());
        studentBeanDetails.setfCompanyName(edtFatherCompanyName.getText().toString().trim());
        studentBeanDetails.setfOfficeAddress(edtFatherOfficeAddess.getText().toString().trim());
        studentBeanDetails.setFatherEmail(edtFatherEmail.getText().toString().trim());
        studentBeanDetails.setFatherPhone(edtFatherPhone.getText().toString().trim());
        studentBeanDetails.setMotherName(edtMotherName.getText().toString().trim());
        studentBeanDetails.setMotherEmail(edtMotherEmail.getText().toString().trim());
        studentBeanDetails.setMotherPhone(edtMotherPhone.getText().toString().trim());
        studentBeanDetails.setmQualification(edtMotherQualification.getText().toString().trim());
        studentBeanDetails.setmOccupation(edtMotherOccupation.getText().toString().trim());
        studentBeanDetails.setmCompanyName(edtMotherCompanyName.getText().toString().trim());
        studentBeanDetails.setmOfficeAddress(edtMotherOfficeAddess.getText().toString().trim());
        enquiryBean.setEnqid(studentBeanDetails.getEnquiryBean().getEnqid());
        enquiryBean.setEnqdesc(studentBeanDetails.getEnquiryBean().getEnqdesc());
        enquiryBean.setNextVisit(studentBeanDetails.getEnquiryBean().getNextVisit());
        enquiryBean.setType(studentBeanDetails.getEnquiryBean().getType());
        //studentBeanDetails.setEnqId(studentBeanDetails.getEnqId());
        //studentBeanDetails.setEnqDescription(studentBeanDetails.getEnqDescription());
       // studentBeanDetails.setJoinDate("0000-00-00");

        if(getIntent().getIntExtra("viewDetail",0)==1){
            enquiryBean.setEnqStatus(studentBeanDetails.getEnquiryBean().getEnqStatus());
          //  studentBeanDetails.setEnqStatus(studentBeanDetails.getEnqStatus());
            /*studentBeanDetails.setJoinDate(studentBeanDetails.getJoinDate());
            studentBeanDetails.setApproved(studentBeanDetails.getApproved());
            studentBeanDetails.setBatchId(studentBeanDetails.getBatchId());
            studentBeanDetails.setBatch_title(studentBeanDetails.getBatch_title());
            studentBeanDetails.setCourseName(studentBeanDetails.getCourseName());
            studentBeanDetails.setDate(studentBeanDetails.getDate());
            studentBeanDetails.setRemarks(studentBeanDetails.getRemarks());*/

        }else{
          //  studentBeanDetails.setEnqStatus(1);
            enquiryBean.setEnqStatus(1);
            //studentBeanDetails.setJoinDate("0000-00-00");
        }

        studentBeanDetails.setEnquiryBean(enquiryBean);

        //studentBeanDetails.setEnqType(studentBeanDetails.getEnqType());
       // studentBeanDetails.setBranchName(studentBeanDetails.getBranchName());
        //studentBeanDetails.setBranchId(studentBeanDetails.getBranchId());



        studentBeanDetails.setMonthlyIncome(edtMonthlyIncome.getText().toString().trim());
      //  studentBeanDetails.setAddress(studentBeanDetails.getAddress());
        studentBeanDetails.setSiblingDetails(edtSiblingDetails.getText().toString().trim());
        //studentBeanDetails.setNextVisit(studentBeanDetails.getNextVisit());
        //studentBeanDetails.setUserName(studentBeanDetails.getUserName());
        //studentBeanDetails.setUserId(studentBeanDetails.getUserId());
        //studentBeanDetails.setUserType(studentBeanDetails.getUserType());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AttUtil.REQ_CODE && resultCode == AttUtil.RES_CODE) {
            try {
                if (data != null) {
                    Bitmap bitmap = null;
                    String imagePath = data.getStringExtra("imagePath");
                    Uri mUri = Uri.fromFile(new File(imagePath));
                    try {
                        InputStream image_stream = getContentResolver().openInputStream(mUri);
                        bitmap = BitmapFactory.decodeStream(image_stream);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    imageBase64 = Base64.encodeToString(bitmapdata, Base64.DEFAULT);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmapTwo = BitmapFactory.decodeFile(imagePath, bmOptions);
                    bitmapTwo = Bitmap.createScaledBitmap(bitmap, imageViewStu.getWidth(),
                            imageViewStu.getHeight(), true);
                    imageViewStu.setImageBitmap(bitmapTwo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == 201 && requestCode == 200) {
            StudentBean studentBean = (StudentBean) data.getSerializableExtra(AttUtil.TAG_STUDENTBEAN);
            setResult(201, new Intent().putExtra(AttUtil.TAG_STUDENTBEAN, studentBean));
            finish();
        } else if (requestCode == AttUtil.REQ_CODE) {
            Toast.makeText(this, "Capture Failed", Toast.LENGTH_LONG).show();
        }else if(requestCode==200){
            setResult(201, new Intent().putExtra(AttUtil.TAG_STUDENTBEAN, studentBeanDetails));
            Toast.makeText(this, "Fees Stucture allocation failed.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}

