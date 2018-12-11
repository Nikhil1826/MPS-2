package com.auribisesmyplayschool.myplayschool.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BranCourBean;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.UserBean;
import com.auribisesmyplayschool.myplayschool.bean.EnquiryBean;
import com.auribisesmyplayschool.myplayschool.bean.SignInBean;
import com.auribisesmyplayschool.myplayschool.bean.StudentBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

public class AddEnquiryActivity extends AppCompatActivity {
    EditText txtEnqFName, txtEnqDOB, edtFatherName, edtFatherQualification, edtFatherOccupation, edtFatherCompanyName,
            edtFatherOfficeAddess, edtFatherEmail, edtFatherPhone, edtMonthlyIncome, edtAddress,
            txtEnqDescription, txtEnqNextVisitDate, edtRecommend;
    //txtEnqCurrentDate
    RadioButton radioEnqMale, radioEnqFemale;
    Spinner spncourse, spnEnquiryCounsellor;
    CheckBox cbNews, cbFriend, cbReport, cbPoster, cbPersonal, cbCall;
    Button btnEquirySubmit;
    EnquiryBean enquiryBean;
    Calendar calendar;
    int c_year, c_month, c_day, hour, mint, reqCode = 0, spnCoursePos = 0, spnEnquiryCounsellorPos = 0, branCourId = 0;
    String cMonthString, cDayString, genderChoice = "", currentDate = "", currentTime = "", reference = "", nextVisit = "";
    DatePickerDialog datePickerDialog;
    //String datefor,timefor;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ArrayList<BranCourBean> courseBeanArrayList;
    ArrayList<UserBean> counsellorBeanArrayList;
    ArrayList<String> stringCourseArrayList, counsellorNAmeArrayList;
    FirebaseFirestore db;
    //EnquiryBean enquiryBean;
    StudentBean studentBean;
    SignInBean signInBean;
    int studentId,size;
    ArrayList<Integer> listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_enquiry);
       // Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Application Form");
        AttUtil .progressDialog(this);
        //toolbar.setTitle("Application Form");
        db = FirebaseFirestore.getInstance();
        initview();
        initList();
    }

    void initList(){
        Intent i = getIntent();
        signInBean = (SignInBean) i.getSerializableExtra(AttUtil.signInBean);
        stringCourseArrayList = new ArrayList<>();
        courseBeanArrayList = (ArrayList<BranCourBean>) i.getSerializableExtra(AttUtil.KEY_COURSE_ARRAYLIST);
        if (courseBeanArrayList.size() > 0) {
            stringCourseArrayList.add("--Select Class--");
            for (int x = 0; x < courseBeanArrayList.size(); x++) {
                stringCourseArrayList.add(courseBeanArrayList.get(x).getCourseName());
            }
        } else
            stringCourseArrayList.add("--No Class Found--");
        spncourse.setAdapter(new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, stringCourseArrayList));

        counsellorNAmeArrayList = new ArrayList<>();
        counsellorBeanArrayList = (ArrayList<UserBean>) i.getSerializableExtra(AttUtil.KEY_COUNSELLOR_ARRAYLIST);
        Log.i("test", counsellorBeanArrayList.toString());
        if (counsellorBeanArrayList.size() > 0) {
            counsellorNAmeArrayList.add("--Select Counsellor--");
            for (int x = 0; x < counsellorBeanArrayList.size(); x++) {
                counsellorNAmeArrayList.add(counsellorBeanArrayList.get(x).getUserName());
            }
        } else
            counsellorNAmeArrayList.add("--No Counsellor Found--");

        if (pref.getInt(AttUtil.shpLoginType, 0) == 2) {
            spnEnquiryCounsellor.setAdapter(new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, counsellorNAmeArrayList));
        }
        if (pref.getInt(AttUtil.shpLoginType, 0) == 4) {
            spnEnquiryCounsellor.setVisibility(View.GONE);
        }
    }

    void initview() {
        listId = new ArrayList<>();
        txtEnqFName = findViewById(R.id.txtEnqFName);
        txtEnqDOB = findViewById(R.id.txtEnqDOB);
        edtFatherName = findViewById(R.id.edtFatherName);
        edtFatherQualification = findViewById(R.id.edtFatherQualification);
        edtFatherOccupation = findViewById(R.id.edtFatherOccupation);
        edtFatherCompanyName = findViewById(R.id.edtFatherCompanyName);
        edtFatherOfficeAddess = findViewById(R.id.edtFatherOfficeAddess);
        edtFatherEmail = findViewById(R.id.edtFatherEmail);
        edtFatherPhone = findViewById(R.id.edtFatherPhone);
        edtMonthlyIncome = findViewById(R.id.edtMonthlyIncome);
        edtAddress = findViewById(R.id.edtAddress);
        enquiryBean = new EnquiryBean();
        studentBean = new StudentBean();
        //txtEnqCurrentDate=findViewById(R.id.txtEnqCurrentDate);
        txtEnqDescription = findViewById(R.id.txtEnqDescription);
        txtEnqNextVisitDate = findViewById(R.id.txtEnqNextVisitDate);
        edtRecommend = findViewById(R.id.edtRecommend);
        courseBeanArrayList = new ArrayList<>();
        counsellorBeanArrayList = new ArrayList<>();
        pref = getSharedPreferences(AttUtil.shpREG, MODE_PRIVATE);
        editor = pref.edit();
        calendar = Calendar.getInstance();
        c_year = calendar.get(Calendar.YEAR);
        c_month = calendar.get(Calendar.MONTH);
        c_day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        mint = calendar.get(Calendar.MINUTE);

        //enquiryBean = new EnquiryBean();

        spncourse = findViewById(R.id.spnEnquiryCourse);
        spnEnquiryCounsellor = findViewById(R.id.spnEnquiryCounsellor);
        radioEnqMale = findViewById(R.id.radioEnqMale);
        radioEnqFemale = findViewById(R.id.radioEnqFemale);
        cbCall = findViewById(R.id.cbEnqCall);
        cbFriend = findViewById(R.id.cbEnqFriend);
        cbNews = findViewById(R.id.cbEnqnews);
        cbPersonal = findViewById(R.id.cbEnqPersonal);
        cbPoster = findViewById(R.id.cbEnqPoster);
        cbReport = findViewById(R.id.cbEnqReport);
        btnEquirySubmit = findViewById(R.id.btnEquirySubmit);
        btnEquirySubmit.setOnClickListener(onClickListener);
        radioEnqMale.setOnClickListener(onClickListener);
        radioEnqFemale.setOnClickListener(onClickListener);
        datePickerDialog = new DatePickerDialog(this, onDateSetListener, c_year, c_month, c_day);
        txtEnqNextVisitDate.setOnClickListener(onClickListener);
        txtEnqDOB.setOnClickListener(onClickListener);
        //txtEnqCurrentDate.setOnClickListener(onClickListener);
        spncourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spnCoursePos = i;
                if (i > 0) {
                    if (courseBeanArrayList.size() > 0)
                        //enquiryBean.setBranCourId(courseBeanArrayList.get(i - 1).getBranCourId());
                        branCourId = courseBeanArrayList.get(i - 1).getBranCourId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnEnquiryCounsellor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spnEnquiryCounsellorPos = i;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.txtEnqDOB) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEnquiryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = year + "-" + (++monthOfYear) + "-" + dayOfMonth;
                        txtEnqDOB.setText(date);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
// if(v.getId()==R.id.txtEnqCurrentDate){
//                Calendar calendar=Calendar.getInstance();
//                DatePickerDialog datePickerDialog=new DatePickerDialog(AddEnquiryActivity.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        String date=year+"-"+(++monthOfYear)+"-"+dayOfMonth;
//                        txtEnqCurrentDate.setText(date);
//                    }
//                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//                datePickerDialog.show();
//            }
            if (v.getId() == R.id.txtEnqNextVisitDate) {
                datePickerDialog.show();
            }
            if (v.getId() == R.id.radioEnqMale) {
                //enquiryBean.setGender("Male");
                genderChoice = "Male";
            }
            if (v.getId() == R.id.radioEnqFemale) {
                //enquiryBean.setGender("Female");
                genderChoice = "Female";
            }
            if (v.getId() == R.id.btnEquirySubmit) {
//                if(txtEnqCurrentDate.getText().toString().length()==0)
                getDate(c_year, c_month, c_day);
//                else{
//                    //datefor=txtEnqCurrentDate.getText().toString().trim();
//                    //enquiryBean.setDate(txtEnqCurrentDate.getText().toString().trim());
//                    currentDate=txtEnqCurrentDate.getText().toString().trim();
//                }
                if (txtEnqNextVisitDate.getText().toString().length() == 0) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());
                    c.add(Calendar.DAY_OF_MONTH, pref.getInt(AttUtil.shpDefaultEnquiryFollowUp, 1));
                    nextVisit = new SimpleDateFormat("yyyy-MM--dd").format(c.getTime());
                } else {
                    nextVisit = txtEnqNextVisitDate.getText().toString().trim();
                }
                getTime(hour, mint);
                String ref = "";
                if (cbPoster.isChecked()) {
                    reference = reference + cbPoster.getText().toString().trim();
                }
                if (cbReport.isChecked()) {
                    if (ref.length() > 0) {
                        ref = ref + ", ";
                    }
                    ref = ref + cbReport.getText().toString().trim();
                }
                if (cbFriend.isChecked()) {
                    if (ref.length() > 0) {
                        ref = ref + ", ";
                    }
                    ref = ref + cbFriend.getText().toString().trim();
                }
                if (cbCall.isChecked()) {
                    if (ref.length() > 0) {
                        ref = ref + ", ";
                    }
                    ref = ref + cbCall.getText().toString().trim();
                }
                if (cbNews.isChecked()) {
                    if (ref.length() > 0) {
                        ref = ref + ", ";
                    }
                    ref = ref + cbNews.getText().toString().trim();
                }
                if (cbPersonal.isChecked()) {
                    if (ref.length() > 0) {
                        ref = ref + ", ";
                    }
                    ref = ref + cbPersonal.getText().toString().trim();
                }
                //enquiryBean.setRefre(ref);
                reference = ref;

                if (validate()) {
                    if (AttUtil.isNetworkConnected(AddEnquiryActivity.this)){
                        AttUtil.pd(1);
                        getEnquiryIdFromDB();
                        //setIntoEnquiryBean();
                        //insertIntoDb();
                    }

                   // else
                        //insertNetConnect();
                }
            }
        }
    };

    public void setIntoEnquiryBean() {
        studentBean.setDob(txtEnqDOB.getText().toString().trim());
        studentBean.setFatherName(edtFatherName.getText().toString().trim());
        studentBean.setMotherName("");
        studentBean.setmOfficeAddress("");
        studentBean.setmCompanyName("");
        studentBean.setmOccupation("");
        studentBean.setmQualification("");
        studentBean.setMotherPhone("");
        studentBean.setMotherEmail("");

        if(spnEnquiryCounsellorPos>0){
            enquiryBean.setUserType(2);
        }else {
            enquiryBean.setUserType(0);
        }
        studentBean.setfQualification(edtFatherQualification.getText().toString().trim());
        studentBean.setfOccupation(edtFatherOccupation.getText().toString().trim());
        studentBean.setFatherPhone(edtFatherPhone.getText().toString().trim());
        studentBean.setFatherEmail(edtFatherEmail.getText().toString().trim());
        studentBean.setfCompanyName(edtFatherCompanyName.getText().toString().trim());
        studentBean.setfOfficeAddress(edtFatherOfficeAddess.getText().toString().trim());
        studentBean.setMonthlyIncome(edtMonthlyIncome.getText().toString().trim());
        studentBean.setAddress(edtAddress.getText().toString().trim());
       // studentBean.setEnqStatus(0);
        studentBean.setStuName(txtEnqFName.getText().toString().trim());
        studentBean.setGender(genderChoice);

        studentBean.setRecommendSomeone(edtRecommend.getText().toString().trim());
        //studentBean.setBranCourId(courseBeanArrayList.get(spnCoursePos-1).getBranCourId());
        //studentBean.setEnqphone(edtFatherPhone.getText().toString().trim());
        if (spnEnquiryCounsellorPos > 0)
            enquiryBean.setUserId(counsellorBeanArrayList.get(spnEnquiryCounsellorPos - 1).getUserId());
        else
            enquiryBean.setUserId(0);
        enquiryBean.setCourseName(courseBeanArrayList.get(spnCoursePos-1).getCourseName());

        if(spnEnquiryCounsellorPos>0){
            enquiryBean.setUserName(counsellorBeanArrayList.get(spnEnquiryCounsellorPos-1).getUserName());
        }else{
            enquiryBean.setUserName("");
        }

        //studentBean.setEnqDescription(txtEnqDescription.getText().toString().trim());
        enquiryBean.setEnqdesc(txtEnqDescription.getText().toString().trim());
       // studentBean.setNextVisit(nextVisit);
        enquiryBean.setNextVisit(nextVisit);
        enquiryBean.setEnqStatus(0);
        enquiryBean.setEnqid(studentId+1);
        studentBean.setCurDate(currentDate);
        studentBean.setCurTime(currentTime);
        //studentBean.setDate("");

        if (cbPersonal.isChecked()) {
            if (reference.length() > 0) {
                reference = reference + ", ";
            }
            reference = reference + cbPersonal.getText().toString().trim();
        }
        studentBean.setReference(reference);
        //enquiryBean.setSource(reference);
        studentBean.setBranchId(signInBean.getBranchId());
        studentBean.setBranchName(signInBean.getBranchName());
        /*if(studentBean.getGender()=="Male"){
            studentBean.setNameprefix("Mr");
        }else{
            studentBean.setNameprefix("Miss");
        }*/
       // studentBean.setEnqId(studentId+1);
        studentBean.setEnquiryBean(enquiryBean);
        studentBean.setStudentId(studentBean.getEnquiryBean().getEnqid());
        insertIntoDb();
       /*if (pref.getInt(AttUtil.shpLoginType, 0) == 4)
            jsonObject.put("userId", new Gson().fromJson(pref.getString(AttUtil.shpCounsellorBean, ""), SignInBean.class).getUserId());*/
    }

    public void getEnquiryIdFromDB(){
      /*  db.collection(Constants.enquiry_application_form_collection).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    if(queryDocumentSnapshots.size()!=0){
                        int chkId = doc.getLong("enqId").intValue();
                        //enquiryId = doc.getLong("enqId").intValue();
                        listId.add(chkId);
                    }else{
                        enquiryId = 0;
                    }
                }

                if(listId.size()>0){
                    enquiryId = Collections.max(listId);
                }else{
                    enquiryId = 0;
                }


                setIntoEnquiryBean();
            }
        });*/

        db.collection(Constants.student_collection).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                            if(queryDocumentSnapshots.size()!=0){
                                int chkId = doc.getLong("studentId").intValue();
                                //enquiryId = doc.getLong("enqId").intValue();
                                listId.add(chkId);
                            }else{
                                studentId = 0;
                            }
                        }

                        if(listId.size()>0){
                            studentId = Collections.max(listId);
                        }else{
                            studentId = 0;
                        }


                        setIntoEnquiryBean();
                    }
                });
    }

    public void insertIntoDb() {
        db.collection(Constants.student_collection)
                .document(String.valueOf(studentBean.getStudentId())).set(studentBean)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AttUtil.pd(0);
                clearfields();
                listId.clear();
                Toast.makeText(AddEnquiryActivity.this, "Application submitted", Toast.LENGTH_SHORT).show();
            }
        });

    }

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
            txtEnqNextVisitDate.setText(year + "-" + cMonthString + "-" + cDayString);
            //nextVisit=year+"-"+cMonthString+"-"+cDayString;
        }
    };

    void getDate(int year, int monthOfYear, int dayOfMonth) {
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
        //datefor = year+"-"+cMonthString+"-"+cDayString;
        //enquiryBean.setDate(year+"-"+cMonthString+"-"+cDayString);
        currentDate = year + "-" + cMonthString + "-" + cDayString;
    }

    void getTime(int hourOfDay, int minute) {
        //timefor = hourOfDay+":"+minute+":00";
        currentTime = hourOfDay + ":" + minute + ":00";
        String timeSet = "";
        if (hourOfDay > 12) {
            hourOfDay -= 12;
            timeSet = "PM";
        } else if (hourOfDay == 0) {
            hourOfDay += 12;
            timeSet = "AM";
        } else if (hourOfDay == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (minute < 10) {
            minutes = "0" + minute;
        } else {
            minutes = String.valueOf(minute);
        }
        //enquiryBean.setTime(hourOfDay + ":" + minutes +" "+timeSet);
    }

    boolean validate() {
        boolean check = true;
        String msg = "Error: ";
        Pattern pattern;
        Matcher matcher;
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (txtEnqFName.getText().toString().length() == 0) {
            check = false;
            msg = msg + "\nInvalid Child's Name";
        }
        if (txtEnqDOB.getText().toString().isEmpty()) {
            check = false;
            msg = msg + "\nSelect D.O.B.";
        }
        if (genderChoice.toString().length() == 0) {
            check = false;
            msg = msg + "\nSelect Gender";
        }
        if (edtFatherName.getText().toString().length() == 0) {
            check = false;
            msg = msg + "\nInvalid Father's Name";
        }
        if (edtFatherQualification.getText().toString().length() == 0) {
            check = false;
            msg = msg + "\nInvalid Father's Qualification";
        }
//        }if(edtFatherOccupation.getText().toString().length()==0){
//            check = false;
//            msg=msg+"\nInvalid Father's occupation";
//        }if(edtFatherCompanyName.getText().toString().length()==0){
//            check = false;
//            msg=msg+"\nInvalid Father's Company Name";
//        }if(edtFatherOfficeAddess.getText().toString().length()==0){
//            check = false;
//            msg=msg+"\nInvalid Father's Office Address";
//        }if(edtFatherCompanyName.getText().toString().length()==0){
//            check = false;
//            msg=msg+"\nInvalid Father's Company Name";
//        }
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(edtFatherEmail.getText().toString().trim());
        if (!matcher.matches()) {
            check = false;
            msg = msg + "\nInvalid Father's E-mail ";
        }
        if (edtFatherPhone.getText().toString().length() <= 0) {
            check = false;
            msg = msg + "\nInvalid Father's Phone Number";
        }
        if (edtMonthlyIncome.getText().toString().length() == 0) {
            check = false;
            msg = msg + "\nInvalid Family's Monthly Income";
        }
        if (edtAddress.getText().toString().length() == 0) {
            check = false;
            msg = msg + "\nInvalid Residential Address";
        }
        if (spnCoursePos == 0) {
            check = false;
            msg = msg + "\nSelect Class";
        }
        if (reference.length() == 0) {
            check = false;
            msg = msg + "\nSelect aleast one reference";
        }

        if (!check) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
        return check;
    }

    void clearfields() {
        txtEnqFName.setText("");
        txtEnqDOB.setText("");
        edtFatherName.setText("");
        edtFatherQualification.setText("");
        edtFatherOccupation.setText("");
        edtFatherCompanyName.setText("");
        edtFatherOfficeAddess.setText("");
        edtFatherEmail.setText("");
        edtFatherPhone.setText("");
        edtMonthlyIncome.setText("");
        edtAddress.setText("");
//        txtEnqCurrentDate.setText("");
        txtEnqDescription.setText("");
        txtEnqNextVisitDate.setText("");
        edtRecommend.setText("");
        spncourse.setSelection(0);
        spnEnquiryCounsellor.setSelection(0);
        cbPersonal.setChecked(false);
        cbPoster.setChecked(false);
        cbNews.setChecked(false);
        cbCall.setChecked(false);
        cbFriend.setChecked(false);
        cbReport.setChecked(false);
        radioEnqMale.setChecked(false);
        radioEnqFemale.setChecked(false);
        reqCode = 0;
        spnCoursePos = 0;
        spnEnquiryCounsellorPos = 0;
        branCourId = 0;
        genderChoice = "";
        currentDate = "";
        currentTime = "";
        reference = "";
        nextVisit = "";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }
}
