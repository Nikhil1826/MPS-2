package com.auribisesmyplayschool.myplayschool.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adapter.MarkAttendanceAdapter;
import com.auribisesmyplayschool.myplayschool.bean.BatchBean;
import com.auribisesmyplayschool.myplayschool.bean.SignInBean;
import com.auribisesmyplayschool.myplayschool.bean.StuAttendanceBean;
import com.auribisesmyplayschool.myplayschool.bean.StudentAttendanceExtractBean;
import com.auribisesmyplayschool.myplayschool.bean.StudentBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.auribisesmyplayschool.myplayschool.classes.MarkAttendanceInterface;
import com.auribisesmyplayschool.myplayschool.classes.RecyclerItemClickListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class StudentAttendanceActivity extends AppCompatActivity implements MarkAttendanceInterface {
    private BatchBean batchBean;
    private int actionChoser = 0, reqCode = 0, studentPresent = 0,
            studentAbsent = 0, responseSignal = 0, discardPrevious = 0,
            dateSignal = 0,cyear = 0, cmonth = 0, cday = 0;
    private SharedPreferences preferences;
    private ArrayList<StudentBean> studentBeanArrayList;
    private MarkAttendanceAdapter markAttendanceAdapter;
    private RecyclerView rvMAStudents;
    private TextView tvMATotal, tvMAPresent, tvMAAbsent, tvMATotalTitle, tvMAPresentTitle, tvMAAbsentTitle,
            tvMABatchName;
    private String studentAttendance = "", noStudent = "No student is linked to this section. Choose another section.",
            noAttendance = "No previous attendance of section. Choose another section.";
    private ArrayList<StuAttendanceBean> stuAttendanceBeanArrayList;
    private ArrayList<StudentAttendanceExtractBean> studentAttendanceExtractBeanArrayList;
    private EditText edtdateto, edtdatefrom;
    private DatePickerDialog datePickerDialog, datePickerDialog1;
    private Calendar calendar;
    private DateFormat format;
    private Date dateto = null, datefrom = null, datecurrent, d;
    ArrayList<BatchBean> bbArrayList;
    FirebaseFirestore db;
    StudentBean studentBean;
    StuAttendanceBean stuAttendanceBean;
    ArrayList<Integer> listId;
    int stuAtdId;
    SignInBean signInBean;
    StuAttendanceBean rcvBean;
    ArrayList<StuAttendanceBean> stuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Attendance");
        inItViews();
        showBatchChooser();
    }

    void inItViews() {
        preferences = getSharedPreferences(AttUtil.shpREG, MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();
        studentBean = new StudentBean();
        listId = new ArrayList<>();
        rcvBean = new StuAttendanceBean();
        stuList = new ArrayList<>();
        if(preferences.getInt(AttUtil.shpLoginType,0)==2){
            bbArrayList = (ArrayList<BatchBean>) getIntent().getSerializableExtra(AttUtil.BATCH_LIST);
            signInBean = (SignInBean) getIntent().getSerializableExtra("userBean");
        }
        calendar = Calendar.getInstance();
        cyear = calendar.get(Calendar.YEAR);
        cmonth = calendar.get(Calendar.MONTH);
        cday = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(StudentAttendanceActivity.this, onDateSetListener, cyear, cmonth, cday);
        datePickerDialog1 = new DatePickerDialog(StudentAttendanceActivity.this, onDateSetListener, cyear, cmonth, cday);
        studentAttendanceExtractBeanArrayList = new ArrayList<>();
        stuAttendanceBeanArrayList = new ArrayList<>();
        stuAttendanceBean = new StuAttendanceBean();
        tvMATotal = findViewById(R.id.tvMATotal);
        tvMAPresent = findViewById(R.id.tvMAPresent);
        tvMAAbsent = findViewById(R.id.tvMAAbsent);

        tvMATotalTitle = findViewById(R.id.tvMATotalTitle);
        tvMAPresentTitle = findViewById(R.id.tvMAPresentTitle);
        tvMAAbsentTitle = findViewById(R.id.tvMAAbsentTitle);
        tvMABatchName = findViewById(R.id.tvMABatchName);

        rvMAStudents = findViewById(R.id.rvMAStudents);
        try {
            rvMAStudents.setLayoutManager(new LinearLayoutManager(StudentAttendanceActivity.this, LinearLayoutManager.VERTICAL, false));
            rvMAStudents.addOnItemTouchListener(new RecyclerItemClickListener(StudentAttendanceActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                }
            }));

        } catch (Exception e) {
            e.printStackTrace();
        }
        studentBeanArrayList = new ArrayList<>();
        batchBean = new BatchBean();
    }

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            format = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
            monthOfYear = monthOfYear + 1;
            String cDayString, cMonthString;
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
            if (dateSignal == 1) {
                edtdateto.setText(year + "-" + cMonthString + "-" + cDayString);
                try {
                    dateto = format.parse(edtdateto.getText().toString().trim());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (dateSignal == 2) {
                edtdatefrom.setText(year + "-" + cMonthString + "-" + cDayString);
                try {
                    datefrom = format.parse(String.valueOf(edtdatefrom.getText().toString().trim()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    @Override
    public void markAttendance(boolean flag, int position) {
        if (actionChoser == 0) {
            if (flag) {
                studentPresent = studentPresent + 1;
                studentAbsent = studentAbsent - 1;
            } else {
                studentPresent = studentPresent - 1;
                studentAbsent = studentAbsent + 1;
            }
            tvMAPresentTitle.setText("Present  ");
            tvMAPresent.setText("" + studentPresent);
            tvMAAbsentTitle.setText("Absent  ");
            tvMAAbsent.setText("" + studentAbsent);
            studentBeanArrayList.get(position).setMarkAttendance(flag);
        } else if (actionChoser == 1) {
            if (flag) {
                studentPresent = studentPresent + 1;
                studentAbsent = studentAbsent - 1;
            } else {
                studentPresent = studentPresent - 1;
                studentAbsent = studentAbsent + 1;
            }
            tvMAPresentTitle.setText("Selected  ");
            tvMAPresent.setText("" + studentPresent);
            tvMAAbsentTitle.setText("Deselected  ");
            tvMAAbsent.setText("" + studentAbsent);
            stuAttendanceBeanArrayList.get(position).setMarked(flag);
        }
    }

    void showBatchChooser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Section");
        /*if(preferences.getInt(AttUtil.shpLoginType,0)==2)
            bbArrayList.addAll(AttUtil.batchBeanArrayListActive);
        else if(preferences.getInt(AttUtil.shpLoginType,0)==3)
            bbArrayList.addAll(AttUtil.batchBeanArrayListTeacher);*/

        String[] options = new String[bbArrayList.size()];
        for (int i = 0; i < bbArrayList.size(); i++) {
            options[i] = bbArrayList.get(i).getBatch_title();
        }
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                batchBean = bbArrayList.get(which);
                tvMABatchName.setText("" + batchBean.getBatch_title() + "");
                showActionOptions();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    void showActionOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        String[] options = {"Mark Attendance", "View Previous Attendance"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                actionChoser = which;
//                invalidateOptionsMenu();
                if (actionChoser == 0){
                    AttUtil.progressDialog(StudentAttendanceActivity.this);
                    AttUtil.pd(1);
                    retrieveStudents();
                } else
                    attendancefilter();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

         void attendancefilter() {
             d = new Date();
             format = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
             try {
                 datecurrent = format.parse(new SimpleDateFormat("yyyy-MM-dd").format(d));
             } catch (ParseException e) {
                 e.printStackTrace();
             }
             android.support.v7.app.AlertDialog.Builder dialogBuilder =
                     new android.support.v7.app.AlertDialog.Builder(StudentAttendanceActivity.this);
             dialogBuilder.setTitle("Choose Date Range");
             LayoutInflater inflater = StudentAttendanceActivity.this.getLayoutInflater();
             View dialogView = inflater.inflate(R.layout.custome_date_layout, null);
             dialogBuilder.setView(dialogView);
             edtdateto = (EditText) dialogView.findViewById(R.id.tv_date_to);
             edtdateto.setText(new SimpleDateFormat("yyyy-MM-dd").format(d));
             try {
                 dateto = format.parse(edtdateto.getText().toString().trim());
             } catch (ParseException e) {
                 e.printStackTrace();
             }

             edtdatefrom = (EditText) dialogView.findViewById(R.id.tv_date_from);
             edtdatefrom.setText(new SimpleDateFormat("yyyy-MM-dd").format(d));
             //final DateFormat format = new SimpleDateFormat("yyyy-mm-dd",Locale.getDefault());
             edtdateto.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     dateSignal = 1;
                     datePickerDialog.show();

                 }
             });
             edtdatefrom.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     dateSignal = 2;
                     datePickerDialog.show();
                 }
             });

             dialogBuilder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     AttUtil.progressDialog(StudentAttendanceActivity.this);
                     AttUtil.pd(1);
                     retrieveAttendance();
                 }
             });
             dialogBuilder.setNegativeButton("Cancel", null);
             android.support.v7.app.AlertDialog alertDialog = dialogBuilder.create();
             alertDialog.setCancelable(false);
             alertDialog.show();
         }

     void retrieveAttendance() {
        final ArrayList<StuAttendanceBean> tempAtdList = new ArrayList<>();
        stuAttendanceBeanArrayList.clear();
        rcvBean = new StuAttendanceBean();
        db.collection(Constants.student_attendance_collection).whereEqualTo("batchId",batchBean.getBatchId())
                .whereEqualTo("batch_title",batchBean.getBatch_title()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    rcvBean = doc.toObject(StuAttendanceBean.class);
                    tempAtdList.add(rcvBean);
                }

                //ArrayList<Date> datechklist = new ArrayList<>();
                /*for(int i=0;i<tempAtdList.size();i++){
                    try {
                        if(format.parse(tempAtdList.get(i).getDate()).after(datefrom)  && format.parse(tempAtdList.get(i).getDate()).before(dateto)){
                            stuAttendanceBeanArrayList.add(tempAtdList.get(i));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (stuAttendanceBeanArrayList.size() > 0 && stuAttendanceBeanArrayList != null) {
//                        findViewById(R.id.lMAStats).setVisibility(View.VISIBLE);
//                        findViewById(R.id.vMAStats).setVisibility(View.VISIBLE);
                   setAtdRetrieveAdapter();
                } else{
                    AttUtil.pd(0);
                    noStudentFoundDialog(noAttendance+"("+")");
                }*/
               // Toast.makeText(StudentAttendanceActivity.this,"CD "+new SimpleDateFormat("yyyy-MM-dd").format(tempAtdList.get(0).getDate()),Toast.LENGTH_SHORT).show();
                Date df = null, dt = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    df = sdf.parse(edtdatefrom.getText().toString().trim());
                    dt = sdf.parse(edtdateto.getText().toString().trim());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                for(int i=0;i<tempAtdList.size();i++){
                    try {
                        if(sdf.parse(tempAtdList.get(i).getDate()).compareTo(df) >= 0){
                            if(sdf.parse(tempAtdList.get(i).getDate()).compareTo(dt) <= 0){
                                stuAttendanceBeanArrayList.add(tempAtdList.get(i));
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if(stuAttendanceBeanArrayList.size()>0){
                    setAtdRetrieveAdapter();
                }else{
                    noStudentFoundDialog(noAttendance+"("+")");
                }
            }
        });
    }

     void setAtdRetrieveAdapter() {
         markAttendanceAdapter = new MarkAttendanceAdapter(stuAttendanceBeanArrayList, this, actionChoser, this);
         rvMAStudents.setAdapter(markAttendanceAdapter);
         tvMATotal.setText("" + stuAttendanceBeanArrayList.size());
         studentPresent = stuAttendanceBeanArrayList.size();
         studentAbsent = 0;
         tvMAPresentTitle.setText("Selected  ");
         tvMAPresent.setText("" + studentPresent);
         tvMAAbsentTitle.setText("Deselected  ");
         tvMAAbsent.setText("" + studentAbsent);
         AttUtil.pd(0);
    }


    void retrieveStudents() {
        studentBeanArrayList.clear();
        db.collection(Constants.student_collection).whereEqualTo("batch_title",batchBean.getBatch_title())
                .whereEqualTo("batchId",batchBean.getBatchId()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    studentBean = doc.toObject(StudentBean.class);
                    studentBeanArrayList.add(studentBean);
                }

                if (studentBeanArrayList.size() > 0 && studentBeanArrayList != null) {
                    tvMATotal.setText("" + studentBeanArrayList.size());
                    studentPresent = studentBeanArrayList.size();
                    studentAbsent = 0;

                    tvMAPresentTitle.setText("Present  ");
                    tvMAPresent.setText("" + studentPresent);
                    tvMAAbsentTitle.setText("Absent  ");
                    tvMAAbsent.setText("" + studentAbsent);
                    setAdapterMethod();
                } else
                    noStudentFoundDialog(noStudent);
                AttUtil.pd(0);
            }
        });
    }

     void setAdapterMethod() {
         markAttendanceAdapter = new MarkAttendanceAdapter(studentBeanArrayList, this,
                 this, actionChoser);
         rvMAStudents.setAdapter(markAttendanceAdapter);

    }

    void noStudentFoundDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage(msg);
        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showBatchChooser();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if(actionChoser==0){
        menu.add(0, 0, 0, "Switch Section").setIcon(R.drawable.ic_circles_switch).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, 1, 0, "Send").setIcon(R.drawable.ic_send).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        }else if(actionChoser==1){
//            menu.add(0,0,0,"Export").setIcon(R.drawable.ic_send).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        if (item.getItemId() == 0) {
            showBatchChooser();
        }
        if (item.getItemId() == 1 && actionChoser == 1) {
            if (studentPresent != 0){
                dumpInput();
            }
            else
                Toast.makeText(this, "No record selected", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == 1 && actionChoser == 0) {
//            if (studentPresent != 0)
            //markAttendance();
            //setDataToBean();
            AttUtil.progressDialog(StudentAttendanceActivity.this);
            AttUtil.pd(1);
            checkPrevExists();
           // getLastStuAtdId();
//            else
//                Toast.makeText(this, "No student selected", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean dumpInput() {
        AttUtil.progressDialog(StudentAttendanceActivity.this);
        AttUtil.pd(1);
        String strFileName ="" +
                batchBean.getBatch_title() + " Attendance Summery " +
                new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) +
                ").xls";
        strFileName.replace("/","-");
        strFileName.replace(":","-");

        File root = null;
        try {
            root = Environment.getExternalStorageDirectory();
            if (root.canWrite()) {
                File fileDir1 = new File(root.getAbsolutePath() + "/" + getResources().getString(R.string.app_name) + "/" +
                        getResources().getString(R.string.manager_attendance));
                if (!fileDir1.exists())
                    fileDir1.mkdirs();


                File fWriter = new File(fileDir1, strFileName);
                WorkbookSettings wbSettings = new WorkbookSettings();
                wbSettings.setLocale(new Locale("en", "EN"));
                WritableWorkbook workbook = Workbook.createWorkbook(fWriter, wbSettings);
                WritableSheet sheet = null;
                for (int x = 0; x < stuAttendanceBeanArrayList.size(); x++) {
                    String status = "Discarded";
                    if (stuAttendanceBeanArrayList.get(x).getStatus() == 1)
                        status = "";
                    else
                        status = "Discarded";
                    sheet = workbook.createSheet("Attendance " +
                            stuAttendanceBeanArrayList.get(x).getDate() + " " + status, x);

                    sheet.addCell(new Label(0, 0, "S.No."));
                    sheet.addCell(new Label(1, 0, "Student Name"));
                    sheet.addCell(new Label(2, 0, "Teacher Name"));
                    sheet.addCell(new Label(3, 0, "Attendance"));

                    int rowIndex = 1;
                    int cellIndex = 0;

                    String[] words = stuAttendanceBeanArrayList.get(x).getAttendance().split(",");
                    for (int y = 0; y < words.length; y++) {
                        String[] words2 = words[y].split("-");
//                        studentAttendanceExtractBeanArrayList.add(
//                                new StudentAttendanceExtractBean(words2[0],words2[1],words2[2]));
                        sheet.addCell(new Label(cellIndex, rowIndex, (y+1) + ""));
                        sheet.addCell(new Label(cellIndex + 1, rowIndex, words2[0] + ""));
                        sheet.addCell(new Label(cellIndex + 2, rowIndex, stuAttendanceBeanArrayList.get(x).getUserName()));
                        if (words2[2].toString().equals("1"))
                            sheet.addCell(new Label(cellIndex + 3, rowIndex, "Present"));
                        else
                            sheet.addCell(new Label(cellIndex + 3, rowIndex, "Absent"));
                        rowIndex++;
                    }

                }
                if (workbook != null) {
                    workbook.write();
                    workbook.close();
                    Uri path = Uri.fromFile(new File(fileDir1, strFileName));
                    Toast.makeText(StudentAttendanceActivity.this, "Excel file is generated. Please check your sdcard!", Toast.LENGTH_SHORT).show();
                    AttUtil.pd(0);
                    return true;
                } else {
                    //Toast.makeText(StudentAttendanceActivity.this, AttUtil.Error_message, Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } catch (Exception e) {
            AttUtil.pd(0);
            e.printStackTrace();
           // Toast.makeText(StudentAttendanceActivity.this, AttUtil.Error_message, Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    void checkPrevExists() {
        db.collection(Constants.student_attendance_collection).whereEqualTo("batchId",batchBean.getBatchId())
                .whereEqualTo("batch_title",batchBean.getBatch_title())
                .whereEqualTo("date",new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    if(!queryDocumentSnapshots.isEmpty())
                    rcvBean = doc.toObject(StuAttendanceBean.class);
                    stuList.add(rcvBean);
                }

                if(stuList.size()>0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(StudentAttendanceActivity.this);
                    builder.setTitle("Warning");
                    builder.setMessage(batchBean.getBatch_title() + "\'s today's attendance has already submitted. Do you want to discard previous record and submit new one?");
                    builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            discardPrevious = 1;
                            // markAttendance();
                            //getLastStuAtdId();
                            updateStausPrev();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            discardPrevious = 0;
                            AttUtil.pd(0);
                        }
                    });
                    builder.setCancelable(false);
                    builder.create().show();
                }else{
                    getLastStuAtdId();
                }


            }
        });
    }

     void updateStausPrev() {
        rcvBean.setStatus(0);
        db.collection(Constants.student_attendance_collection).document(String.valueOf(rcvBean.getStuAttId()))
                .update("status",rcvBean.getStatus()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                getLastStuAtdId();
            }
        });
    }

    void getLastStuAtdId() {
        db.collection(Constants.student_attendance_collection).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    if (!queryDocumentSnapshots.isEmpty()){
                        listId.add(doc.getLong("stuAttId").intValue());
                    }else{
                        stuAtdId = 1;
                    }
                }

                if(listId.size()>0){
                    stuAtdId = Collections.max(listId);
                    stuAtdId = stuAtdId + 1;
                }else{
                    stuAtdId = 1;
                }
                //setDataToBean();
                formatData();

            }
        });
    }

    void markAttendance(){
        db.collection(Constants.student_attendance_collection).document(String.valueOf(stuAttendanceBean.getStuAttId()))
                .set(stuAttendanceBean).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AttUtil.pd(0);
                listId.clear();
                nextBatchAttendanceDialog();
            }
        });
    }

     void setDataToBean() {
        if(discardPrevious != 1){
            stuAttendanceBean.setAttendance(studentAttendance);
            stuAttendanceBean.setBranchId(batchBean.getBranchId());
            stuAttendanceBean.setBatchId(batchBean.getBatchId());
            stuAttendanceBean.setBatch_title(batchBean.getBatch_title());
            stuAttendanceBean.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            stuAttendanceBean.setTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
            stuAttendanceBean.setUserId(signInBean.getUserId());
            stuAttendanceBean.setUserName(signInBean.getUserName());
        }else{
            stuAttendanceBean.setAttendance(studentAttendance);
            stuAttendanceBean.setBranchId(rcvBean.getBranchId());
            stuAttendanceBean.setBatchId(rcvBean.getBatchId());
            stuAttendanceBean.setBatch_title(rcvBean.getBatch_title());
            stuAttendanceBean.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            stuAttendanceBean.setTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
            stuAttendanceBean.setUserId(rcvBean.getUserId());
            stuAttendanceBean.setUserName(rcvBean.getUserName());
        }
         stuAttendanceBean.setStuAttId(stuAtdId);
         stuAttendanceBean.setStatus(1);
         markAttendance();
    }

    void formatData() {
        studentAttendance = "";
        for (int i = 0; i < studentBeanArrayList.size(); i++) {
            if (studentBeanArrayList.get(i).isMarkAttendance()) {
                if (i == 0)
                    studentAttendance = studentBeanArrayList.get(i).getStuName() + "-" + studentBeanArrayList.get(i).getStudentId() + "-" + 1;
                else
                    studentAttendance = studentAttendance + "," + studentBeanArrayList.get(i).getStuName() + "-" + studentBeanArrayList.get(i).getStudentId() + "-" + 1;
            } else if (!studentBeanArrayList.get(i).isMarkAttendance()) {
                if (i == 0)
                    studentAttendance = studentBeanArrayList.get(i).getStuName() + "-" + studentBeanArrayList.get(i).getStudentId() + "-" + 0;
                else
                    studentAttendance = studentAttendance + "," + studentBeanArrayList.get(i).getStuName() + "-" + studentBeanArrayList.get(i).getStudentId() + "-" + 0;
            }
        }
        setDataToBean();
    }

    void nextBatchAttendanceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Success");
        builder.setMessage("Attendance Marked.");
        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showBatchChooser();
            }
        });
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }
}
