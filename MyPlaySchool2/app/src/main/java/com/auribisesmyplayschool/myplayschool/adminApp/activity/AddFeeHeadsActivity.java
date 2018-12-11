package com.auribisesmyplayschool.myplayschool.adminApp.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adminApp.adapter.CategoryAdapter;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.CategoryBean;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.FeeCostBean;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.FeeHeadBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

import dmax.dialog.SpotsDialog;

public class AddFeeHeadsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText editTextHeadName;
    private Spinner spinnerFeeType;
    private ListView listViewCategories;
    private int branchId, request, feeType,adminId;
    private ArrayList<CategoryBean> categoryBeanArrayList;
    ArrayList<FeeCostBean> feeCostBeanArrayList;
    CategoryBean categoryBean;
    //    private ArrayList<Integer> feeTypeCountArrayList;
    private CategoryAdapter categoryAdapter;
    private ArrayList<String> feeTypeList = new ArrayList<>();
    //private Button submitButton;
    private SharedPreferences preferences;
    //private LinearLayout footerLayout;
    String[] feeTypeCountArrayList;
    ArrayAdapter adapter;
    FirebaseFirestore db;
    FeeHeadBean feeHeadBean;
    FeeCostBean feeCostBean;
    int feeHeadId=0;
    ArrayList<Integer> listId;

    private void initViews() {
        listId = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        categoryBeanArrayList = new ArrayList<>();
        feeCostBeanArrayList = new ArrayList<>();
        categoryBean = new CategoryBean();
        feeHeadBean = new FeeHeadBean();
        feeTypeList.add("--Select a fee type--");
        feeTypeList.add("One Time");
        feeTypeList.add("Annual");
        feeTypeList.add("Monthly");
        feeTypeList.add("Transportation");
        branchId = getIntent().getIntExtra("branchId", 0);
        adminId = getIntent().getIntExtra("adminId",0);
        LayoutInflater inflater = getLayoutInflater();
        //footerLayout = (LinearLayout) inflater.inflate(R.layout.list_footer_layout, null);
        preferences = getSharedPreferences(AttUtil.shpREG, MODE_PRIVATE);
        //submitButton = footerLayout.findViewById(R.id.buttonSubmit);
        editTextHeadName = findViewById(R.id.edtName);
        spinnerFeeType = findViewById(R.id.spinnerFeeType);
        listViewCategories = findViewById(R.id.listViewCategories);
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, feeTypeList);
        spinnerFeeType.setAdapter(adapter);
        spinnerFeeType.setOnItemSelectedListener(this);
        //submitButton.setOnClickListener(this);
        //listViewCategories.addFooterView(footerLayout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fee_heads);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        AttUtil.progressDialog(this);
        AttUtil.pd(1);
        retrieveCategories();
    }

     void retrieveCategories() {
        db.collection(Constants.fee_category_collection).whereEqualTo("branchId",branchId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    categoryBean = doc.toObject(CategoryBean.class);
                    categoryBeanArrayList.add(categoryBean);
                }
                if(categoryBeanArrayList.size()>0){
                    categoryAdapter = new CategoryAdapter(AddFeeHeadsActivity.this, R.layout.adapter_categories, categoryBeanArrayList, 2);
                    listViewCategories.setAdapter(categoryAdapter);
                }else{
                    Toast.makeText(AddFeeHeadsActivity.this,"No Categories found.",Toast.LENGTH_SHORT).show();
                }
                AttUtil.pd(0);

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        feeType = position;
        if (position>0){
            for (int i = 0; i < categoryBeanArrayList.size(); i++)
                categoryBeanArrayList.get(i).setCost(0);
            categoryAdapter.notifyDataSetChanged();
        }
        if (position==4){
            listViewCategories.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        if (item.getItemId() ==1){
            if (valid()){
                AttUtil.progressDialog(this);
                AttUtil.pd(1);
                getLastFeeHeadId();
            }

                //setDataToBean();
        }

        return super.onOptionsItemSelected(item);
    }

     void getLastFeeHeadId() {
        db.collection(Constants.fee_head_collection).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    if(queryDocumentSnapshots.size()>0){
                        int chkId = doc.getLong("headId").intValue();
                        listId.add(chkId);
                    }else {
                        feeHeadId = 1;
                    }
                }

                if(listId.size()>0){
                    feeHeadId = Collections.max(listId);
                    feeHeadId = feeHeadId + 1;
                }else{
                    feeHeadId = 1;
                }
                setDataToBean();
            }
        });
    }

    void setDataToBean() {
        feeHeadBean.setHeadName(editTextHeadName.getText().toString().trim());
        feeHeadBean.setBranchId(branchId);
        feeHeadBean.setFeeType(feeType);
       /* if(feeHeadId!=0){
            feeHeadBean.setHeadId(feeHeadId+1);
        }else{
            feeHeadBean.setHeadId(1);
        }*/
       feeHeadBean.setHeadId(feeHeadId);
        feeHeadBean.setAdminId(adminId);

        //for(int i=0;i<categoryBeanArrayList.size();i++){
        int i = 0;
        while (i<categoryBeanArrayList.size()){
            if(categoryBeanArrayList.get(i).getCost()!=0){
                feeCostBean = new FeeCostBean();
                feeCostBean.setBranchId(categoryBeanArrayList.get(i).getBranchId());
                feeCostBean.setCategoryId(categoryBeanArrayList.get(i).getFeeCategoryId());
                feeCostBean.setCategoryName(categoryBeanArrayList.get(i).getCategoryName());
                feeCostBean.setHeadId(feeHeadBean.getHeadId());
                feeCostBean.setCost(categoryBeanArrayList.get(i).getCost());
                feeCostBeanArrayList.add(feeCostBean);
                i++;
            }else{
                i++;
            }
        }
       // }
        feeHeadBean.setFeeCostBeanArrayList(feeCostBeanArrayList);
        addFeeHead();
    }

    void addFeeHead() {
        db.collection(Constants.fee_head_collection).document(String.valueOf(feeHeadBean.getHeadId())).set(feeHeadBean)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AttUtil.pd(0);
                        clearFields();
                        listId.clear();
                        feeCostBeanArrayList = new ArrayList<>();
                        Toast.makeText(AddFeeHeadsActivity.this, "Fee head added successfully", Toast.LENGTH_LONG).show();
                    }
                });
    }

     void clearFields() {
         editTextHeadName.setText("");
         spinnerFeeType.setSelection(0);
         for (int i = 0; i < categoryBeanArrayList.size(); i++)
             categoryBeanArrayList.get(i).setCost(0);
         categoryAdapter.notifyDataSetChanged();
    }

    private boolean valid() {
        boolean valid = true;
        if (editTextHeadName.getText().toString().trim().isEmpty()) {
            valid = false;
            editTextHeadName.setError("Head name cannot be empty");
            Toast.makeText(this, "Head name cannot be empty.", Toast.LENGTH_LONG).show();
        }
        if (feeType == 0) {
            valid = false;
            Toast.makeText(this, "Select a Fee Type to continue.", Toast.LENGTH_LONG).show();
        }
        if (feeType == 4){
            try {
                if( Integer.parseInt(feeTypeCountArrayList[feeType-1])>0) {
                    valid = false;
                    Toast.makeText(this, "Only one fee head is allowed in Transportation Category.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"Add").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }
}
