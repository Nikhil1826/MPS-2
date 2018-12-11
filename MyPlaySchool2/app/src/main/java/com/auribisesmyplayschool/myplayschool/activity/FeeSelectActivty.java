package com.auribisesmyplayschool.myplayschool.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adapter.FeeAdapterThree;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.FeeHeadBean;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.RoutesBean;
import com.auribisesmyplayschool.myplayschool.bean.AccountBean;
import com.auribisesmyplayschool.myplayschool.bean.AdmissionBean;
import com.auribisesmyplayschool.myplayschool.bean.FeeCategoryBean;
import com.auribisesmyplayschool.myplayschool.bean.FeeCostBean;
import com.auribisesmyplayschool.myplayschool.bean.StudentBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.auribisesmyplayschool.myplayschool.classes.PayFeeInterface;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class FeeSelectActivty extends AppCompatActivity implements PayFeeInterface {
    RecyclerView listViewFeesOT, listViewFeesAT, listViewFeesMT, listViewFeesTT;
    SharedPreferences preferences;
    FeeAdapterThree feeAdapterTwoOT, feeAdapterTwoAT, feeAdapterTwoMT, feeAdapterTwoTT;

    TextView textViewAmountMonth, textViewAmountAnnual, textViewAmountOne, textViewAmountTransportation;
    String joinDate;
    Spinner spinnerFeeCategory, year;
    boolean newQuarter = false;
    int month, request, admissionId, studentId, spinnerFeeCategoryPos = 0;
    ArrayList<String> mnthSelected = new ArrayList();
    int feeCategorySelected;
    int reqCode = 0, activitySignal = 0;
    private ArrayList<FeeCategoryBean> feeCategoryBeanArrayList;
    ArrayList<FeeHeadBean> feeHeadBeanArrayList;
    FeeHeadBean feeHeadBean;
    FeeCostBean feeCostBean;
    RoutesBean routesBean;
    FeeCategoryBean feeCategoryBean;
    ArrayList<Integer> listId,accountListId;
    private ArrayList<FeeCostBean> feeCostBeanArrayList,
            oneTimeFeeHeadList = new ArrayList<>(),
            annualFeeHeadList = new ArrayList<>(),
            monthlyFeeHeadList = new ArrayList<>(),
            transportionFeeHeadList = new ArrayList<>();
    //            oneTimeFeeHeadListDialog = new ArrayList<>(),
//            annualFeeHeadListDialog = new ArrayList<>(),
//            monthlyFeeHeadListDialog = new ArrayList<>();
    ArrayList<String> feeCategoryList = new ArrayList<String>();

    Button btnFeeSelectPlan;
    StudentBean studentBeanDetails;
    ArrayList<RoutesBean> routesBeanArrayList;
    ArrayList<com.auribisesmyplayschool.myplayschool.adminApp.bean.FeeCostBean> feeCostBeanAdminAppArrayList;
    //EditTextFeeNew
    EditText edtAdvanceCashFeeSelect, editTextCashFeeSelect, editTextChequeFeeSelect,
            editTextRemarksFeeSelect, editTextJoinFeeSelect, edtChequeDate, edtNEFTFeeSelect,
            edtNEFTDateFeeSelect;
    RadioButton radioCashFeeSelect, radioChequeFeeSelect, radioNEFTFeeSelect;
    FirebaseFirestore db;
    int oneTimeAmountSelect = 0, annualAmountSelect = 0, monthlyAmountSelect = 0, transactionAmountSelect = 0;
    int feePaidId,check,accountId,signal;
    //Date currentTime;
    //String currentDate,curTime;
    Calendar calendar;
    int c_year,c_month,c_day,hour,mint;
    String cDayString,cMonthString,currentTime,currentDate;

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


    void initViews(){
        db = FirebaseFirestore.getInstance();
        routesBeanArrayList = new ArrayList<>();
        feeCategoryBeanArrayList = new ArrayList<>();
        feeCostBeanAdminAppArrayList = new ArrayList<>();
        listId = new ArrayList<>();
        accountListId = new ArrayList<>();
        feeCategoryBean = new FeeCategoryBean();
        feeHeadBean = new FeeHeadBean();
        routesBean = new RoutesBean();
        calendar = Calendar.getInstance();
        c_year = calendar.get(Calendar.YEAR);
        c_month = calendar.get(Calendar.MONTH);
        c_day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        mint = calendar.get(Calendar.MINUTE);
       /* currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aa");
        currentDate = df.format(currentTime);
        curTime = sdf.format(currentTime);*/
        feeHeadBeanArrayList = new ArrayList<>();
        feeCostBeanArrayList = new ArrayList<>();
        preferences = getSharedPreferences(AttUtil.shpREG, MODE_PRIVATE);
        btnFeeSelectPlan = findViewById(R.id.btnFeeSelectPlan);
        textViewAmountMonth = findViewById(R.id.textViewAmountMonth);
        textViewAmountAnnual = findViewById(R.id.textViewAmountAnnual);
        textViewAmountOne = findViewById(R.id.textViewAmountOne);
        textViewAmountTransportation = findViewById(R.id.textViewAmountTransportation);
        TextView eTextRecv = findViewById(R.id.eTextRecv);
        eTextRecv.setText("CHK");
        TextView eTextRecv2 = findViewById(R.id.eTextRecv2);
        eTextRecv2.setText("CHK");
        TextView eTextRecv3 = findViewById(R.id.eTextRecv3);
        eTextRecv3.setText("CHK");
        listViewFeesOT = findViewById(R.id.listViewFeesOne);
        listViewFeesAT = findViewById(R.id.listViewFeesAnnual);
        listViewFeesMT = findViewById(R.id.listViewFeesMonth);
        listViewFeesTT = findViewById(R.id.listViewFeesTransportation);
        listViewFeesOT.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listViewFeesAT.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listViewFeesMT.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listViewFeesTT.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        spinnerFeeCategory = findViewById(R.id.spinnerFeeType);

        spinnerFeeCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                spinnerFeeCategoryPos = position;
                if (position > 0) {
                    int id = feeCategoryBeanArrayList.get(position - 1).getFeeCategoryId();
                    oneTimeFeeHeadList.clear();
                    annualFeeHeadList.clear();
                    monthlyFeeHeadList.clear();
                    transportionFeeHeadList.clear();
                    for (int i = 0; i < feeCostBeanArrayList.size(); i++) {
                        if (feeCostBeanArrayList.get(i).getFeeType() == 1 && feeCostBeanArrayList.get(i).getCategoryId() == id) {
                            oneTimeFeeHeadList.add(feeCostBeanArrayList.get(i));
                        } else if (feeCostBeanArrayList.get(i).getFeeType() == 2 && feeCostBeanArrayList.get(i).getCategoryId() == id) {
                            annualFeeHeadList.add(feeCostBeanArrayList.get(i));
                        } else if (feeCostBeanArrayList.get(i).getFeeType() == 3 && feeCostBeanArrayList.get(i).getCategoryId() == id) {
                            monthlyFeeHeadList.add(feeCostBeanArrayList.get(i));
                        } else if (feeCostBeanArrayList.get(i).getFeeType() == 4) {
                            //CHECK
                            //&& feeCostBeanArrayList.get(i).getCategoryId() == id
                             transportionFeeHeadList.add(feeCostBeanArrayList.get(i));
                       }
                    }
                }

                feeAdapterTwoOT = new FeeAdapterThree(FeeSelectActivty.this,
                        oneTimeFeeHeadList, (PayFeeInterface) FeeSelectActivty.this);
                feeAdapterTwoAT = new FeeAdapterThree(FeeSelectActivty.this,
                        annualFeeHeadList, (PayFeeInterface) FeeSelectActivty.this);
                feeAdapterTwoMT = new FeeAdapterThree(FeeSelectActivty.this,
                        monthlyFeeHeadList, (PayFeeInterface) FeeSelectActivty.this);
                //transportation signal
                feeAdapterTwoTT = new FeeAdapterThree(FeeSelectActivty.this,
                        transportionFeeHeadList, 1, (PayFeeInterface) FeeSelectActivty.this,
                        true, routesBeanArrayList);

                listViewFeesOT.setAdapter(feeAdapterTwoOT);
                listViewFeesAT.setAdapter(feeAdapterTwoAT);
                listViewFeesMT.setAdapter(feeAdapterTwoMT);
                listViewFeesTT.setAdapter(feeAdapterTwoTT);

                edtAdvanceCashFeeSelect = findViewById(R.id.edtAdvanceCashFeeSelect);
                editTextChequeFeeSelect = findViewById(R.id.editTextChequeFeeSelect);
                edtChequeDate = findViewById(R.id.edtChequeDate);

                edtNEFTFeeSelect = findViewById(R.id.edtNEFTFeeSelect);
                edtNEFTDateFeeSelect = findViewById(R.id.edtNEFTDateFeeSelect);
                editTextRemarksFeeSelect = findViewById(R.id.editTextRemarksFeeSelect);
                editTextJoinFeeSelect = findViewById(R.id.editTextJoinFeeSelect);

                radioCashFeeSelect = findViewById(R.id.radioCashFeeSelect);
                radioChequeFeeSelect = findViewById(R.id.radioChequeFeeSelect);
                radioNEFTFeeSelect = findViewById(R.id.radioNEFTFeeSelect);


                editTextJoinFeeSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        DatePickerDialog datePickerDialog = new DatePickerDialog(FeeSelectActivty.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                joinDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                //editTextJoin.setText(joinDate);
                                editTextJoinFeeSelect.setText(joinDate);
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                    }
                });
                edtChequeDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        DatePickerDialog datePickerDialog = new DatePickerDialog(FeeSelectActivty.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edtChequeDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                    }
                });
                edtNEFTDateFeeSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar calendar = Calendar.getInstance();
                        DatePickerDialog datePickerDialog = new DatePickerDialog(FeeSelectActivty.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edtNEFTDateFeeSelect.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                    }
                });
                amountViews();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnFeeSelectPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttUtil.progressDialog(FeeSelectActivty.this);
