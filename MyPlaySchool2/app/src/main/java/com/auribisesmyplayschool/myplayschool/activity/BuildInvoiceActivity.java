package com.auribisesmyplayschool.myplayschool.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adapter.FeeAdapterThree;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BranCourBean;
import com.auribisesmyplayschool.myplayschool.bean.AccountBean;
import com.auribisesmyplayschool.myplayschool.bean.BatchBean;
import com.auribisesmyplayschool.myplayschool.bean.FeeCostBean;
import com.auribisesmyplayschool.myplayschool.bean.StudentBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.auribisesmyplayschool.myplayschool.classes.PayFeeInterface;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class BuildInvoiceActivity extends AppCompatActivity implements PayFeeInterface {
    ArrayList<String> batchNameArray, spinList;
    ArrayList<BatchBean> batchBeanArrayList,batchBeanArrayListActive;
    ArrayList<Integer> batchIdArray, branCourIdArray;
    private Spinner spnSelectBatch;
    private EditText edtClassStartingDate;
    private int posBatch = 0, reqCode = 0, responseSignal = 0, callActivitySignal = 0, totalBalance = 0;
    private StudentBean studentBean;
    private SharedPreferences preferences;
    private ArrayList<FeeCostBean> feeCostBeanArrayList,
            oneTimeFeeHeadList = new ArrayList<>(),
            annualFeeHeadList = new ArrayList<>(),
            monthlyFeeHeadList = new ArrayList<>(),
            transportFeeHeadList = new ArrayList<>();
    private TextView tvFeeStructureName, textViewAmountOne, textViewAmountAnnual,textViewAmountTransport,
            textViewAmountMonth, tvMultipleSignM, tvMultipleMonthsM, tvMultipleSignA, tvMultipleMonthsA,
            tvMultipleSignT,tvMultipleMonthsT,tvTEndingDate,tvTStartingDate;
    private FeeAdapterThree feeAdapterTwoOT, feeAdapterTwoAT, feeAdapterTwoMT, feeAdapterTwoTT;
    private RecyclerView listViewFeesOT, listViewFeesAT, listViewFeesMT,listViewFeesTT;
    private Button btnBuildInvoice;
    String stringAStartingDate = "", stringAEndingDate = "", stringMStartingDate = "", stringMEndingDate = ""
            , stringTStartingDate = "", stringTEndingDate = "";
    private TextView tvMStartingDate, tvMEndingDate, tvAStartingDate, tvAEndingDate, tvBalanceAccount;
    private FeeCostBean feeCostBeanInvoice;
    private ArrayList<AccountBean> accountBeanArrayList;
    private LinearLayout rlBalanceAccount;
    private int oneTimeAmountSelect = 0, annualAmountSelect = 0, monthlyAmountSelect = 0, transportAmountSelect = 0, noOfMonthsM = 0, noOfMonthsT = 0, noOfMonthsA = 0;
    private String edtClassStartingDateString = "";
    private SimpleDateFormat simpleDateFormat;
    FirebaseFirestore db;
    FeeCostBean feeCostBean;
    int check,activeStudents;
    ArrayList<BranCourBean> courBeanArrayList;
    BatchBean batchBean;
    AccountBean accountBean;
    ArrayList<Integer> listAccountId;
    int accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_invoice);
        db = FirebaseFirestore.getInstance();
        AttUtil.progressDialog(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Generate Invoice");
        initviews();
        if (AttUtil.isNetworkConnected(this)){
            AttUtil.pd(1);
            if(callActivitySignal!=2){
                retrieveIntoDb();
            }else{
                retrieveInvoice();
            }

        }/*else
            retrieveNetConnect();*/
    }

     void retrieveInvoice() {
        db.collection(Constants.fee_paid_collection).whereEqualTo("studentId",studentBean.getStudentId())
                .whereEqualTo("isPayFee",false).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    feeCostBeanInvoice = doc.toObject(FeeCostBean.class);
                    feeCostBeanArrayList.add(feeCostBeanInvoice);
                }
                setDataToLayout();
            }
        });
    }

    void retrieveIntoDb() {
        db.collection(Constants.fee_paid_collection).whereEqualTo("studentId",studentBean.getStudentId())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    feeCostBean = doc.toObject(FeeCostBean.class);
                    feeCostBeanArrayList.add(feeCostBean);
                }
                setDataToLayout();
            }
        });
    }

     void setDataToLayout() {
         if (feeCostBeanArrayList.size() > 0) {
             for (int i = 0; i < feeCostBeanArrayList.size(); i++) {
                // if (feeCostBeanArrayList.get(i).getFeeStuId() > 0) {
                     if (feeCostBeanArrayList.get(i).getFeeType() == 1) {
                         oneTimeFeeHeadList.add(feeCostBeanArrayList.get(i));
                     } else if (feeCostBeanArrayList.get(i).getFeeType() == 2) {
                         annualFeeHeadList.add(feeCostBeanArrayList.get(i));
                         if (callActivitySignal == 1) {
                             try {
                                 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                 Date date1 = formatter.parse(feeCostBeanArrayList.get(i).getEndingDate());
                                 Date date2 = simpleDateFormat.parse(stringAStartingDate);
                                 if (date1.compareTo(date2) > 0) {
                                     String s = simpleDateFormat.format(date1);
                                     Calendar c = Calendar.getInstance();
                                     try {
                                         c.setTime(simpleDateFormat.parse(s));
                                     } catch (ParseException e) {
                                         e.printStackTrace();
                                     }
                                     c.add(Calendar.DATE, 1);
                                     stringAStartingDate = simpleDateFormat.format(c.getTime());
                                     c.add(Calendar.YEAR, 1);
                                     c.add(Calendar.DAY_OF_MONTH, -1);
                                     stringAEndingDate = simpleDateFormat.format(c.getTime());
                                     noOfMonthsA = 12;
                                     calculateAnnualAmount();
                                 }
                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
                         } else if (callActivitySignal == 0) {
                             try {
                                 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                 Date date2 = simpleDateFormat.parse(stringAStartingDate);
                                 String s = simpleDateFormat.format(date2);
                                 Calendar c = Calendar.getInstance();
                                 try {
                                     c.setTime(simpleDateFormat.parse(s));
                                 } catch (ParseException e) {
                                     e.printStackTrace();
                                 }
                                 c.add(Calendar.YEAR, 1);
                                 c.add(Calendar.DAY_OF_MONTH, -1);
                                 stringAEndingDate = simpleDateFormat.format(c.getTime());
                                 noOfMonthsA = 12;
                                 calculateAnnualAmount();
                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
                         }
                     } else if (feeCostBeanArrayList.get(i).getFeeType() == 3) {
                         monthlyFeeHeadList.add(feeCostBeanArrayList.get(i));
                         Log.i("test", monthlyFeeHeadList.toString());
                         try {
                             SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                             Date date1 = formatter.parse(feeCostBeanArrayList.get(i).getEndingDate());
                             Date date2 = simpleDateFormat.parse(stringMStartingDate);
                             if (date1.compareTo(date2) > 0) {
                                 String s = simpleDateFormat.format(date1);
                                 Calendar c = Calendar.getInstance();
                                 try {
                                     c.setTime(simpleDateFormat.parse(s));
                                 } catch (ParseException e) {
                                     e.printStackTrace();
                                 }
                                 c.add(Calendar.DATE, 1);
                                 stringMStartingDate = simpleDateFormat.format(c.getTime());
                             }
                         } catch (Exception e) {
                             e.printStackTrace();
                         }
                     } else if (feeCostBeanArrayList.get(i).getFeeType() == 4) {
                         transportFeeHeadList.add(feeCostBeanArrayList.get(i));
                         Log.i("test", transportFeeHeadList.toString());
                         try {
                             SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                             Date date1 = formatter.parse(feeCostBeanArrayList.get(i).getEndingDate());
                             Date date2 = simpleDateFormat.parse(stringTStartingDate);
                             if (date1.compareTo(date2) > 0) {
                                 String s = simpleDateFormat.format(date1);
                                 Calendar c = Calendar.getInstance();
                                 try {
                                     c.setTime(simpleDateFormat.parse(s));
                                 } catch (ParseException e) {
                                     e.printStackTrace();
                                 }
                                 c.add(Calendar.DATE, 1);
                                 stringTStartingDate = simpleDateFormat.format(c.getTime());
                             }
                         } catch (Exception e) {
                             e.printStackTrace();
                         }
                     }
                 //}
             }
             tvAStartingDate.setText(stringAStartingDate);
             tvAEndingDate.setText(stringAEndingDate);
             tvMStartingDate.setText(stringMStartingDate);
             tvTStartingDate.setText(stringTStartingDate);

             for(int i=0;i<feeCostBeanArrayList.size();i++){
                     tvFeeStructureName.setText("Fee Structure: " + feeCostBeanArrayList.get(i).getCategoryName());
             }

             if (callActivitySignal == 2) {
                 feeAdapterTwoOT = new FeeAdapterThree(BuildInvoiceActivity.this,
                         oneTimeFeeHeadList, 4, BuildInvoiceActivity.this);
                 feeAdapterTwoAT = new FeeAdapterThree(BuildInvoiceActivity.this,
                         annualFeeHeadList, 4, BuildInvoiceActivity.this);
                 feeAdapterTwoMT = new FeeAdapterThree(BuildInvoiceActivity.this,
                         monthlyFeeHeadList, 4, BuildInvoiceActivity.this);
                 feeAdapterTwoTT = new FeeAdapterThree(BuildInvoiceActivity.this,
                         transportFeeHeadList,4, BuildInvoiceActivity.this);
             } else {
                 feeAdapterTwoOT = new FeeAdapterThree(BuildInvoiceActivity.this,
                         oneTimeFeeHeadList, 3, BuildInvoiceActivity.this);
                 feeAdapterTwoAT = new FeeAdapterThree(BuildInvoiceActivity.this,
                         annualFeeHeadList, 3, BuildInvoiceActivity.this);
                 feeAdapterTwoMT = new FeeAdapterThree(BuildInvoiceActivity.this,
                         monthlyFeeHeadList,3, BuildInvoiceActivity.this);
                 feeAdapterTwoTT = new FeeAdapterThree(BuildInvoiceActivity.this,
                         transportFeeHeadList,3, BuildInvoiceActivity.this);
             }
             listViewFeesOT.setAdapter(feeAdapterTwoOT);
             listViewFeesAT.setAdapter(feeAdapterTwoAT);
             listViewFeesMT.setAdapter(feeAdapterTwoMT);
             listViewFeesTT.setAdapter(feeAdapterTwoTT);


             if (callActivitySignal == 2) {
                 db.collection(Constants.student_account_collection).whereEqualTo("studentId",studentBean.getStudentId())
                         .whereEqualTo("signal",1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                     @Override
                     public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                         for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                             accountBean = doc.toObject(AccountBean.class);
                             accountBeanArrayList.add(accountBean);
                         }
                         totalBalance = 0;
                         for (int i = 0; i < accountBeanArrayList.size(); i++) {
                             if (accountBeanArrayList.get(i).getInOutSignal() == 0 &&
                                     accountBeanArrayList.get(i).getSignal() == 1)
                                 totalBalance = totalBalance + accountBeanArrayList.get(i).getAmount();
                             if (accountBeanArrayList.get(i).getInOutSignal() == 1)
                                 totalBalance = totalBalance - accountBeanArrayList.get(i).getAmount();
                         }
                         tvBalanceAccount.setText("Balance: " + totalBalance);
                         AttUtil.pd(0);
                     }
                 });

             }else{
                 AttUtil.pd(0);
             }

         }
    }

    void initviews() {
        batchBean = new BatchBean();
        batchBeanArrayList = (ArrayList<BatchBean>) getIntent().getSerializableExtra(AttUtil.BATCH_LIST);
        courBeanArrayList = (ArrayList<BranCourBean>) getIntent().getSerializableExtra(AttUtil.KEY_COURSE_ARRAYLIST);
        accountBean = new AccountBean();
        batchBeanArrayListActive = new ArrayList<>();
         simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
         preferences = getSharedPreferences(AttUtil.shpREG, MODE_PRIVATE);
         tvFeeStructureName =  findViewById(R.id.tvFeeStructureName);
         listViewFeesOT =  findViewById(R.id.listViewFeesOne);
         listViewFeesAT =  findViewById(R.id.listViewFeesAnnual);
         listViewFeesMT =  findViewById(R.id.listViewFeesMonth);
         listViewFeesTT =  findViewById(R.id.listViewFeesTransport);
         listViewFeesOT.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
         listViewFeesAT.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
         listViewFeesMT.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
         listViewFeesTT.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
         btnBuildInvoice =  findViewById(R.id.btnBuildInvoice);

         tvMultipleSignM =  findViewById(R.id.tvMultipleSignM);
         tvMultipleMonthsM =  findViewById(R.id.tvMultipleMonthsM);
         tvMultipleSignA =  findViewById(R.id.tvMultipleSignA);
         tvMultipleMonthsA =  findViewById(R.id.tvMultipleMonthsA);
         tvMultipleSignT =  findViewById(R.id.tvMultipleSignT);
         tvMultipleMonthsT =  findViewById(R.id.tvMultipleMonthsT);

         textViewAmountAnnual =  findViewById(R.id.textViewAmountAnnual);
         textViewAmountMonth =  findViewById(R.id.textViewAmountMonth);
         textViewAmountTransport =  findViewById(R.id.textViewAmountTransport);
         textViewAmountOne =  findViewById(R.id.textViewAmountOne);

         tvAStartingDate =  findViewById(R.id.tvAStartingDate);
         tvAEndingDate =  findViewById(R.id.tvAEndingDate);
         tvMStartingDate =  findViewById(R.id.tvMStartingDate);
         tvMEndingDate =  findViewById(R.id.tvMEndingDate);
         tvTStartingDate =  findViewById(R.id.tvTStartingDate);
         tvTEndingDate =  findViewById(R.id.tvTEndingDate);

         tvBalanceAccount =  findViewById(R.id.tvBalanceAccount);

         feeCostBeanArrayList = new ArrayList<>();
         //studentBean = new StudentBean();
         accountBeanArrayList = new ArrayList<>();
         studentBean = (StudentBean) getIntent().getSerializableExtra(AttUtil.TAG_STUDENTBEAN);
         callActivitySignal = getIntent().getIntExtra("callActivitySignal", 0);

         if (callActivitySignal == 1) {
             findViewById(R.id.cvBatchAndStartingDate).setVisibility(View.GONE);
             stringAStartingDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
             stringMStartingDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
             stringTStartingDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
             btnBuildInvoice.setText("Send");
             tvMultipleMonthsM.setVisibility(View.VISIBLE);
             tvMultipleSignM.setVisibility(View.VISIBLE);
             tvMultipleMonthsT.setVisibility(View.VISIBLE);
             tvMultipleSignT.setVisibility(View.VISIBLE);
             tvMultipleMonthsA.setVisibility(View.VISIBLE);
             //tvMultipleSignA.setVisibility(View.VISIBLE);
         } else if (callActivitySignal == 2) {
             getSupportActionBar().setTitle("Pay Fees");
             btnBuildInvoice.setText("Pay Fees");
             rlBalanceAccount =  findViewById(R.id.rlBalanceAccount);
             findViewById(R.id.cvBatchAndStartingDate).setVisibility(View.GONE);
             feeCostBeanInvoice = new FeeCostBean();
             //feeCostBeanInvoice = (FeeCostBean) getIntent().getSerializableExtra(AttUtil.TAG_INVOICEBEAN);
            // Log.i("test", feeCostBeanInvoice.toString());
             TextView eTextNet =  findViewById(R.id.eTextNet);
             eTextNet.setText("NET\nREC\nBAL");
             TextView eTextNet2 =  findViewById(R.id.eTextNet2);
             eTextNet2.setText("NET\nREC\nBAL");
             TextView eTextNet3 =  findViewById(R.id.eTextNet3);
             eTextNet3.setText("NET\nREC\nBAL");
             TextView eTextNet4 =  findViewById(R.id.eTextNet4);
             eTextNet4.setText("NET\nREC\nBAL");
             TextView eTextRecv =  findViewById(R.id.eTextRecv);
             eTextRecv.setVisibility(View.VISIBLE);
             eTextRecv.setText("AMT");
             TextView eTextRecv2 =  findViewById(R.id.eTextRecv2);
             eTextRecv2.setVisibility(View.VISIBLE);
             eTextRecv2.setText("AMT");
             TextView eTextRecv3 =  findViewById(R.id.eTextRecv3);
             eTextRecv3.setVisibility(View.VISIBLE);
             eTextRecv3.setText("AMT");
             TextView eTextRecv4 =  findViewById(R.id.eTextRecv4);
             eTextRecv4.setVisibility(View.VISIBLE);
             eTextRecv4.setText("AMT");

             findViewById(R.id.viewMonthly).setVisibility(View.GONE);
             findViewById(R.id.viewAnnual).setVisibility(View.GONE);
             findViewById(R.id.viewTransport).setVisibility(View.GONE);
             findViewById(R.id.cvBalanceAccount).setVisibility(View.VISIBLE);
             tvAStartingDate.setVisibility(View.GONE);
             tvAEndingDate.setVisibility(View.GONE);
             tvMStartingDate.setVisibility(View.GONE);
             tvMEndingDate.setVisibility(View.GONE);
             tvTStartingDate.setVisibility(View.GONE);
             tvTEndingDate.setVisibility(View.GONE);

         } else {
             tvMultipleMonthsM.setVisibility(View.VISIBLE);
             tvMultipleSignM.setVisibility(View.VISIBLE);
             tvMultipleMonthsT.setVisibility(View.VISIBLE);
             tvMultipleSignT.setVisibility(View.VISIBLE);
             tvMultipleMonthsA.setVisibility(View.VISIBLE);
             //tvMultipleSignA.setVisibility(View.VISIBLE);
             batchNameArray = new ArrayList<>();
             spinList = new ArrayList<>();
             batchIdArray = new ArrayList<>();
             branCourIdArray = new ArrayList<>();
             batchBeanArrayListActive = new ArrayList<>();
             batchNameArray.clear();
             batchIdArray.clear();
             branCourIdArray.clear();
//        batchNameArray.add("--Select Section--");
//        batchIdArray.add(0);
             selectBatchFunction();
         }
         dateListeners();

         btnBuildInvoice.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 AttUtil.progressDialog(BuildInvoiceActivity.this);
                 AttUtil.pd(1);
                 if (callActivitySignal == 2) {
                     if (totalBalance >= (oneTimeAmountSelect + annualAmountSelect + monthlyAmountSelect+transportAmountSelect) ||
                             (oneTimeAmountSelect + annualAmountSelect + monthlyAmountSelect+transportAmountSelect) == 0)
                         //payFeeFunction();
                         getLastAccountId();
                         //Toast.makeText(BuildInvoiceActivity.this,""+(oneTimeAmountSelect + annualAmountSelect + monthlyAmountSelect+transportAmountSelect),Toast.LENGTH_SHORT).show();
                     else
                         inSufficientBalance();
                 } else {
                     emailNoteDialog();
                 }

             }
         });
    }

     void payFeeFunction() {
        check = feeCostBeanArrayList.size();
        for(int i=0;i<feeCostBeanArrayList.size();i++){
            check = check - 1;
            feeCostBeanArrayList.get(i).setCreditAmount(feeCostBeanArrayList.get(i).getCreditAmount()+feeCostBeanArrayList.get(i).getCreditAmountTemp());
            db.collection(Constants.fee_paid_collection).document(String.valueOf(feeCostBeanArrayList.get(i).getFeePaidId()))
                    .update("creditAmount",feeCostBeanArrayList.get(i).getCreditAmount())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if(check == 0){
                                //updateAccountBean();
                               // getLastAccountId();
                                AttUtil.pd(0);
                                AlertDialog.Builder builder = new AlertDialog.Builder(BuildInvoiceActivity.this);
                                builder.setMessage("Balance updated.");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        setResult(AttUtil.RES_CODE, new Intent().putExtra("signal", 1));
                                        finish();
                                    }
                                });
                                builder.setCancelable(false);
                                builder.create().show();
                            }
                        }
                    });

        }
    }

     void getLastAccountId() {
        listAccountId = new ArrayList<>();
        db.collection(Constants.student_account_collection).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    if(!queryDocumentSnapshots.isEmpty()){
                        accountId = doc.getLong("accountId").intValue();
                        listAccountId.add(accountId);
                    }else{
                        accountId = 1;
                    }
                }

                if(listAccountId.size()>0){
                    accountId = Collections.max(listAccountId);
                    accountId = accountId + 1;
                }else{
                    accountId = 1;
                }
                updateAccountBean();
            }
        });
    }

    void updateAccountBean() {
        accountBean = new AccountBean();
        accountBean.setSignal(1);
        accountBean.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        accountBean.setAccountId(accountId);
        accountBean.setInOutSignal(1);
        accountBean.setInOutType(1);
        accountBean.setTime(new SimpleDateFormat("hh:mm:ss").format(new Date()));
        accountBean.setAmount(oneTimeAmountSelect+monthlyAmountSelect+annualAmountSelect+transportAmountSelect);
        accountBean.setAdmissionId(studentBean.getAdmissionId());
        accountBean.setStudentId(studentBean.getStudentId());
        insertIntoAccDB();
    }

     void insertIntoAccDB() {
        db.collection(Constants.student_account_collection).document(String.valueOf(accountBean.getAccountId()))
                .set(accountBean).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                updateStudentAccAmount();
            }
        });
    }

     void updateStudentAccAmount() {
        studentBean.setAccountAmount(studentBean.getAccountAmount()-(oneTimeAmountSelect+monthlyAmountSelect+annualAmountSelect+transportAmountSelect));
        studentBean.setTotalCreditAmount(studentBean.getTotalCreditAmount()+(oneTimeAmountSelect+monthlyAmountSelect+annualAmountSelect+transportAmountSelect));
        updateToStuDB();
    }

     void updateToStuDB() {
        db.collection(Constants.student_collection).document(String.valueOf(studentBean.getStudentId()))
                .update("accountAmount",studentBean.getAccountAmount(),"totalCreditAmount",studentBean.getTotalCreditAmount())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       /* AttUtil.pd(0);
                        AlertDialog.Builder builder = new AlertDialog.Builder(BuildInvoiceActivity.this);
                        builder.setMessage("Balance updated.");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setResult(AttUtil.RES_CODE, new Intent().putExtra("signal", 1));
                                finish();
                            }
                        });
                        builder.setCancelable(false);
                        builder.create().show();*/
                       payFeeFunction();
                    }
                });

    }

    EditText editText;
    void emailNoteDialog() {
        AttUtil.pd(0);
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
        dialogBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (invoiceValidation()){
                    AttUtil.progressDialog(BuildInvoiceActivity.this);
                    AttUtil.pd(1);
                   setDataToBean();
                   // Toast.makeText(BuildInvoiceActivity.this,""+tvMultipleMonthsM.getText().toString(),Toast.LENGTH_SHORT).show();
                }
                    //generateInvoice();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", null);
        android.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

     void setDataToBean() {
         if(callActivitySignal == 0){
             studentBean.setBatchId(batchIdArray.get(posBatch));
             studentBean.setBranCourId(branCourIdArray.get(posBatch));
             studentBean.setStartingDate(edtClassStartingDate.getText().toString().trim());
             studentBean.setBatch_title(batchBeanArrayList.get(posBatch).getBatch_title());
             studentBean.setApproved(1);
             studentBean.setAdmStatus(2);
             for(int i=0;i<courBeanArrayList.size();i++){
                 if(courBeanArrayList.get(i).getBranCourId() == studentBean.getBranCourId()){
                     studentBean.setCourseName(courBeanArrayList.get(i).getCourseName());
                     break;
                 }
             }
             //studentBean.setTotalCreditAmount((oneTimeAmountSelect + (Integer.parseInt(tvMultipleMonthsA.getText().toString())*annualAmountSelect) + (Integer.parseInt(tvMultipleMonthsM.getText().toString())*monthlyAmountSelect)+(Integer.parseInt(tvMultipleMonthsT.getText().toString())*transportAmountSelect)));
         }

         check = feeCostBeanArrayList.size();
        for(int i=0;i<feeCostBeanArrayList.size();i++){
            check = check - 1;
            if(feeCostBeanArrayList.get(i).isChecked()){
                feeCostBean = new FeeCostBean();
                feeCostBean.setHeadName(feeCostBeanArrayList.get(i).getHeadName());
                feeCostBean.setPayFee(false);
                feeCostBean.setChecked(true);
                feeCostBean.setFeePaidId(feeCostBeanArrayList.get(i).getFeePaidId());

                if(feeCostBeanArrayList.get(i).getFeeType() == 1){
                    feeCostBean.setOtStartingDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    feeCostBean.setOtEndingDate("0000-00-00");
                    feeCostBean.setaStartingDate("");
                    feeCostBean.setaEndingDate("");
                    feeCostBean.setmStartingDate("");
                    feeCostBean.setmEndingDate("");
                    feeCostBean.setStartingDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    feeCostBean.setEndingDate("0000-00-00");
                    feeCostBean.setTotalSellingCost(feeCostBeanArrayList.get(i).getSellingAmount());
                }

                if(feeCostBeanArrayList.get(i).getFeeType() == 2){
                    feeCostBean.setaStartingDate(stringAStartingDate);
                    feeCostBean.setaEndingDate(stringAEndingDate);
                    feeCostBean.setOtStartingDate("");
                    feeCostBean.setOtEndingDate("");
                    feeCostBean.setmStartingDate("");
                    feeCostBean.setmEndingDate("");
                    feeCostBean.settStartingDate("");
                    feeCostBean.settEndingDate("");
                    feeCostBean.setStartingDate(stringAStartingDate);
                    feeCostBean.setEndingDate(stringAEndingDate);
                    feeCostBean.setNoOfMonths(noOfMonthsA);
                    if (feeCostBeanArrayList.get(i).getNoOfMonths() < 12) {
                        Double total = Double.valueOf(feeCostBeanArrayList.get(i).getSellingAmount());
                        int iTotal = (int) Math.round(total / 12);
                        feeCostBean.setTotalSellingCost(feeCostBean.getNoOfMonths()*iTotal);
                    } else if (feeCostBeanArrayList.get(i).getNoOfMonths() == 12) {
                        //totalSellingCost = totalSellingCost + feeCostBeanArrayList.get(j).getSellingAmount();
                        feeCostBean.setTotalSellingCost(feeCostBeanArrayList.get(i).getSellingAmount());
                    } else if (feeCostBeanArrayList.get(i).getNoOfMonths() > 12) {
                        Double total = Double.valueOf(feeCostBeanArrayList.get(i).getSellingAmount());
                        int iTotal = (int) Math.round(total / 12);
                        feeCostBean.setTotalSellingCost(feeCostBean.getNoOfMonths()*iTotal);
                    }
                }

                if(feeCostBeanArrayList.get(i).getFeeType() == 3){
                    feeCostBean.setmStartingDate(stringMStartingDate);
                    feeCostBean.setmEndingDate(stringMEndingDate);
                    feeCostBean.setOtStartingDate("");
                    feeCostBean.setOtEndingDate("");
                    feeCostBean.setaStartingDate("");
                    feeCostBean.setaEndingDate("");
                    feeCostBean.settStartingDate("");
                    feeCostBean.settEndingDate("");
                    feeCostBean.setStartingDate(stringMStartingDate);
                    feeCostBean.setEndingDate(stringMEndingDate);
                    feeCostBean.setNoOfMonths(noOfMonthsM);
                    feeCostBean.setTotalSellingCost(feeCostBean.getNoOfMonths()*feeCostBeanArrayList.get(i).getSellingAmount());
                }

                if(feeCostBeanArrayList.get(i).getFeeType() == 4){
                    feeCostBean.settStartingDate(stringTStartingDate);
                    feeCostBean.settEndingDate(stringTEndingDate);
                    feeCostBean.setOtStartingDate("");
                    feeCostBean.setOtEndingDate("");
                    feeCostBean.setaStartingDate("");
                    feeCostBean.setaEndingDate("");
                    feeCostBean.setmStartingDate("");
                    feeCostBean.setmEndingDate("");
                    feeCostBean.setStartingDate(stringTStartingDate);
                    feeCostBean.setEndingDate(stringTEndingDate);
                    feeCostBean.setNoOfMonths(noOfMonthsT);
                    feeCostBean.setTotalSellingCost(feeCostBean.getNoOfMonths()*feeCostBeanArrayList.get(i).getSellingAmount());
                }
                generateInvoice();
                //Toast.makeText(BuildInvoiceActivity.this,"s "+feeCostBean.getHeadName(),Toast.LENGTH_SHORT).show();

            }else{
                feeCostBean = new FeeCostBean();
                feeCostBean.setPayFee(true);
                feeCostBean.setFeePaidId(feeCostBeanArrayList.get(i).getFeePaidId());
                feeCostBean.setStartingDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                feeCostBean.setCycleNumber("");
                feeCostBean.setEndingDate("");
                generateInvoice();
            }
        }
    }

    void generateInvoice() {
        db.collection(Constants.fee_paid_collection).document(String.valueOf(feeCostBean.getFeePaidId()))
                .update("isPayFee",feeCostBean.isPayFee(),"aEndingDate",feeCostBean.getaEndingDate(),
                        "aStartingDate",feeCostBean.getaStartingDate(),"mEndingDate",feeCostBean.getmEndingDate(),
                        "mStartingDate",feeCostBean.getmStartingDate(),"otEndingDate",feeCostBean.getOtEndingDate(),
                        "otStartingDate",feeCostBean.getOtStartingDate(),"tEndingDate",feeCostBean.gettEndingDate(),
                        "tStartingDate",feeCostBean.gettStartingDate(),"noOfMonths",feeCostBean.getNoOfMonths(),
                        "startingDate",feeCostBean.getStartingDate(),"endingDate",feeCostBean.getEndingDate(),
                        "invoiceDate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                        "totalSellingCost",feeCostBean.getTotalSellingCost())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if(check == 0){
                    updateStudentDetails();
                }
            }
        });
    }

     void updateStudentDetails() {
        int totalSellingCost = Integer.parseInt(textViewAmountOne.getText().toString())+
                Integer.parseInt(textViewAmountAnnual.getText().toString())+
                (noOfMonthsM * Integer.parseInt(textViewAmountMonth.getText().toString()))+
                (noOfMonthsT * Integer.parseInt(textViewAmountTransport.getText().toString()));
        db.collection(Constants.student_collection).document(String.valueOf(studentBean.getStudentId()))
                .update("batchId",studentBean.getBatchId(),"batch_title",studentBean.getBatch_title(),
                        "branCourId",studentBean.getBranCourId(),"startingDate",studentBean.getStartingDate(),"approved",studentBean.getApproved(),
                        "courseName",studentBean.getCourseName(),"admStatus",studentBean.getAdmStatus(),
                        "totalSellingCost",totalSellingCost)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               getDataFromBatch();
            }
        });
    }


     void getDataFromBatch() {
        db.collection(Constants.branchCollection).document(String.valueOf(studentBean.getBranchId()))
                .collection(Constants.branch_course_collection).document(String.valueOf(studentBean.getBranCourId()))
                .collection(Constants.batch_section_collection).document(String.valueOf(studentBean.getBatchId()))
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                batchBean = documentSnapshot.toObject(BatchBean.class);
                activeStudents = documentSnapshot.getLong("activeStudents").intValue();
                activeStudents = activeStudents + 1;
                batchBean.setActiveStudents(activeStudents);
                addDataToBatch();
            }
        });
    }

     void addDataToBatch() {
         db.collection(Constants.branchCollection).document(String.valueOf(studentBean.getBranchId()))
                 .collection(Constants.branch_course_collection).document(String.valueOf(studentBean.getBranCourId()))
                 .collection(Constants.batch_section_collection).document(String.valueOf(studentBean.getBatchId()))
                 .update("activeStudents",batchBean.getActiveStudents()).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
                 AttUtil.pd(0);
                 AlertDialog.Builder builder = new AlertDialog.Builder(BuildInvoiceActivity.this);
                 if (callActivitySignal == 1) {
                     builder.setMessage("Fees invoice is sent to parents");
                 } else {
                     builder.setMessage(studentBean.getStuName() + " is admitted into " + batchNameArray.get(posBatch) +
                             ". Fees invoice is sent to parents.");
                 }
                 builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         setResult(AttUtil.RES_CODE, new Intent().putExtra("signal", 1)
                                 .putExtra("batchBean",batchBean));
                         finish();
                     }
                 });
                 builder.setCancelable(false);
                 builder.create().show();
             }
         });
    }

    boolean invoiceValidation() {
        boolean ch = true;
        String msg = "Error:";
        if (!textViewAmountAnnual.getText().toString().trim().equals("0")) {
            if (stringAStartingDate.length() == 0) {
                tvAStartingDate.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
                ch = false;
                msg = msg + "\nInvalid Annual Starting Date";
            }
            if (stringAEndingDate.length() == 0) {
                tvAEndingDate.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
                ch = false;
                msg = msg + "\nInvalid Annual Ending Date";
            }
        }
        if (!textViewAmountMonth.getText().toString().trim().equals("0")) {
            if (stringMStartingDate.length() == 0) {
                tvMStartingDate.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
                ch = false;
                msg = msg + "\nInvalid Monthly Starting Date";
            }
            if (stringMEndingDate.length() == 0) {
                tvMEndingDate.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
                ch = false;
                msg = msg + "\nInvalid Monthly Ending Date";
            }
        }if (!textViewAmountTransport.getText().toString().trim().equals("0")) {
            if (stringTStartingDate.length() == 0) {
                tvTStartingDate.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
                ch = false;
                msg = msg + "\nInvalid Transportation Starting Date";
            }
            if (stringTEndingDate.length() == 0) {
                tvTEndingDate.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
                ch = false;
                msg = msg + "\nInvalid Transportation Ending Date";
            }
        }
        if (textViewAmountAnnual.getText().toString().trim().equals("0") &&
                textViewAmountMonth.getText().toString().trim().equals("0") &&
                textViewAmountOne.getText().toString().trim().equals("0")&&
                textViewAmountTransport.getText().toString().trim().equals("0")) {
            ch = false;
            msg = msg + "\nSelect Atleast One Fee Head.";
        }

        if (!ch)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        return ch;
    }

    void inSufficientBalance() {
        AttUtil.pd(0);
        android.support.v7.app.AlertDialog.Builder dialog =
                new android.support.v7.app.AlertDialog.Builder(BuildInvoiceActivity.this);
        if (totalBalance >= (oneTimeAmountSelect + annualAmountSelect + monthlyAmountSelect+transportAmountSelect))
            dialog.setMessage("Insufficient balance in account.");
        else
            dialog.setMessage("Invalid amount to submit.");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Done", null);
        dialog.create().show();
    }

    void dateListeners() {

        tvAStartingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(BuildInvoiceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        stringAStartingDate = year + "-" + (++monthOfYear) + "-" + dayOfMonth;
                        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                        Date newDate = null;
                        try {
                            newDate = spf.parse(stringAStartingDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        spf = new SimpleDateFormat("yyyy-MM-dd");
                        stringAStartingDate = spf.format(newDate);
                        tvAStartingDate.setText(stringAStartingDate);
                        stringAEndingDate = "";
                        tvAEndingDate.setText("Ending Date");
                        tvAStartingDate.setTextColor(ContextCompat.getColor(BuildInvoiceActivity.this,
                                android.R.color.black));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                Calendar c = Calendar.getInstance();
                String date[] = stringAStartingDate.split("-");
                c.setTime(new Date());
                c.set(Calendar.YEAR, Integer.parseInt(date[0]));
                c.set(Calendar.DATE, Integer.parseInt(date[2]));
                c.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        tvAEndingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stringAStartingDate.length() > 0) {
                    Calendar calendar = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(BuildInvoiceActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            stringAEndingDate = year + "-" + (++monthOfYear) + "-" + dayOfMonth;
                            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                            Date newDate = null;
                            try {
                                newDate = spf.parse(stringAEndingDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            spf = new SimpleDateFormat("yyyy-MM-dd");
                            stringAEndingDate = spf.format(newDate);
                            tvAEndingDate.setText(stringAEndingDate);
                            tvAEndingDate.setTextColor(ContextCompat.getColor(BuildInvoiceActivity.this,
                                    android.R.color.black));

                            Calendar endDate = Calendar.getInstance();
                            endDate.setTime(newDate);
                            Date date1 = null;
                            Calendar startDate = Calendar.getInstance();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                date1 = formatter.parse(stringAStartingDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            startDate.setTime(date1);
                            int diffYear = endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR);
                            noOfMonthsA = diffYear * 12 + endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH);
                            noOfMonthsA = noOfMonthsA + 1;
                            calculateAnnualAmount();

                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    Calendar c = Calendar.getInstance();
                    String date[] = stringAStartingDate.split("-");
                    c.setTime(new Date());
                    c.set(Calendar.YEAR, Integer.parseInt(date[0]));
                    c.set(Calendar.DATE, Integer.parseInt(date[2]));
                    c.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                    datePickerDialog.show();
                } else
                    Toast.makeText(BuildInvoiceActivity.this, "Select starting date first", Toast.LENGTH_SHORT).show();
            }
        });
        tvMStartingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(BuildInvoiceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        stringMStartingDate = year + "-" + (++monthOfYear) + "-" + 1;
                        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                        Date newDate = null;
                        try {
                            newDate = spf.parse(stringMStartingDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        spf = new SimpleDateFormat("yyyy-MM-dd");
                        stringMStartingDate = spf.format(newDate);
                        tvMStartingDate.setText(stringMStartingDate);
                        stringMEndingDate = "";
                        tvMEndingDate.setText("Ending Date");
                        tvMStartingDate.setTextColor(ContextCompat.getColor(BuildInvoiceActivity.this,
                                android.R.color.black));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                Calendar c = Calendar.getInstance();
                String date[] = stringMStartingDate.split("-");
                c.setTime(new Date());
                c.set(Calendar.YEAR, Integer.parseInt(date[0]));
                c.set(Calendar.DATE, Integer.parseInt(date[2]));
                c.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        tvMEndingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stringMStartingDate.length() > 0) {
                    final Calendar calendar = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(BuildInvoiceActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar c = Calendar.getInstance();
                            c.setTime(new Date());
                            c.set(Calendar.YEAR, year);
                            c.set(Calendar.MONTH, monthOfYear);

                            stringMEndingDate = year + "-" + (++monthOfYear) + "-" + c.getActualMaximum(Calendar.DAY_OF_MONTH);
                            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                            Date newDate = null;
                            try {
                                newDate = spf.parse(stringMEndingDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            spf = new SimpleDateFormat("yyyy-MM-dd");
                            stringMEndingDate = spf.format(newDate);
                            tvMEndingDate.setText(stringMEndingDate);
                            tvMEndingDate.setTextColor(ContextCompat.getColor(BuildInvoiceActivity.this,
                                    android.R.color.black));

                            Calendar endDate = Calendar.getInstance();
                            endDate.setTime(newDate);
                            Date date1 = null;
                            Calendar startDate = Calendar.getInstance();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                date1 = formatter.parse(stringMStartingDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            startDate.setTime(date1);
                            int diffYear = endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR);
                            noOfMonthsM = diffYear * 12 + endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH);
                            noOfMonthsM = noOfMonthsM + 1;
                            tvMultipleMonthsM.setText(noOfMonthsM + "");
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    Calendar c = Calendar.getInstance();
                    String date[] = stringMStartingDate.split("-");
                    c.setTime(new Date()); //
                    c.set(Calendar.YEAR, Integer.parseInt(date[0]));
                    c.set(Calendar.DATE, Integer.parseInt(date[2]));
                    c.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                    datePickerDialog.show();
                } else
                    Toast.makeText(BuildInvoiceActivity.this, "Select strting date first", Toast.LENGTH_SHORT).show();
            }
        });
        tvTStartingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(BuildInvoiceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        stringTStartingDate = year + "-" + (++monthOfYear) + "-" + 1;
                        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                        Date newDate = null;
                        try {
                            newDate = spf.parse(stringTStartingDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        spf = new SimpleDateFormat("yyyy-MM-dd");
                        stringTStartingDate = spf.format(newDate);
                        tvTStartingDate.setText(stringTStartingDate);
                        stringTEndingDate = "";
                        tvTEndingDate.setText("Ending Date");
                        tvTStartingDate.setTextColor(ContextCompat.getColor(BuildInvoiceActivity.this,
                                android.R.color.black));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                Calendar c = Calendar.getInstance();
                String date[] = stringTStartingDate.split("-");
                c.setTime(new Date());
                c.set(Calendar.YEAR, Integer.parseInt(date[0]));
                c.set(Calendar.DATE, Integer.parseInt(date[2]));
                c.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });
        tvTEndingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stringTStartingDate.length() > 0) {
                    final Calendar calendar = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(BuildInvoiceActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar c = Calendar.getInstance();
                            c.setTime(new Date());
                            c.set(Calendar.YEAR, year);
                            c.set(Calendar.MONTH, monthOfYear);

                            stringTEndingDate = year + "-" + (++monthOfYear) + "-" + c.getActualMaximum(Calendar.DAY_OF_MONTH);
                            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                            Date newDate = null;
                            try {
                                newDate = spf.parse(stringTEndingDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            spf = new SimpleDateFormat("yyyy-MM-dd");
                            stringTEndingDate = spf.format(newDate);
                            tvTEndingDate.setText(stringTEndingDate);
                            tvTEndingDate.setTextColor(ContextCompat.getColor(BuildInvoiceActivity.this,
                                    android.R.color.black));

                            Calendar endDate = Calendar.getInstance();
                            endDate.setTime(newDate);
                            Date date1 = null;
                            Calendar startDate = Calendar.getInstance();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                date1 = formatter.parse(stringTStartingDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            startDate.setTime(date1);
                            int diffYear = endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR);
                            noOfMonthsT = diffYear * 12 + endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH);
                            noOfMonthsT = noOfMonthsT + 1;
                            tvMultipleMonthsT.setText(noOfMonthsT + "");

                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    Calendar c = Calendar.getInstance();
                    String date[] = stringTStartingDate.split("-");
                    c.setTime(new Date()); //
                    c.set(Calendar.YEAR, Integer.parseInt(date[0]));
                    c.set(Calendar.DATE, Integer.parseInt(date[2]));
                    c.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                    datePickerDialog.show();
                } else
                    Toast.makeText(BuildInvoiceActivity.this, "Select strting date first", Toast.LENGTH_SHORT).show();
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
        transportAmountSelect=0;
        for (int i = 0; i < oneTimeFeeHeadList.size(); i++) {
            if (callActivitySignal == 2)
                oneTimeAmountSelect = oneTimeAmountSelect + (oneTimeFeeHeadList.get(i).getCreditAmountTemp());
            else {
                if (oneTimeFeeHeadList.get(i).isChecked()) {
                    oneTimeAmountSelect = oneTimeAmountSelect + (oneTimeFeeHeadList.get(i).getSellingCost());
                }
            }
        }
        for (int i = 0; i < annualFeeHeadList.size(); i++) {
            if (callActivitySignal == 2)
                annualAmountSelect = annualAmountSelect + (annualFeeHeadList.get(i).getCreditAmountTemp());
            else {
                if (annualFeeHeadList.get(i).isChecked()) {
                    annualAmountSelect = annualAmountSelect + (annualFeeHeadList.get(i).getSellingCost());
                }
            }

        }
        for (int i = 0; i < monthlyFeeHeadList.size(); i++) {
            if (callActivitySignal == 2)
                monthlyAmountSelect = monthlyAmountSelect + (monthlyFeeHeadList.get(i).getCreditAmountTemp());
            else {
                if (monthlyFeeHeadList.get(i).isChecked()) {
                    monthlyAmountSelect = monthlyAmountSelect + (monthlyFeeHeadList.get(i).getSellingCost());
                }
            }
        }
        for (int i = 0; i < transportFeeHeadList.size(); i++) {
            if (callActivitySignal == 2)
                transportAmountSelect = transportAmountSelect + (transportFeeHeadList.get(i).getCreditAmountTemp());
            else {
                if (transportFeeHeadList.get(i).isChecked()) {
                    transportAmountSelect = transportAmountSelect + (transportFeeHeadList.get(i).getSellingCost());
                }
            }
        }

        textViewAmountOne.setText("" + oneTimeAmountSelect + "");
        textViewAmountAnnual.setText("" + annualAmountSelect + "");
        textViewAmountMonth.setText("" + monthlyAmountSelect + "");
        textViewAmountTransport.setText("" + transportAmountSelect + "");

        if (callActivitySignal == 2) {
            if (totalBalance < (oneTimeAmountSelect + annualAmountSelect + monthlyAmountSelect+transportAmountSelect)) {
                //tvBalanceAccount.setTextColor(ContextCompat.getColor(this, android.R.color.white));
                rlBalanceAccount.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                tvBalanceAccount.setText("Balance: " + totalBalance);
            } else {
                tvBalanceAccount.setText("Balance: " + (totalBalance - (oneTimeAmountSelect + annualAmountSelect
                        + monthlyAmountSelect+transportAmountSelect)));
                rlBalanceAccount.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
                //tvBalanceAccount.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            }
        } else {
            calculateAnnualAmount();
        }
    }

    @Override
    public int getSelecedMonths() {
        return 0;
    }

    @Override
    public void feeHeadCheck(int i) {

    }

    @Override
    public int getAdapterType() {
        return 0;
    }

    void selectBatchFunction() {
        if (batchBeanArrayList.size() > 0 || batchBeanArrayList != null) {
              batchBeanArrayListActive.clear();
            for (int i = 0; i < batchBeanArrayList.size(); i++) {
                if (batchBeanArrayList.get(i).getBatchStatus() == 1) {
                     batchBeanArrayListActive.add(batchBeanArrayList.get(i));
                }
            }
        }
        for (int i = 0; i < batchBeanArrayListActive.size(); i++) {
            if (batchBeanArrayListActive.get(i).getBatchStatus() == 1) {
                batchIdArray.add(batchBeanArrayListActive.get(i).getBatchId());
                branCourIdArray.add(batchBeanArrayListActive.get(i).getBranCourId());
                batchNameArray.add(batchBeanArrayListActive.get(i).getBatch_title());
                spinList.add(batchBeanArrayListActive.get(i).getBatch_title() + "");
            }
        }

        if (spinList.size() == 1) {
            batchNameArray.add("--No Section Available--");
            batchIdArray.add(0);
            branCourIdArray.add(0);
        }
        spnSelectBatch =  findViewById(R.id.spnBatchAssign);
        edtClassStartingDate =  findViewById(R.id.edtClassStartingDate);

        spnSelectBatch.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, spinList));
        edtClassStartingDateString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        edtClassStartingDate.setText(edtClassStartingDateString);
        setStartingAnnualDate(edtClassStartingDateString);
        setStartingMonthDate(edtClassStartingDateString);
        setStartingTransportDate(edtClassStartingDateString);
        edtClassStartingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(BuildInvoiceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = year + "-" + (++monthOfYear) + "-" + dayOfMonth;
                        edtClassStartingDate.setText(date);
                        edtClassStartingDateString = date;
                        setStartingAnnualDate(date);
                        setStartingMonthDate(date);
                        setStartingTransportDate(date);

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        spnSelectBatch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView var1, View var2, int position, long var4) {
                try {
                    posBatch = position;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onNothingSelected(AdapterView var1) {
            }
        });
    }

    void setStartingAnnualDate(String date) {
        stringAStartingDate = date;
        tvAStartingDate.setText(stringAStartingDate);
        stringAEndingDate = "";
        tvAEndingDate.setText("Ending Date");
        tvAStartingDate.setTextColor(ContextCompat.getColor(BuildInvoiceActivity.this,
                android.R.color.black));
    }

    void setStartingMonthDate(String date) {

        stringMStartingDate = date;
        tvMStartingDate.setText(stringMStartingDate);
        stringMEndingDate = "";
        tvMEndingDate.setText("Ending Date");
        tvMEndingDate.setTextColor(ContextCompat.getColor(BuildInvoiceActivity.this,
                android.R.color.black));
    }

    void setStartingTransportDate(String date) {
        stringTStartingDate = date;
        tvTStartingDate.setText(stringTStartingDate);
        stringTEndingDate = "";
        tvTEndingDate.setText("Ending Date");
        tvTEndingDate.setTextColor(ContextCompat.getColor(BuildInvoiceActivity.this,
                android.R.color.black));
    }

    void calculateAnnualAmount() {
        if (noOfMonthsA < 12) {
            Double total = Double.parseDouble(annualAmountSelect + "");
            int iTotal = (int) Math.round(total / 12);
            Log.i("test", iTotal + " - total");
            textViewAmountAnnual.setText((noOfMonthsA * iTotal) + "");
            tvMultipleMonthsA.setText("(" + noOfMonthsA + "x" + iTotal + ")");
            tvMultipleMonthsA.setVisibility(View.VISIBLE);
        } else if (noOfMonthsA == 12) {
            textViewAmountAnnual.setText(annualAmountSelect + "");
            tvMultipleMonthsA.setVisibility(View.GONE);
        } else if (noOfMonthsA > 12) {
            Double total = Double.parseDouble(annualAmountSelect + "");
            int iTotal = (int) Math.round(total / 12);
            Log.i("test", iTotal + " - total");
            textViewAmountAnnual.setText((noOfMonthsA * iTotal) + "");
            tvMultipleMonthsA.setText("(" + noOfMonthsA + "x" + iTotal + ")");
            tvMultipleMonthsA.setVisibility(View.VISIBLE);
        }
    }



}
