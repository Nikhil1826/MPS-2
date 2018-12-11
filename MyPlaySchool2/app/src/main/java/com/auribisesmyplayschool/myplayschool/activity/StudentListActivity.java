package com.auribisesmyplayschool.myplayschool.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adapter.ApproveAdapter;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BranCourBean;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.CourseBean;
import com.auribisesmyplayschool.myplayschool.bean.AdmissionBean;
import com.auribisesmyplayschool.myplayschool.bean.BatchBean;
import com.auribisesmyplayschool.myplayschool.bean.FeeCostBean;
import com.auribisesmyplayschool.myplayschool.bean.StudentBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.auribisesmyplayschool.myplayschool.classes.StudentComparator;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
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

public class StudentListActivity extends AppCompatActivity {

    Intent rcv;
    ListView lv;
    String[] options1;
    //feeStructureViewSignal = 0 resend , 1 view
    int pos, key = 0, reqCode = 0, studentChoose = 1, var4, var5, var6, active = 1, feeStructureViewSignal = 0;
    StudentBean student;
    ArrayList<StudentBean> studentsList = new ArrayList(), activeStudents = new ArrayList<>(), inactiveStudents = new ArrayList<>(),
            displayStudentList, joiningStudentList = new ArrayList<>();
    SharedPreferences pref;
    int posBatch = 0;
    ApproveAdapter studentListAdapter;
    ArrayList<String> spinList;
    ArrayList<Integer> batchIdArray;
    ArrayList<BatchBean> batchList;
    ArrayList<BranCourBean> courseBeanArrayList;
    ArrayList<FeeCostBean> feeCostBeanArrayList;
    FeeCostBean feeCostBean;
    //ArrayList<StudentBean> studentBeanArrayList;
    Spinner spGroup;
    EditText edtClassStartingDate;
    Button btnSelect;
    int lateJoiningFlag = 0, request = 0;
    //private ArrayList<FeeCostBean> feeCostBeanArrayList;
    String stringAStartingDate = "", stringAEndingDate = "", stringMStartingDate = "", stringMEndingDate = "";
    private AlertDialog alertDialogInvoice;
    private TextView tvMStartingDate, tvMEndingDate, tvAStartingDate, tvAEndingDate;
    private String strFileName = "";
    private File fWriter;
    private Uri path;
    FirebaseFirestore db;
    String desc;
    BatchBean batchBean;
    String batchName;
    int otFee,aFee,mFee,tFee,branchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        pref = getSharedPreferences(AttUtil.shpREG, Context.MODE_PRIVATE);
        initViews();
        student = new StudentBean();
        branchId = getIntent().getIntExtra("branchId",0);
        if (rcv.hasExtra(AttUtil.KEY_BATCH_ID)) {
            key = rcv.getIntExtra(AttUtil.KEY_BATCH_ID, 0);
            studentChoose = rcv.getIntExtra("choose", 1);
            lateJoiningFlag = rcv.getIntExtra("lateJoining", 0);
        }

