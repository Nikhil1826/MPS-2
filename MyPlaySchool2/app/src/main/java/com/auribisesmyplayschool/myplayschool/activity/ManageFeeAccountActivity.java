package com.auribisesmyplayschool.myplayschool.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adapter.AccountDetailsAdapter;
import com.auribisesmyplayschool.myplayschool.bean.AccountBean;
import com.auribisesmyplayschool.myplayschool.bean.StudentBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class ManageFeeAccountActivity extends AppCompatActivity {
    private int responseSignal=0,reqCode=0,position,receiptViewSignal=0;
    private ListView lvAccountBalance;
    private TextView tvAccountBalance;
    private ArrayList<AccountBean> accountBeanArrayList;
    private StudentBean studentBean;
    private SharedPreferences preferences;
    private AccountDetailsAdapter accountDetailsAdapter;
    private EditText editTextRemarksAcc,edtChequeDate,editTextChequeAcc,edtAdvanceCashAcc,
            edtNEFTAcc,edtNEFTDate;
    RadioButton radioCashAcc,radioChequeAcc,radioNEFTAcc;
    private String account_act_deact;
    FirebaseFirestore db;
    AccountBean accountBean;
    int accountId;
    String currentTime,currentDate,cDayString,cMonthString;
    Calendar calendar;
    int cyear,cmonth,cday,hour,mint,studentAccountAmount;
    ArrayList<Integer> listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_fee_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Account Details");
        db = FirebaseFirestore.getInstance();
        listId = new ArrayList<>();
        AttUtil.progressDialog(this);
        calendar = Calendar.getInstance();
        cyear = calendar.get(Calendar.YEAR);
        cmonth = calendar.get(Calendar.MONTH);
        cday = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        mint = calendar.get(Calendar.MINUTE);
        getDate(cyear,cmonth,cday);
        getTime(hour,mint);
        preferences=getSharedPreferences(AttUtil.shpREG, Context.MODE_PRIVATE);
        lvAccountBalance=findViewById(R.id.lvAccountBalance);
        lvAccountBalance.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("test",accountBeanArrayList.get(i).toString());
                if(accountBeanArrayList.get(i).getInOutSignal()==0){
                    position=i;
                    accountBean = accountBeanArrayList.get(position);
                    if(accountBeanArrayList.get(position).getSignal()==1){
                        account_act_deact="De-activate Payment";
                    }else{
                        account_act_deact="Activate Payment";
                    }
                    show_options();
                }
            }
        });
        tvAccountBalance=(TextView) findViewById(R.id.tvAccountBalance);
        accountBeanArrayList = new ArrayList<>();
        accountBean = new AccountBean();
        studentBean = (StudentBean)getIntent().getSerializableExtra(AttUtil.TAG_STUDENTBEAN);
        if(AttUtil.isNetworkConnected(this)){
            AttUtil.pd(1);
            retrieveIntoDb();
        }

       /* else
            retrieveNetConnect();*/
    }

    void show_options() {
        AlertDialog.Builder build = new AlertDialog.Builder(this);

        String[] options;
        options=new String[3];
        options[0]=account_act_deact;
        options[1]="Resend receipt";
        options[2]="View receipt";

        build.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if(AttUtil.isNetworkConnected(ManageFeeAccountActivity.this)){
                            AttUtil.pd(1);
                            actDeactivate();
                        }

                        /*else
                            actDeactivateNetConnect();*/
                        break;
                    case 1:
                        receiptViewSignal=1;
                       /* if(AttUtil.isNetworkConnected(ManageFeeAccountActivity.this))
                            resendRecipt();
                        else
                            resendReceiptNetConnect();*/
                        break;
                    case 2:
                        receiptViewSignal=2;
                        /*if(AttUtil.isNetworkConnected(ManageFeeAccountActivity.this))
                            resendRecipt();
                        else
                            resendReceiptNetConnect();*/
                        break;
                }
            }
        });

        build.create().show();
    }

     void actDeactivate() {
        if(accountBean.getSignal()==1)
            accountBean.setSignal(0);
        else
            accountBean.setSignal(1);

        db.collection(Constants.student_account_collection).document(String.valueOf(accountBean.getAccountId()))
                .update("signal",accountBean.getSignal()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                accountBeanArrayList.set(position,accountBean);
                accountDetailsAdapter.notifyDataSetChanged();
                calculateBalance();
                AttUtil.pd(0);
            }
        });
    }

    void retrieveIntoDb() {
        db.collection(Constants.student_account_collection).whereEqualTo("studentId",studentBean.getStudentId())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    if(doc.exists()){
                        accountBean = doc.toObject(AccountBean.class);
                        accountBeanArrayList.add(accountBean);
                    }
                }
                accountDetailsAdapter = new AccountDetailsAdapter(ManageFeeAccountActivity.this,
                        R.layout.adapter_account_activity,accountBeanArrayList);
                calculateBalance();
                AttUtil.pd(0);
            }
        });
    }

    void calculateBalance(){
        int total = 0;
        for(int i=0;i<accountBeanArrayList.size();i++){
            if(accountBeanArrayList.get(i).getInOutSignal()==0&&
                    accountBeanArrayList.get(i).getSignal()==1)
                total=total+accountBeanArrayList.get(i).getAmount();
            if(accountBeanArrayList.get(i).getInOutSignal()==1)
                total=total-accountBeanArrayList.get(i).getAmount();
        }
        tvAccountBalance.setText("Balance: "+total);
        lvAccountBalance.setAdapter(accountDetailsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"Pay Fees").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0,2,0,"Receipt").setIcon(R.drawable.ic_info)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        if(item.getItemId()==1)
            payFee();
        if(item.getItemId()==2){
            AlertDialog.Builder dialog =new AlertDialog.Builder(ManageFeeAccountActivity.this);
           /* if(preferences.getInt(AttUtil.shpReceiptSignal,0)==1){
                dialog.setMessage("Send Receipt is enable.");
            }else if(preferences.getInt(AttUtil.shpReceiptSignal,0)==0){
                dialog.setMessage("Send Receipt is disable.");
            }*/
            dialog.setCancelable(false);
            dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    Dialog dialog;
    void payFee() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_add_amount_dialog);
        dialog.setTitle("Add Amount");

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        radioCashAcc=(RadioButton)dialog.findViewById(R.id.radioCashAcc);
        radioChequeAcc=(RadioButton)dialog.findViewById(R.id.radioChequeAcc);
        radioNEFTAcc=(RadioButton)dialog.findViewById(R.id.radioNEFTAcc);

        Button btnAddAccAmount = (Button)dialog.findViewById(R.id.btnAddAccAmount);
        edtAdvanceCashAcc = (EditText) dialog.findViewById(R.id.edtAdvanceCashAcc);
        editTextChequeAcc = (EditText) dialog.findViewById(R.id.editTextChequeAcc);
        edtChequeDate = (EditText) dialog.findViewById(R.id.edtChequeDate);

        edtNEFTAcc = dialog.findViewById(R.id.edtNEFTAcc);
        edtNEFTDate =  dialog.findViewById(R.id.edtNEFTDate);
        edtChequeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                DatePickerDialog datePickerDialog=new DatePickerDialog(ManageFeeAccountActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        edtChequeDate.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                    }
                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        edtNEFTDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
                DatePickerDialog datePickerDialog=new DatePickerDialog(ManageFeeAccountActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        edtNEFTDate.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                    }
                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        editTextChequeAcc.setEnabled(false);
        edtChequeDate.setEnabled(false);

        edtNEFTAcc.setEnabled(false);
        edtNEFTDate.setEnabled(false);

        radioNEFTAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radioNEFTAcc.isChecked()){
                    edtNEFTAcc.setEnabled(true);
                    edtNEFTDate.setEnabled(true);
                    editTextChequeAcc.setEnabled(false);
                    edtChequeDate.setEnabled(false);
                }

            }
        });

        radioChequeAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radioChequeAcc.isChecked()){
                    editTextChequeAcc.setEnabled(true);
                    edtChequeDate.setEnabled(true);
                    edtNEFTAcc.setEnabled(false);
                    edtNEFTDate.setEnabled(false);
                }

            }
        });

        radioCashAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radioCashAcc.isChecked()){
                    editTextChequeAcc.setEnabled(false);
                    edtChequeDate.setEnabled(false);
                    edtNEFTAcc.setEnabled(false);
                    edtNEFTDate.setEnabled(false);
                }
            }
        });
        editTextRemarksAcc = dialog.findViewById(R.id.editTextRemarksAcc);
        btnAddAccAmount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View var1) {
                if(addAmountValidation()){
                    if(AttUtil.isNetworkConnected(ManageFeeAccountActivity.this)){
                        AttUtil.pd(1);
                        getLastAccountId(dialog);
                    }
                    /*else
                        addAmountNetConnect();*/
                }
            }
        });
        dialog.show();
    }

     void getLastAccountId(final Dialog dialog) {
        db.collection(Constants.student_account_collection).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.size()>0){
                    for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                        int chkId = doc.getLong("accountId").intValue();
                        listId.add(chkId);
                    }
                    accountId = Collections.max(listId);
                    accountId = accountId + 1;
                }else {
                    accountId = 1;
                }
                setDatatoBean(dialog);
            }
        });
    }

    void setDatatoBean(Dialog dialog) {
        accountBean = new AccountBean();
        accountBean.setAccountId(accountId);
        accountBean.setStudentId(studentBean.getStudentId());
        accountBean.setAdmissionId(studentBean.getStudentId());
        accountBean.setAmount(Integer.parseInt(edtAdvanceCashAcc.getText().toString().trim()));
        if(radioCashAcc.isChecked()){
            accountBean.setInOutType(1);
            accountBean.setChequeDate("");
            accountBean.setChequeNo("");
        }
        if(radioChequeAcc.isChecked()){
            accountBean.setInOutType(0);
            accountBean.setChequeNo(editTextChequeAcc.getText().toString().trim());
            accountBean.setChequeDate(edtChequeDate.getText().toString().trim());
        }
        if(radioNEFTAcc.isChecked()){
            accountBean.setInOutType(2);
            accountBean.setNeftTransaction(edtNEFTAcc.getText().toString().trim());
            accountBean.setNeftDate(edtNEFTDate.getText().toString().trim());
        }

        accountBean.setInOutSignal(0);
        accountBean.setTime(currentTime);
        accountBean.setDate(currentDate);
        accountBean.setSignal(1);
        getStudentAccountAmount(dialog);
        //addAmountIntoDb(dialog);
    }

     void getStudentAccountAmount(final Dialog dialog) {
        db.collection(Constants.student_collection).document(String.valueOf(studentBean.getStudentId()))
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                studentAccountAmount = documentSnapshot.getLong("accountAmount").intValue();
                studentAccountAmount = studentAccountAmount + accountBean.getAmount();
                updateStudentAccountAmount(dialog);
            }
        });
    }

     void updateStudentAccountAmount(final Dialog dialog) {
        studentBean.setAccountAmount(studentAccountAmount);
        db.collection(Constants.student_collection).document(String.valueOf(studentBean.getStudentId()))
                .update("accountAmount",studentBean.getAccountAmount()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                addAmountIntoDb(dialog);
            }
        });
    }

    void addAmountIntoDb(final Dialog dialog) {
        db.collection(Constants.student_account_collection).document(String.valueOf(accountBean.getAccountId()))
                .set(accountBean).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                accountBeanArrayList.add(accountBean);
                accountDetailsAdapter.notifyDataSetChanged();
                calculateBalance();
                listId.clear();
                AttUtil.pd(0);
                dialog.hide();
            }
        });
    }

    boolean addAmountValidation(){
        boolean check=true;
        String msg="Error: ";
        if(edtAdvanceCashAcc.getText().toString().length()==0){
            msg=msg+"\nInvalid Amount";
            check=false;
        }if(!radioCashAcc.isChecked()&&!radioChequeAcc.isChecked()&&!radioNEFTAcc.isChecked()){
            msg=msg+"\nSelect One Payment Option";
            check=false;
        }if(radioChequeAcc.isChecked()&&editTextChequeAcc.getText().toString().trim().length()<6){
            msg=msg+"\nInvalid Cheque Number";
            check=false;
        }if(radioChequeAcc.isChecked()&&edtChequeDate.getText().toString().trim().length()==0){
            msg=msg+"\nInvalid Cheque Date";
            check=false;
        }if(radioNEFTAcc.isChecked() && edtNEFTAcc.getText().toString().trim().length()==0){
            msg=msg+"\nInvalid Transaction Number";
            check=false;
        }if(radioNEFTAcc.isChecked()&&edtNEFTDate.getText().toString().trim().length()==0){
            msg=msg+"\nInvalid Date";
            check=false;
        }
        if(!check)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        return check;
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
}