//                oneTimeFeeHeadListDialog.clear();
//                oneTimeFeeHeadListDialog.addAll(oneTimeFeeHeadList);
//                annualFeeHeadListDialog.clear();
//                annualFeeHeadListDialog.addAll(annualFeeHeadList);
//                monthlyFeeHeadListDialog.clear();
//                monthlyFeeHeadListDialog.addAll(monthlyFeeHeadList);
//                monthlyFeeHeadListDialog.clear();
//                monthlyFeeHeadListDialog.addAll(monthlyFeeHeadList);
                if (feeHeadIndicator == 0) {
                    Log.i("test", "1");
                    AttUtil.pd(0);
                    Toast.makeText(FeeSelectActivty.this, "Error: Select atleast one fee head", Toast.LENGTH_SHORT).show();
                } else if (edtAdvanceCashFeeSelect.getText().toString().length() == 0) {
                    Log.i("test", "2");
                    AttUtil.pd(0);
                    Toast.makeText(FeeSelectActivty.this, "Error: Invalid Advance Amount", Toast.LENGTH_SHORT).show();
                }
//                else if (transportionFeeHeadList.size() > 0) {
//                    Log.i("test", "3");
//                    if (transactionAmountSelect == 0) {
//                        AttUtil.pd(0);
//                        Toast.makeText(FeeSelectActivty.this, "Error: Invalid Transportation Amount", Toast.LENGTH_SHORT).show();
//                    }
//                }
                else if (editTextJoinFeeSelect.getText().toString().length() == 0) {
                    AttUtil.pd(0);
                    Toast.makeText(FeeSelectActivty.this, "Error: Invalid Joining Date", Toast.LENGTH_SHORT).show();
                } else if (!radioCashFeeSelect.isChecked() && !radioChequeFeeSelect.isChecked() && !radioNEFTFeeSelect.isChecked()) {
                    AttUtil.pd(0);
                    Toast.makeText(FeeSelectActivty.this, "Error: Select One Payment Option", Toast.LENGTH_SHORT).show();
                } else if (radioChequeFeeSelect.isChecked() && editTextChequeFeeSelect.getText().toString().trim().length() < 6) {
                    AttUtil.pd(0);
                    Toast.makeText(FeeSelectActivty.this, "Error: Invalid Cheque Number", Toast.LENGTH_SHORT).show();
                } else if (radioChequeFeeSelect.isChecked() && edtChequeDate.getText().toString().trim().length() == 0) {
                    AttUtil.pd(0);
                    Toast.makeText(FeeSelectActivty.this, "Error: Invalid Cheque Date", Toast.LENGTH_SHORT).show();
                } else if (radioNEFTFeeSelect.isChecked() && edtNEFTFeeSelect.getText().toString().trim().length() ==0){
                    AttUtil.pd(0);
                    Toast.makeText(FeeSelectActivty.this, "Error: Invalid Transaction Number", Toast.LENGTH_SHORT).show();
                } else if (radioNEFTFeeSelect.isChecked() && edtNEFTDateFeeSelect.getText().toString().trim().length() == 0) {
                    AttUtil.pd(0);
                    Toast.makeText(FeeSelectActivty.this, "Error: Invalid Date", Toast.LENGTH_SHORT).show();
                }/*else if(mnthSelected.size()!=Integer.parseInt(textViewMonthsDialog.getText().toString().trim())){
                    Toast.makeText(FeeSelectActivty.this, "Error: Invalid Selected Months.Change amount of monthly fee head.", Toast.LENGTH_SHORT).show();
                }*/ else {
                    emailNoteDialog();
                }
                //payAmountDialog();
            }
        });


    }

    EditText editText;
    void emailNoteDialog() {
        final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);
        dialogBuilder.setTitle("Note For E-mail");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 20, 10, 0);
        editText = new EditText(this);
        editText.setHint("Note");

        layout.addView(editText, params);
        dialogBuilder.setView(layout);
        dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (AttUtil.isNetworkConnected(FeeSelectActivty.this)){
                    AttUtil.pd(1);
                    getFeePaidId();
                }
                //else
                    //submitNetConnect();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", null);
        android.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

     void getFeePaidId() {
        db.collection(Constants.fee_paid_collection).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.size()>0){
                    for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                        int chkId = doc.getLong("feePaidId").intValue();
                        listId.add(chkId);
                        //feePaidId = doc.getLong("feePaidId").intValue();
                    }
                    feePaidId = Collections.max(listId);
                    feePaidId = feePaidId + 1;
                }else {
                    feePaidId = 1;
                }
               // Toast.makeText(FeeSelectActivty.this,""+feePaidId,Toast.LENGTH_SHORT).show();
                submitFees();
            }
        });
    }

    void submitFees() {
         for(int i=0;i<feeCostBeanArrayList.size();i++){
             if(feeCostBeanArrayList.get(i).isChecked()){
                 feeCostBean = new FeeCostBean();
                 feeCostBean.setHeadId(feeCostBeanArrayList.get(i).getHeadId());
                 feeCostBean.setHeadName(feeCostBeanArrayList.get(i).getHeadName());
                 feeCostBean.setBranchId(feeCostBeanArrayList.get(i).getBranchId());
                 feeCostBean.setStudentId(studentId);
                 feeCostBean.setCost(feeCostBeanArrayList.get(i).getCost());
                 feeCostBean.setSellingAmount(feeCostBeanArrayList.get(i).getSellingAmount());
                 feeCostBean.setSellingCost(feeCostBeanArrayList.get(i).getSellingAmount());
                 feeCostBean.setDiscount(feeCostBeanArrayList.get(i).getDiscount());
                 feeCostBean.setFeeType(feeCostBeanArrayList.get(i).getFeeType());
                 feeCostBean.setAdmissionId(studentId);
                 feeCostBean.setFeePaidId(feePaidId);
                 feePaidId++;
                 if(feeCostBeanArrayList.get(i).getFeeType()!=4){
                     feeCostBean.setCategoryId(feeCostBeanArrayList.get(i).getCategoryId());
                     feeCostBean.setCategoryName(feeCostBeanArrayList.get(i).getCategoryName());
                 }else {
                     feeCostBean.setRouteId(feeCostBeanArrayList.get(i).getRouteId());
                 }
                 addToDB();
             }
        }
        getLastAccountId();

    }

     void addToDB() {
        db.collection(Constants.fee_paid_collection).document(String.valueOf(feeCostBean.getFeePaidId())).set(feeCostBean)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
    }

     void getLastAccountId() {
        db.collection(Constants.student_account_collection).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.size()>0){
                    for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                        int chkId = doc.getLong("accountId").intValue();
                        accountListId.add(chkId);
                    }
                    accountId = Collections.max(accountListId);
                    accountId = accountId + 1;
                }else {
                    accountId = 1;
                }
                setDataToAccountBean();
            }
        });
    }

    void setDataToAccountBean() {
         AccountBean accountBean = new AccountBean();
         accountBean.setAccountId(accountId);
         accountBean.setStudentId(studentId);
         accountBean.setAdmissionId(studentId);
         accountBean.setAmount(Integer.parseInt(edtAdvanceCashFeeSelect.getText().toString().trim()));
         if(radioCashFeeSelect.isChecked()){
             accountBean.setInOutType(1);
             accountBean.setChequeDate("");
             accountBean.setChequeNo("");
         }
         if(radioChequeFeeSelect.isChecked()){
             accountBean.setInOutType(0);
             accountBean.setChequeNo(editTextChequeFeeSelect.getText().toString().trim());
             accountBean.setChequeDate(edtChequeDate.getText().toString().trim());
         }
         if(radioNEFTFeeSelect.isChecked()){
             accountBean.setInOutType(2);
             accountBean.setNeftTransaction(edtNEFTFeeSelect.getText().toString().trim());
             accountBean.setNeftDate(edtNEFTDateFeeSelect.getText().toString().trim());
         }
         accountBean.setInOutSignal(0);
         accountBean.setTime(currentTime);
         accountBean.setDate(currentDate);
         accountBean.setSignal(1);

         insertDatatoDB(accountBean);
    }

     void insertDatatoDB(AccountBean accountBean) {
        db.collection(Constants.student_account_collection).document(String.valueOf(accountBean.getAccountId()))
                .set(accountBean).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                insertStudentDataToDB();
            }
        });
    }

     void insertStudentDataToDB() {
        studentBeanDetails.setFeeCategorySelected(feeCategoryBeanArrayList.get(spinnerFeeCategoryPos - 1).getFeeCategoryId());
        studentBeanDetails.setAdmissionId(studentId);
        studentBeanDetails.setJoinDate(editTextJoinFeeSelect.getText().toString().trim());
        studentBeanDetails.setAdmStatus(1);
        studentBeanDetails.setRemarks(editTextRemarksFeeSelect.getText().toString().trim());
        studentBeanDetails.setAccountAmount(Integer.parseInt(edtAdvanceCashFeeSelect.getText().toString().trim()));
        //DATE CHECK
        db.collection(Constants.student_collection).document(String.valueOf(studentBeanDetails.getStudentId()))
                .set(studentBeanDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AttUtil.pd(0);
                alertSuccess();
            }
        });
    }



    void alertSuccess() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Fees plan selected");
        builder.setCancelable(false);
        builder.setPositiveButton("DONE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                setResult(201,new Intent().putExtra(AttUtil.TAG_STUDENTBEAN,studentBeanDetails));
                FeeSelectActivty.this.finish();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_select_activty);
        //admissionId = getIntent().getIntExtra("admissionId", 0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        studentId = getIntent().getIntExtra("studentId", 0);
        activitySignal = getIntent().getIntExtra("activitySignal", 0);
        studentBeanDetails = (StudentBean) getIntent().getSerializableExtra("studentbean");
        initViews();
        getDate(c_year,c_month,c_day);
        getTime(hour,mint);
        AttUtil.progressDialog(this);
        AttUtil.pd(1);
        fetchFeeCategories();
    }

     void fetchFeeCategories() {
        db.collection(Constants.fee_category_collection).whereEqualTo("branchId",studentBeanDetails.getBranchId())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    feeCategoryBean = doc.toObject(FeeCategoryBean.class);
                    feeCategoryBeanArrayList.add(feeCategoryBean);
                }
                feeCategoryList.add("--Select Fee Structure--");
                for (int i = 0; i < feeCategoryBeanArrayList.size(); i++)
                    feeCategoryList.add(feeCategoryBeanArrayList.get(i).getCategoryName());
                ArrayAdapter adapter = new ArrayAdapter(FeeSelectActivty.this, android.R.layout.simple_spinner_item, feeCategoryList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerFeeCategory.setAdapter(adapter);
               // fetchFeeCostList();
                if(feeCategoryBeanArrayList.size()>0){
                    fetchRoutesList();
                }else{
                    AttUtil.pd(0);
                    Toast.makeText(FeeSelectActivty.this,"No fee structure found!!",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

     void fetchFeeCostList() {
        db.collection(Constants.fee_head_collection).whereEqualTo("branchId",studentBeanDetails.getBranchId())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    feeHeadBean = doc.toObject(FeeHeadBean.class);
                    feeHeadBeanArrayList.add(feeHeadBean);
                }

                for(int i=0;i<feeHeadBeanArrayList.size();i++){
                    if(feeHeadBeanArrayList.get(i).getFeeType() != 4){
                        for(int j=0;j<feeHeadBeanArrayList.get(i).getFeeCostBeanArrayList().size();j++){
                            feeCostBeanAdminAppArrayList.add(feeHeadBeanArrayList.get(i).getFeeCostBeanArrayList().get(j));
                        }
                        //setFeeCostBean();
                    }else{
                        feeCostBean = new FeeCostBean();
                        feeCostBean.setBranchId(feeHeadBeanArrayList.get(i).getBranchId());
                        feeCostBean.setHeadId(feeHeadBeanArrayList.get(i).getHeadId());
                        feeCostBean.setFeeType(feeHeadBeanArrayList.get(i).getFeeType());
                        feeCostBean.setHeadName(feeHeadBeanArrayList.get(i).getHeadName());
                        addToFeeCostArrayList();
                    }
                }
                setFeeCostBean();

                /*for(int i=0;i<feeCostBeanAdminAppArrayList.size();i++){
                        feeCostBean = new FeeCostBean();
                        feeCostBean.setBranchId(feeCostBeanAdminAppArrayList.get(i).getBranchId());
                        feeCostBean.setCategoryId(feeCostBeanAdminAppArrayList.get(i).getCategoryId());
                        feeCostBean.setCategoryName(feeCostBeanAdminAppArrayList.get(i).getCategoryName());
                        feeCostBean.setHeadId(feeCostBeanAdminAppArrayList.get(i).getHeadId());
                        feeCostBean.setCost(feeCostBeanAdminAppArrayList.get(i).getCost());
                        getFeeHeadDetails();
                   // feeCostBeanArrayList.add(feeCostBean);
                }*/
            }
        });
    }

     void setFeeCostBean() {
        for(int i=0;i<feeCostBeanAdminAppArrayList.size();i++){
            feeCostBean = new FeeCostBean();
            feeCostBean.setBranchId(feeCostBeanAdminAppArrayList.get(i).getBranchId());
            feeCostBean.setCategoryId(feeCostBeanAdminAppArrayList.get(i).getCategoryId());
            feeCostBean.setCategoryName(feeCostBeanAdminAppArrayList.get(i).getCategoryName());
            feeCostBean.setHeadId(feeCostBeanAdminAppArrayList.get(i).getHeadId());
            feeCostBean.setCost(feeCostBeanAdminAppArrayList.get(i).getCost());
            getFeeHeadDetails();
        }
    }

    void getFeeHeadDetails() {
        for(int i=0;i<feeHeadBeanArrayList.size();i++){
            if(feeCostBean.getHeadId()==feeHeadBeanArrayList.get(i).getHeadId()){
                feeCostBean.setHeadName(feeHeadBeanArrayList.get(i).getHeadName());
                feeCostBean.setFeeType(feeHeadBeanArrayList.get(i).getFeeType());
            }
        }
        addToFeeCostArrayList();
    }

     void addToFeeCostArrayList() {
         feeCostBeanArrayList.add(feeCostBean);
         AttUtil.pd(0);
    }

    void fetchRoutesList() {
        db.collection(Constants.routes_collection).whereEqualTo("branchId",studentBeanDetails.getBranchId())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    routesBean = doc.toObject(RoutesBean.class);
                    routesBeanArrayList.add(routesBean);
                }
                fetchFeeCostList();
            }
        });
    }

    void amountViews() {
        editTextChequeFeeSelect.setEnabled(false);
        edtChequeDate.setEnabled(false);
        edtNEFTFeeSelect.setEnabled(false);
        edtNEFTDateFeeSelect.setEnabled(false);

        radioChequeFeeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioChequeFeeSelect.isChecked()) {
                    editTextChequeFeeSelect.setEnabled(true);
                    edtChequeDate.setEnabled(true);
                    edtNEFTFeeSelect.setEnabled(false);
                    edtNEFTDateFeeSelect.setEnabled(false);
                }

            }
        });

        radioNEFTFeeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioNEFTFeeSelect.isChecked()) {
                    editTextChequeFeeSelect.setEnabled(false);
                    edtChequeDate.setEnabled(false);
                    edtNEFTFeeSelect.setEnabled(true);
                    edtNEFTDateFeeSelect.setEnabled(true);
                }

            }
        });

        radioCashFeeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioCashFeeSelect.isChecked()) {
                    editTextChequeFeeSelect.setEnabled(false);
                    edtChequeDate.setEnabled(false);
                    edtNEFTFeeSelect.setEnabled(false);
                    edtNEFTDateFeeSelect.setEnabled(false);
                }

            }
        });
    }


    @Override
    public void setPayAmount() {

    }

    @Override
    public void setSelectedAmount() {
        oneTimeAmountSelect = 0;
        annualAmountSelect = 0;
        monthlyAmountSelect = 0;
        transactionAmountSelect = 0;
        for (int i = 0; i < oneTimeFeeHeadList.size(); i++) {
            if (oneTimeFeeHeadList.get(i).isChecked())
                oneTimeAmountSelect = oneTimeAmountSelect + (oneTimeFeeHeadList.get(i).getCost() - oneTimeFeeHeadList.get(i).getDiscount());
        }
        for (int i = 0; i < annualFeeHeadList.size(); i++) {
            if (annualFeeHeadList.get(i).isChecked())
                annualAmountSelect = annualAmountSelect + (annualFeeHeadList.get(i).getCost() - annualFeeHeadList.get(i).getDiscount());
        }
        for (int i = 0; i < monthlyFeeHeadList.size(); i++) {
            if (monthlyFeeHeadList.get(i).isChecked())
                monthlyAmountSelect = monthlyAmountSelect + (monthlyFeeHeadList.get(i).getCost() - monthlyFeeHeadList.get(i).getDiscount());
        }
        for (int i = 0; i < transportionFeeHeadList.size(); i++) {
            if (transportionFeeHeadList.get(i).isChecked() && transportionFeeHeadList.get(i).getFeeType() == 4)
                transactionAmountSelect = transactionAmountSelect + transportionFeeHeadList.get(i).getSellingAmount();
        }
        textViewAmountOne.setText("" + oneTimeAmountSelect + "");
        textViewAmountAnnual.setText("" + annualAmountSelect + "");
        textViewAmountMonth.setText("" + monthlyAmountSelect + "");
        textViewAmountTransportation.setText("" + transactionAmountSelect + "");
    }

    @Override
    public int getSelecedMonths() {
        return mnthSelected.size();
    }

    int feeHeadIndicator = 0;

    @Override
    public void feeHeadCheck(int i) {
        if (i == 1)
            feeHeadIndicator += 1;
        else
            feeHeadIndicator -= 1;
    }

    @Override
    public int getAdapterType() {
        return 0;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            setResult(201);
            finish();
        }
       /* if (item.getItemId() == 1) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(FeeSelectActivty.this);
            if (preferences.getInt(AttUtil.shpReceiptSignal, 0) == 1 && preferences.getInt(AttUtil.shpFeeStructureSignal, 0) == 1) {
                dialog.setMessage("Receipt are being send.\n" +
                        "Fee structure are being send.");
            } else if (preferences.getInt(AttUtil.shpReceiptSignal, 0) == 1 && preferences.getInt(AttUtil.shpFeeStructureSignal, 0) == 0) {
                dialog.setMessage("Receipt are being send.\n" +
                        "Fee structure are not being send.");
            } else if (preferences.getInt(AttUtil.shpReceiptSignal, 0) == 0 && preferences.getInt(AttUtil.shpFeeStructureSignal, 0) == 1) {
                dialog.setMessage("Receipt are not being send.\n" +
                        "Fee structure are being send.");
            } else if (preferences.getInt(AttUtil.shpReceiptSignal, 0) == 0 && preferences.getInt(AttUtil.shpFeeStructureSignal, 0) == 0) {
                dialog.setMessage("Receipt are not being send.\n" +
                        "Fee structure are not being send.");
            }

            dialog.setCancelable(false);
            dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.create().show();
        }*/
        return super.onOptionsItemSelected(item);
    }
}
