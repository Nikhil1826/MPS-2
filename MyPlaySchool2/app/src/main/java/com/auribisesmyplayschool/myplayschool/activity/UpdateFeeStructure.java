package com.auribisesmyplayschool.myplayschool.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adapter.FeeAdapterThree;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.FeeHeadBean;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.RoutesBean;
import com.auribisesmyplayschool.myplayschool.bean.FeeCostBean;
import com.auribisesmyplayschool.myplayschool.bean.StudentBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.auribisesmyplayschool.myplayschool.classes.PayFeeInterface;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class UpdateFeeStructure extends AppCompatActivity implements PayFeeInterface {
    StudentBean studentBean;
    private SharedPreferences preferences;

    private ArrayList<FeeCostBean> feeCostBeanArrayList,feeCostPaidArrayList,
            oneTimeFeeHeadList=new ArrayList<>(),
            annualFeeHeadList=new ArrayList<>(),
            monthlyFeeHeadList=new ArrayList<>(),
            transportFeeHeadList=new ArrayList<>();
    ArrayList<RoutesBean> routesBeanArrayList;
    private TextView tvFeeStructureName,textViewAmountOne,textViewAmountAnnual,textViewAmountMonth,textViewAmountTransport;
    private RecyclerView listViewFeesOT,listViewFeesAT,listViewFeesMT,listViewFeesTT;
    private Button btnUpdateFeePlan;
    private FeeAdapterThree feeAdapterTwoOT,feeAdapterTwoAT,feeAdapterTwoMT,feeAdapterTwoTT;
    FirebaseFirestore db;
    FeeCostBean feeCostBean;
    FeeHeadBean feeHeadBean;
    RoutesBean routesBean;
    ArrayList<FeeHeadBean> feeHeadBeanArrayList;
    ArrayList<com.auribisesmyplayschool.myplayschool.adminApp.bean.FeeCostBean> feeCostBeanAdminAppArrayList;
    int categoryId,check,check2,feePaidId,check3=0;
    ArrayList<Integer> listId;
    int chkId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_fee_structure);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Structure");
        AttUtil.progressDialog(UpdateFeeStructure.this);
        studentBean = (StudentBean)getIntent().getSerializableExtra(AttUtil.TAG_STUDENTBEAN);
        db = FirebaseFirestore.getInstance();
        preferences=getSharedPreferences(AttUtil.shpREG,MODE_PRIVATE);
        feeCostBeanArrayList = new ArrayList<>();
        feeCostBean = new FeeCostBean();
        listId = new ArrayList<>();
        feeHeadBean = new FeeHeadBean();
        routesBean = new RoutesBean();
        feeHeadBeanArrayList = new ArrayList<>();
        feeCostPaidArrayList = new ArrayList<>();
        routesBeanArrayList=new ArrayList<>();
        feeCostBeanAdminAppArrayList = new ArrayList<>();
        tvFeeStructureName=findViewById(R.id.tvFeeStructureName);
        textViewAmountMonth = findViewById(R.id.textViewAmountMonth);
        textViewAmountAnnual = findViewById(R.id.textViewAmountAnnual);
        textViewAmountOne = findViewById(R.id.textViewAmountOne);
        textViewAmountTransport = findViewById(R.id.textViewAmountTransport);
        TextView eTextRecv =  findViewById(R.id.eTextRecv);
        eTextRecv.setText("CHK");
        TextView eTextRecv2 =  findViewById(R.id.eTextRecv2);
        eTextRecv2.setText("CHK");
        TextView eTextRecv3 =  findViewById(R.id.eTextRecv3);
        eTextRecv3.setText("CHK");
        TextView eTextRecv4 =  findViewById(R.id.eTextRecv4);
        eTextRecv4.setText("CHK");
        listViewFeesOT= findViewById(R.id.listViewFeesOne);
        listViewFeesAT= findViewById(R.id.listViewFeesAnnual);
        listViewFeesMT= findViewById(R.id.listViewFeesMonth);
        listViewFeesTT= findViewById(R.id.listViewFeesTransport);
        listViewFeesOT.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listViewFeesAT.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listViewFeesMT.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listViewFeesTT.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        AttUtil.pd(1);
        getLastFeePaidId();
        retrieveFromDb();


        btnUpdateFeePlan=(Button) findViewById(R.id.btnUpdateFeePlan);
        if(AttUtil.isNetworkConnected(this)){
            //retrieveIntoDb();
        }

       /* else
            retrieveNetConnect();*/
        btnUpdateFeePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailNoteDialog();
            }
        });
    }

     void retrieveFromDb() {
        db.collection(Constants.fee_paid_collection).whereEqualTo("studentId",studentBean.getStudentId())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    feeCostBean = doc.toObject(FeeCostBean.class);
                    feeCostPaidArrayList.add(feeCostBean);
                }

                for(int i=0;i<feeCostPaidArrayList.size();i++){
                    if(feeCostPaidArrayList.get(i).getCategoryId() != 0){
                        categoryId = feeCostPaidArrayList.get(i).getCategoryId();
                    }
                }
                fetchFeeCostList();
            }
        });
    }

     void fetchFeeCostList() {
         db.collection(Constants.fee_head_collection).whereEqualTo("branchId",studentBean.getBranchId())
                 .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
             @Override
             public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                 for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                     feeHeadBean = doc.toObject(FeeHeadBean.class);
                     feeHeadBeanArrayList.add(feeHeadBean);
                 }

                 for(int i=0;i<feeHeadBeanArrayList.size();i++){
                     if(feeHeadBeanArrayList.get(i).getFeeType() != 4) {
                         for (int j = 0; j < feeHeadBeanArrayList.get(i).getFeeCostBeanArrayList().size(); j++) {
                             //if(feeHeadBeanArrayList.get(i).getFeeType() != 4) {
                             check2 = 1;
                             if (feeHeadBeanArrayList.get(i).getFeeCostBeanArrayList().get(j).getCategoryId() == categoryId) {
                                 feeCostBeanAdminAppArrayList.add(feeHeadBeanArrayList.get(i).getFeeCostBeanArrayList().get(j));
                             }
                             //}
                         }
                     }
                 }
                 if(check2 == 1){
                     setFeeCostBean();
                 }
             }
         });
    }


    void checkRouteId() {
        for(int i=0;i<feeCostPaidArrayList.size();i++){
            if(feeCostPaidArrayList.get(i).getRouteId() != 0){
                feeCostBean.setRouteId(feeCostPaidArrayList.get(i).getRouteId());
            }
        }
       // addToFeeCostArrayList();
    }


    void setFeeCostBean() {
        check = feeCostBeanAdminAppArrayList.size();
        for(int i=0;i<feeCostBeanAdminAppArrayList.size();i++){
            check = check - 1;
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
            if(feeCostBean.getHeadId() == feeHeadBeanArrayList.get(i).getHeadId()){
                feeCostBean.setHeadName(feeHeadBeanArrayList.get(i).getHeadName());
                feeCostBean.setFeeType(feeHeadBeanArrayList.get(i).getFeeType());
            }
        }
        addToFeeCostArrayList();
    }

    void addToFeeCostArrayList() {
        feeCostBeanArrayList.add(feeCostBean);
         if(check == 0){
            fetchRoutesList();
            //setData();
        }
    }

     void fetchRoutesList() {
        db.collection(Constants.routes_collection).whereEqualTo("branchId",studentBean.getBranchId())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    routesBean = doc.toObject(RoutesBean.class);
                    routesBeanArrayList.add(routesBean);
                }
                for(int i=0;i<feeHeadBeanArrayList.size();i++){
                    if(feeHeadBeanArrayList.get(i).getFeeType() == 4){
                        feeCostBean = new FeeCostBean();
                        feeCostBean.setBranchId(feeHeadBeanArrayList.get(i).getBranchId());
                        feeCostBean.setHeadId(feeHeadBeanArrayList.get(i).getHeadId());
                        feeCostBean.setFeeType(feeHeadBeanArrayList.get(i).getFeeType());
                        feeCostBean.setHeadName(feeHeadBeanArrayList.get(i).getHeadName());
                        checkRouteId();
                        feeCostBeanArrayList.add(feeCostBean);
                    }
                }
                setData();
            }
        });
    }


    void setData(){
      // fetchRoutesList();
        for(int i=0;i<feeCostPaidArrayList.size();i++){
            for(int j=0;j<feeCostBeanArrayList.size();j++){
                if(feeCostPaidArrayList.get(i).getHeadId() == feeCostBeanArrayList.get(j).getHeadId()){
                    feeCostBeanArrayList.get(j).setChecked(true);
                    feeCostBeanArrayList.get(j).setFeePaidId(feeCostPaidArrayList.get(i).getFeePaidId());
                    feeCostBeanArrayList.get(j).setAdmissionId(feeCostPaidArrayList.get(i).getAdmissionId());
                    feeCostBeanArrayList.get(j).setFeeType(feeCostPaidArrayList.get(i).getFeeType());
                    feeCostBeanArrayList.get(j).setDiscount(feeCostPaidArrayList.get(i).getDiscount());
                    feeCostBeanArrayList.get(j).setCost(feeCostPaidArrayList.get(i).getCost());
                    feeCostBeanArrayList.get(j).setStudentId(feeCostPaidArrayList.get(i).getStudentId());
                    feeCostBeanArrayList.get(j).setSellingCost(feeCostPaidArrayList.get(i).getSellingAmount());
                }
            }
        }

        for(int i=0;i<feeCostBeanArrayList.size();i++){
           /* if(feeCostBeanArrayList.get(i).getFeeStuId()>0)
                feeCostBeanArrayList.get(i).setChecked(true);*/
            if(feeCostBeanArrayList.get(i).getFeeType()==1){
                oneTimeFeeHeadList.add(feeCostBeanArrayList.get(i));
            }else if(feeCostBeanArrayList.get(i).getFeeType()==2){
                annualFeeHeadList.add(feeCostBeanArrayList.get(i));
            }else if(feeCostBeanArrayList.get(i).getFeeType()==3){
                monthlyFeeHeadList.add(feeCostBeanArrayList.get(i));
            }else if(feeCostBeanArrayList.get(i).getFeeType()==4){
                transportFeeHeadList.add(feeCostBeanArrayList.get(i));
            }
        }


        tvFeeStructureName.setText("Fee Structure: "+feeCostBeanArrayList.get(0).getCategoryName());
        feeAdapterTwoOT=new FeeAdapterThree(UpdateFeeStructure.this,
                oneTimeFeeHeadList,2,UpdateFeeStructure.this);
        feeAdapterTwoAT=new FeeAdapterThree(UpdateFeeStructure.this,
                annualFeeHeadList,2,UpdateFeeStructure.this);
        feeAdapterTwoMT=new FeeAdapterThree(UpdateFeeStructure.this,
                monthlyFeeHeadList,2,UpdateFeeStructure.this);
        feeAdapterTwoTT = new FeeAdapterThree(UpdateFeeStructure.this,
                transportFeeHeadList, 2,UpdateFeeStructure.this,
                true,routesBeanArrayList);
        Log.i("test",transportFeeHeadList.toString());
        listViewFeesOT.setAdapter(feeAdapterTwoOT);
        listViewFeesAT.setAdapter(feeAdapterTwoAT);
        listViewFeesMT.setAdapter(feeAdapterTwoMT);
        listViewFeesTT.setAdapter(feeAdapterTwoTT);
        AttUtil.pd(0);
    }




    @Override
    public void setPayAmount() {

    }

    @Override
    public void setSelectedAmount() {
        int oneTimeAmountSelect=0,annualAmountSelect=0,monthlyAmountSelect=0,transactionAmountSelect = 0;
        for(int i=0;i<oneTimeFeeHeadList.size();i++){
            if(oneTimeFeeHeadList.get(i).isChecked())
                oneTimeAmountSelect = oneTimeAmountSelect+(oneTimeFeeHeadList.get(i).getSellingCost());
        }
        for(int i=0;i<annualFeeHeadList.size();i++){
            if(annualFeeHeadList.get(i).isChecked())
                annualAmountSelect = annualAmountSelect+(annualFeeHeadList.get(i).getSellingCost());
        }
        for(int i=0;i<monthlyFeeHeadList.size();i++){
            if(monthlyFeeHeadList.get(i).isChecked())
                monthlyAmountSelect = monthlyAmountSelect+(monthlyFeeHeadList.get(i).getSellingCost());
        }for (int i = 0; i < transportFeeHeadList.size(); i++) {
            if (transportFeeHeadList.get(i).isChecked()&&transportFeeHeadList.get(i).getFeeType()==4)
                transactionAmountSelect = transactionAmountSelect + transportFeeHeadList.get(i).getSellingCost();
        }

        textViewAmountOne.setText(""+oneTimeAmountSelect+"");
        textViewAmountAnnual.setText(""+annualAmountSelect+"");
        textViewAmountMonth.setText(""+monthlyAmountSelect+"");
        textViewAmountTransport.setText("" + transactionAmountSelect + "");
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

    EditText editText;
    void emailNoteDialog(){
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
                if(AttUtil.isNetworkConnected(UpdateFeeStructure.this)){
                    AttUtil.pd(1);
                    submitFees();
                }
                /*else
                    retrieveNetConnect();*/
            }
        });
        dialogBuilder.setNegativeButton("Cancel",null);
        android.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

     void submitFees() {
        check3 = feeCostBeanArrayList.size();
         for(int i=0;i<feeCostBeanArrayList.size();i++){
             check3 = check3 - 1;
             if(feeCostBeanArrayList.get(i).isChecked()){
                 if(feeCostBeanArrayList.get(i).getFeePaidId() != 0){
                     feeCostBean = new FeeCostBean();
                     feeCostBean.setHeadId(feeCostBeanArrayList.get(i).getHeadId());
                     feeCostBean.setHeadName(feeCostBeanArrayList.get(i).getHeadName());
                     feeCostBean.setBranchId(feeCostBeanArrayList.get(i).getBranchId());
                     feeCostBean.setStudentId(feeCostBeanArrayList.get(i).getStudentId());
                     feeCostBean.setCost(feeCostBeanArrayList.get(i).getCost());
                     feeCostBean.setSellingAmount(feeCostBeanArrayList.get(i).getSellingCost());
                     feeCostBean.setDiscount(feeCostBeanArrayList.get(i).getDiscount());
                     feeCostBean.setFeeType(feeCostBeanArrayList.get(i).getFeeType());
                     feeCostBean.setAdmissionId(feeCostBeanArrayList.get(i).getStudentId());
                     feeCostBean.setFeePaidId(feeCostBeanArrayList.get(i).getFeePaidId());
                     if(feeCostBeanArrayList.get(i).getFeeType()!=4){
                         feeCostBean.setCategoryId(feeCostBeanArrayList.get(i).getCategoryId());
                         feeCostBean.setCategoryName(feeCostBeanArrayList.get(i).getCategoryName());
                     }else {
                         feeCostBean.setRouteId(feeCostBeanArrayList.get(i).getRouteId());
                     }
                     addToDB();
                 }else{
                     feeCostBean = new FeeCostBean();
                     feeCostBean.setHeadId(feeCostBeanArrayList.get(i).getHeadId());
                     feeCostBean.setHeadName(feeCostBeanArrayList.get(i).getHeadName());
                     feeCostBean.setBranchId(feeCostBeanArrayList.get(i).getBranchId());
                     feeCostBean.setStudentId(studentBean.getStudentId());
                     feeCostBean.setCost(feeCostBeanArrayList.get(i).getCost());
                     feeCostBean.setSellingAmount(feeCostBeanArrayList.get(i).getSellingCost());
                     feeCostBean.setDiscount(feeCostBeanArrayList.get(i).getDiscount());
                     feeCostBean.setFeeType(feeCostBeanArrayList.get(i).getFeeType());
                     feeCostBean.setAdmissionId(studentBean.getStudentId());
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
         }
    }

     void addToDB() {
         db.collection(Constants.fee_paid_collection).document(String.valueOf(feeCostBean.getFeePaidId())).set(feeCostBean)
                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void aVoid) {
                         if(check3 == 0){
                             AttUtil.pd(0);
                             final AlertDialog.Builder dialog =new AlertDialog.Builder(UpdateFeeStructure.this);
                             dialog.setMessage("Fee structure revised." );
                             dialog.setCancelable(false);
                             dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                     listId.clear();
                                     finish();
                                 }
                             });
                             dialog.show();
                         }
                     }
                 });

    }

    void getLastFeePaidId() {
         db.collection(Constants.fee_paid_collection).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
             @Override
             public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                 if(queryDocumentSnapshots.size()>0){
                     for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                         chkId = doc.getLong("feePaidId").intValue();
                         listId.add(chkId);
                     }
                 }else {
                     feePaidId = 1;
                 }

                 if(listId.size()>0){
                     feePaidId = Collections.max(listId);
                     feePaidId = feePaidId + 1;
                 }else{
                     feePaidId = 1;
                 }
                 submitFees();
             }
         });
    }
}