        if(lateJoiningFlag == 1){
            displayStudentList = (ArrayList<StudentBean>) getIntent().getSerializableExtra(AttUtil.KEY_STUDENT_LATE_JOINING_ARRAYLIST);
            setAdapterData();
        }else if(lateJoiningFlag == 2){
            AttUtil.pd(1);
            displayStudentList = new ArrayList<>();
            fetchStudentsList();

        } else{
            AttUtil.pd(1);
            displayStudentList = new ArrayList<>();
            batchName = getIntent().getStringExtra(AttUtil.KEY_BATCH_NAME);
            fetchStudentsList();

            /*for(int i=0;i<batchList.size();i++){
                if(batchList.get(i).getBatch_title().equals(batchName)){
                    Toast.makeText(StudentListActivity.this,"active "+batchList.get(i).getActiveStudents(),Toast.LENGTH_SHORT).show();
                    Toast.makeText(StudentListActivity.this,"inactive "+batchList.get(i).getInActiveStudents(),Toast.LENGTH_SHORT).show();
                }
            }*/
        }


    }



     /*void fetchFeeCostList() {
        db.collection(Constants.fee_paid_collection).whereEqualTo("branchId",branchId)
                .whereEqualTo("isPayFee",false).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    feeCostBean = doc.toObject(FeeCostBean.class);
                    feeCostBeanArrayList.add(feeCostBean);
                }
                if(feeCostBeanArrayList.size()>0){
                    assignToStu();
                }
            }
        });
    }

     void assignToStu() {
        for(int i=0;i<displayStudentList.size();i++){
             otFee=0;
             aFee=0;
             mFee=0;
             tFee=0;
            for(int j=0;j<feeCostBeanArrayList.size();j++){
                if(displayStudentList.get(i).getStudentId() == feeCostBeanArrayList.get(j).getStudentId()){
                    if(feeCostBeanArrayList.get(j).getFeeType() == 1){
                        otFee = feeCostBeanArrayList.get(j).getSellingAmount();
                    }else if(feeCostBeanArrayList.get(j).getFeeType() == 2){
                        if (feeCostBeanArrayList.get(j).getNoOfMonths() < 12) {
                            Double total = Double.valueOf(feeCostBeanArrayList.get(j).getSellingAmount());
                            int iTotal = (int) Math.round(total / 12);
                            aFee = feeCostBeanArrayList.get(j).getNoOfMonths()*iTotal;
                        } else if (feeCostBeanArrayList.get(j).getNoOfMonths() == 12) {
                            aFee = feeCostBeanArrayList.get(j).getSellingAmount();
                        } else if (feeCostBeanArrayList.get(j).getNoOfMonths() > 12) {
                            Double total = Double.valueOf(feeCostBeanArrayList.get(j).getSellingAmount());
                            int iTotal = (int) Math.round(total / 12);
                            aFee = feeCostBeanArrayList.get(j).getNoOfMonths()*iTotal;
                        }

                    }else if(feeCostBeanArrayList.get(j).getFeeType() == 3){
                        if(feeCostBeanArrayList.get(j).getNoOfMonths() > 0){
                            mFee = feeCostBeanArrayList.get(j).getNoOfMonths() * feeCostBeanArrayList.get(j).getSellingAmount();
                        }else{
                            mFee = feeCostBeanArrayList.get(j).getSellingAmount();
                        }

                    }else{
                        if(feeCostBeanArrayList.get(j).getNoOfMonths() > 0){
                            tFee = feeCostBeanArrayList.get(j).getNoOfMonths() * feeCostBeanArrayList.get(j).getSellingAmount();
                        }else{
                            tFee = feeCostBeanArrayList.get(j).getSellingAmount();
                        }

                    }
                    displayStudentList.get(i).setTotalSellingCost(otFee+mFee+tFee+aFee);

                }
            }
        }
         setAdapterData();
         AttUtil.pd(0);
    }*/

    void setAdapterData(){
        getSupportActionBar().setTitle("Manage Students"+"("+displayStudentList.size()+")");
        studentListAdapter = new ApproveAdapter(StudentListActivity.this, R.layout.adapter_approve_students, displayStudentList);
        lv.setAdapter(studentListAdapter);
    }

     void fetchStudentsList() {
        if(lateJoiningFlag == 0){
            db.collection(Constants.student_collection).whereEqualTo("batch_title",batchName)
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                        student = doc.toObject(StudentBean.class);
                        studentsList.add(student);
                    }

                    for (int i = 0; i < studentsList.size(); i++) {
                        if (studentsList.get(i).getAdmStatus() == 2)
                            activeStudents.add(studentsList.get(i));
                        else if (studentsList.get(i).getAdmStatus() == 3 || studentsList.get(i).getAdmStatus() == 4)
                            inactiveStudents.add(studentsList.get(i));
                    }
                    displayStudentList.addAll(activeStudents);
                    //fetchFeeCostList();
                    setAdapterData();
                    AttUtil.pd(0);

                }
            });
        }else{
            db.collection(Constants.student_collection).whereEqualTo("branchId",branchId).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                        student = doc.toObject(StudentBean.class);
                        studentsList.add(student);
                    }

                    for (int i = 0; i < studentsList.size(); i++) {
                        if (studentsList.get(i).getAdmStatus() == 2)
                            activeStudents.add(studentsList.get(i));
                        else if (studentsList.get(i).getAdmStatus() == 3 || studentsList.get(i).getAdmStatus() == 4)
                            inactiveStudents.add(studentsList.get(i));
                    }
                    displayStudentList.addAll(activeStudents);
                    setAdapterData();
                    AttUtil.pd(0);
                   // fetchFeeCostList();
                }
            });
        }

    }

    void initViews() {
        AttUtil.progressDialog(this);
        Calendar cal = Calendar.getInstance();
        var6 = cal.get(Calendar.DAY_OF_MONTH);
        var4 = cal.get(Calendar.MONTH);
        var5 = cal.get(Calendar.YEAR);
        lv = (ListView) findViewById(R.id.lv);
        lv.setOnItemClickListener(itemclk);

        rcv = getIntent();
        db = FirebaseFirestore.getInstance();
        feeCostBeanArrayList = new ArrayList<>();
        feeCostBean = new FeeCostBean();

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinList = new ArrayList<>();
        batchIdArray = new ArrayList<>();
        //batchList = new ArrayList<>();
        batchList = (ArrayList<BatchBean>) rcv.getSerializableExtra(AttUtil.BATCH_LIST);
        //batchNameArray = new ArrayList<>();
        //feeCostBeanArrayList = new ArrayList<>();
        //batchBeanArrayListActiveTemp = new ArrayList<>();
        //courseBeanArrayListTemp = new ArrayList<>();
        //batchNameArray = new ArrayList<>();
        //courseNameArrayList = new ArrayList<>();
        //courseBeanArrayList = new ArrayList<>();
        courseBeanArrayList = (ArrayList<BranCourBean>) getIntent()
                .getSerializableExtra(AttUtil.KEY_COURSE_ARRAYLIST);
    }

    AdapterView.OnItemClickListener itemclk = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            pos = position;
            student = displayStudentList.get(position);
            show_options();
        }
    };

    void show_options() {
        final android.support.v7.app.AlertDialog.Builder build = new android.support.v7.app.AlertDialog.Builder(this);
        if (lateJoiningFlag == 1) {
            String[] optionsLaterJoinings = {"View Student",
                    "View Class Details", "View Remarks", "Fee Structure",
                    "Activate Student", "Update Joining Date",
                    student.getStuName() + "'s Account",
                    "Send Message to Parent", "Call Parent"};
            options1 = optionsLaterJoinings;
        } else if (lateJoiningFlag == 0) {
            if (active == 1) {
                String[] optionsActive = {"View Student",
                        "View Class Details",
                        "View Remarks",
                        "To next class",
                        "Change Student Section",
                        "Fee Structure",
                        "Manage Fee",
                        student.getStuName() + "'s Account",
                        "Send Message to Parent",
                        "Call Parent",
                        "Deactivate",
                        "Resend Credentials"
                };
                options1 = optionsActive;
            } else if (active == 0) {
                String[] optionsInactive = {
                        "View Student",
                        "View Class Details",
                        "View Remarks",
                        "To next class",
                        "Change Student Section",
                        "Manage Fee",
                        student.getStuName() + "'s Account",
                        "Send Message to Parent",
                        "Call Parent",
                        "Activate",
                        "Resend Credentials"};
                options1 = optionsInactive;
            }
        } else if (lateJoiningFlag == 2) {
            if (active == 1) {
                String[] optionsActive = {"View Student",
                        "View Class Details",
                        "View Remarks",
                        "Fee Structure",
                        "Manage Fee",
                        student.getStuName() + "'s Account"
                };
                options1 = optionsActive;
            } else if (active == 0) {
                String[] optionsInactive = {
                        "View Student",
                        "View Class Details",
                        "View Remarks",
                        "Manage Fee",
                        student.getStuName() + "'s Account"};
                options1 = optionsInactive;
            }
        }
        if (lateJoiningFlag == 1) {
            build.setItems(options1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i;
                    switch (which) {
                        case 0:
                            i = new Intent(StudentListActivity.this, AddStudentActivity.class);
                            i.putExtra(AttUtil.TAG_STUDENTBEAN, student);
                            i.putExtra("viewDetail", 1);
                            startActivityForResult(i, AttUtil.REQ_CODE);
                            break;
                        case 1:
                            showCourseDetails();
                            break;
                        case 2:
                            viewRemarks();
                            break;
                        case 3:
                            showOptionsFeeStructure();
                            break;
                        case 4:
                            startActivityForResult(new Intent(StudentListActivity.this, BuildInvoiceActivity.class)
                                    .putExtra(AttUtil.TAG_STUDENTBEAN, student)
                                    .putExtra(AttUtil.BATCH_LIST,batchList).putExtra(AttUtil.KEY_COURSE_ARRAYLIST,courseBeanArrayList), 101);
                            break;
                        case 5:
                            updateJoiningDate();
                            break;
                        case 6:
                            startActivity(new Intent(StudentListActivity.this,
                                    ManageFeeAccountActivity.class).putExtra(AttUtil.TAG_STUDENTBEAN, student));
                            break;
                        case 7:
                            callParentOptions(1);
                            break;
                        case 8:
                            callParentOptions(0);
                            break;
                    }
                }
            });
            build.create().show();
        } else if (lateJoiningFlag == 0) {
            if (active == 1) {
                build.setItems(options1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i;

                        switch (which) {
                            case 0:
                                i = new Intent(StudentListActivity.this, AddStudentActivity.class);
                                i.putExtra(AttUtil.TAG_STUDENTBEAN, student);
                                i.putExtra("viewDetail", 1);
                                startActivityForResult(i, AttUtil.REQ_CODE);
                                break;
                            case 1:
                                showCourseDetails();
                                break;
                            case 2:
                                viewRemarks();
                                break;
                            case 3:
                                //changeCourseBatchDialog();
                                break;
                            case 4:
                                //viewBatch();
                                break;
                            case 5:
                                showOptionsFeeStructure();
//                                startActivity(new Intent(StudentListActivity.this,UpdateFeeStructure.class)
//                                        .putExtra(AttUtil.TAG_STUDENTBEAN,student));
                                //Toast.makeText(StudentListActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
                                break;
                            case 6:
                                startActivity(new Intent(StudentListActivity.this,
                                        GenerateFeeInvoceActivity.class).putExtra(AttUtil.TAG_STUDENTBEAN, student));
                                break;
                            case 7:
                                startActivity(new Intent(StudentListActivity.this,
                                        ManageFeeAccountActivity.class).putExtra(AttUtil.TAG_STUDENTBEAN, student));
                                break;
                            case 8:
                                callParentOptions(1);
                                break;
                            case 9:
                                callParentOptions(0);
                                break;
                            case 10:
                                AttUtil.progressDialog(StudentListActivity.this);
                                AttUtil.pd(1);
                                if (active == 1)
                                    activateDeactivateStudent(student, 0);
                                else
                                    activateDeactivateStudent(student, 1);
                                break;
                            case 11:
                                //resendCredentials(student);
                                break;

                        }
                    }
                });
                build.create().show();
            }
            if (active == 0) {
                build.setItems(options1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i;

                        switch (which) {
                            case 0:
                                i = new Intent(StudentListActivity.this, AddStudentActivity.class);
                                i.putExtra(AttUtil.TAG_STUDENTBEAN, student);
                                i.putExtra("viewDetail", 1);
                                startActivityForResult(i, AttUtil.REQ_CODE);
                                break;
                            case 1:
                                showCourseDetails();
                                break;
                            case 2:
                                viewRemarks();
                                break;
                            case 3:
                               /* if (student.getAdmStatus() == 4) {
                                    Toast.makeText(StudentListActivity.this, "This student can't be converted as active one, as "
                                            + student.getStuName() + " is promoted to next class", Toast.LENGTH_SHORT).show();
                                } else {
                                    changeCourseBatchDialog();
                                }*/
                                break;
                            case 4:
                                /*if (student.getAdmStatus() == 4)
                                    Toast.makeText(StudentListActivity.this, "This student can't be converted as active one, as "
                                            + student.getStuName() + " is promoted to next class", Toast.LENGTH_SHORT).show();
                                else
                                    viewBatch();*/
                                break;
                            case 5:
                               /* startActivity(new Intent(StudentListActivity.this,
                                        GenerateFeeInvoceActivity.class)
                                        .putExtra(AttUtil.TAG_STUDENTBEAN, student)
                                        .putExtra("signalToGenrateInvoice", 1));*/
                                break;
                            case 6:
                                /*startActivity(new Intent(StudentListActivity.this,
                                        ManageFeeAccountActivity.class).putExtra(AttUtil.TAG_STUDENTBEAN, student));*/
                                break;
                            case 7:
                                //callParentOptions(1);
                                break;
                            case 8:
                                //callParentOptions(0);
                                break;
                            case 9:
                                if (student.getAdmStatus() == 4) {
                                    Toast.makeText(StudentListActivity.this, "This student can't be converted as active one, as "
                                            + student.getStuName() + " is promoted to next class", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (active == 1)
                                        activateDeactivateStudent(student, 0);
                                    else
                                        activateDeactivateStudent(student, 1);
                                }
                                break;
                            case 10:
                                //resendCredentials(student);
                                break;
                        }
                    }
                });
                build.create().show();
            }
        } else if (lateJoiningFlag == 2) {
            if (active == 1) {
                build.setItems(options1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i;

                        switch (which) {
                            case 0:
                                i = new Intent(StudentListActivity.this, AddStudentActivity.class);
                                i.putExtra(AttUtil.TAG_STUDENTBEAN, student);
                                i.putExtra("viewDetail", 1);
                                startActivityForResult(i, AttUtil.REQ_CODE);
                                break;
                            case 1:
                                showCourseDetails();
                                break;
                            case 2:
                                viewRemarks();
                                break;
                            case 3:
                                showOptionsFeeStructure();
                                break;
                            case 4:
                                startActivity(new Intent(StudentListActivity.this,
                                        GenerateFeeInvoceActivity.class).putExtra(AttUtil.TAG_STUDENTBEAN, student));
                                break;
                            case 5:
                                startActivity(new Intent(StudentListActivity.this,
                                        ManageFeeAccountActivity.class).putExtra(AttUtil.TAG_STUDENTBEAN, student));
                                break;
                        }
                    }
                });
                build.create().show();
            }
            if (active == 0) {
                build.setItems(options1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i;

                        switch (which) {
                            case 0:
                                i = new Intent(StudentListActivity.this, AddStudentActivity.class);
                                i.putExtra(AttUtil.TAG_STUDENTBEAN, student);
                                i.putExtra("viewDetail", 1);
                                startActivityForResult(i, AttUtil.REQ_CODE);
                                break;
                            case 1:
                                showCourseDetails();
                                break;
                            case 2:
                                viewRemarks();
                                break;
                            case 3:
                                /*startActivity(new Intent(StudentListActivity.this,
                                        GenerateFeeInvoceActivity.class)
                                        .putExtra(AttUtil.TAG_STUDENTBEAN, student)
                                        .putExtra("signalToGenrateInvoice", 1));*/
                                break;
                            case 4:
                              /*  startActivity(new Intent(StudentListActivity.this,
                                        ManageFeeAccountActivity.class).putExtra(AttUtil.TAG_STUDENTBEAN, student));*/
                                break;
                        }
                    }
                });
                build.create().show();
            }
        }
    }

    int chkPos;

     void activateDeactivateStudent(StudentBean student, int status) {
       // active = status;
         StudentBean studentBeanUpdated = student;
         displayStudentList.remove(pos);
         if (status == 1) {
             for (int i = 0; i < batchList.size(); i++) {
                 if (batchList.get(i).getBatch_title().equals(studentBeanUpdated.getBatch_title())) {
                     chkPos = i;
                     batchList.get(i).setActiveStudents(batchList.get(i).getActiveStudents() + 1);
                     batchList.get(i).setInActiveStudents(batchList.get(i).getInActiveStudents() - 1);
                 }
             }
             /*AttUtil.batchBeanArrayListActive.clear();
             for (int i = 0; i < AttUtil.batchBeanArrayList.size(); i++) {
                 if (AttUtil.batchBeanArrayList.get(i).getBatchStatus() == 1)
                     AttUtil.batchBeanArrayListActive.add(AttUtil.batchBeanArrayList.get(i));
             }*/
             studentBeanUpdated.setAdmStatus(2);
             studentBeanUpdated.setFinishDate("0000-00-00");
             activeStudents.add(studentBeanUpdated);
             inactiveStudents.remove(pos);
         }else {
             for (int i = 0; i < batchList.size(); i++) {
                 if (batchList.get(i).getBatch_title().equals(studentBeanUpdated.getBatch_title())) {
                     chkPos = i;
                     batchList.get(i).setActiveStudents(batchList.get(i).getActiveStudents() - 1);
                     batchList.get(i).setInActiveStudents(batchList.get(i).getInActiveStudents() + 1);
                 }
             }
             /*AttUtil.batchBeanArrayListActive.clear();
             for (int i = 0; i < AttUtil.batchBeanArrayList.size(); i++) {
                 if (AttUtil.batchBeanArrayList.get(i).getBatchStatus() == 1)
                     AttUtil.batchBeanArrayListActive.add(AttUtil.batchBeanArrayList.get(i));
             }*/
             studentBeanUpdated.setFinishDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
             studentBeanUpdated.setAdmStatus(3);
             Toast.makeText(this, "Deactivated Successfully!", Toast.LENGTH_LONG).show();
             inactiveStudents.add(studentBeanUpdated);
             activeStudents.remove(pos);
         }
         getSupportActionBar().setTitle("Manage Students(" + displayStudentList.size() + ")");
         studentListAdapter.notifyDataSetChanged();
        updateDataToDB(studentBeanUpdated,chkPos,status);
    }

     void updateDataToDB(final StudentBean student, final int chkPos, final int status) {
        db.collection(Constants.student_collection).document(String.valueOf(student.getStudentId()))
                .update("finishDate",student.getFinishDate(),"admStatus",student.getAdmStatus())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updateBatchData(student,chkPos,status);
                    }
                });
    }

     void updateBatchData(StudentBean student, int chkPos, final int status) {
        db.collection(Constants.branchCollection).document(String.valueOf(this.student.getBranchId()))
                .collection(Constants.branch_course_collection).document(String.valueOf(student.getBranCourId()))
                .collection(Constants.batch_section_collection).document(String.valueOf(student.getBatchId()))
                .update("activeStudents",batchList.get(chkPos).getActiveStudents()
                        ,"inActiveStudents",batchList.get(chkPos).getInActiveStudents())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AttUtil.pd(0);
                        setResult(901,new Intent().putExtra(AttUtil.BATCH_LIST,batchList));
                        if(status == 1){
                            Toast.makeText(StudentListActivity.this, "Activated Successfully!", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(StudentListActivity.this, "Deactivated Successfully!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    void viewRemarks() {
         android.support.v7.app.AlertDialog.Builder builder = new
                 android.support.v7.app.AlertDialog.Builder(this);
         builder.setTitle("Remarks");
         builder.setMessage(student.getRemarks() + "");
         builder.setCancelable(false);
         builder.setPositiveButton("Done", null);
         builder.create().show();
    }

    void updateJoiningDate() {
         AlertDialog.Builder builder = new AlertDialog.Builder(StudentListActivity.this);
         builder.setTitle("Select Joining Date");
         LayoutInflater inflater = StudentListActivity.this.getLayoutInflater();
         View dialogView = inflater.inflate(R.layout.dialog_late_joining, null);
         builder.setView(dialogView);

         final EditText etxtJoiningDate = (EditText) dialogView.findViewById(R.id.selectJoiningDate);
         final EditText etxtJoiningDescription = (EditText) dialogView.findViewById(R.id.joiningDateDescription);

         etxtJoiningDate.setText(student.getJoinDate() + "");
         etxtJoiningDate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Calendar calendar = Calendar.getInstance();
                 DatePickerDialog datePickerDialog = new DatePickerDialog(StudentListActivity.this, new DatePickerDialog.OnDateSetListener() {
                     @Override
                     public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                         String date = year + "-" + (++monthOfYear) + "-" + dayOfMonth;
                         SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                         Date newDate = null;
                         try {
                             newDate = spf.parse(date);
                         } catch (ParseException e) {
                             e.printStackTrace();
                         }
                         spf = new SimpleDateFormat("yyyy-MM-dd");
                         date = spf.format(newDate);
                         etxtJoiningDate.setText(date);

                     }
                 }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                 datePickerDialog.show();
             }
         });

         final String logDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
         builder.setPositiveButton("Submit Date", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 AttUtil.progressDialog(StudentListActivity.this);
                 AttUtil.pd(1);
                 if (student.getDescription().toString().length() > 0 || student.getDescription().toString() != null){
                   desc = student.getDescription() + ";" + logDate + ":" + etxtJoiningDescription.getText().toString().trim() + "(" + student.getJoinDate() + ")";
                 }else{
                     desc = logDate + ":" + etxtJoiningDescription.getText().toString().trim() + "(" + student.getJoinDate() + ")";
                 }
                 student.setJoinDate(etxtJoiningDate.getText().toString().trim());
                 db.collection(Constants.student_collection).document(String.valueOf(student.getStudentId()))
                         .update("joinDate",student.getJoinDate(),
                                 "description",desc).addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void aVoid) {
                         studentListAdapter.notifyDataSetChanged();
                         AttUtil.pd(0);
                         Toast.makeText(StudentListActivity.this, "Joining Date Updated", Toast.LENGTH_SHORT).show();
                     }
                 });

             }
         });

         builder.setNegativeButton("Cancel", null);
         builder.create().show();
    }

    void showCourseDetails() {
        View myView = null;
        myView = getLayoutInflater().inflate(R.layout.student_course_details, null);
        final android.support.v7.app.AlertDialog.Builder var1 = new android.support.v7.app.AlertDialog.Builder(this);
        var1.setTitle("Detail");
        TextView tvCoursetitle = (TextView) myView.findViewById(R.id.stu_cour_txtcoursetitle);
        TextView tvbatchTitle = (TextView) myView.findViewById(R.id.stu_cour_txtbatchtitle);

        TextView stu_cour_txtbatchtimmng = (TextView) myView.findViewById(R.id.stu_cour_txtbatchtimmng);
        TextView stu_cour_txtcourseduration = (TextView) myView.findViewById(R.id.stu_cour_txtcourseduration);
        TextView stu_cour_txtcoursetime = (TextView) myView.findViewById(R.id.stu_cour_txtcoursetime);
        TextView stu_cour_txttotalfee = (TextView) myView.findViewById(R.id.stu_cour_txttotalfee);

        if (lateJoiningFlag != 2) {
            stu_cour_txtcourseduration.setVisibility(View.GONE);
            stu_cour_txtcoursetime.setVisibility(View.GONE);
            stu_cour_txttotalfee.setVisibility(View.GONE);
        }

        tvCoursetitle.setText("Class: " + student.getCourseName());
        if (student.getBatchId() == 0) {
            tvbatchTitle.setText("Section : Not Assigned");
        } else {
            tvbatchTitle.setText("Section : " + student.getBatch_title());
        }

        stu_cour_txtcourseduration.setText("Account Balance: " + student.getAccountAmount());
        stu_cour_txtcoursetime.setText("Total Amount: " + student.getTotalSellingCost());
        stu_cour_txttotalfee.setText("Balance: " + (student.getTotalSellingCost() - student.getTotalCreditAmount()));
        if (student.getAdmStatus() == 1) {
            stu_cour_txtbatchtimmng.setText("Status : Yet to initialize");
        } else if (student.getAdmStatus() == 2) {
            stu_cour_txtbatchtimmng.setText("Status : Running");
        } else if (student.getAdmStatus() == 3) {
            stu_cour_txtbatchtimmng.setText("Status : Finished");
        } else if (student.getAdmStatus() == 4) {
            stu_cour_txtbatchtimmng.setText("Status : Promoted");
        }


        var1.setNegativeButton("Done", null);
        var1.setView(myView);
        var1.create().show();
    }

    void showOptionsFeeStructure() {
        final android.support.v7.app.AlertDialog.Builder build = new android.support.v7.app.AlertDialog.Builder(this);
        String[] options = {"View Fee Structure", "Update Fee Structure", "Resend Fee Structure"};
        build.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        feeStructureViewSignal = 1;
                        if (AttUtil.isNetworkConnected(StudentListActivity.this))
                            resendFeeStructure();
                        /*else
                            resendFeeStructureNetConnect();*/
                        break;
                    case 1:
                        startActivity(new Intent(StudentListActivity.this, UpdateFeeStructure.class)
                                .putExtra(AttUtil.TAG_STUDENTBEAN, student));
                        break;
                    case 2:
                       // emailNoteDialog();
                        break;
                }
            }
        });
        build.create().show();

    }

     void resendFeeStructure() {
    }

    void callParentOptions(final int callMsg) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StudentListActivity.this);
        String[] options = {"Father", "Mother"};
        if (callMsg == 0)
            options = new String[]{"Call Father", "Call Mother"};
        else
            options = new String[]{"Message Father", "Message Mother"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listParentPhoneNumbers(which, callMsg);
            }
        });
        builder.create().show();
    }

    void listParentPhoneNumbers(int signal, final int callMsg) {
        if (signal == 0) {
            if (student.getFatherPhone().toString().length() == 0)
                Toast.makeText(this, "No contact details", Toast.LENGTH_SHORT).show();
            else {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StudentListActivity.this);
                String[] options = student.getFatherPhone().split(",");
                final String[] finalOptions = options;
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (callMsg == 0)
                            callFunction(finalOptions[which]);
                        if (callMsg == 1)
                            msgFunction(finalOptions[which]);
                    }
                });
                builder.create().show();
            }
        } else if (signal == 1) {
            if (student.getMotherPhone().toString().length() == 0)
                Toast.makeText(this, "No contact details", Toast.LENGTH_SHORT).show();
            else {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StudentListActivity.this);
                String[] options = student.getMotherPhone().split(",");
                final String[] finalOptions = options;
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (callMsg == 0)
                            callFunction(finalOptions[which]);
                        if (callMsg == 1)
                            msgFunction(finalOptions[which]);
                    }
                });
                builder.create().show();
            }
        }

    }

    void callFunction(String phone) {
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone.toString().trim()));
        startActivity(i);
    }

    void msgFunction(String phone) {
        Intent j = new Intent(Intent.ACTION_VIEW);
        j.setType("vnd.android-dir/mms-sms");
        j.putExtra("address", phone.toString().trim());
        startActivity(j);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AttUtil.REQ_CODE && resultCode == AttUtil.RES_CODE) {
            StudentBean sBean = (StudentBean) data.getSerializableExtra(AttUtil.TAG_STUDENTBEAN);
            displayStudentList.remove(pos);
            displayStudentList.add(pos, sBean);
            studentListAdapter.notifyDataSetChanged();
        }
        if (requestCode == 101 && resultCode == AttUtil.RES_CODE) {
            displayStudentList.remove(student);
            getSupportActionBar().setTitle("Manage Students(" + displayStudentList.size() + ")");
            batchBean = (BatchBean) data.getSerializableExtra("batchBean");
            setResult(601,new Intent().putExtra("batchBean", batchBean).putExtra("position",pos));
            studentListAdapter.notifyDataSetChanged();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        /*if(item.getItemId() == R.id.action_bkup){
            //showDumpDialog();
        }else*/
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.action_inactive) {
            loadInactive();
        } else if (item.getItemId() == R.id.action_active) {
            loadActive();
        } else if (item.getItemId() == R.id.studentExport) {
            exportStudentFeeSummary();
        } else if (item.getItemId() == R.id.Sort) {
            askForSortOptions();
        }
        /*else if(item.getItemId() == R.id.action_change){
            //showFeeDialog();
        }
        if(key ==0){

        }*/
        return super.onOptionsItemSelected(item);
    }

    void loadInactive() {
        if (inactiveStudents.size() > 0) {
            active = 0;
            displayStudentList.clear();
            displayStudentList.addAll(inactiveStudents);
            getSupportActionBar().setTitle("Manage Students(" + displayStudentList.size() + ")");
            studentListAdapter.notifyDataSetChanged();
        } else
            Toast.makeText(this, "No inactive student in this batch", Toast.LENGTH_SHORT).show();
    }

    void loadActive() {
        if (activeStudents.size() > 0) {
            active = 1;
            displayStudentList.clear();
            displayStudentList.addAll(activeStudents);
            getSupportActionBar().setTitle("Manage Students(" + displayStudentList.size() + ")");
            studentListAdapter.notifyDataSetChanged();
        } else
            Toast.makeText(this, "No active student in this batch", Toast.LENGTH_SHORT).show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.view_student, menu);
        /*if (key == 0) {
            menu.findItem(R.id.action_change).setVisible(false);
        }else{
            menu.findItem(R.id.action_change).setVisible(true);
        }*/
        if (lateJoiningFlag == 1) {
            menu.findItem(R.id.action_inactive).setVisible(false);
            menu.findItem(R.id.action_active).setVisible(false);
            menu.findItem(R.id.studentExport).setVisible(false);
        }

        MenuItem sortitem = menu.findItem(R.id.Sort);
        sortitem.setIcon(R.drawable.filter);

        MenuItem searchitem = menu.findItem(R.id.Search);
        SearchManager searchManager = (SearchManager) StudentListActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchitem != null) {
            searchView = (SearchView) searchitem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(StudentListActivity.this.getComponentName()));
        }
        searchView.setOnQueryTextListener(onQueryTextListener);

        return true;
    }

    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            studentListAdapter.filter(newText.toString());
            studentListAdapter.notifyDataSetChanged();
            return false;
        }
    };

    void exportStudentFeeSummary() {
        strFileName = pref.getString(AttUtil.shpBranchName, "") + " -" +
                " Fee summary " + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) + ".xls";
        strFileName.replace("/","-");
        strFileName.replace(":","-");
        File root = null;
        String str = "";
        try {
            root = Environment.getExternalStorageDirectory();
            if (root.canWrite()) {
                File fileDir = new File(root.getAbsolutePath() + "/" + getResources().getString(R.string.app_name) + "/" +
                        getResources().getString(R.string.manager_fee_summary));
                if (!fileDir.exists())
                    fileDir.mkdirs();


                fWriter = new File(fileDir, strFileName);
                WorkbookSettings wbSettings = new WorkbookSettings();
                wbSettings.setLocale(new Locale("en", "EN"));

                WritableWorkbook workbook = Workbook.createWorkbook(fWriter, wbSettings);
                WritableSheet sheet = workbook.createSheet("Student List", 0);
                sheet.addCell(new Label(0, 0, "S.No."));
                sheet.addCell(new Label(1, 0, "Registration No."));
                sheet.addCell(new Label(2, 0, "Student Name"));
                sheet.addCell(new Label(3, 0, "Date of Birth"));
                sheet.addCell(new Label(4, 0, "Gender"));
                sheet.addCell(new Label(5, 0, "Father Name"));
                sheet.addCell(new Label(6, 0, "Phone No."));
                sheet.addCell(new Label(7, 0, "Father's Email"));
                sheet.addCell(new Label(8, 0, "Mother Name"));
                sheet.addCell(new Label(9, 0, "Phone No."));
                sheet.addCell(new Label(10, 0, "Mother's Email"));
                sheet.addCell(new Label(11, 0, "Class"));
                sheet.addCell(new Label(12, 0, "Section"));
                sheet.addCell(new Label(13, 0, "Account Balance"));
                sheet.addCell(new Label(14, 0, "Total Fees"));
                sheet.addCell(new Label(15, 0, "Balance"));
                sheet.addCell(new Label(16, 0, "Status"));
                sheet.addCell(new Label(17, 0, "Registeration Date"));
                sheet.addCell(new Label(18, 0, "Starting Date"));
                sheet.addCell(new Label(19, 0, "Ending Date"));
                sheet.addCell(new Label(20, 0, "Branch Name"));

                int rowIndex = 1;
                int cellIndex = 0;
                for (int i = 0; i < displayStudentList.size(); i++) {
                    StudentBean studentBean = displayStudentList.get(i);
                    sheet.addCell(new Label(cellIndex, rowIndex, "" + (i + 1)));
                    sheet.addCell(new Label(cellIndex + 1, rowIndex, studentBean.getRegId() + ""));
                    sheet.addCell(new Label(cellIndex + 2, rowIndex, studentBean.getStuName()));
                    sheet.addCell(new Label(cellIndex + 3, rowIndex, studentBean.getDob()));
                    sheet.addCell(new Label(cellIndex + 4, rowIndex, studentBean.getGender()));
                    sheet.addCell(new Label(cellIndex + 5, rowIndex, studentBean.getFatherName()));
                    sheet.addCell(new Label(cellIndex + 6, rowIndex, studentBean.getFatherPhone()));
                    sheet.addCell(new Label(cellIndex + 7, rowIndex, studentBean.getFatherEmail()));
                    sheet.addCell(new Label(cellIndex + 8, rowIndex, studentBean.getMotherName()));
                    sheet.addCell(new Label(cellIndex + 9, rowIndex, studentBean.getMotherPhone()));
                    sheet.addCell(new Label(cellIndex + 10, rowIndex, studentBean.getMotherEmail()));
                    sheet.addCell(new Label(cellIndex + 11, rowIndex, studentBean.getCourseName()));
                    sheet.addCell(new Label(cellIndex + 12, rowIndex, studentBean.getBatch_title()));
                    sheet.addCell(new Label(cellIndex + 13, rowIndex, studentBean.getAccountAmount() + ""));
                    sheet.addCell(new Label(cellIndex + 14, rowIndex, studentBean.getTotalSellingCost() + ""));
                    sheet.addCell(new Label(cellIndex + 15, rowIndex, "" + (studentBean.getTotalSellingCost() - studentBean.getTotalCreditAmount())));
                    if (studentBean.getAdmStatus() == 1) {
                        str = "Yet to initialize";
                    } else if (studentBean.getAdmStatus() == 2) {
                        str = "Running";
                    } else if (studentBean.getAdmStatus() == 3) {
                        str = "Finished";
                    } else if (studentBean.getAdmStatus() == 4) {
                        str = "Promoted";
                    }
                    sheet.addCell(new Label(cellIndex + 16, rowIndex, str));
                    sheet.addCell(new Label(cellIndex + 17, rowIndex, studentBean.getDate()));
                    sheet.addCell(new Label(cellIndex + 18, rowIndex, studentBean.getStartingDate()));
                    sheet.addCell(new Label(cellIndex + 19, rowIndex, studentBean.getFinishDate()));
                    sheet.addCell(new Label(cellIndex + 20, rowIndex, pref.getString(AttUtil.shpBranchName, "")));
                    rowIndex++;
                }

                workbook.write();
                workbook.close();
                path = Uri.fromFile(new File(fileDir, strFileName));
                Toast.makeText(this, "File Exported to SD card", Toast.LENGTH_LONG);
            }
        } catch (Exception e) {
            AttUtil.pd(0);
            e.printStackTrace();
        }

    }

    void askForSortOptions() {
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String[] items = new String[]{"Sort By Name", "Sort By Joining Date"};
        if (lateJoiningFlag == 1)
            items = new String[]{"Sort By Name", "Sort By Joining Date"};
        else if (lateJoiningFlag == 0) {
            if (active == 1)
                items = new String[]{"Sort By Name", "Sort By Starting Date"};
            if (active == 0)
                items = new String[]{"Sort By Name", "Sort By Finish Date"};
        }
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Collections.sort(displayStudentList, new StudentComparator(0));
                        studentListAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        if (lateJoiningFlag == 1)
                            Collections.sort(displayStudentList, new StudentComparator(3));
                        else if (active == 1)
                            Collections.sort(displayStudentList, new StudentComparator(1));
                        else if (active == 0)
                            Collections.sort(displayStudentList, new StudentComparator(2));
                        studentListAdapter.notifyDataSetChanged();
                        break;


                }
            }
        });
        //StudentComparator(0) = by name
        //StudentComparator(1) = by starting date
        //StudentComparator(2) = by finish date
        //StudentComparator(3) = by joining date

        dialog = builder.create();
        dialog.show();
    }
}
