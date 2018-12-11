package com.auribisesmyplayschool.myplayschool.activity;

import android.app.DatePickerDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adapter.EduEnquiryAdapter;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BranCourBean;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.CourseBean;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.UserBean;
import com.auribisesmyplayschool.myplayschool.bean.EnquiryBean;
import com.auribisesmyplayschool.myplayschool.bean.SignInBean;
import com.auribisesmyplayschool.myplayschool.bean.StudentBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
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
import java.util.Date;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

public class ManageEnquiryActivity extends AppCompatActivity {

    private EduEnquiryAdapter eduEnquiryAdapter;
    private EditText edtdateto, edtdatefrom, edtNextDate;
    private Date dateto = null, datefrom = null, datecurrent, d;
    private DateFormat format;
    private int dateSignal = 0, cyear, cmonth, cday, reqCode = 0, retrieveSignal, posEnquiry = 0,
            formSignal = 0, activitySignal = 0, studentId = 0, rbtnSignal = 0, cbExportEnqSignal = 0, cbSearchEnqSignal = 1;
    private DatePickerDialog datePickerDialog, datePickerDialog1;
    private Calendar calendar;
    private SharedPreferences preferences;
    private ArrayList<StudentBean> studentBeanArrayList, tempStudentBeanArrayList,totalStudentList,filterStudentList;
    ArrayList<EnquiryBean> enquiryBeanArrayList;
    private ArrayList<BranCourBean> courseBeanArrayList;
    private ArrayList<UserBean> teacherBeanArrayList;
    private ListView listViewEnquiry;
    private StudentBean studentBean;
   // EnquiryBean enquiryBean;
    private int branchId = 0, branCourId = 0, responseSignal = 0;
    private String dateFrom, dateTo, reference = "";
    private RelativeLayout rlEFReset;
    private TextView tvEnquiryFilterreset;
    private CheckBox cbExportEnquiries, cbSearchEnquiries;
    private String strFileName = "", base64File = "";
    private File fWriter;
    private Uri path;
    FirebaseFirestore db;
    SignInBean signInBean;
    String cDayString,cMonthString,currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_enquiry);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = FirebaseFirestore.getInstance();
        preferences = getSharedPreferences(AttUtil.shpREG, MODE_PRIVATE);
        initview();
    }

    void initview() {
        //courseBeanArrayList = new ArrayList<>();
        teacherBeanArrayList = new ArrayList<>();
       // enquiryBean = new EnquiryBean();
        enquiryBeanArrayList = new ArrayList<>();
        Intent ircv = getIntent();
        if (ircv.hasExtra("activitySignal")) {
            activitySignal = ircv.getIntExtra("activitySignal", 0);
            branchId = ircv.getIntExtra(AttUtil.branchId, 0);
            dateFrom = ircv.getStringExtra("dateFrom");
            dateTo = ircv.getStringExtra("dateTo");
            branCourId = ircv.getIntExtra(AttUtil.branCourId, 0);
            reference = ircv.getStringExtra(AttUtil.reference);
            studentId = ircv.getIntExtra(AttUtil.studentId, 0);
            Log.i("test", reference + "");
        }



        courseBeanArrayList = (ArrayList<BranCourBean>) ircv.getSerializableExtra(AttUtil.KEY_COURSE_ARRAYLIST);
        signInBean = (SignInBean) ircv.getSerializableExtra(AttUtil.signInBean);
       // teacherBeanArrayList = (ArrayList<UserBean>) ircv.getSerializableExtra(AttUtil.KEY_USER_ARRAYLIST);
        studentBean = new StudentBean();
        calendar = Calendar.getInstance();
        cyear = calendar.get(Calendar.YEAR);
        cmonth = calendar.get(Calendar.MONTH);
        cday = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(ManageEnquiryActivity.this, onDateSetListener, cyear, cmonth, cday);
        datePickerDialog1 = new DatePickerDialog(ManageEnquiryActivity.this, onDateSetListener, cyear, cmonth, cday);
       d = new Date();
        format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            datecurrent = format.parse(new SimpleDateFormat("yyyy-MM-dd").format(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }
       getDate(cyear,cmonth,cday);
        listViewEnquiry =  findViewById(R.id.lvEnquiry);
        studentBeanArrayList = new ArrayList<>();
        totalStudentList = new ArrayList<>();
        tempStudentBeanArrayList = new ArrayList<>();
        rlEFReset = (RelativeLayout) findViewById(R.id.rlEFReset);
        tvEnquiryFilterreset = (TextView) findViewById(R.id.tvEnquiryFilterreset);
        tvEnquiryFilterreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentBeanArrayList.clear();
                studentBeanArrayList.addAll(tempStudentBeanArrayList);
                eduEnquiryAdapter.notifyDataSetChanged();
                rlEFReset.setVisibility(View.GONE);
                rbtnSignal = 0;
            }
        });
        listViewEnquiry.setOnItemClickListener(onItemClickListener);
        if (AttUtil.isNetworkConnected(ManageEnquiryActivity.this)) {
            AttUtil.progressDialog(this);
            AttUtil.pd(1);
            retrieveFromDb();
        } else {
           // retrieveNetConnect();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        if (item.getItemId() == 1) {
            enquirylistfilter();
        }
        if (item.getItemId() == 2) {
            retrieveSignal = 2;
           // updateNextVisitDialog();
        }
        if (item.getItemId() == R.id.enquiryRecordInfo) {
            recordInfo();
        }
        if (item.getItemId() == 3) {
            //filterTypeDialog();
        }
        return super.onOptionsItemSelected(item);
    }



    void recordInfo() {
        android.support.v7.app.AlertDialog.Builder dialogBuilder =
                new android.support.v7.app.AlertDialog.Builder(ManageEnquiryActivity.this);
        dialogBuilder.setTitle("Information");
        LinearLayout layout = new LinearLayout(ManageEnquiryActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(15, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 0, 0);
        params1.setMargins(30, 0, 0, 0);
        LinearLayout enquiryLinearLayout = new LinearLayout(ManageEnquiryActivity.this);
        LinearLayout counsellingLinearLayout = new LinearLayout(ManageEnquiryActivity.this);
//        LinearLayout demoLinearLayout = new LinearLayout(ManageEnquiryActivity.this);
        LinearLayout registerLinearLayout = new LinearLayout(ManageEnquiryActivity.this);
        LinearLayout discardLinearLayout = new LinearLayout(ManageEnquiryActivity.this);
        enquiryLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        counsellingLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
//        demoLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        registerLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        discardLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView enquiry = new TextView(ManageEnquiryActivity.this);
        TextView counselling = new TextView(ManageEnquiryActivity.this);
//        TextView demo = new TextView(ManageEnquiryActivity.this);
        TextView register = new TextView(ManageEnquiryActivity.this);
        TextView discard = new TextView(ManageEnquiryActivity.this);
        enquiry.setText(" - Enquiry");
        counselling.setText(" - Counsellinging");
//        demo.setText(" - Demo Student");
        register.setText(" - Registered Student");
        discard.setText(" - Discarded Enquiry");
        View enquiryview = new View(ManageEnquiryActivity.this);
        View counsellingview = new View(ManageEnquiryActivity.this);
//        View demoview = new View(ManageEnquiryActivity.this);
        View registerview = new View(ManageEnquiryActivity.this);
        View discardview = new View(ManageEnquiryActivity.this);
        enquiryview.setBackgroundColor(ContextCompat.getColor(ManageEnquiryActivity.this, R.color.colorSeaGreen));
        counsellingview.setBackgroundColor(ContextCompat.getColor(ManageEnquiryActivity.this, R.color.colorYellow));
//        demoview.setBackgroundColor(ContextCompat.getColor(ManageEnquiryActivity.this, R.color.colorBlue));
        registerview.setBackgroundColor(ContextCompat.getColor(ManageEnquiryActivity.this, R.color.colorGreen));
        discardview.setBackgroundColor(ContextCompat.getColor(ManageEnquiryActivity.this, R.color.colorOrange));

        enquiryLinearLayout.addView(enquiryview, 0, params1);
        enquiryLinearLayout.addView(enquiry, 1, params);
        counsellingLinearLayout.addView(counsellingview, 0, params1);
        counsellingLinearLayout.addView(counselling, 1, params);
//        demoLinearLayout.addView(demoview, 0, params1);
//        demoLinearLayout.addView(demo, 1, params);
        registerLinearLayout.addView(registerview, 0, params1);
        registerLinearLayout.addView(register, 1, params);
        discardLinearLayout.addView(discardview, 0, params1);
        discardLinearLayout.addView(discard, 1, params);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            enquiry.setTextAppearance(android.R.style.TextAppearance_Medium);
            counselling.setTextAppearance(android.R.style.TextAppearance_Medium);
//            demo.setTextAppearance(android.R.style.TextAppearance_Medium);
            register.setTextAppearance(android.R.style.TextAppearance_Medium);
            discard.setTextAppearance(android.R.style.TextAppearance_Medium);
        }

        layout.addView(enquiryLinearLayout, params);
        layout.addView(counsellingLinearLayout, params);
//        layout.addView(demoLinearLayout, params);
        layout.addView(registerLinearLayout, params);
        layout.addView(discardLinearLayout, params);

        dialogBuilder.setView(layout);
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        android.support.v7.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void retrieveFromDb() {
        db.collection(Constants.student_collection)
                .whereEqualTo("branchId",signInBean.getBranchId())/*.whereEqualTo("curDate",currentDate)*/
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                    if (doc.exists()) {
                        studentBean = doc.toObject(StudentBean.class);
                        //studentBeanArrayList.add(studentBean);
                        totalStudentList.add(studentBean);
                    }
                }
                if(totalStudentList.size()>0){
                    for(int i=0;i<totalStudentList.size();i++){
                        if(totalStudentList.get(i).getCurDate().equals(currentDate)){
                            studentBeanArrayList.add(totalStudentList.get(i));
                        }
                    }
                }
                if (studentBeanArrayList.size() > 0 && studentBeanArrayList != null) {
                    getSupportActionBar().setTitle("Enquiries(" + studentBeanArrayList.size() + ")");
                    eduEnquiryAdapter = new EduEnquiryAdapter(ManageEnquiryActivity.this, R.layout.adapter_edu_enquiry_list
                            , studentBeanArrayList);
                    listViewEnquiry.setAdapter(eduEnquiryAdapter);
                }else{
                    getSupportActionBar().setTitle("Enquiries(" + 0 + ")");
                    Toast.makeText(ManageEnquiryActivity.this,"No enquiries found",Toast.LENGTH_SHORT).show();
                }
                AttUtil.pd(0);
            }
        });

    }

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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AttUtil.REQ_CODE && resultCode == AttUtil.RES_CODE) {
            StudentBean bean = (StudentBean) data.getSerializableExtra(AttUtil.TAG_STUDENTBEAN);
            setResult(125,new Intent().putExtra(AttUtil.TAG_STUDENTBEAN, bean));
            studentBeanArrayList.remove(posEnquiry);
            studentBeanArrayList.add(posEnquiry, bean);
            eduEnquiryAdapter.notifyDataSetChanged();
        }
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            posEnquiry = position;
           // Log.i("test", studentBean.toString());
            studentBean = studentBeanArrayList.get(position);
            startActivityForResult(new Intent(ManageEnquiryActivity.this, ViewEnquiryDetailActivity.class)
                    .putExtra(AttUtil.TAG_STUDENTBEAN, studentBean)
                    .putExtra(AttUtil.KEY_COURSE_ARRAYLIST, courseBeanArrayList)
                    //.putExtra(AttUtil.KEY_USER_ARRAYLIST, teacherBeanArrayList)
                    .putExtra(AttUtil.KEY_COUNSELLOR_ARRAYLIST, (ArrayList<UserBean>) getIntent().getSerializableExtra(AttUtil.KEY_COUNSELLOR_ARRAYLIST))
                    .putExtra("activitySignal", activitySignal), AttUtil.REQ_CODE);
                     overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
            //show_options();
        }
    };

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
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
            if (dateSignal == 3) {
                edtNextDate.setText(year + "-" + cMonthString + "-" + cDayString);
            }
            /*if(dateSignal==4){
                edtchequedate.setText(year+"-"+cMonthString+"-"+cDayString);
            }
            if(dateSignal==5){
                edtcourseStartingDate.setText(year+"-"+cMonthString+"-"+cDayString);
            }*/
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);
        MenuItem searchitem = menu.findItem(R.id.Search_list);
        SearchManager searchManager = (SearchManager) ManageEnquiryActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchitem != null) {
            searchView = (SearchView) searchitem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(ManageEnquiryActivity.this.getComponentName()));
        }
        searchView.setOnQueryTextListener(onQueryTextListener);
        if (activitySignal == 0 || activitySignal == 1 && reference.equals("")) {
            menu.add(0, 1, 0, "By Date Range/To Export").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            menu.add(0, 2, 0, "By Visit").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            menu.add(0, 3, 0, "By Type").setIcon(R.drawable.filter).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        }
        return super.onCreateOptionsMenu(menu);
    }

    SearchView.OnQueryTextListener onQueryTextListener
            = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if (eduEnquiryAdapter!=null){
                eduEnquiryAdapter.filter(newText.toString());
                eduEnquiryAdapter.notifyDataSetChanged();
            }
            return false;
        }
    };

    void enquirylistfilter() {
        android.support.v7.app.AlertDialog.Builder dialogBuilder =
                new android.support.v7.app.AlertDialog.Builder(ManageEnquiryActivity.this);
        dialogBuilder.setTitle("Choose Date Range");
        LayoutInflater inflater = ManageEnquiryActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custome_date_layout, null);
        dialogBuilder.setView(dialogView);
        edtdateto =  dialogView.findViewById(R.id.tv_date_to);
        edtdateto.setText(new SimpleDateFormat("yyyy-MM-dd").format(d));
        try {
            dateto = format.parse(edtdateto.getText().toString().trim());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        edtdatefrom = (EditText) dialogView.findViewById(R.id.tv_date_from);
        cbExportEnquiries = (CheckBox) dialogView.findViewById(R.id.cbExportEnquiries);
        cbSearchEnquiries = (CheckBox) dialogView.findViewById(R.id.cbSearchEnquiries);
        if (activitySignal == 0) {
            cbExportEnquiries.setVisibility(View.VISIBLE);
            cbSearchEnquiries.setVisibility(View.VISIBLE);
        }
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
        cbExportEnquiries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbExportEnquiries.isChecked())
                    cbExportEnqSignal = 1;
                else
                    cbExportEnqSignal = 0;
            }
        });
        cbSearchEnquiries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbSearchEnquiries.isChecked())
                    cbSearchEnqSignal = 1;
                else
                    cbSearchEnqSignal = 0;
            }
        });
        cbSearchEnqSignal = 1;
        dialogBuilder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                retrieveSignal = 1;
                if (cbSearchEnquiries.isChecked() || cbExportEnquiries.isChecked()) {
                    if (AttUtil.isNetworkConnected(ManageEnquiryActivity.this)) {
                        //retrieveFromDb();
                        getFilterLlist();
                    } /*else {
                        retrieveNetConnect();
                    }*/
                } else {
                    Toast.makeText(ManageEnquiryActivity.this, "Select alteat one option", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", null);
        android.support.v7.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

     void getFilterLlist() {
         filterStudentList = new ArrayList<>();
         Date df = null, dt = null;
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         try {
             df = sdf.parse(edtdatefrom.getText().toString().trim());
             dt = sdf.parse(edtdateto.getText().toString().trim());
         } catch (ParseException e) {
             e.printStackTrace();
         }

         for (int i = 0; i < totalStudentList.size(); i++) {
             try {
                 if (sdf.parse(totalStudentList.get(i).getCurDate()).compareTo(df) >= 0) {
                     if (sdf.parse(totalStudentList.get(i).getCurDate()).compareTo(dt) <= 0) {
                         filterStudentList.add(totalStudentList.get(i));
                     }
                 }
             } catch (ParseException e) {

             }
         }

         if(filterStudentList.size()>0){
             studentBeanArrayList.clear();
             //eduEnquiryAdapter.notifyDataSetChanged();

             for(int i=0;i<filterStudentList.size();i++){
                 studentBeanArrayList.add(filterStudentList.get(i));
             }
             getSupportActionBar().setTitle("Enquiries(" + studentBeanArrayList.size() + ")");
             eduEnquiryAdapter = new EduEnquiryAdapter(ManageEnquiryActivity.this, R.layout.adapter_edu_enquiry_list
                     , studentBeanArrayList);
             listViewEnquiry.setAdapter(eduEnquiryAdapter);
         }else{
             Toast.makeText(ManageEnquiryActivity.this,"No enquiry found",Toast.LENGTH_SHORT).show();
         }
     }



}
