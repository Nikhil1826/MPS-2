package com.auribisesmyplayschool.myplayschool.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adapter.InvoiceAdapter;
import com.auribisesmyplayschool.myplayschool.bean.FeeCostBean;
import com.auribisesmyplayschool.myplayschool.bean.StudentBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GenerateFeeInvoceActivity extends AppCompatActivity {
    private ListView lvInvoice;
    private FloatingActionButton fabInvoice;
    private SharedPreferences preferences;
    private ArrayList<FeeCostBean> feeCostBeanArrayListInvoice,feeCostBeanArrayList;
    private StudentBean studentBean;
    private int responseSignal=0,reqCode=0,signalToGenrateInvoice=0,invoiceViewSignal=0,position=0;
    private InvoiceAdapter invoiceAdapter;
    private TextView tvMStartingDate,tvMEndingDate,tvAStartingDate,tvAEndingDate;
    String stringAStartingDate="",stringAEndingDate="",stringMStartingDate="",stringMEndingDate="";
    private AlertDialog alertDialogInvoice;
    FirebaseFirestore db;
    FeeCostBean feeCostBean;
    int totalSellingCost,totalCreditCost;
    ArrayList<String> invoiceDateList;
    Set<String> invoiceDateSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_fee_invoce);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Invoices");
        db = FirebaseFirestore.getInstance();
        feeCostBean = new FeeCostBean();
        preferences=getSharedPreferences(AttUtil.shpREG, Context.MODE_PRIVATE);
        lvInvoice=(ListView)findViewById(R.id.lvInvoice);
        fabInvoice=(FloatingActionButton) findViewById(R.id.fabInvoice);
        //signal to disable fab button, as student is inactive or promoted
        signalToGenrateInvoice = getIntent().getIntExtra("signalToGenrateInvoice",0);
        if(signalToGenrateInvoice==1)
            fabInvoice.setVisibility(View.GONE);
        fabInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(GenerateFeeInvoceActivity.this,BuildInvoiceActivity.class)
                        .putExtra(AttUtil.TAG_STUDENTBEAN,studentBean).putExtra("callActivitySignal",1),101);
            }
        });
        feeCostBeanArrayListInvoice = new ArrayList<>();
        feeCostBeanArrayList = new ArrayList<>();
        invoiceDateList = new ArrayList<>();
        invoiceDateSet = new HashSet<>();
        studentBean = (StudentBean)getIntent().getSerializableExtra(AttUtil.TAG_STUDENTBEAN);
        lvInvoice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                final android.support.v7.app.AlertDialog.Builder build = new android.support.v7.app.AlertDialog.Builder(GenerateFeeInvoceActivity.this);
                String[] options = {"Settle fees","Resend Invoice","View Invoice"};
                build.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                startActivityForResult(new Intent(GenerateFeeInvoceActivity.this,BuildInvoiceActivity.class)
                                        .putExtra(AttUtil.TAG_STUDENTBEAN,studentBean)
                                        .putExtra("callActivitySignal",2),101);
                                break;
                            case 1:
                                invoiceViewSignal=2;
                                /*if(AttUtil.isNetworkConnected(GenerateFeeInvoceActivity.this))
                                    resendInvoice();
                                else
                                    resendInvoiceNetConnect();*/
                                break;
                            case 2:
                                invoiceViewSignal=3;
                                /*if(AttUtil.isNetworkConnected(GenerateFeeInvoceActivity.this))
                                    resendInvoice();
                                else
                                    resendInvoiceNetConnect();*/
                                break;
                        }
                    }
                });
                build.create().show();
            }
        });
        fetchInvoiveList();
    }

     void fetchInvoiveList() {
         AttUtil.progressDialog(this);
         AttUtil.pd(1);
        db.collection(Constants.fee_paid_collection).whereEqualTo("studentId",studentBean.getStudentId())
                .whereEqualTo("isPayFee",false).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    feeCostBean = doc.toObject(FeeCostBean.class);
                    feeCostBeanArrayList.add(feeCostBean);
                }

                if(feeCostBeanArrayList.size()>0){
                    for(int i=0;i<feeCostBeanArrayList.size();i++){
                        invoiceDateSet.add(feeCostBeanArrayList.get(i).getInvoiceDate());
                    }

                    for (Iterator<String> it = invoiceDateSet.iterator(); it.hasNext(); ) {
                        String f = it.next();
                        totalSellingCost = 0;
                        totalCreditCost = 0;
                        feeCostBean = new FeeCostBean();
                        for(int j=0;j<feeCostBeanArrayList.size();j++){
                            if(feeCostBeanArrayList.get(j).getInvoiceDate().equals(f)){
                                /*if(feeCostBeanArrayList.get(j).getFeeType() == 1){
                                    totalSellingCost = totalSellingCost + feeCostBeanArrayList.get(j).getSellingAmount();
                                }else if(feeCostBeanArrayList.get(j).getFeeType() == 2){
                                    if (feeCostBeanArrayList.get(j).getNoOfMonths() < 12) {
                                        Double total = Double.valueOf(feeCostBeanArrayList.get(j).getSellingAmount());
                                        int iTotal = (int) Math.round(total / 12);
                                        totalSellingCost = totalSellingCost + feeCostBeanArrayList.get(j).getNoOfMonths()*iTotal;
                                    } else if (feeCostBeanArrayList.get(j).getNoOfMonths() == 12) {
                                        totalSellingCost = totalSellingCost + feeCostBeanArrayList.get(j).getSellingAmount();
                                    } else if (feeCostBeanArrayList.get(j).getNoOfMonths() > 12) {
                                        Double total = Double.valueOf(feeCostBeanArrayList.get(j).getSellingAmount());
                                        int iTotal = (int) Math.round(total / 12);
                                        totalSellingCost = totalSellingCost + feeCostBeanArrayList.get(j).getNoOfMonths()*iTotal;
                                    }
                                }else if(feeCostBeanArrayList.get(j).getFeeType() == 3){
                                    totalSellingCost = totalSellingCost + (feeCostBeanArrayList.get(j).getNoOfMonths()*feeCostBeanArrayList.get(j).getSellingAmount());
                                }else{
                                    totalSellingCost = totalSellingCost + (feeCostBeanArrayList.get(j).getNoOfMonths()*feeCostBeanArrayList.get(j).getSellingAmount());
                                }
                                totalCreditCost = totalCreditCost + feeCostBeanArrayList.get(j).getCreditAmount();
                                feeCostBean.setTotalSellingCost(totalSellingCost);
                                feeCostBean.setTotalCreditAmount(totalCreditCost);
                                feeCostBean.setInvoiceDate(f);*/
                                totalSellingCost = totalSellingCost + feeCostBeanArrayList.get(j).getTotalSellingCost();
                                totalCreditCost = totalCreditCost + feeCostBeanArrayList.get(j).getCreditAmount();
                                feeCostBean.setTotalSellingCost(totalSellingCost);
                                feeCostBean.setTotalCreditAmount(totalCreditCost);
                                feeCostBean.setInvoiceDate(f);
                            }
                        }
                        feeCostBeanArrayListInvoice.add(feeCostBean);
                    }
                    AttUtil.pd(0);
                    if(feeCostBeanArrayListInvoice.size()>0){
                        invoiceAdapter = new InvoiceAdapter(GenerateFeeInvoceActivity.this,
                                R.layout.adapter_invoice_listitem,feeCostBeanArrayListInvoice);
                        lvInvoice.setAdapter(invoiceAdapter);
                    }

                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"Account").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        if(item.getItemId()==1)
            startActivity(new Intent(GenerateFeeInvoceActivity.this,
                    ManageFeeAccountActivity.class).putExtra(AttUtil.TAG_STUDENTBEAN, studentBean));
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101&&resultCode==AttUtil.RES_CODE){
            feeCostBeanArrayList.clear();
            invoiceDateSet.clear();
            feeCostBeanArrayListInvoice.clear();
            invoiceAdapter.notifyDataSetChanged();
            fetchInvoiveList();
        }
    }
}
